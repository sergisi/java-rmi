package main;

import adaptators.AdaptateSystem;
import common.SessionMaker;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private static AdaptateSystem system = new AdaptateSystem();
    private static Registry registry;
    private static ClientPromiseImpl clientPromise = new ClientPromiseImpl();

    public static void main(String[] args) {
        try {
            SessionMaker sessionMaker = getSessionMaker();
            String idStudent = startExam(sessionMaker, clientPromise);
            answerExam(sessionMaker, clientPromise, idStudent);
            finishExam();
        } catch (RemoteException | NotBoundException | InterruptedException e) {
            if (clientPromise.isExamFinished()) {
                finishExam();
            }
            e.printStackTrace();
        }
    }

    private static void finishExam() {
        system.println("The exam has finished, you have a score of "
                + clientPromise.getCorrectAnswers() + "/" +
                clientPromise.getTotalQuestions());
    }

    private static void answerExam(SessionMaker sessionMaker, ClientPromiseImpl clientPromise, String idStudent) {
        while (sessionMaker.hasNext(idStudent) && !clientPromise.isExamFinished()) {
            answerQuestion(sessionMaker, idStudent);
        }
    }

    private static void answerQuestion(SessionMaker sessionMaker, String idStudent) {
        String question = sessionMaker.next(idStudent);
        system.println(question);
        Integer number = Integer.parseInt(system.readLn());
        sessionMaker.answerQuestion(idStudent, number);
    }

    private static String startExam(SessionMaker sessionMaker, ClientPromiseImpl clientPromise) throws InterruptedException {
        system.println("Put your id for this session");
        String idStudent = system.readLn();
        sessionMaker.newSession(idStudent, clientPromise);
        synchronized (clientPromise) {
            while (!clientPromise.isStartExam()) {
                clientPromise.wait(1000); // Checks every second if the server has responded.
            }
        }
        return idStudent;
    }

    private static SessionMaker getSessionMaker() throws RemoteException, NotBoundException {
        if (registry == null) {
            registry = LocateRegistry.getRegistry();
        }
        SessionMaker sessionMaker = (SessionMaker) registry.lookup("SessionMaker");
        return sessionMaker;
    }

    public static void setSystem(AdaptateSystem sys) {
        system = sys;
    }

    public static void setRegistry(Registry reg) {
        registry = reg;
    }


    public static void setClientPromise(ClientPromiseImpl client) {
        clientPromise = client;
    }
}

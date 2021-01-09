package main;

import adaptators.AdaptSystem;
import common.SessionMaker;
import exceptions.ExamHasFinishedException;
import rest.Http;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private static AdaptSystem system = new AdaptSystem();
    private static Registry registry;
    private static ClientPromiseImpl clientPromise;

    public static void main(String[] args) {
        try {
            if (clientPromise == null) {
                clientPromise = new ClientPromiseImpl();
            }
            SessionMaker sessionMaker = getSessionMaker();
            String idStudent = startExam(sessionMaker, clientPromise);
            answerExam(sessionMaker, clientPromise, idStudent);
            finishExam();
        } catch (RemoteException | NotBoundException | InterruptedException e) {
            if (clientPromise.isExamFinished()) {
                finishExam();
            }
            e.printStackTrace();
        } catch (ExamHasFinishedException e) {
            // Is always true.
            if (clientPromise.isExamFinished()) {
                finishExam();
            }
        }
    }

    private static void finishExam() {
        system.printLn("The exam has finished, you have a score of "
                + clientPromise.getCorrectAnswers() + "/" +
                clientPromise.getTotalQuestions());
    }

    private static void answerExam(SessionMaker sessionMaker, ClientPromiseImpl clientPromise, String idStudent) throws ExamHasFinishedException {
        while (sessionMaker.hasNext(idStudent) && !clientPromise.isExamFinished()) {
            answerQuestion(sessionMaker, idStudent);
        }
    }

    private static void answerQuestion(SessionMaker sessionMaker, String idStudent) throws ExamHasFinishedException {
        String question = sessionMaker.next(idStudent);
        system.printLn(question);
        Integer number = null;
        do {
            try {
                number = Integer.parseInt(system.readLn());
            } catch (NumberFormatException ignored) {}
        } while (number == null);
        sessionMaker.answerQuestion(idStudent, number);
    }

    private static String startExam(SessionMaker sessionMaker, ClientPromiseImpl clientPromise) throws InterruptedException {
        system.printLn("Put your username and password for this session");
        String usernameStudent = system.readLn();
        String passwordStudent = system.readLn();
        Http http = new Http();
        http.authenticate_student(usernameStudent, passwordStudent);

        System.exit(0);
        String idStudent = ""; //
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
        return (SessionMaker) registry.lookup("SessionMaker");
    }

    public static void setSystem(AdaptSystem sys) {
        system = sys;
    }

    public static void setRegistry(Registry reg) {
        registry = reg;
    }


    public static void setClientPromise(ClientPromiseImpl client) {
        clientPromise = client;
    }
}

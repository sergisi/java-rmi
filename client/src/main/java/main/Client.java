package main;

import adaptators.AdaptSystem;
import common.SessionMaker;
import exceptions.ExamHasFinishedException;
import rest.Http;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private static AdaptSystem system = new AdaptSystem();
    private static Registry registry;
    private static ClientPromiseImpl clientPromise;
    private static Http http = new Http();

    public static void main(String[] args) {
        try {
            if (clientPromise == null) {
                clientPromise = new ClientPromiseImpl();
            }
            authenticate();
            String idStudent = http.getStudentId();
            String examLocation = chooseExamToConnect();
            SessionMaker sessionMaker = getSessionMaker(examLocation);
            startExam(sessionMaker, clientPromise, idStudent);
            answerExam(sessionMaker, clientPromise, idStudent);
            finishExam();
        } catch (NotBoundException | InterruptedException | IOException e) {
            safeFinishExam();
            e.printStackTrace();
            system.exit(1);
        } catch (ExamHasFinishedException e) {
            safeFinishExam();
        }
    }

    private static void safeFinishExam() {
        if (clientPromise.isExamFinished()) {
            finishExam();
        }
    }

    private static String chooseExamToConnect() throws IOException {
        String examLocation = null;
        system.printLn("Options:\n" +
                " - search <key words>: In order to search an exam by it's description\n" +
                " - list : In order to list all exams\n" +
                " - choose: In order to choose the exam in which you want to connect\n");
        while (examLocation == null) {
            String cmd = system.readLn();
            examLocation = executeCmd(cmd);
        }
        return examLocation;
    }

    private static String executeCmd(String cmd) throws IOException {
        if (cmd.startsWith("search")) {
            system.printLn(http.getListSearchExams(cmd.replace("search ", "")));
        }
        if (cmd.startsWith("list")) {
            system.printLn(http.getListAllExams());
        }
        if (cmd.startsWith("choose")) {
            String examLocation =  http.getExamLocation(cmd.replace("choose ", ""));
            system.printLn(examLocation);
            return examLocation;
        }
        return null;
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
            } catch (NumberFormatException ignored) {
            }
        } while (number == null);
        sessionMaker.answerQuestion(idStudent, number);
    }

    private static String startExam(SessionMaker sessionMaker, ClientPromiseImpl clientPromise, String idStudent) throws InterruptedException, IOException {
        sessionMaker.newSession(idStudent, clientPromise);
        synchronized (clientPromise) {
            while (!clientPromise.isStartExam()) {
                clientPromise.wait(1000); // Checks every second if the server has responded.
            }
        }
        return idStudent;
    }

    private static void authenticate() {
        system.printLn("Put your username");
        String usernameStudent = system.readLn();
        system.printLn("Put your password");
        String passwordStudent = system.readLn();
        http.authenticateStudent(usernameStudent, passwordStudent);
    }

    private static SessionMaker getSessionMaker(String examLocation) throws RemoteException, NotBoundException {
        if (registry == null) {
            registry = LocateRegistry.getRegistry();
        }
        return (SessionMaker) registry.lookup(examLocation);
    }

    public static void setSystem(AdaptSystem sys) {
        system = sys;
    }

    public static void setRegistry(Registry reg) {
        registry = reg;
    }

    public static void setHttp(Http http) {
        Client.http = http;
    }

    public static void setClientPromise(ClientPromiseImpl client) {
        clientPromise = client;
    }
}

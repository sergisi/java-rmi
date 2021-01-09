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
import java.util.HashMap;

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
            SessionMaker sessionMaker = getSessionMaker();
            authenticate();
            String idStudent = http.getStudentId();
            String examLocationPort = chooseExamToConnect();
            startExam(sessionMaker, clientPromise, idStudent);
            answerExam(sessionMaker, clientPromise, idStudent);
            finishExam();
        } catch (RemoteException | NotBoundException | InterruptedException e ) {
            if (clientPromise.isExamFinished()) {
                finishExam();
            }
            e.printStackTrace();
        } catch (ExamHasFinishedException e) {
            // Is always true.
            if (clientPromise.isExamFinished()) {
                finishExam();
            }
        }catch (IOException e){
            system.exit_error();
        }
    }

    private static String chooseExamToConnect() throws IOException{
        String exam_location_port = null;
        system.printLn("Options:\n" +
                " - search <key words>: In order to search an exam by it's description\n" +
                " - list : In order to list all exams\n" +
                " - choose: In order to choose the exam in wich you want to connect\n");
        while (exam_location_port == null){
            String cmd = system.readLn();
            exam_location_port = execute_cmd(cmd);
            System.out.println(exam_location_port);
        }
        return exam_location_port;
    }

    private static String execute_cmd(String cmd) throws IOException{
        if(cmd.startsWith("search")){
            System.out.println(http.getListSearchExams(cmd.replace("search ", "")));
        }
        if(cmd.startsWith("list")){
            System.out.println(http.getListAllExams());
        }
        if(cmd.startsWith("choose")){
            return http.getExamLocationPort(cmd.replace("choose ", ""));
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
            } catch (NumberFormatException ignored) {}
        } while (number == null);
        sessionMaker.answerQuestion(idStudent, number);
    }

    private static String startExam(SessionMaker sessionMaker, ClientPromiseImpl clientPromise, String idStudent) throws InterruptedException, IOException{
        sessionMaker.newSession(idStudent, clientPromise);
        synchronized (clientPromise) {
            while (!clientPromise.isStartExam()) {
                clientPromise.wait(1000); // Checks every second if the server has responded.
            }
        }
        return idStudent;
    }

    private static void authenticate(){
        system.printLn("Put your username");
        String usernameStudent = system.readLn();
        system.printLn("Put your password");
        String passwordStudent = system.readLn();
        http.authenticateStudent(usernameStudent, passwordStudent);
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

package main;

import adaptators.AdaptParse;
import adaptators.AdaptSystem;
import rest.Http;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;

public class Professor {

    private static AdaptSystem sys = new AdaptSystem();
    private static AdaptParse parser = new AdaptParse();
    private static Registry registry;
    private static SessionMakerImplementation session;

    public static void main(String[] args) {
        if (args.length != 2) {
            sys.printLn("Please pass two arguments: <inputFile> <outputFile>");
            return;
        }
        Http http = new Http();
        authenticate(http);
        try {
            if (Professor.session == null)
                session = initializeSession(args[0]);
            if (registry == null) {
                sys.printLn("");
                registry = startRegistry();
            }
            try {
                createExamWs(session, http, registry.REGISTRY_PORT);
            }catch (IOException e){
                e.printStackTrace();
                System.exit(1);
            }
            registry.bind("SessionMaker", session);
            startExam(session);
            finishExam(session, http);
            saveExam(args[1], session);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    private static void startExam(SessionMakerImplementation session){
        sys.printLn("To start the exam press enter");
        sys.readLn();
        session.startExam();
    }

    private static void authenticate(Http http){
        sys.printLn("Put your username");
        String usernameProfessor = sys.readLn();
        sys.printLn("Put your password");
        String passwordProfessor = sys.readLn();
        http.authenticateProfessor(usernameProfessor, passwordProfessor);
    }

    private static void createExamWs(SessionMakerImplementation session, Http http, Integer registry_port) throws IOException{
        sys.printLn("Give a description for the exam");
        String description = sys.readLn();
        sys.printLn("Enter the date of the exam");
        String date = sys.readLn();
        String location = registry_port.toString();
        String idExam = http.createExam(description,date,location);
        session.setIdExam(idExam);
    }

    private static void finishExam(SessionMakerImplementation session, Http http) {
        sys.printLn("To finish the exam press enter");
        sys.readLn();
        session.finishExam(http);
    }

    private static void saveExam(String arg, SessionMakerImplementation session) {
        try (PrintWriter out = sys.getOutputFile(arg)) {
            out.println("Student Id; Correct Question; Total Questions; Score");
            int numberOfQuestions = session.getNumberOfQuestions();
            session.getResults().forEach((pair) -> {
                Integer correctAnswers = pair.getValue().getCorrectAnswers();
                out.println(pair.getKey() +
                        "; " + correctAnswers.toString() +
                        "; " + numberOfQuestions +
                        "; " + 10 * correctAnswers / (double) numberOfQuestions);
            });
            sys.printLn("The exam has finished, the results can be found at: " + arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Registry startRegistry() throws RemoteException {
        return startRegistry(1099);
    }

    private static Registry startRegistry(Integer port) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list();
            // The above call will throw an exception
            // if the registry does not already exist
            return registry;
        } catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }

    private static SessionMakerImplementation initializeSession(String inputFile) throws RemoteException {
        List<Question> questions = parser.parseQuestionsFile(inputFile);
        return new SessionMakerImplementation(questions);
    }

    public static void setSys(AdaptSystem sys) {
        Professor.sys = sys;
    }

    public static void setParser(AdaptParse parser) {
        Professor.parser = parser;
    }

    public static void setRegistry(Registry registry) {
        Professor.registry = registry;
    }

    public static void setSession(SessionMakerImplementation session) {
        Professor.session = session;
    }
}

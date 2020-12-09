package main;

import adaptators.AdaptParse;
import adaptators.AdaptSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
        try {
            if (Professor.session == null)
                session = initializeSession(args[0]);
            if (registry == null) {
                registry = startRegistry();
            }
            registry.bind("SessionMaker", session);
            startExam(session);
            finishExam(session);
            saveExam(args[1], session);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    private static void startExam(SessionMakerImplementation session) {
        sys.printLn("To start the exam press enter");
        sys.readLn();
        session.startExam();
    }

    private static void finishExam(SessionMakerImplementation session) {
        sys.printLn("To finish the exam press enter");
        sys.readLn();
        session.finishExam();
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

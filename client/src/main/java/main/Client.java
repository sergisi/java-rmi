package main;

import adaptators.AdaptateSystem;
import common.ClientPromise;
import common.SessionMaker;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private static AdaptateSystem system = new AdaptateSystem();

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            SessionMaker sessionMaker = (SessionMaker) registry.lookup("SessionMaker");
            ClientPromiseImpl clientPromise = new ClientPromiseImpl();
            synchronized (clientPromise) {
                system.println("Put your id for this session");
                String idStudent = system.readLn();
                sessionMaker.newSession(idStudent, clientPromise);
                clientPromise.wait();
                while (sessionMaker.hasNext(idStudent)) {
                    String question = sessionMaker.next(idStudent);
                    system.println(question);
                }

            }
       } catch (RemoteException | NotBoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setSystem(AdaptateSystem sys) {
        system = sys;
    }
}

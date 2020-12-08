package common;

import exceptions.ExamHasFinishedException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionMaker extends Remote {
    void newSession(String idStudent, ClientPromise client) throws RemoteException;
    void answerQuestion(String idStudent, Integer answer) throws ExamHasFinishedException, RemoteException;
    boolean hasNext(String idStudent) throws RemoteException;
    String next(String idStudent) throws ExamHasFinishedException, RemoteException;
}

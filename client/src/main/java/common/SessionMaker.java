package common;

import exceptions.ExamHasFinishedException;

import java.rmi.Remote;

public interface SessionMaker extends Remote {
    void newSession(String idStudent, ClientPromise client);
    void answerQuestion(String idStudent, Integer answer) throws ExamHasFinishedException;
    boolean hasNext(String idStudent);
    String next(String idStudent) throws ExamHasFinishedException;
}

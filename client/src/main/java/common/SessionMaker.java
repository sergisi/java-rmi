package common;

import java.rmi.Remote;

public interface SessionMaker extends Remote {
    void newSession(String idStudent, ClientPromise client);
    void answerQuestion(String idStudent, Integer answer);
    Boolean hasNext(String idStudent);
    String next(String idStudent);
}

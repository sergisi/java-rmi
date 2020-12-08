package common;

public interface SessionMaker {
    void newSession(String idStudent, ClientPromise client);
    void answerQuestion(String idStudent, Integer answer);
    boolean hasNext(String idStudent);
    String next(String idStudent);
}

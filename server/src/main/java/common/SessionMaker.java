package common;

import exceptions.ExamHasFinishedException;

public interface SessionMaker {
    void newSession(String idStudent, ClientPromise client);
    void answerQuestion(String idStudent, Integer answer) throws ExamHasFinishedException;
    boolean hasNext(String idStudent);
    String next(String idStudent) throws ExamHasFinishedException;
}

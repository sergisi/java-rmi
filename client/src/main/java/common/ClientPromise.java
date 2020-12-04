package common;

import java.rmi.Remote;

public interface ClientPromise extends Remote {
    void finishExam(Integer correctAnswer, Integer totalQuestions);
    void startExam();
}

package Common;

public interface ClientPromise {
    void finishExam(Integer correctAnswer, Integer totalQuestions);
    void startExam();
}

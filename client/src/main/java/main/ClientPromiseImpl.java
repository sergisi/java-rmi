package main;

import common.ClientPromise;

public class ClientPromiseImpl implements ClientPromise {
    private boolean examFinished = false;
    private boolean startExam = false;
    private Integer correctAnswers, totalQuestions;

    public void finishExam(Integer correctAnswers, Integer totalQuestions) {
        this.examFinished = true;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
    }

    public synchronized void startExam() {
        startExam = true;
        this.notifyAll();
    }

    public boolean isExamFinished() {
        return examFinished;
    }

    public boolean isStartExam() {
        return startExam;
    }


    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

}

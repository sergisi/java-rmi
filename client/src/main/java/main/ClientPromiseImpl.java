package main;

import common.ClientPromise;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientPromiseImpl extends UnicastRemoteObject implements ClientPromise {
    private boolean examFinished = false;
    private boolean startExam = false;
    private Integer correctAnswers, totalQuestions;

    public ClientPromiseImpl() throws RemoteException {
        super();
    }

    public void finishExam(Integer correctAnswers, Integer totalQuestions) throws RemoteException {
        this.examFinished = true;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
    }

    public synchronized void startExam() throws RemoteException {
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

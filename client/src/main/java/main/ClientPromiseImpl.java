package main;

import adaptators.AdaptateSystem;
import common.ClientPromise;

public class ClientPromiseImpl implements ClientPromise {
    private final AdaptateSystem sys;

    public ClientPromiseImpl() {
        this(new AdaptateSystem());
    }

    public ClientPromiseImpl(AdaptateSystem sys) {
        super();
        this.sys = sys;

    }


    public void finishExam(Integer correctAnswer, Integer totalQuestions) {
        sys.println("The exam has finished. Your score was " + correctAnswer.toString() + "/" + totalQuestions.toString());
        sys.exit(0);
    }

    public synchronized void startExam() {
        this.notifyAll();
    }
}

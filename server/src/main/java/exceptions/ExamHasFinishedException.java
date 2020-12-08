package exceptions;

import java.io.Serializable;
import java.rmi.Remote;

public class ExamHasFinishedException extends Exception implements Remote {

    public ExamHasFinishedException (String s) {

    }
}

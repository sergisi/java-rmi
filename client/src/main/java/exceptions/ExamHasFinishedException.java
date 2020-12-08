package exceptions;

import java.io.Serializable;
import java.rmi.Remote;

public class ExamHasFinishedException extends Exception implements Serializable {

    public ExamHasFinishedException (String s) {

    }
}

import java.util.Objects;

public class UserSession {
    public final int correctAnswer;
    public final int actualQuestion;

    public UserSession(int correctAnswer, int actualQuestion) {
        this.correctAnswer = correctAnswer;
        this.actualQuestion = actualQuestion;
    }
    public UserSession(){
        this(0,0);
    }

    public UserSession nextQuestionCorrect(){
        return new UserSession(correctAnswer+1, actualQuestion+1);
    }
    public UserSession nextQuestion(){
        return new UserSession(correctAnswer, actualQuestion+1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSession that = (UserSession) o;
        return correctAnswer == that.correctAnswer && actualQuestion == that.actualQuestion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(correctAnswer, actualQuestion);
    }
}

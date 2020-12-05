package main;

import java.util.Objects;

public class UserSession {
    public final Integer correctAnswers;
    public final Integer actualQuestion;

    public UserSession(Integer correctAnswers, Integer actualQuestion) {
        this.correctAnswers = correctAnswers;
        this.actualQuestion = actualQuestion;
    }

    public UserSession() {
        this(0, 0);
    }

    public UserSession nextQuestionCorrect() {
        return new UserSession(correctAnswers + 1, actualQuestion + 1);
    }

    public UserSession nextQuestion() {
        return new UserSession(correctAnswers, actualQuestion + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSession that = (UserSession) o;
        return correctAnswers.equals(that.correctAnswers) && actualQuestion.equals(that.actualQuestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correctAnswers, actualQuestion);
    }
}

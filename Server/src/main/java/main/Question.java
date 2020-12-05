package main;

import Exceptions.BadQuestionExceptions;

import java.util.Objects;
import java.util.stream.IntStream;

public class Question {

    private final String question;
    private final Integer correctAnswer;

    public Question(String question, Integer correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public Boolean isCorrectAnswer(Integer choice){
        return correctAnswer == choice;
    }

    public String getQuestion(){
        return question;
    }

    //main.Question?;choice1;choice2;choice3;...;correct_answer_number.
    public static Question parseLine(String question) throws BadQuestionExceptions {
        String [] splitQuestion = question.split(";");
        if(splitQuestion.length < 4){
            throw new BadQuestionExceptions("Not a valid main.Question");
        }
        String questionStatement = splitQuestion[0];
        Integer correct_answer = Integer.parseInt(splitQuestion[splitQuestion.length-1]);
        String[] answers = java.util.Arrays.stream(splitQuestion, 1, splitQuestion.length - 1)
                .toArray(String[]::new);
        String parsedQuestion = parseQuestion(questionStatement, answers);
        return new Question(parsedQuestion, correct_answer);
    }

    private static String parseQuestion(String questionStatement, String[] answers){
        StringBuilder parsedQuestion = new StringBuilder(questionStatement + "\n");
        for (int i = 1; i <= answers.length; i++) {
            parsedQuestion.append("- [").append(Integer.toString(i)).append("]: ").append(answers[i - 1]).append("\n");
        }
        return parsedQuestion.toString();
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", correctAnswer=" + correctAnswer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return Objects.equals(question, question1.question) && Objects.equals(correctAnswer, question1.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, correctAnswer);
    }
}

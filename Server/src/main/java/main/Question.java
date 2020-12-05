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
        String [] splitedQuestion = question.split(";");
        if(splitedQuestion.length < 4){
            throw new BadQuestionExceptions("Not a valid main.Question");
        }
        String questionStatement = splitedQuestion[0];
        Integer correct_answer = Integer.parseInt(splitedQuestion[splitedQuestion.length-1]);
        String[] answers = IntStream.range(1, splitedQuestion.length)
                .mapToObj(i -> splitedQuestion[i])
                .toArray(String[]::new);
        String parsedQuestion = parseQuestion(questionStatement, answers);
        return new Question(parsedQuestion, correct_answer);
    }

    private static String parseQuestion(String questionStatement, String[] answers){
        String parsedQuestion = questionStatement+"\n";
        for (Integer i = 0; i < answers.length; i++) {
            i++;
            parsedQuestion += "- ["+ i.toString() + "]: "+ answers[i]+"\n";
            i--;
        }
        return parsedQuestion;
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

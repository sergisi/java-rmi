package main;

import common.ClientPromise;
import common.SessionMaker;
import exceptions.ExamHasFinishedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;

public class SessionMakerImplementation implements SessionMaker {

    private final ArrayList<Question> questions;
    private final HashMap<String, UserSession> users;
    private final HashMap<String, ClientPromise> clients;
    private boolean examFinished;
    private boolean examStarted;

    public SessionMakerImplementation(ArrayList<Question> questions) {
        this.questions = questions;
        this.users = new HashMap<>();
        this.clients = new HashMap<>();
        this.examStarted = false;
        this.examFinished = false;
    }

    @Override
    public void newSession(String idStudent, ClientPromise client) {
        if (!examStarted) {
            synchronized (users) {
                users.put(idStudent, new UserSession());
            }
            synchronized (clients) {
                clients.put(idStudent, client);
            }
        }
    }

    @Override
    public void answerQuestion(String idStudent, Integer answer) throws ExamHasFinishedException {
        hasExamFinishedThenThrow();
        UserSession actualUserSession = users.get(idStudent);
        Integer actualQuestion = actualUserSession.getActualQuestion();
        Question question = questions.get(actualQuestion);
        synchronized (users) {
            users.put(idStudent, question.isCorrectAnswer(answer) ?
                    actualUserSession.nextQuestionCorrect()
                    : actualUserSession.nextQuestion());
        }
    }

    private void hasExamFinishedThenThrow() throws ExamHasFinishedException {
        if (examFinished) {
            throw new ExamHasFinishedException("The exam has finished.");
        }
    }

    @Override
    public boolean hasNext(String idStudent) {
        UserSession currentSession = users.get(idStudent);
        Integer currentAnswer = currentSession.getActualQuestion();
        return !examFinished && currentAnswer < questions.size();
    }

    @Override
    public String next(String idStudent) throws ExamHasFinishedException {
        isOkayNext(idStudent);
        UserSession currentSession = users.get(idStudent);
        Integer currentQuestion = currentSession.getActualQuestion();
        return questions.get(currentQuestion).getQuestion();
    }

    private void isOkayNext(String idStudent) throws ExamHasFinishedException {
        hasExamFinishedThenThrow();
        if (!this.hasNext(idStudent)) {
            throw new NoSuchElementException("There is no next question.");
        }
    }

    protected void finishExam() {
        this.examFinished = true;
        Set<String> idStudents = clients.keySet();
        for (String idStudent : idStudents) {
            Integer correctAnswers = users.get(idStudent).getActualQuestion();
            clients.get(idStudent).finishExam(correctAnswers, questions.size());
        }
    }

    protected void startExam() {
        this.examStarted = true;
        Set<String> idStudents = clients.keySet();
        for (String idStudent : idStudents) {
            clients.get(idStudent).startExam();
        }
    }

    public HashMap<String, ClientPromise> getClients() {
        return clients;
    }

    public HashMap<String, UserSession> getUsers() {
        return users;
    }

    public void setUserSession(String idStudent, UserSession userState) {
        users.put(idStudent, userState);
    }
}

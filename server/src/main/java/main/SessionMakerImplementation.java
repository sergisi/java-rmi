package main;

import adaptators.AdaptSystem;
import common.ClientPromise;
import common.SessionMaker;
import exceptions.ExamHasFinishedException;

import java.util.*;
import java.util.stream.Stream;

public class SessionMakerImplementation implements SessionMaker {

    private final List<Question> questions;
    private final HashMap<String, UserSession> users;
    private final HashMap<String, ClientPromise> clients;
    private boolean examFinished;
    private boolean examStarted;
    private final AdaptSystem sys;

    public SessionMakerImplementation(List<Question> questions) {
        this(questions, new AdaptSystem());
    }

    public SessionMakerImplementation(List<Question> questions, AdaptSystem sys) {
        this.questions = questions;
        this.users = new HashMap<>();
        this.clients = new HashMap<>();
        this.examStarted = false;
        this.examFinished = false;
        this.sys = sys;
    }

    @Override
    public void newSession(String idStudent, ClientPromise client) {
        if (!examStarted) {
            synchronized (users) {
                users.put(idStudent, new UserSession());
            }
            synchronized (clients) {
                clients.put(idStudent, client);
                sys.println("A new Student has connected. There are " + clients.size() + " students");
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

    protected Stream<Map.Entry<String, UserSession>> getResults() {
        return this.users.entrySet().stream();
    }

    protected int getNumberOfQuestions() {
        return this.questions.size();
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

package main;

import Common.ClientPromise;
import Common.SessionMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SessionMakerImplementation implements SessionMaker {

    private final ArrayList<Question> questions;
    private final HashMap<String, UserSession> users;
    private final HashMap<String, ClientPromise> clients;
    private boolean examStarted;

    public SessionMakerImplementation(ArrayList<Question> questions) {
        this.questions = questions;
        this.users = new HashMap<>();
        this.clients = new HashMap<>();
        this.examStarted = false;
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
    public void answerQuestion(String idStudent, Integer answer) {
        UserSession userSession = users.get(idStudent);
        Integer questionId = userSession.actualQuestion;
        Question question = questions.get(questionId);
        synchronized (users) {
            users.put(idStudent,
                    question.isCorrectAnswer(answer) ?
                            userSession.nextQuestionCorrect()
                            : userSession.nextQuestion());
        }
    }

    @Override
    public boolean hasNext(String idStudent) {
        UserSession currentSession = users.get(idStudent);
        Integer questionId = currentSession.actualQuestion;
        return questionId < this.questions.size();
    }

    @Override
    public String next(String idStudent) {
        UserSession currentSession = users.get(idStudent);
        Integer currentQuestion = currentSession.actualQuestion;
        return questions.get(currentQuestion).getQuestion();
    }

    protected void finishExam() {
        Set<String> idStudents = clients.keySet();
        for (String idStudent : idStudents) {
            Integer correctAnswers = users.get(idStudent).correctAnswers;
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
}

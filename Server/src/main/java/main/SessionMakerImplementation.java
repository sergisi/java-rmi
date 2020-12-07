package main;

import Common.ClientPromise;
import Common.SessionMaker;
import main.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SessionMakerImplementation implements SessionMaker {

    private final ArrayList<Question> questions;
    private final HashMap<String, UserSession> users;
    private final HashMap<String, ClientPromise> clients;
    private boolean examStarted;

    public SessionMakerImplementation(ArrayList<Question> questions){
        this.questions = questions;
        this.users = new HashMap<>();
        this.clients = new HashMap<>();
        this.examStarted = false;
    }

    @Override
    public void newSession(String idStudent, ClientPromise client) {
        if(!examStarted) {
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
        UserSession actualUserSession = users.get(idStudent);
        Integer actualQuestion = actualUserSession.actualQuestion;
        Question question = questions.get(actualQuestion);
        if (question.isCorrectAnswer(answer)){
            synchronized (users) {
                users.put(idStudent, actualUserSession.nextQuestionCorrect());
            }
        }else{
            synchronized (users) {
                users.put(idStudent, actualUserSession.nextQuestion());
            }
        }
    }

    @Override
    public boolean hasNext(String idStudent) {
        UserSession currentSession = users.get(idStudent);
        Integer currentAnswer = currentSession.actualQuestion;
        try{
            Question nextQuestion = questions.get(currentAnswer);
        }catch (Exception e){
            return false;
        }
        return true;
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

    protected void startExam(){
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

    public void setUserSession(String idStudent, UserSession userState){ users.put(idStudent, userState); }

}

import Common.ClientPromise;
import Common.SessionMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SessionMakerImplementation implements SessionMaker {

    private ArrayList<Question> questions;
    private HashMap<String, UserSession> users;
    private HashMap<String, ClientPromise> clients;
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
                users.put(idStudent, new UserSession(actualUserSession.actualQuestion, actualUserSession.correctAnswers + 1));
            }
        }
    }

    @Override
    public boolean hasNext(String idStudent) {
        UserSession currentSession = users.get(idStudent);
        Integer currentAnswer = currentSession.actualQuestion;
        try{
            Question nextQuestion = questions.get(currentAnswer+1);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public String next(String idStudent) {
        UserSession currentSession = users.get(idStudent);
        Integer currentQuestion = currentSession.actualQuestion;
        synchronized (users) {
            users.put(idStudent, new UserSession(currentSession.actualQuestion + 1, currentSession.correctAnswers));
        }
        return questions.get(currentQuestion+1).getQuestion();
    }

    protected void finishExam() {
        Set<String> idStudents = clients.keySet();
        Integer correctAnswers = 0;
        for (String idStudent : idStudents) {
            correctAnswers = users.get(idStudent).correctAnswers;
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
}

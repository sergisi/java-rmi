package main;

import adaptators.AdaptSystem;
import common.SessionMaker;
import exceptions.ExamHasFinishedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rest.Http;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static org.mockito.Mockito.*;

class ClientTest {

    private AdaptSystem sysMock;
    private Registry regMock;
    private SessionMaker sessionMock;
    private ClientPromiseImpl clientMock;
    private Http httpMock;

    @BeforeEach
    void setUp() throws RemoteException, NotBoundException {
        sysMock = mock(AdaptSystem.class);
        Client.setSystem(sysMock);
        regMock = mock(Registry.class);
        Client.setRegistry(regMock);
        sessionMock = mock(SessionMaker.class);
        when(regMock.lookup("NewSessionMaker")).thenReturn(sessionMock);
        clientMock = mock(ClientPromiseImpl.class);
        Client.setClientPromise(clientMock);
        httpMock = mock(Http.class);
        Client.setHttp(httpMock);
    }

    @AfterEach
    void tearUp() {
        Client.setSystem(new AdaptSystem());
        Client.setRegistry(null);
        Client.setClientPromise(null);
        Client.setHttp(new Http());
    }

    @Test
    void main() throws IOException, NotBoundException, ExamHasFinishedException {
        whenInitialize();
        Client.main(new String[0]);
        verify(sysMock).printLn("Put your username");
        verify(sysMock).printLn("Put your password");
        verify(sysMock).printLn("Options:\n" +
                " - search <key words>: In order to search an exam by it's description\n" +
                " - list : In order to list all exams\n" +
                " - choose: In order to choose the exam in which you want to connect\n");
        verify(sysMock).printLn("search1234");
        verify(sysMock).printLn("listExams");
        verify(sysMock).printLn("NewSessionMaker");
        verify(sysMock).printLn("Question");
        verify(sysMock).printLn("Question2");
        verify(sysMock).printLn("The exam has finished, you have a score of 1/3");
    }

    private void whenInitialize() throws ExamHasFinishedException, IOException {
        whenHttpMock();
        whenSysMock();
        whenSessionMock();
        whenClientMock();
    }

    private void whenClientMock() {
        when(clientMock.isStartExam())
                .thenReturn(false)
                .thenReturn(true);
        when(clientMock.isExamFinished())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);
        when(clientMock.getCorrectAnswers())
                .thenReturn(1);
        when(clientMock.getTotalQuestions())
                .thenReturn(3);
    }

    private void whenSessionMock() throws ExamHasFinishedException {
        when(sessionMock.hasNext("2"))
                .thenReturn(true);
        when(sessionMock.next("2"))
                .thenReturn("Question")
                .thenReturn("Question2");
    }

    private void whenSysMock() {
        when(sysMock.readLn())
                .thenReturn("user")
                .thenReturn("pass")
                .thenReturn("search 1234")
                .thenReturn("list")
                .thenReturn("choose 1")
                .thenReturn("1234")
                .thenReturn("1")
                .thenReturn("2");
    }

    private void whenHttpMock() throws IOException {
        when(httpMock.getListAllExams()).thenReturn("listExams");
        when(httpMock.getListSearchExams("1234")).thenReturn("search1234");
        when(httpMock.getExamLocation("1")).thenReturn("NewSessionMaker");
        when(httpMock.getStudentId()).thenReturn("2");
    }

}
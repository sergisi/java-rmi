package main;

import adaptators.AdaptSystem;
import common.SessionMaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static org.mockito.Mockito.*;

class ClientTest {

    private AdaptSystem sysMock;
    private Registry regMock;
    private SessionMaker sessionMock;
    private ClientPromiseImpl clientMock;

    @BeforeEach
    void setUp() throws RemoteException, NotBoundException {
        sysMock = mock(AdaptSystem.class);
        Client.setSystem(sysMock);
        regMock = mock(Registry.class);
        Client.setRegistry(regMock);
        sessionMock = mock(SessionMaker.class);
        when(regMock.lookup("SessionMaker")).thenReturn(sessionMock);
        clientMock = mock(ClientPromiseImpl.class);
        Client.setClientPromise(clientMock);
    }

    @Test
    void main() throws RemoteException, NotBoundException {
        whenInitilize();
        Client.main(new String[0]);
        verify(sysMock).println("Put your id for this session");
        verify(sysMock).println("Question");
        verify(sysMock).println("Question2");
        verify(sysMock).println("The exam has finished, you have a score of 1/3");
    }

    private void whenInitilize() {
        when(sysMock.readLn()).thenReturn("1234")
                .thenReturn("1")
                .thenReturn("2");
        when(sessionMock.hasNext("1234"))
                .thenReturn(true);
        when(sessionMock.next("1234"))
                .thenReturn("Question")
                .thenReturn("Question2");
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

    @AfterEach
    void tearUp() {
        Client.setRegistry(null);
        Client.setSystem(new AdaptSystem());
        Client.setClientPromise(new ClientPromiseImpl());
    }
}
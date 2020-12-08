package main;

import adaptators.AdaptSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ClientPromiseImplTest {
    private ClientPromiseImpl client;
    private AdaptSystem systemMock;


    @BeforeEach
    void setUp() throws RemoteException {
        systemMock = mock(AdaptSystem.class);
        client = new ClientPromiseImpl();
    }

    @Test
    void finishExam() throws IOException {
        client.finishExam(10, 15);
        assertTrue(client.isExamFinished());
        assertEquals(client.getCorrectAnswers(), 10);
        assertEquals(client.getTotalQuestions(), 15);
    }

    @Test
    void startExam() throws RemoteException {
        client = spy(ClientPromiseImpl.class);
        client.startExam();
        synchronized (client) {
            // notifyAll cannot be tested, as it is implemented by Object
            // so it is called notifyAll from object instead of the Spy.
            // At the end, it explodes when running both tests.
            // verify(client).notifyAll();
        }
        assertTrue(client.isStartExam());
    }


}
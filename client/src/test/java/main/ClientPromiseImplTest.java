package main;

import adaptators.AdaptateSystem;
import common.ClientPromise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ClientPromiseImplTest {
    private ClientPromise client;
    private AdaptateSystem systemMock;


    @BeforeEach
    void setUp() {
        systemMock = mock(AdaptateSystem.class);
        client = new ClientPromiseImpl(systemMock);
    }

    @Test
    void finishExam() throws IOException {
        client.finishExam(10, 15);
        verify(systemMock).exit(0);
        verify(systemMock).println("The exam has finished. Your score was 10/15");
    }

    @Test
    void startExam() {

    }
}
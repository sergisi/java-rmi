package main;

import adaptators.AdaptParse;
import adaptators.AdaptSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class ProfessorTest {


    private AdaptSystem sys;
    private AdaptParse parser;
    private Registry registry;
    private SessionMakerImplementation sessionMaker;
    private PrintWriter printW;

    @BeforeEach
    void setUp() {
        sessionMaker = mock(SessionMakerImplementation.class);
        registry = mock(Registry.class);
        parser = mock(AdaptParse.class);
        sys = mock(AdaptSystem.class);
        Professor.setParser(parser);
        Professor.setRegistry(registry);
        Professor.setSys(sys);
        Professor.setSessionMaker(ignored -> sessionMaker);
        printW = mock(PrintWriter.class);
    }

    @AfterEach()
    void tearUp() {
        Professor.setSys(new AdaptSystem());
        Professor.setParser(new AdaptParse());
        Professor.setRegistry(null);
        Professor.setSessionMaker(SessionMakerImplementation::new);
    }

    @Test
    void main() throws IOException {
        whenInitialize();
        Professor.main(new String[]{"inputFile", "outputFile"});
        verify(sys, times(2)).readLn();
        verify(sys).println("To start the exam press enter");
        verify(sys).println("To finish the exam press enter");
        verify(printW).println("Student Id; Correct Question; Total Questions; Score");
        verify(printW).println("Hola; 2; 3; 6.666666666666667");
    }

    private void whenInitialize() throws IOException {
        when(sys.readLn()).thenReturn("");
        when(sys.getOutputFile("outputFile"))
                .thenReturn(printW);
        when(sessionMaker.getNumberOfQuestions())
                .thenReturn(3);
        when(sessionMaker.getResults()).thenReturn(
                getMockStream()
        );
    }

    private Stream<Map.Entry<String, UserSession>> getMockStream() {
        return Stream.of(new Map.Entry<String, UserSession>() {
                             @Override
                             public String getKey() {
                                 return "Hola";
                             }

                             @Override
                             public UserSession getValue() {
                                 return new UserSession(2, 2);
                             }

                             @Override
                             public UserSession setValue(UserSession user) {
                                 return user;
                             }


                             @Override
                             public boolean equals(Object o) {
                                 return false;
                             }

                             @Override
                             public int hashCode() {
                                 return 0;
                             }
                         }
        );
    }

    @Test
    void mainWith0Arguments() {
        Professor.main(new String[0]);
        verify(sys).println("Please pass two arguments: <inputFile> <outputFile>");
    }
}
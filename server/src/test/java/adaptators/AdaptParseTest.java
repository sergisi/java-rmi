package adaptators;

import main.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdaptParseTest {

    private List<String> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        String questionTextTwoChoices = "Hola Que tal?;Tot Bé;Reguling Regulang;1";
        list.add(questionTextTwoChoices);
        String questionTextThreeChoices = "Hola Que tal?;Tot Bé;Reguling Regulang;Es podria estar millor;1";
        list.add(questionTextThreeChoices);
        String questionTextFourChoices = "Hola Que tal?;Tot Bé;Reguling Regulang;Es podria estar millor;Estoy a tope jefe d'equipo;2";
        list.add(questionTextFourChoices);
    }


    @Test
    void parseQuestionsFile() throws IOException {
        AdaptSystem sys = mock(AdaptSystem.class);
        AdaptParse adaptParse = new AdaptParse(sys);
        when(sys.getContents("filepath")).thenReturn(list);
        List<Question> questions = adaptParse.parseQuestionsFile("filepath");
        assertEquals(3, questions.size());
        assertTrue(questions.get(2).isCorrectAnswer(2));
    }
}
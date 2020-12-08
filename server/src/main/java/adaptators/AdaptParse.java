package adaptators;

import exceptions.BadQuestionException;
import main.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdaptParse {

    private final AdaptSystem system;

    public AdaptParse() {
        this(new AdaptSystem());
    }

    public AdaptParse(AdaptSystem system) {
        this.system = system;
    }

    public List<Question> parseQuestionsFile(String filepath) {
        ArrayList<Question> list = new ArrayList<>();
        try {
            for (String line : system.getContents(filepath)) {
                list.add(Question.parseLine(line));
            }
        } catch (
                IOException | BadQuestionException e) {
            e.printStackTrace();
        }
        return list;
    }
}

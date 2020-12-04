public class Question {

    private String question;
    private Integer correctAnswer;

    public Question(String question, Integer correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public Boolean isCorrectAnswer(Integer choice){
        return correctAnswer == choice;
    }

    public String getQuestion(){
        return question;
    }

    public static Question parseLine(String question){
        return new Question("Hola", 2);
    }

}

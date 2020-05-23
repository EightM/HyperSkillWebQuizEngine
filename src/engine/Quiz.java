package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Quiz {

    private static int idCount = 1;

    private String title;
    private String text;
    private List<String> options;
    @JsonIgnore
    private int answer;
    private final int id;

    public Quiz(String title, String text, int answer, List<String> options) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.id = Quiz.idCount;
        Quiz.idCount++;
    }

    public Quiz(Quiz other) {
        this.title = other.title;
        this.text = other.text;
        this.options = List.copyOf(other.options);
        this.answer = other.answer;
        this.id = Quiz.idCount;
        Quiz.idCount++;
    }

    @JsonIgnore
    public int getAnswer() {
        return answer;
    }

    @JsonProperty("answer")
    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }
}

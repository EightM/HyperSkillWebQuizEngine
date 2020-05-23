package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class Quiz {

    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
    @NotEmpty
    @Size(min = 2)
    private List<String> options;
    @JsonIgnore
    private List<Integer> answer;
    private int id;

    public Quiz() {
        this.answer = new ArrayList<>();
    }

    public Quiz(String title, String text, List<Integer> answer, List<String> options) {
        this.title = title;
        this.text = text;
        this.options = options;
        if (options == null) {
            this.answer = new ArrayList<>();
        } else {
            this.answer = List.copyOf(answer);
        }
    }

    public Quiz(Quiz other) {
        this.title = other.title;
        this.text = other.text;
        this.options = List.copyOf(other.options);
        if (options == null) {
            this.answer = new ArrayList<>();
        } else {
            this.answer = List.copyOf(other.answer);
        }
    }

    @JsonIgnore
    public List<Integer> getAnswer() {
        return answer;
    }

    @JsonProperty("answer")
    public void setAnswer(List<Integer> answer) {
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

    public void setId(int id) {
        this.id = id;
    }
}

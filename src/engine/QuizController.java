package engine;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuizController {

    @GetMapping(path = "/api/quiz")
    public Quiz getQuiz() {
        return createQuiz();
    }

    @PostMapping(path = "/api/quiz")
    public QuizResult solveQuiz(@RequestParam int answer) {
        if (answer == 2) {
            return new QuizResult(true, "Congratulations, you're right!");
        }

        return new QuizResult(false, "Wrong answer! Please, try again.");
    }

    private Quiz createQuiz() {
        String title = "The Java Logo";
        String text = "What is depicted on the Java logo?";
        List<String> options = List.of("Robot", "Tea leaf", "Cup of coffee", "Bug");
        return new Quiz(title, text, options);
    }
}

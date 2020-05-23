package engine;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class QuizController {

    List<Quiz> quizzes = new ArrayList<>();

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz) {
        quizzes.add(quiz);
        quiz.setId(quizzes.indexOf(quiz) + 1);
        return quiz;
    }

    @GetMapping(value = "/api/quizzes/{id}")
    public Quiz getQuizById(@PathVariable int id) {
        Optional<Quiz> findQuiz = quizzes.stream().filter(quiz -> quiz.getId() == id).findFirst();
        if (findQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }

        return findQuiz.get();
    }

    @GetMapping(value = "/api/quizzes")
    public List<Quiz> getAllQuizes() {
        return quizzes;
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public QuizResult solveQuiz(@PathVariable int id, @RequestBody QuizAnswer quizAnswer) {

        if (id - 1 >= quizzes.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found. Current size: " + quizzes.size());
        }

        Quiz quiz = quizzes.get(id - 1);
        if (quiz.getAnswer().equals(quizAnswer.getAnswer())) {
            return new QuizResult(true, "Right!");
        }

        return new QuizResult(false, "Wrong.");
    }
}

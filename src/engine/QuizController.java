package engine;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class QuizController {

    List<Quiz> quizzes = new ArrayList<>();

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        quizzes.add(quiz);

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
    public QuizResult solveQuiz(@PathVariable int id, @RequestParam int answer) {
        Quiz quiz = quizzes.get(id - 1);
        if (quiz.getAnswer() == answer) {
            return new QuizResult(true, "Right!");
        }

        return new QuizResult(false, "Wrong.");
    }
}

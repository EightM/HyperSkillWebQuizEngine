package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class QuizController {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz) {
        quizRepository.save(quiz);
        return quiz;
    }

    @GetMapping(value = "/api/quizzes/{id}")
    public Quiz getQuizById(@PathVariable int id) {
        Optional<Quiz> findQuiz = quizRepository.findById(id);
        if (findQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }

        return findQuiz.get();
    }

    @GetMapping(value = "/api/quizzes")
    public Iterable<Quiz> getAllQuizes() {
        return quizRepository.findAll();
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public QuizResult solveQuiz(@PathVariable int id, @RequestBody QuizAnswer quizAnswer) {

        Optional<Quiz> findQuiz = quizRepository.findById(id);
        if (findQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found.");
        }

        List<Integer> rightAnswers = findQuiz.get().getAnswer();
        if (rightAnswers.size() == quizAnswer.getAnswer().size()
                && rightAnswers.containsAll(quizAnswer.getAnswer())) {
            return new QuizResult(true, "Right!");
        }

        return new QuizResult(false, "Wrong.");
    }
}

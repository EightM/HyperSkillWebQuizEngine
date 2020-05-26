package engine.controllers;

import engine.quiz.Quiz;
import engine.quiz.QuizAnswer;
import engine.quiz.QuizRepository;
import engine.quiz.QuizResult;
import engine.user.User;
import engine.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class QuizController {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public QuizController(QuizRepository quizRepository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.quizRepository = quizRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz, Principal principal) {
        User findUser = userRepository.findByEmail(principal.getName());
        if (findUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found");
        }
        quiz.setUser(findUser);
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

    @PostMapping(path = "/api/register")
    public void registerUser(@Valid @RequestBody User user) {
        User existedUser = userRepository.findByEmail(user.getEmail());
        if (existedUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already registered");
        }


        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/api/quizzes/{id}")
    public void deleteQuiz(@PathVariable int id, Principal principal) {
        User findUser = userRepository.findByEmail(principal.getName());
        if (findUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found");
        }

        Optional<Quiz> findQuiz = quizRepository.findById(id);
        if (findQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found.");
        }

        if (!findQuiz.get().getUser().equals(findUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        quizRepository.delete(findQuiz.get());
    }
}

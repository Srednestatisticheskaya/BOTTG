package ru.tgbot.tgbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tgbot.tgbot.model.JokeCall;
import ru.tgbot.tgbot.repository.JokeRepository;
import ru.tgbot.tgbot.service.JokeService;
import ru.tgbot.tgbot.model.Joke;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("jokes")
@RequiredArgsConstructor
public class JokeController {
    private final JokeService jokeService;
    private final JokeRepository jokeRepository;

    @GetMapping
    public ResponseEntity<List<Joke>> getAllJokes() {
        List<Joke> jokes = jokeService.getAllJokes();
        return ResponseEntity.ok(jokes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Joke> getJokeById(@PathVariable("id") Long id) {
        Optional<Joke> joke = jokeService.getJokesById(id);
        return joke.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity addNewJoke(@RequestBody Joke text) {

        return jokeService.addNewJoke(text).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Joke> updateJoke(@PathVariable Long id, @RequestBody Joke updatedJoke) {
        Joke updatedJokes = jokeService.updateJoke(id, updatedJoke);

        return ResponseEntity.ok(updatedJokes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJoke(@PathVariable Long id) {
            return jokeService.deleteJoke(id);
            }

    @GetMapping("/calls/{id}")
        public ResponseEntity<List<JokeCall>> getJokeCallsByJokeId(@PathVariable("id") Long id) {
            Optional<Joke> joke = jokeService.getJokesById(id);
            if (joke.isPresent()) {
                // Генерируем userId (ваша логика генерации userId)
                Long userId = 1L; // Пример генерации userId

                List<JokeCall> jokeCalls = jokeService.getJokeCallsByJokeId(id, userId);

                return ResponseEntity.ok(jokeCalls);
            } else {
                return ResponseEntity.notFound().build();
            }
    }
    @GetMapping("/top")
    public ResponseEntity<List<Joke>> getTopJokes() {
        List<Joke> topJokes = jokeRepository.findTop5ByOrderByCallsDesc();
        return ResponseEntity.ok(topJokes);
    }

}
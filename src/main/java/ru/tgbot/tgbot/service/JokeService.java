package ru.tgbot.tgbot.service;

import org.springframework.http.ResponseEntity;
import ru.tgbot.tgbot.model.Joke;
import ru.tgbot.tgbot.model.JokeCall;

import java.util.List;
import java.util.Optional;

public interface JokeService {
    List<Joke> getAllJokes();
    Joke updateJoke(Long id, Joke joke);

    Optional<Joke> addNewJoke(Joke json);
    ResponseEntity<String> deleteJoke(Long id);

    Optional<Joke> getJokesById(Long id);
    // В интерфейсе JokeService

    List<JokeCall> getJokeCallsByJokeId(Long id, Long userId);


}
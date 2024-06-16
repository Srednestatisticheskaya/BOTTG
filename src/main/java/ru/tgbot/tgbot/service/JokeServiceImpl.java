package ru.tgbot.tgbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.tgbot.tgbot.model.JokeCall;
import ru.tgbot.tgbot.model.JokeHistory;
import ru.tgbot.tgbot.repository.JokeCallRepository;
import ru.tgbot.tgbot.repository.JokeRepository;
import ru.tgbot.tgbot.model.Joke;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {
    private final JokeRepository jokeRepository;
    private final JokeCallRepository jokeCallRepository;


    @Override
    public Optional<Joke> addNewJoke(Joke newJoke) {
        try {
            newJoke.setJoke(newJoke.getJoke());
            newJoke.setTimeCreated(LocalDate.now());
            newJoke.setTimeUpdated(LocalDate.now());
            if (newJoke.getJokeHistory() == null) {
                newJoke.setJokeHistory(new ArrayList<>());
            }
            newJoke.getJokeHistory().add(new JokeHistory(null, newJoke,new Date()));

            Joke savedJoke = jokeRepository.save(newJoke);

            return Optional.of(savedJoke);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }


    }

    @Override
    public List<Joke> getAllJokes() {
        return jokeRepository.findAll();
    }

    @Override
    public Optional<Joke> getJokesById(Long id) {
        return jokeRepository.findById(id);
    }

    @Override
    public Joke updateJoke(Long id, Joke joke) {
        // Проверяем, существует ли шутка с данным id
        if (jokeRepository.existsById(id)) {
            // Получаем текущую шутку по id
            Optional<Joke> existingJokeOptional = jokeRepository.findById(id);
            if (existingJokeOptional.isPresent()) {
                Joke existingJoke = existingJokeOptional.get();

                // Обновляем данные шутки
                existingJoke.setJoke(joke.getJoke());
                existingJoke.setTimeUpdated(LocalDate.now()); // Обновляем дату обновления

                // Сохраняем обновленную шутку

                return jokeRepository.save(existingJoke);
            } else {
                throw new IllegalArgumentException("Шутка с id=" + id + " не найдена");
            }
        } else {
            throw new IllegalArgumentException("Шутка с id=" + id + " не найдена");
        }
    }

    @Override
    public ResponseEntity<String> deleteJoke(Long id) {
        if (jokeRepository.findById(id).isPresent()) {
            jokeRepository.deleteById(id);
            return ResponseEntity.ok("Ваша шутка про пупу и лупу успешно удалена :) ");
        } else {
            throw new NoSuchElementException("Шутка с " +id + " ID не найдена");
        }
    }

    @Override
    public List<JokeCall> getJokeCallsByJokeId(Long id, Long userId) {
        // Получение списка вызовов анекдота по идентификатору шутки
        List<JokeCall> jokeCalls = jokeCallRepository.findByJokeId(id);

        // Создание записи о вызове анекдота
        JokeCall jokeCall = new JokeCall();
        jokeCall.setJoke(jokeRepository.findById(id).orElse(null)); // Устанавливаем анекдот по его id
        jokeCall.setUserId(userId); // Устанавливаем id пользователя
        jokeCall.setCallTime(LocalDateTime.now()); // Устанавливаем текущее время

        // Сохранение записи о вызове анекдота
        jokeCallRepository.save(jokeCall);

        return jokeCalls;
    }

}











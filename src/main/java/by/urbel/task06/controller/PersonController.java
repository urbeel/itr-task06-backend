package by.urbel.task06.controller;

import by.urbel.task06.entity.Person;
import by.urbel.task06.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping()
    public List<Person> createRandomPeople(@RequestParam Long seed, @RequestParam int limit, @RequestParam int page,
                                           @RequestParam double errors, @RequestParam String locale) {
        return personService.generatePersons(seed+page, limit, errors, locale);
    }
}

package by.urbel.task06.service;

import by.urbel.task06.entity.Person;
import by.urbel.task06.util.ErrorEmulation;
import by.urbel.task06.util.Generator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    public Person generatePerson(Generator generator) {
        String[] firstNameAndGender = generator.generateFirstNameAndGender();
        return Person.builder().id(generator.generateId())
                .firstName(firstNameAndGender[0])
                .lastName(generator.generateLastName(firstNameAndGender[1]))
                .middleName(generator.generateMiddleName(firstNameAndGender[1]))
                .phoneNumber(generator.generatePhoneNumber())
                .address(generator.generateAddress())
                .build();
    }

    public List<Person> generatePersons(long seed, int number, double errors, String locale) {
        List<Person> people = new ArrayList<>();
        Generator generator = new Generator(seed,locale);
        ErrorEmulation errorEmulation = new ErrorEmulation(seed,locale);
        for (int i = 0; i < number; i++) {
            people.add(generatePerson(generator));
        }
        errorEmulation.emulateErrors(people,errors);
        return people;
    }
}

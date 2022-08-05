package by.urbel.task06.util;

import by.urbel.task06.entity.Person;

import java.util.List;

public class ErrorEmulation {
    private final Randomizer randomizer;
    private final String locale;

    private static final String DATA_DIR="/data/";
    private static final String ABC_FILE="/ABC.txt";

    public ErrorEmulation(long seed, String locale) {
        this.randomizer = new Randomizer(seed);
        this.locale = locale;
    }

    public void emulateErrors(List<Person> people, double error) {
        if (error == 0) return;
        double chance = error % 1;
        for (Person person : people) {
            double randomNumber = randomizer.getRandomDouble();
            makeErrors(person, error);
            if (randomNumber < chance) {
                this.makeError(person);
            }
        }
    }

    private void makeErrors(Person person, double error) {
        int errorNumber = (int) error;
        for (int i = 0; i < errorNumber; i++) {
            this.makeError(person);
        }
    }

    private void makeError(Person person) {
        int fieldNumber = randomizer.getRandomInt(0, 5);
        switch (fieldNumber) {
            case 0 -> person.setFirstName(makeErrorInString(person.getFirstName()));
            case 1 -> person.setMiddleName(makeErrorInString(person.getMiddleName()));
            case 2 -> person.setLastName(makeErrorInString(person.getLastName()));
            case 3 -> person.setAddress(makeErrorInString(person.getAddress()));
            case 4 -> person.setPhoneNumber(makeErrorInString(person.getPhoneNumber()));
        }
    }

    private String makeErrorInString(String value) {
        if (value.length() <= 2) {
            return insertRandomChar(value, locale);
        }
        int choice = randomizer.getRandomInt(0, 3);
        switch (choice) {
            case 0 -> value = deleteRandomChar(value);
            case 1 -> value = insertRandomChar(value, locale);
            case 3 -> value = swapWithNearChar(value);
        }
        return value;
    }

    private String insertRandomChar(String value, String locale) {
        String characters = Parser.parseFile(DATA_DIR + locale + ABC_FILE);
        char character = characters.charAt(randomizer.getRandomInt(0, characters.length()));
        int position = randomizer.getRandomInt(0, value.length());
        String firstPart = value.substring(0, position);
        String lastPart = value.substring(position);
        return firstPart + character + lastPart;
    }

    private String deleteRandomChar(String value) {
        int position = randomizer.getRandomInt(0, value.length());
        StringBuilder stringBuilder = new StringBuilder(value);
        stringBuilder.deleteCharAt(position);
        return stringBuilder.toString();
    }

    private String swapWithNearChar(String value) {
        int position = randomizer.getRandomInt(0, value.length());
        int secondPosition = position == 0 ? position + 1 : position - 1;
        return swapChars(value, position, secondPosition);
    }

    private String swapChars(String value, int indexOne, int indexTwo) {
        StringBuilder stringBuilder = new StringBuilder(value);
        char temp = value.charAt(indexOne);
        stringBuilder.setCharAt(indexOne, value.charAt(indexTwo));
        stringBuilder.setCharAt(indexTwo, temp);
        return stringBuilder.toString();
    }
}

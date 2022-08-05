package by.urbel.task06.util;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Generator {
    private final Randomizer randomizer;
    private final List<String[]> firstNamesAndGender;
    private final List<String[]> lastNamesAndGender;
    private final List<String[]> middleNamesAndGender;
    private final List<String[]> citiesAndRegions;
    private final List<String[]> streets;
    private final String locale;

    private static final Map<String, String> countryNames =
            Map.of("ru_RU", "РФ", "pl_PL", "RP", "en_US", "USA");
    private static final String DATA_DIR = "/data/";
    private static final String FIRST_NAMES_FILE = "/firstNames.csv";
    private static final String LAST_NAMES_FILE = "/lastNames.csv";
    private static final String MIDDLE_NAMES_FILE = "/middleNames.csv";
    private static final String CITIES_FILE = "/cities.csv";
    private static final String STREETS_FILE = "/streets.csv";
    private static final String[] RU_COUNTRY_CODES = {"+7", "8"};
    private static final String[] PL_COUNTRY_CODES = {"+48"};
    private static final String[] US_COUNTRY_CODES = {"+1"};
    private static final String HOUSE_NUMBER_RU = "д. ";
    private static final String HOUSE_NUMBER_EMPTY = "";

    private static final String FLAT_NUMBER_RU = "кв. ";
    private static final String FLAT_NUMBER_PL = "m. ";

    public Generator(long seed, String locale) {
        this.randomizer = new Randomizer(seed);
        this.locale = locale;
        this.firstNamesAndGender = Parser.parseCsv(DATA_DIR + locale + FIRST_NAMES_FILE);
        this.lastNamesAndGender = Parser.parseCsv(DATA_DIR + locale + LAST_NAMES_FILE);
        this.middleNamesAndGender = locale.equals("en_US") ? null :
                Parser.parseCsv(DATA_DIR + locale + MIDDLE_NAMES_FILE);
        this.citiesAndRegions = Parser.parseCsv(DATA_DIR + locale + CITIES_FILE);
        this.streets = Parser.parseCsv(DATA_DIR + locale + STREETS_FILE);
    }

    public String generatePhoneNumber() {
        String[] countryPhoneCodes = switch (locale) {
            case "ru_RU" -> RU_COUNTRY_CODES;
            case "pl_PL" -> PL_COUNTRY_CODES;
            case "en_US" -> US_COUNTRY_CODES;
            default -> null;
        };
        String townCode = switch (locale) {
            case "ru_RU", "en_US" -> randomizer.getStrNumber(3);
            case "pl_PL" -> randomizer.getStrNumber(2);
            default -> null;
        };
        if (countryPhoneCodes != null) {
            return randomizer.getRandomItem(countryPhoneCodes) +
                    "(" +
                    townCode +
                    ")" +
                    randomizer.getStrNumber(3) +
                    "-" +
                    randomizer.getStrNumber(2) +
                    "-" +
                    randomizer.getStrNumber(2);
        }
        return "";
    }

    public String generateId() {
        UUID uuid = randomizer.getRandomUUID();
        return String.valueOf(uuid.getMostSignificantBits()).replace("-", "");
    }

    public String[] generateFirstNameAndGender() {
        return randomizer.getRandomItem(firstNamesAndGender);
    }

    public String generateLastName(String gender) {
        List<String> lastNames = lastNamesAndGender.stream().filter(
                item -> item[1].equalsIgnoreCase(gender) || item[1].equalsIgnoreCase("u")
        ).map(elem -> elem[0]).toList();
        return randomizer.getRandomItem(lastNames);
    }

    public String generateMiddleName(String gender) {
        if (locale.equals("en_US")) return "";
        List<String> middleNames = middleNamesAndGender.stream().filter(
                item -> item[1].equalsIgnoreCase(gender) || item[1].equalsIgnoreCase("u")
        ).map(elem -> elem[0]).toList();
        return randomizer.getRandomItem(middleNames);
    }

    public String generateAddress() {
        String[] cityAndRegion = randomizer.getRandomItem(citiesAndRegions);
        String street = randomizer.getRandomItem(streets)[0];
        String wordHouse = locale.equals("ru_RU") ? HOUSE_NUMBER_RU : HOUSE_NUMBER_EMPTY;
        String houseNumber = wordHouse + randomizer.getRandomInt(1, 500);
        String flat = randomizer.getRandomBoolean() ? generateFlat() : "";
        if (locale.equals("en_US")) {
            return houseNumber + " " + street + flat + ", " + cityAndRegion[0] + ", "
                    + cityAndRegion[1]+", "+ countryNames.get(locale);
        } else {
            return countryNames.get(locale) + ", " + cityAndRegion[1] + ", " + cityAndRegion[0] + ", "
                    + street + ", " + houseNumber + flat;
        }
    }

    private String generateFlat() {
        if (locale.equals("en_US")) {
            return ", " + randomizer.getRandomInt(1, 26) + (char) randomizer.getRandomInt(65, 72);
        } else {
            String wordFlat = switch (locale) {
                case "ru_RU" -> FLAT_NUMBER_RU;
                case "pl_PL" -> FLAT_NUMBER_PL;
                default -> "";
            };
            return ", " + wordFlat + randomizer.getRandomInt(1, 200);
        }
    }
}

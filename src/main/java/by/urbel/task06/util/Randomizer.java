package by.urbel.task06.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Randomizer {
    private final Random random;

    public Randomizer(long seed) {
        this.random = new Random(seed);
    }

    public <T> T getRandomItem(List<T> list) {
        if (!list.isEmpty()) {
            return list.get(random.nextInt(list.size()));
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Provided list is empty");
        }
    }

    public <T> T getRandomItem(T[] list) {
        if (list.length != 0) {
            return list[random.nextInt(list.length)];
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Provided list is empty");
        }
    }

    public String getStrNumber(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }

    public boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public int getRandomInt(int min, int max) {
        if (min==max){
            return min;
        }
        return random.nextInt(max - min) + min;
    }

    public double getRandomDouble() {
        return random.nextDouble();
    }

    public UUID getRandomUUID() {
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return UUID.nameUUIDFromBytes(bytes);
    }
}

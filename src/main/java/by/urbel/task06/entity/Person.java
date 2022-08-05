package by.urbel.task06.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String address;
}

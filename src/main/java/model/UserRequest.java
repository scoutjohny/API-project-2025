package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class UserRequest {


    private String title;
    @JsonProperty("firstName")
    private String first_name;
    private String lastName;
    private String picture;
    private String gender;
    private String email;
    private String dateOfBirth;
    private String phone;
    private UserLocation location;

    public static UserRequest createUser(){
        Faker faker = new Faker(new Locale("en-US"));

        String prefix = faker.name().prefix();
        if(prefix.equalsIgnoreCase("Ms.")){
            prefix = "ms";
        }else if(prefix.equalsIgnoreCase("Miss")){
            prefix = "miss";
        } else {
            prefix = "mr";
        }

        String gender = "male";
        if(prefix.equalsIgnoreCase("ms")||prefix.equalsIgnoreCase("miss")){
            gender = "female";
        }

        UserLocation location = UserLocation.builder()
                .street(faker.address().streetAddress())
                .city(faker.address().cityName())
                .state(faker.address().state())
                .country(faker.address().country())
                .timeZone(faker.address().timeZone())
                .build();

        UserRequest user = UserRequest.builder()
                .title(prefix)
                .first_name(faker.name().firstName())
                .lastName(faker.name().lastName())
                .picture(faker.internet().image(faker.number().randomDigitNotZero(),faker.number().randomDigitNotZero(),faker.random().nextBoolean(),faker.number().randomDigitNotZero()+".jpg"))
                .gender(gender)
                //.gender(faker.demographic().sex().toLowerCase())
                .email(faker.internet().emailAddress())
                .dateOfBirth(String.valueOf(LocalDateTime.of(faker.number().numberBetween(1945,2005),faker.number().numberBetween(1,12),faker.number().numberBetween(1,28),faker.number().numberBetween(0,23),faker.number().numberBetween(0,59),faker.number().numberBetween(0,59))))
                .phone(faker.phoneNumber().cellPhone())
                .location(location)
                .build();

        return user;
    }
}

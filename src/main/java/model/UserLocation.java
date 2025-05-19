package model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With

public class UserLocation {
    private String street;
    private String city;
    private String state;
    private String country;
    private String timeZone;
}

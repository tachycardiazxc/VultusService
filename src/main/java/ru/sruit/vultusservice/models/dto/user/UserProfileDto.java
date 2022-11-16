package ru.sruit.vultusservice.models.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfileDto {

    private String firstName;
    private String lastName;
    private String surname;
    private String phone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthday;
    private String position;
    private String subdivision;
    private String workPhone;
    private int workPlace;
    private String description;
    private String statusName;
    private String statusText;
    private boolean superBusy;
    private String photoUrl;

}

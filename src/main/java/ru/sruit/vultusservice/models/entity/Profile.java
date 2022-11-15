package ru.sruit.vultusservice.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private String firstName;
    private String lastName;
    private String surname;
    private String phone;
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

    public Profile(User user,
                   String firstName,
                   String lastName,
                   String surname,
                   String phone,
                   LocalDate birthday,
                   String position,
                   String subdivision,
                   String workPhone,
                   int workPlace,
                   String description,
                   String statusName,
                   String statusText,
                   boolean superBusy,
                   String photoUrl) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.surname = surname;
        this.phone = phone;
        this.birthday = birthday;
        this.position = position;
        this.subdivision = subdivision;
        this.workPhone = workPhone;
        this.workPlace = workPlace;
        this.description = description;
        this.statusName = statusName;
        this.statusText = statusText;
        this.superBusy = superBusy;
        this.photoUrl = photoUrl;
    }
}

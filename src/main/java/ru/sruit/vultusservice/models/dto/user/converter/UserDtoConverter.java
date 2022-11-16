package ru.sruit.vultusservice.models.dto.user.converter;

import ru.sruit.vultusservice.models.dto.user.UserProfileDto;
import ru.sruit.vultusservice.models.entity.Profile;

public class UserDtoConverter {

    public static UserProfileDto profileEntityToDto(Profile profile) {
        return UserProfileDto.builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .surname(profile.getSurname())
                .phone(profile.getPhone())
                .birthday(profile.getBirthday())
                .position(profile.getPosition())
                .subdivision(profile.getSubdivision())
                .workPhone(profile.getWorkPhone())
                .workPlace(profile.getWorkPlace())
                .description(profile.getDescription())
                .statusName(profile.getStatusName())
                .statusText(profile.getStatusText())
                .superBusy(profile.isSuperBusy())
                .photoUrl(profile.getPhotoUrl())
                .build();
    }

}

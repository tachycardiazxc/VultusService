package ru.sruit.vultusservice.services.entity;

import ru.sruit.vultusservice.models.entity.Profile;

public interface ProfileService {

    Profile getProfileByUserUsername(String username);

}

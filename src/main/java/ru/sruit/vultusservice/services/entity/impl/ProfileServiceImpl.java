package ru.sruit.vultusservice.services.entity.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sruit.vultusservice.models.entity.Profile;
import ru.sruit.vultusservice.repositories.ProfileRepository;
import ru.sruit.vultusservice.services.entity.ProfileService;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public Profile getProfileByUserUsername(String username) {
        return profileRepository.getProfileByUserUsername(username);
    }
}

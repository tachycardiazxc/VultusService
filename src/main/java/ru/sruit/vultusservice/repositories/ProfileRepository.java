package ru.sruit.vultusservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sruit.vultusservice.models.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT p FROM Profile p JOIN FETCH p.user WHERE p.user.username = :username")
    Profile getProfileByUserUsername(String username);

}

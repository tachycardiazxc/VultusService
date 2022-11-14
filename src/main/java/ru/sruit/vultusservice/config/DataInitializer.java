package ru.sruit.vultusservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.sruit.vultusservice.models.entity.Profile;
import ru.sruit.vultusservice.models.entity.Role;
import ru.sruit.vultusservice.models.entity.User;
import ru.sruit.vultusservice.repositories.ProfileRepository;
import ru.sruit.vultusservice.repositories.RoleRepository;
import ru.sruit.vultusservice.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

@ConditionalOnExpression("${vultus.runInitializer:true}")
@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;


    private static final int AMOUNT_OF_ADMINS = intInRange(3, 5);
    private static final int AMOUNT_OF_USERS = intInRange(15, 25);

    private final int PROGRESS_BAR_STEPS = (AMOUNT_OF_ADMINS) + (AMOUNT_OF_USERS);

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
    }

    private static int intInRange(int startBorder, int endBorder) {
        Random random = new Random(System.nanoTime());
        return startBorder + random.nextInt(endBorder - startBorder);
    }

    private void progressBar(int remain) {
        char incomplete = '░';
        char complete = '█';

        int barLength = 25;
        int remainPercent = ((100 * remain) / PROGRESS_BAR_STEPS);
        int step = 100 / barLength;

        String barStart = "[";
        String barEnd = "]";

        StringBuilder sb = new StringBuilder();
        Stream.generate(() -> incomplete).limit(barLength).forEach(sb::append);
        for (int i = 0; i < remainPercent / step; i++) {
            sb.replace(i, i + 1, String.valueOf(complete));
        }
        sb.append(barEnd).append(" - ").append(remainPercent).append("%");
        System.out.print("\r" + barStart + sb);
        if (PROGRESS_BAR_STEPS == remain) {
            System.out.println("\n");
        }
    }

    @PostConstruct
    public void run() {
        Role adminRole = roleRepository.save(new Role("ADMIN"));
        Role userRole = roleRepository.save(new Role("USER"));

        int[] remain = {0};

        System.out.println("Running test data initializer");

        // creating admin
        Stream.iterate(0, x -> x + 1)
                .limit(AMOUNT_OF_ADMINS)
                .forEach(x -> {
                    progressBar(++remain[0]);

                    User user = userRepository.save(new User(
                            "adminUsername" + x,
                            "adminPdw" + x,
                            Set.of(userRole, adminRole)));

                    // creating profile for admin
                    profileRepository.save(new Profile(
                            user,
                            "adminFirstName" + x,
                            "adminLastName" + x,
                            "adminSurname" + x,
                            "adminPhone" + x,
                            LocalDate.now().minusYears(intInRange(18, 50)),
                            "adminPosition" + x,
                            "adminSubdivision" + x,
                            "adminWorkPhone" + x,
                            x,
                            "adminDesc" + x,
                            "adminStatusName" + x,
                            "adminStatusText" + x,
                            intInRange(0, 1) == 1,
                            "adminPhotoUrl" + x
                    ));
                });

        // creating user
        Stream.iterate(0, x -> x + 1)
                .limit(AMOUNT_OF_USERS)
                .forEach(x -> {
                    progressBar(++remain[0]);

                    User user = userRepository.save(new User(
                            "userUsername" + x,
                            "userPdw" + x,
                            Set.of(userRole)));

                    // creating profile for user
                    profileRepository.save(new Profile(
                            user,
                            "userFirstName" + x,
                            "userLastName" + x,
                            "userSurname" + x,
                            "userPhone" + x,
                            LocalDate.now().minusYears(intInRange(18, 50)),
                            "userPosition" + x,
                            "userSubdivision" + x,
                            "userWorkPhone" + x,
                            x,
                            "userDesc" + x,
                            "userStatusName" + x,
                            "userStatusText" + x,
                            intInRange(0, 1) == 1,
                            "userPhotoUrl" + x
                    ));
                });

    }

}

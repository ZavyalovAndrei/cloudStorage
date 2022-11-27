package com.cloudStorage.initialisation;

import com.cloudStorage.exeptions.Errors;
import com.cloudStorage.properties.StorageProperties;
import com.cloudStorage.repository.RoleRepository;
import com.cloudStorage.model.EntityUser;
import com.cloudStorage.model.EntityRole;
import com.cloudStorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class createUser implements ApplicationRunner {

    private static final String USER1_LOGIN = "user@internet.com";
    private static final String USER1_PASSWORD = "hadgehog";
    private static final String USER1_ROLE = "ROLE_USER";
    private static final String USER2_LOGIN = "user2@global.net";
    private static final String USER2_PASSWORD = "jerboa";
    private static final String USER2_ROLE = "ROLE_ADMIN";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Path location;

    private final PasswordEncoder bcryptEncoder;

    @Autowired
    public createUser(UserRepository userRepository,
                      RoleRepository roleRepository, StorageProperties storageProperties, PasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.location = Paths.get(storageProperties.getRootLocation());
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        var roles = Stream.of(EntityRole.builder().role(USER1_ROLE).build(), EntityRole.builder().role(USER2_ROLE).build())
                .toList();

        roleRepository.saveAll(roles);

        var users = Stream.of(EntityUser.builder().login(USER1_LOGIN).password(bcryptEncoder
                                .encode(USER1_PASSWORD)).entityRole(roles.get(0)).build(),
                        EntityUser.builder().login(USER2_LOGIN).password(bcryptEncoder.encode(USER2_PASSWORD))
                                .entityRole(roles.get(1)).build())
                .toList();

        userRepository.saveAll(users);
        userRepository.findAll().forEach(entityUserPDO -> {
            try {
                Files.createDirectories(location.resolve(entityUserPDO.getLogin()));
            } catch (IOException e) {
                throw new RuntimeException(Errors.USER_DIRECTORIES_NOT_CREATED.value() + e.getMessage());
            }
            System.out.println(entityUserPDO);
        });
    }
}
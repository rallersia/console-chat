package edu.school21.server.services;

import edu.school21.server.models.User;
import edu.school21.server.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsersServiceImpl implements UsersService{
    private final UsersRepository<User> repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository<User> repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public operationStatus signUpUser(User user) {
        if (!repository.findByUsername(user.getUsername()).isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            repository.save(user);
            return operationStatus.SUCCESS;
        } else {
            return operationStatus.FAIL;
        }
    }

    @Override
    public operationStatus singInUser(User user) {
        Optional<User> userInTable = repository.findByUsername(user.getUsername());

        if (!userInTable.isPresent()) {
            return operationStatus.FAIL;
        } else {
            if (passwordEncoder.matches(user.getPassword(), userInTable.get().getPassword())) {
                user.setIdentifier(userInTable.get().getIdentifier());
                return operationStatus.SUCCESS;
            } else {
                return operationStatus.FAIL;
            }
        }
    }

    @Override
    public operationStatus sendMessage(User user, String message) {
        return null;
    }
}

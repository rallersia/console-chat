package edu.school21.server.services;

import edu.school21.server.models.User;

public interface UsersService {
    public operationStatus signUpUser(User user);
    public operationStatus singInUser(User user);
    public operationStatus sendMessage(User user, String message);
}



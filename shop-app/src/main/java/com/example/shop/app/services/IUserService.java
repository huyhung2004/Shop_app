package com.example.shop.app.services;

import com.example.shop.app.DTO.UserDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.exception.InvalidParamException;
import com.example.shop.app.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws  Exception;
    String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException;
}

package com.example.shop.app.controller;

import com.example.shop.app.DTO.UserDTO;
import com.example.shop.app.DTO.UserLoginDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.exception.InvalidParamException;
import com.example.shop.app.models.User;
import com.example.shop.app.services.IUserService;
import com.example.shop.app.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMessages= result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if (!userDTO.getPassword().equals(userDTO.getReTypePassword())){
                return ResponseEntity.badRequest().body("password does not match");
            }
            User user= userService.createUser(userDTO);
            return ResponseEntity.ok(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (DataNotFoundException | InvalidParamException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}

package com.example.shop.app.services;

import com.example.shop.app.DTO.UserDTO;
import com.example.shop.app.conponents.JwtTokenUtil;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.exception.InvalidParamException;
import com.example.shop.app.exception.PermissionDenyException;
import com.example.shop.app.models.Role;
import com.example.shop.app.models.User;
import com.example.shop.app.repositories.RoleRepository;
import com.example.shop.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber=userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role=roleRepository.findById(userDTO.getRoleId()).orElseThrow(()->new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("You can't register an admin account");
        }
        User newUser=User.builder()
                .fullName(userDTO.getFullname())
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAcooutId())
                .build();
        newUser.setRoleId(role);
        //kiểm tra nếu có account id,không yêu cầu password
        if (userDTO.getFacebookAccountId()==0&&userDTO.getGoogleAcooutId()==0){
            String password=userDTO.getPassword();
            String encodedPassword=passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException {
        Optional<User> optionalUser= userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phone number/ password");
        }
        User existingUser =optionalUser.get();
        if (existingUser.getFacebookAccountId()==0&&existingUser.getGoogleAccountId()==0){
            if (!passwordEncoder.matches(password,existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(phoneNumber,password,existingUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}

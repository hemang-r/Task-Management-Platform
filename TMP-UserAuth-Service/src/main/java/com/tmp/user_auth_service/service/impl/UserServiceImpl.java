package com.tmp.user_auth_service.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tmp.user_auth_service.dao.UserDao;
import com.tmp.user_auth_service.dto.LoginRequest;
import com.tmp.user_auth_service.dto.LoginResponse;
import com.tmp.user_auth_service.dto.UserProfileResponse;
import com.tmp.user_auth_service.dto.UserRegistrationRequest;
import com.tmp.user_auth_service.entity.User;
import com.tmp.user_auth_service.exception_handler.BadRequestException;
import com.tmp.user_auth_service.exception_handler.ResourceNotFoundException;
import com.tmp.user_auth_service.service.UserService;
import com.tmp.user_auth_service.utills.ApplicationConstants;
import com.tmp.user_auth_service.utills.CommonServices;
import com.tmp.user_auth_service.utills.TokenUtil;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final CommonServices commonServices;
    private final TokenUtil tokenUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserDao userDao,
            CommonServices commonServices,
            TokenUtil tokenUtil,
            PasswordEncoder passwordEncoder) {

        this.userDao = userDao;
        this.commonServices = commonServices;
        this.tokenUtil = tokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<Object> userRegister(
            UserRegistrationRequest request) {

        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        User existingUser =
                userDao.findByEmailAndIsActiveTrue(
                        request.getEmail());

        if (existingUser != null) {
            throw new BadRequestException(
                    "User already exists");
        }

        String hashedPassword =
                passwordEncoder.encode(
                        request.getPassword());

        User user = new User();

        BeanUtils.copyProperties(request, user);

        user.setPasswordHash(hashedPassword);
        user.setRole("USER");
        user.setIsActive(true);

        userDao.save(user);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        "User successfully registered"));
    }

    @Override
    public ResponseEntity<Object> userLogin(
            LoginRequest loginRequest) {

        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        User user =
                userDao.findByEmailAndIsActiveTrue(
                        loginRequest.getEmail());

        if (user == null) {
            throw new BadRequestException(
                    "Invalid email or password");
        }

        boolean passwordMatched =
                passwordEncoder.matches(
                        loginRequest.getPassword(),
                        user.getPasswordHash());

        if (!passwordMatched) {
            throw new BadRequestException(
                    "Invalid email or password");
        }

        String token =
                tokenUtil.generateToken(user);

        LoginResponse response =
                new LoginResponse(
                        token,
                        user.getId().toString(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getRole());

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        response));
    }

    @Override
    public ResponseEntity<Object> getMyProfile() {

        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        User user =
                userDao.findByEmailAndIsActiveTrue(
                        email);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found");
        }

        UserProfileResponse response =
                buildUserProfileResponse(user);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        response));
    }

    @Override
    public ResponseEntity<Object> getUserById(
            UUID id) {

        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        User user =
                userDao.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ResourceNotFoundException(
                    "User not found");
        }

        UserProfileResponse response =
                buildUserProfileResponse(user);

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        response));
    }

    @Override
    public ResponseEntity<Object> getAllUsers() {

        LOGGER.info(ApplicationConstants.ENTER_LABEL);

        List<UserProfileResponse> users =
                userDao.findAll()
                       .stream()
                       .filter(user ->
                               Boolean.TRUE.equals(
                                       user.getIsActive()))
                       .map(this::buildUserProfileResponse)
                       .collect(Collectors.toList());

        LOGGER.info(ApplicationConstants.EXIT_LABEL);

        return ResponseEntity.ok(
                commonServices.generateGenericSuccessResponse(
                        users));
    }

    private UserProfileResponse buildUserProfileResponse(
            User user) {

        UserProfileResponse response =
                new UserProfileResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        response.setIsActive(user.getIsActive());

        return response;
    }
}

package com.online.shopping.service;

import com.online.shopping.entity.UserInfo;
import com.online.shopping.exception.ResourceNotFoundException;
import com.online.shopping.repository.UserRepository;
import com.online.shopping.util.OnlineShoppingAppUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.online.shopping.util.OnlineShoppingAppConstants.USER_SAVED_SUCCESSFULLY;

@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;

    /**
     * Constructor Injection - All Argument Constructor
     * @param userRepository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method to get the User by UsedId
     * @param userId
     * @return
     */
    public Optional<UserInfo> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    /**
     * Method to save user to DB
     * @param userInfo
     * @return
     */
    public Optional<String> saveUser(UserInfo userInfo) {
        log.info("Started UserService  saveUser() method calling..");
        userInfo.setId(OnlineShoppingAppUtility.generateUserId(15));
        Optional<UserInfo> userInfoObject = userRepository.findById(userInfo.getId());
        if (!userInfoObject.isEmpty()) {
            log.debug("User " + userInfo.getName() + "  is already available in database !");
            throw new ResourceNotFoundException("User " + userInfo.getName() + "  is already available in database !");
        }
        UserInfo user = new UserInfo(userInfo.getId(), userInfo.getName(),
                userInfo.getEmail(), userInfo.getPassword(), userInfo.getRoles());
        userRepository.save(user);
        log.debug("User Object Successfully Saved to DB", user);

        log.info("Exiting | UserService  saveUser() method calling..");
        return Optional.of(USER_SAVED_SUCCESSFULLY);
    }

    /**
     * Method to retrieve all the available users in DB
     * @return
     */
    public List<UserInfo> getAllUsers() {
        log.info("Started UserService  getAllUsers() method calling..");
        List<UserInfo> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("Users are not available in DB !");
        }
        log.info("Exiting | UserService  getAllUsers() method calling..");
        return users;
    }
}

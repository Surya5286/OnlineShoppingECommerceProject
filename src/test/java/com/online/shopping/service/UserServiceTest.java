package com.online.shopping.service;

import com.online.shopping.entity.UserInfo;
import com.online.shopping.exception.ResourceNotFoundException;
import com.online.shopping.repository.UserRepository;
import com.online.shopping.util.OnlineShoppingAppUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserInfo userInfo;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository);
        userInfo = new UserInfo("testUserInfoId", "testName", "test.email@gmail.com", "testPassword", "test-user");
    }

    @DisplayName("Junit Test case for SaveUser method - returns Success Message String")
    @Test
    void givenUserObject_whenSaveUser_thenReturnSuccessMessageString() {
        // given - precondition or setup
        given(userRepository.findById("testUserInfoId"))
                .willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        userRepository.save(userInfo);

        // then - verify the output
        verify(userRepository, times(1)).save(userInfo);
    }

    @DisplayName("Junit Test case for SaveUser method - throws Exception (negative scenario)")
//    @Test
    void givenUserObject_whenSaveUser_thenThrowsException() {
        // given - precondition or setup

        MockedStatic<OnlineShoppingAppUtility> mockedStatic = mockStatic(OnlineShoppingAppUtility.class);
        mockedStatic.when(() -> OnlineShoppingAppUtility.generateUserId(15)).thenReturn("testUserInfoId");

        given(userRepository.findById(userInfo.getId()))
                .willReturn(Optional.of(userInfo));

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.saveUser(userInfo);
        });

        // then - verify the output
        verify(userRepository, never()).save(any(UserInfo.class));
    }

    @DisplayName("Junit Test case for GetAllUsers method - should return UserInfo List")
    @Test
    void givenUserList_whenGetAllUsers_thenReturnUserList() {
        // given - precondition or setup
        given(userRepository.findAll()).willReturn(List.of(userInfo));

        // when -  action or the behaviour that we are going test
        List<UserInfo> userList = userService.getAllUsers();

        // then - verify the output
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(1);
    }

    @DisplayName("Junit Test case for GetAllUsers method - throws exception (negative scenario)")
    @Test
    void givenUserList_whenGetAllUsers_thenThrowsException() {
        // given - precondition or setup
        given(userRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getAllUsers();
        });

        // then - verify the output
        verify(userRepository, times(1)).findAll();
    }

}
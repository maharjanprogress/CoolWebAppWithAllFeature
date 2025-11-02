package com.progress.coolProject.Utils;

import com.progress.coolProject.DTO.LoginAndRegistration.UserDTO;
import com.progress.coolProject.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConvertUserUtils {
    public UserDTO convertUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoleId(user.getRole().getId());
        userDTO.setPassword("********");
        return userDTO;
    }
    public List<UserDTO> convertUsersToDTO(List<User> users) {
        return users.stream()
                .map(this::convertUserToDTO)
                .toList();
    }
}

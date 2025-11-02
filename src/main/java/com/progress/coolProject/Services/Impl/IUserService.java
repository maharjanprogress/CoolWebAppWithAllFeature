package com.progress.coolProject.Services.Impl;


import com.progress.coolProject.DTO.LoginAndRegistration.LoginDTO;
import com.progress.coolProject.DTO.LoginAndRegistration.LoginOutput;
import com.progress.coolProject.DTO.LoginAndRegistration.UserDTO;

import java.util.List;

public interface IUserService {
    void createUser(UserDTO userDTO);
    void updateUser(Long id,UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    LoginOutput verify(LoginDTO dto);
}

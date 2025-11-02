package com.progress.coolProject.Services.Users;

import com.progress.coolProject.DTO.LoginAndRegistration.LoginDTO;
import com.progress.coolProject.DTO.LoginAndRegistration.LoginOutput;
import com.progress.coolProject.DTO.LoginAndRegistration.UserDTO;
import com.progress.coolProject.Entity.Role;
import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Repo.RoleRepo;
import com.progress.coolProject.Repo.UserRepo;
import com.progress.coolProject.Services.Impl.IUserService;
import com.progress.coolProject.Services.JWT.JWTService;
import com.progress.coolProject.Utils.ConvertUserUtils;
import com.progress.coolProject.Utils.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    AuthenticationManager authmanager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ConvertUserUtils convertUserUtils;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Override
    public void createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepo.findByUserName(userDTO.getUserName());
        if (existingUser.isPresent()) {
            throw new DuplicateKeyException("Username already exists");
        }
        Optional<Role> role = roleRepo.findById(userDTO.getRoleId());
        if(role.isEmpty()){
            throw new IllegalArgumentException("Role not found");
        }
        boolean emailValid = EmailValidator.isValidEmail(userDTO.getEmail());
        if (!emailValid) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (userDTO.getFirstName() == null || userDTO.getLastName() == null || userDTO.getUserName() == null || userDTO.getPassword() == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserName(userDTO.getUserName());
        user.setPasswordHash(encoder.encode(userDTO.getPassword()));
        user.setRole(role.get());
        user.setEmail(userDTO.getEmail());
        userRepo.save(user);
    }

    @Override
    public void updateUser(Long id, UserDTO userDTO) {

    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public UserDTO getUserById(Long id) {
        return convertUserUtils.convertUserToDTO(userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return convertUserUtils.convertUsersToDTO(userRepo.findAll());
    }

    @Override
    public LoginOutput verify(LoginDTO dto) {
        Authentication authentication = authmanager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword()));
        if (authentication.isAuthenticated()) {
            User user = userRepo.findByUserName(dto.getUserName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String token = jwtService.generateToken(dto.getUserName(), user.getId());
            LoginOutput output = new LoginOutput();
            output.setRole(user.getRole().getRoleAlias());
            output.setUserName(user.getUserName());
            output.setToken(token);
            output.setUserId(user.getId());
            return output;
        }
        else return null;
    }
}

package com.progress.coolProject.Utils;

import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Filter.JWTFilter;
import com.progress.coolProject.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUser {
    private final UserRepo userRepo;

    public User getCurrentUser(){
        Long currentUserId = JWTFilter.getCurrentUserId();
        if(currentUserId != null){
            return userRepo.findById(currentUserId).orElse(null);
        }
        return null;
    }

}

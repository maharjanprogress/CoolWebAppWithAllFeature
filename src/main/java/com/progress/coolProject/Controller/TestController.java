package com.progress.coolProject.Controller;

import com.progress.coolProject.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public ResponseEntity<ResponseDTO> getTest() {
        return new ResponseEntity<>(ResponseDTO.success("success"), HttpStatus.OK);
    }

}

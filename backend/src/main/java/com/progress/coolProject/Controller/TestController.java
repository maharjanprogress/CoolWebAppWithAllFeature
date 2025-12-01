package com.progress.coolProject.Controller;

import com.progress.coolProject.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test/get")
    public ResponseEntity<ResponseDTO> getTest() {
        return new ResponseEntity<>(ResponseDTO.success("success"), HttpStatus.OK);
    }

    @MessageMapping("/message")
    @SendTo("/topic/reply")
    public String processMessageFromClient(String message) throws Exception {
        // Simulate processing delay
        Thread.sleep(1000);
        System.out.println(message);
        return "Test Server receives: " + message;
    }

}

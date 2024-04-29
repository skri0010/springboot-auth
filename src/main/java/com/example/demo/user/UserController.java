package com.example.demo.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRespository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService; // Inject KafkaProducerService

    @GetMapping
    public List<User> getAllUsers() {
        kafkaProducerService.sendMessage("All users retrieved!");
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        kafkaProducerService.sendMessage("User with Name : " + user.getFirstName() + " retrieved"); // Publish message
        return user;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        kafkaProducerService.sendMessage("User created: " + savedUser.getId()); // Publish message to Kafka
        return savedUser;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            User updatedUser = userRepository.save(existingUser);
            kafkaProducerService.sendMessage("User updated: " + updatedUser.getId()); // Publish message to Kafka
            return updatedUser;
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            kafkaProducerService.sendMessage("User deleted: " + id); // Publish message to Kafka
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }
}

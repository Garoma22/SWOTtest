package pack1.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pack1.models.Car;
import pack1.models.DTO.UserDTO;
import pack1.models.User;
import pack1.services.Service;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class Controller {
    private final Service service;

    @Autowired
    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/showAll")
    public List<User> getAllUsers() {
        List<User> users = service.showAll();
        return users;
    }

    @PostMapping("/addUser")
    public void addNewUser(@Valid @RequestBody User user) {
        service.addUser(user);
    }

    @PostMapping("/addCar")
    public void addCarToUser(@RequestBody Car car, @RequestParam Long user_id) {
        service.addNewCarToUser(car, user_id);
    }

    @DeleteMapping("/removeUser")
    public void removeUserById(@RequestParam Long user_id) {
        service.removeUser(user_id);
    }

    //For variety, here we delete not by ID but by name
    @DeleteMapping("/removeCar")
    public void removeCar(@RequestParam String car_name, @RequestParam String user_last_name) {
        service.removeCar(car_name, user_last_name);
    }

    @DeleteMapping("/removeAllCarsFromUser")
    public void removeAllCars(@RequestParam Long user_id) {
        service.deleteAllCars(user_id);
    }

    @PatchMapping("/updateUserEmail")
    public void updateUserEmail(@RequestParam Long id, @RequestParam String newEmail)
    {
        service.changeEmail(id, newEmail);
    }
    @PatchMapping("/updateCarData")
    public void updateCar(@RequestParam Long id, @RequestParam Long price){
        service.changeCarPrice(id, price);
    }

    @GetMapping("/allUserData")
    public UserDTO getAllUserData(@RequestParam Long id){
        return service.getUserData(id);
    }
}


package pack1.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pack1.models.Car;
import pack1.models.DTO.CarDTO;
import pack1.models.DTO.UserDTO;
import pack1.models.User;
import pack1.repositories.CarRepository;
import pack1.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Service {

    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Autowired
    public Service(UserRepository userRepository, CarRepository carRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    public List<User> showAll() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }


    public ResponseEntity<?> addNewCarToUser(Car car, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = optionalUser.get();
        car.setUser(user);
        carRepository.save(car);

        return ResponseEntity.ok("Car added to user successfully");
    }

    public ResponseEntity<?> removeUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
            return ResponseEntity.ok("User removed successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

    }

    public void removeCar(String carName, String userLastName) {
            Optional<User> optionalUser = userRepository.findByUserLastName(userLastName);
            Optional<Car> optionalCar = carRepository.findByCarName(carName);
            if (optionalUser.isPresent() && optionalCar.isPresent()) {
                User user = optionalUser.get();
                Car car = optionalCar.get();
                if (car.getUser().equals(user)) {
                    carRepository.delete(car);
                }
            }
    }

    public void deleteAllCars(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            carRepository.deleteAll(carRepository.findAllCarsById(user));
        }
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void changeEmail(Long id, String newEmail) {
        Optional<User> optionalUser = getUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(newEmail);
            userRepository.save(user);
        }
    }

    public void changeCarPrice(Long id, Long price) {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.setPrice(Math.toIntExact(price));
            carRepository.save(car);
        }
    }

    public UserDTO getUserData(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId((long) user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());

            List<Car> cars = carRepository.findByUser(user);
            List<CarDTO> carDTOs = new ArrayList<>();
            for (Car car : cars) {
                CarDTO carDTO = new CarDTO();
                carDTO.setId((long) car.getId());
                carDTO.setName(car.getName());
                carDTO.setPrice(car.getPrice());
                carDTOs.add(carDTO);
            }
            userDTO.setCars(carDTOs);
            return userDTO;
        } else {
            return null;
        }
    }
}







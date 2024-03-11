package pack1.services;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pack1.models.Car;
import pack1.models.User;
import pack1.repositories.CarRepository;
import pack1.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @InjectMocks
    private Service service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CarRepository carRepository;

    @Test
    void showAllTest(){

        User user1 = new User("Ivanov","Ivan","ivanov@gmail.com");
        User user2 = new User("Petrov","Petr","petrov@gmail.com");
        List<User> expectedUsers = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = service.showAll();
        verify(userRepository, times(1)).findAll();
        Assertions.assertEquals(expectedUsers.size(), actualUsers.size());
        Assertions.assertEquals(expectedUsers.get(0), actualUsers.get(0));
        Assertions.assertEquals(expectedUsers.get(1), actualUsers.get(1));


    }


    @Test
    void addUserTest(){
        User user = new User("Ivanov","Ivan","ivanov@gmail.com");
        when(userRepository.save(user)).thenReturn(user);
        service.addUser(user);
        verify(userRepository, times(1)).save(user);
    }
    @Test
    public void testAddNewCarToUser_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> response = service.addNewCarToUser(new Car(), 1L);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"), response);
        verify(carRepository, never()).save(any());
    }

    @Test
    public void testAddNewCarToUser_WhenUserExists() {
        User user = new User("Ivanov","Ivan","ivanov@gmail.com");
        user.setId(1);
        Car car = new Car("Jaguar",25000,user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<?> response = service.addNewCarToUser(car, 1L);
        verify(carRepository, times(1)).save(car);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body("Car added to user successfully"), response);

    }

    @Test
    public void testRemoveUser_WhenUserExists() {
        User user = new User("Ivanov","Ivan","ivanov@gmail.com");
        user.setId(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<?> response = service.removeUser(1L);
        verify(userRepository, times(1)).delete(user);
        assertEquals(ResponseEntity.ok("User removed successfully"), response);
    }

    @Test
    public void testRemoveUser_WhenUserDoesNotExist() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> response = service.removeUser(1L);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"), response);
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void testRemoveCar_WhenUserAndCarExist() {
        User user = new User("Ivan","Ivanov","ivanov@gmail.com");
        Car car = new Car("Toyota",25000,user);
        when(userRepository.findByUserLastName("Ivanov")).thenReturn(Optional.of(user));
        when(carRepository.findByCarName("Toyota")).thenReturn(Optional.of(car));
        service.removeCar("Toyota", "Ivanov");
        verify(carRepository, times(1)).delete(car);
    }

    @Test
    public void testRemoveCar_WhenUserOrCarDoNotExist() {
        when(userRepository.findByUserLastName("Doe")).thenReturn(Optional.empty());
        when(carRepository.findByCarName("Toyota")).thenReturn(Optional.of(new Car()));
        service.removeCar("Toyota", "Doe");
        verify(carRepository, never()).delete(any());
    }

    @Test
    public void testGetUserById_WhenUserExists() {
        User user = new User("Ivan","Ivanov","ivanov@gmail.com");
        user.setId(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> result = service.getUserById(1L);
        Assertions.assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testGetUserById_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = service.getUserById(1L);
        assertFalse(result.isPresent());
    }
    @Test
    public void testChangeEmail() {
        User user = new User("Ivan","Ivanov","oldEmail@gmail.com");
        user.setId(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        service.changeEmail(1L, "newEmail@example.com");
        assertEquals("newEmail@example.com", user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testChangeCarPrice() {
        User user = new User("Ivan","Ivanov","Email@gmail.com");
        Car car = new Car("Toyota",25000, user);
        car.setId(1);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        service.changeCarPrice(1L, 15000L);
        assertEquals(15000, car.getPrice());
        verify(carRepository, times(1)).save(car);
    }

    //todo на последний метод тест не получилось написать. Голова не варит, а там что-то не бьется.

}

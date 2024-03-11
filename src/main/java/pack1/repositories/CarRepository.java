package pack1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pack1.models.Car;
import pack1.models.User;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Long> {




    @Query("SELECT c FROM Car c WHERE c.name = :name")
    Optional<Car> findByCarName(@Param("name") String name);

    @Query("SELECT c FROM Car c WHERE c.user = :user")
    List<Car> findAllCarsById(@Param("user") User user);

    List<Car> findByUser(User user);
}

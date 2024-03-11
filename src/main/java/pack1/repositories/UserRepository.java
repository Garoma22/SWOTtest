package pack1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pack1.models.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();



    @Query("SELECT u FROM User u WHERE u.lastName = :lastName")
    Optional<User> findByUserLastName(@Param("lastName") String lastName);



}

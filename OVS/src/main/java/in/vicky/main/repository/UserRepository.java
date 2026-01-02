package in.vicky.main.repository;

import in.vicky.main.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // This must match the method called in AuthController
    User findByEmail(String email); 
}
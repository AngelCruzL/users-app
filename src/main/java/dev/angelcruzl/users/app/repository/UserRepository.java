package dev.angelcruzl.users.app.repository;

import dev.angelcruzl.users.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByActiveTrue();

    Optional<User> findByIdAndActiveTrue(Long id);

    Optional<User> findByEmail(String email);

}

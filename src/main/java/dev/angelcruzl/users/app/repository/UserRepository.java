package dev.angelcruzl.users.app.repository;

import dev.angelcruzl.users.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByActiveTrue(Pageable pageable);

    Optional<User> findByIdAndActiveTrue(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}

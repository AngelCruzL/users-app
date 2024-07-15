package dev.angelcruzl.users.app.repository;

import dev.angelcruzl.users.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

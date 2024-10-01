package it.tafaq.springboot.onlineshop.repository;

import it.tafaq.springboot.onlineshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

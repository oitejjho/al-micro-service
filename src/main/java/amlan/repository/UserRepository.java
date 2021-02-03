package amlan.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import amlan.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  boolean existsByUsername(String username);

  Optional<User> findByUsername(String username);

  @Transactional
  void deleteByUsername(String username);

}

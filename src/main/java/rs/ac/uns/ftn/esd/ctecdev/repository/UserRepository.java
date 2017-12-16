package rs.ac.uns.ftn.esd.ctecdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.esd.ctecdev.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}

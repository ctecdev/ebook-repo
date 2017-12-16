package rs.ac.uns.ftn.esd.ctecdev.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.esd.ctecdev.model.User;
import rs.ac.uns.ftn.esd.ctecdev.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	public User findOne(Integer id){
		return userRepo.findOne(id);
	}
	
	public List<User> findAll(){
		return userRepo.findAll();
	}
	
	public User save(User user){
		return userRepo.save(user);
	}
	
	public void remove(Integer id){
		userRepo.delete(id);
	}
	
}

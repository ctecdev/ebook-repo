package rs.ac.uns.ftn.esd.ctecdev.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.esd.ctecdev.model.Category;
import rs.ac.uns.ftn.esd.ctecdev.model.EBook;
import rs.ac.uns.ftn.esd.ctecdev.model.User;
import rs.ac.uns.ftn.esd.ctecdev.service.EBookService;
import rs.ac.uns.ftn.esd.ctecdev.service.UserService;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.UserDTO;

@RestController
@RequestMapping(value="api/users")
public class UserController {

	private static final Logger LOG = Logger.getLogger(UserController.class);
	
	@Autowired
	UserService userServ;
	@Autowired
	EBookService ebServ;
	
	//GET ALL
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		try{
		
			List<User> users = userServ.findAll();
			//convert students to DTOs
			List<UserDTO> eBooksDTO = new ArrayList<>();
			for (User u : users) {
				eBooksDTO.add(new UserDTO(u));
			}
			return new ResponseEntity<>(eBooksDTO, HttpStatus.OK);
	
		} catch (RuntimeException e) {
	  		  LOG.error("Error users/all.", e);
	          throw e;
	  	} catch (Exception e) {
	          LOG.error("Error users/all.", e);
	          throw new RuntimeException(e);
	  	}
	}
	
	// GET ONE
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUser(@PathVariable Integer id){
		try {
			
			User user = userServ.findOne(id);
			if(user == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
		
		} catch (RuntimeException e) {
			  LOG.error("Error users/one.", e);
	        throw e;
		} catch (Exception e) {
	        LOG.error("Error users/one.", e);
	        throw new RuntimeException(e);
		}
	}
	
	// CREATE
	@RequestMapping(method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO){	
		try {

			User user = new User(userDTO);
			int passInt = userDTO.getPassword().hashCode();
			user.setPassword(String.valueOf(passInt));
			userServ.save(user);
			return new ResponseEntity<>(new UserDTO(user), HttpStatus.CREATED);	
			
  		} catch (RuntimeException e) {
  		  LOG.error("Error while creating.", e);
          throw e;
  		} catch (Exception e) {
          LOG.error("Error while creating.", e);
          throw new RuntimeException(e);
  		}
	}
	
	// UPDATE
	@RequestMapping(method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<UserDTO> updateEBook(@RequestBody UserDTO userDTO){	
		try {

			User user = userServ.findOne(userDTO.getId());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setUsername(userDTO.getUsername());
			/*user.setPassword( 
			* modal unesi staru lozinku > provera lozinke
			* > ako je u redu 
			* > modal sa dva polja unesi novu lozinku, potvrdi lozinku
			* 
			*/
			user.setType(userDTO.getType());
			user.setCategory(new Category(userDTO.getCategory()));
			
			userServ.save(user);
			return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);	
			
  		} catch (RuntimeException e) {
  		  LOG.error("Error while updating.", e);
          throw e;
  		} catch (Exception e) {
          LOG.error("Error while updating.", e);
          throw new RuntimeException(e);
  		}
	}
	
	// DELETE
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
		try {
			
			User user = userServ.findOne(id);
			if (user==null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else {
				/**
				 * Set eBook.user to null
				 */
				for (EBook eb : user.geteBooks()){
					eb.setUser(null);
					ebServ.save(eb);
				}
				return new ResponseEntity<>(HttpStatus.OK);	
			}
			
  		} catch (RuntimeException e) {
  		  LOG.error("Error while deleting.", e);
          throw e;
  		} catch (Exception e) {
          LOG.error("Error while deleting.", e);
          throw new RuntimeException(e);
  		}
		
	}
	
	// GET USER BOOKS
	
	// CHECK OLD PASSWORD
	// CHANGE PASSWORD
	// LOGIN
	
}

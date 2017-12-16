package rs.ac.uns.ftn.esd.ctecdev.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import rs.ac.uns.ftn.esd.ctecdev.model.UserType;
import rs.ac.uns.ftn.esd.ctecdev.service.CategoryService;
import rs.ac.uns.ftn.esd.ctecdev.service.UserService;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.CategoryDTO;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.EBookDTO;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.UserDTO;

@RestController
@RequestMapping(value="api/categories")
public class CategoryController {

	private static final Logger LOG = Logger.getLogger(CategoryController.class);
	
	@Autowired
	CategoryService catServ;
	@Autowired
	UserService uServ;
	
	//GET not empty - with books
	@RequestMapping(value="/wb", method = RequestMethod.GET)
	public ResponseEntity<List<CategoryDTO>> getAllCategoriesWithBooks() {
		try {
			
			List<Category> categories = catServ.findAll();
			//convert students to DTOs
			List<CategoryDTO> categoriesDTO = new ArrayList<>();
			//Default category
			Category defCat = catServ.findOneByName("~~~ All books ~~~"); //name: '~~~ All books ~~~'
			categoriesDTO.add(new CategoryDTO(defCat));
			
			for (Category c : categories) {
				if(c.geteBooks().isEmpty()==false)
					categoriesDTO.add(new CategoryDTO(c));
			}
			return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
		
		} catch (RuntimeException e) {
			  LOG.error("Error categories.", e);
	      throw e;
		} catch (Exception e) {
	      LOG.error("Error categories.", e);
	      throw new RuntimeException(e);
		}
}
	
	//GET ALL
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoryDTO>> getAllCategories() {
		try {
			
			List<Category> categories = catServ.findAll();
			//Default category
			Category defCat = catServ.findOneByName("~~~ All books ~~~"); //name: '~~~ All books ~~~'
			categories.remove(defCat);
			//convert students to DTOs
			List<CategoryDTO> categoriesDTO = new ArrayList<>();
			for (Category s : categories) {
				categoriesDTO.add(new CategoryDTO(s));
			}
			return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
		
		} catch (RuntimeException e) {
			  LOG.error("Error categories.", e);
	      throw e;
		} catch (Exception e) {
	      LOG.error("Error categories.", e);
	      throw new RuntimeException(e);
		}
	}
	
	// GET ONE
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<CategoryDTO> getCategory(@PathVariable Integer id){
		try {
			
			Category cat = catServ.findOne(id);
			if(cat == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<>(new CategoryDTO(cat), HttpStatus.OK);
			
		} catch (RuntimeException e) {
			  LOG.error("Error categories/one.", e);
	      throw e;
		} catch (Exception e) {
	      LOG.error("Error categories/one.", e);
	      throw new RuntimeException(e);
		}
	}
	
	// CREATE
	@RequestMapping(method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO catDTO){	
		try {
			
			Category cat = new Category();
			cat.setName(catDTO.getName());
			catServ.save(cat);
			return new ResponseEntity<>(new CategoryDTO(cat), HttpStatus.CREATED);	
			
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
	public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO catDTO){
		try {
			
			Category cat = catServ.findOne(catDTO.getId());
			cat.setName(catDTO.getName());
			catServ.save(cat);
			return new ResponseEntity<>(new CategoryDTO(cat), HttpStatus.OK);	
			
  		} catch (RuntimeException e) {
  		  LOG.error("Error while updating.", e);
          throw e;
  		} catch (Exception e) {
          LOG.error("Error while updating.", e);
          throw new RuntimeException(e);
  		}
	}
	
	//All CATEGORY EBOOKS
	@RequestMapping(value="/{id}/ebooks", method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<List<EBookDTO>> getCategoryEBooks(@PathVariable Integer id){
		try {
			
			Category cat = catServ.findOne(id);
			if (cat==null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else {
				Set<EBook> eBooks = cat.geteBooks();
				List<EBookDTO> eBooksDTO = new ArrayList<>();
				for (EBook eb : eBooks){
					EBookDTO ebDTO = new EBookDTO(eb);
					eBooksDTO.add(ebDTO);
				}
				return new ResponseEntity<>(eBooksDTO, HttpStatus.OK);
			}
			
  		} catch (RuntimeException e) {
  		  LOG.error("Error.", e);
          throw e;
  		} catch (Exception e) {
          LOG.error("Error.", e);
          throw new RuntimeException(e);
  		}
		
	}
	
	//All CATEGORY USERS
	@RequestMapping(value="/{id}/users", method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<List<UserDTO>> getCategoryUsers(@PathVariable Integer id){
		try {
			
			Category cat = catServ.findOne(id);
			if (cat==null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else {
				Set<User> users = cat.getUsers();
				List<UserDTO> usersDTO = new ArrayList<>();
				for (User u : users){
					UserDTO uDTO = new UserDTO();
					uDTO.setId(u.getId());
					uDTO.setFirstName(u.getFirstName());
					uDTO.setLastName(u.getLastName());
					uDTO.setUsername(u.getUsername());
					uDTO.setType(u.getType());
					//uDTO.setCategory.. leave empty
					usersDTO.add(uDTO);
				}
				return new ResponseEntity<>(usersDTO, HttpStatus.OK);	
			}
			
  		} catch (RuntimeException e) {
  		  LOG.error("Error.", e);
          throw e;
  		} catch (Exception e) {
          LOG.error("Error.", e);
          throw new RuntimeException(e);
  		}
		
	}
	
	// DELETE
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteCategory(@PathVariable Integer id){
		try {
			
			Category cat = catServ.findOne(id);
			if (cat==null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else if (cat.getName().equals("DefaultCategory"))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			else {
				
				//eBooks ??? brisanje || logickoBrisanje || uklanjanjeKategorije
				/**
				 * EBook in category.
				 * Create default category.name="Other" that cannot be deleted
				 * Set eBook.category to default category 
				 */
				for (EBook eb : cat.geteBooks()){
					Category c = catServ.findOneByName("DefaultCategory");
					if (c==null){
						c = new Category();
						c.setName("DefaultCategory");
						catServ.save(c);
					}
					eb.setCategory(c);
				}
				/**
				 * User in category.
				 * Remove user.category
				 * Set user.type to GUEST
				 */
				for (User u : cat.getUsers()){
					u.setCategory(null);
					u.setType(UserType.GUEST.getEnumValue());
					uServ.save(u);
				}
				catServ.remove(id);
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
	
	
}

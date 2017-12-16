package rs.ac.uns.ftn.esd.ctecdev.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.esd.ctecdev.model.Category;
import rs.ac.uns.ftn.esd.ctecdev.repository.CategoryRepository;
@Service
public class CategoryService {

	@Autowired
	CategoryRepository catRepo;
	
	public Category findOne(Integer id){
		return catRepo.findOne(id);
	}
	
	public List<Category> findAll(){
		return catRepo.findAll();
	}
	
	public Category save(Category category){
		return catRepo.save(category);
	}
	
	public void remove(Integer id){
		catRepo.delete(id);
	}
	
	public Category findOneByName(String name){
		return catRepo.findOneByName(name);
	}
	
}

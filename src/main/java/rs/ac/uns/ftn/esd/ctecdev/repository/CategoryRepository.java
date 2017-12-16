package rs.ac.uns.ftn.esd.ctecdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.esd.ctecdev.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Category findOneByName(String name);
	
}

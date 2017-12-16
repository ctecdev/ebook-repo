package rs.ac.uns.ftn.esd.ctecdev.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.esd.ctecdev.model.EBook;
import rs.ac.uns.ftn.esd.ctecdev.repository.EBookRepository;

@Service
public class EBookService {

	@Autowired
	EBookRepository ebRepo;
	
	public Long count(){
		return ebRepo.count();
	}
	
	public EBook findOne(String uuid){
		return ebRepo.findOne(uuid);
	}
	
	public Page<EBook> findAll(Pageable pageable) {
		return ebRepo.findAll(pageable);
	}
	
	public List<EBook> findAll(){
		return ebRepo.findAll();
	}
	
	public EBook save(EBook eBook){
		return ebRepo.save(eBook);
	}
	
	public void remove(String uuid){
		ebRepo.delete(uuid);
	}
	
}

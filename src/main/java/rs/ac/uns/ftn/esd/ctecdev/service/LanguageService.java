package rs.ac.uns.ftn.esd.ctecdev.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.esd.ctecdev.model.Language;
import rs.ac.uns.ftn.esd.ctecdev.repository.LanguageRepository;

@Service
public class LanguageService {

	@Autowired
	LanguageRepository langRepo;
	
	public Language findOne(Integer id){
		return langRepo.findOne(id);
	}
	
	public List<Language> findAll(){
		return langRepo.findAll();
	}
	
	public Language save(Language language){
		return langRepo.save(language);
	}
	
	public void remove(Integer id){
		langRepo.delete(id);
	}
	
}

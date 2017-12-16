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

import rs.ac.uns.ftn.esd.ctecdev.model.EBook;
import rs.ac.uns.ftn.esd.ctecdev.model.Language;
import rs.ac.uns.ftn.esd.ctecdev.service.EBookService;
import rs.ac.uns.ftn.esd.ctecdev.service.LanguageService;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.EBookDTO;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.LanguageDTO;
@RestController
@RequestMapping(value="api/languages")
public class LanguageController {
	
	private static final Logger LOG = Logger.getLogger(LanguageController.class);

	@Autowired
	LanguageService langServ;
	@Autowired
	EBookService ebServ;
	
	// GET ALL
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<LanguageDTO>> getAllLanguages(){
		try{
			
			List<Language> langs = langServ.findAll();
			//convert students to DTOs
			List<LanguageDTO> langsDTO = new ArrayList<>();
			for (Language lang : langs) {
				langsDTO.add(new LanguageDTO(lang));
			}
			return new ResponseEntity<>(langsDTO, HttpStatus.OK);
	
		} catch (RuntimeException e) {
	  		  LOG.error("Error languages/all.", e);
	          throw e;
	  	} catch (Exception e) {
	          LOG.error("Error languages/all.", e);
	          throw new RuntimeException(e);
	  	}
	}
	
	// GET ONE
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<LanguageDTO> getLanguage(@PathVariable Integer id){
		try {
			
			Language lang = langServ.findOne(id);
			if(lang == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<>(new LanguageDTO(lang), HttpStatus.OK);
		
		} catch (RuntimeException e) {
			  LOG.error("Error languages/one.", e);
	        throw e;
		} catch (Exception e) {
	        LOG.error("Error languages/one.", e);
	        throw new RuntimeException(e);
		}
	}
	
	// CREATE
	@RequestMapping(method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<LanguageDTO> saveLanguage(@RequestBody LanguageDTO langDTO){	
		try {

			Language lang = new Language(langDTO);
			langServ.save(lang);
			return new ResponseEntity<>(new LanguageDTO(lang), HttpStatus.CREATED);	
			
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
	public ResponseEntity<LanguageDTO> updateLanguage(@RequestBody LanguageDTO langDTO){	
		try {

			Language lang = langServ.findOne(langDTO.getId());
			lang.setName(langDTO.getName());
			langServ.save(lang);
			return new ResponseEntity<>(new LanguageDTO(lang), HttpStatus.OK);	
			
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
	public ResponseEntity<Void> deleteLanguage(@PathVariable Integer id){
		try {
			
			Language lang = langServ.findOne(id);
			if (lang==null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else {
				/**
				 * Seting eBook.language to null
				 */
				for (EBook eb : lang.geteBooks()){
					eb.setLanguage(null);
					ebServ.save(eb);
				}
				//remove Language
				langServ.remove(id);
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
	
	// GET ALL LANGUAGE BOOKS
	@RequestMapping(value="/{id}/books", method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<List<EBookDTO>> getEBookFiles(@RequestBody LanguageDTO langDTO){	
		try {

			Language lang = langServ.findOne(langDTO.getId());
			if(lang==null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else {
				Set<EBook> ebooks = lang.geteBooks();
				List<EBookDTO> ebooksDTO = new ArrayList<>();
				for (EBook eb : ebooks){
					EBookDTO ebd = new EBookDTO(eb);
					ebooksDTO.add(ebd);
				}
				return new ResponseEntity<>(ebooksDTO, HttpStatus.OK);	
			}
			
			
  		} catch (RuntimeException e) {
  		  LOG.error("Error while creating.", e);
          throw e;
  		} catch (Exception e) {
          LOG.error("Error while creating.", e);
          throw new RuntimeException(e);
  		}
	}
	
	
}

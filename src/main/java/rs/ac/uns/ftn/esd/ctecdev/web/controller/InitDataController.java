package rs.ac.uns.ftn.esd.ctecdev.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.esd.ctecdev.model.Category;
import rs.ac.uns.ftn.esd.ctecdev.model.Language;
import rs.ac.uns.ftn.esd.ctecdev.model.User;
import rs.ac.uns.ftn.esd.ctecdev.model.UserType;
import rs.ac.uns.ftn.esd.ctecdev.service.CategoryService;
import rs.ac.uns.ftn.esd.ctecdev.service.EBookFileService;
import rs.ac.uns.ftn.esd.ctecdev.service.EBookService;
import rs.ac.uns.ftn.esd.ctecdev.service.LanguageService;
import rs.ac.uns.ftn.esd.ctecdev.service.UserService;

@RestController
@RequestMapping(value="/init")
public class InitDataController {
	
	@Autowired
	EBookService ebServ;
	@Autowired
	EBookFileService ebfServ;
	@Autowired
	CategoryService catServ;
	@Autowired
	LanguageService langServ;
	@Autowired
	UserService userServ;
	
	
	@RequestMapping(method=RequestMethod.GET)
	String string(){
		
		//Language
		Language langSRB = new Language("Srbski");
		langServ.save(langSRB);
		
		//Category
		Category cat0 = new Category("~~~ All books ~~~");
		Category cat1 = new Category("Antologije i monografije");
		Category cat2 = new Category("Arhitektura i dizajn");
		Category cat3 = new Category("Autobiografije i biografije");
		Category cat4 = new Category("Biznis i ekonomija");
		Category cat5 = new Category("Domaci pisci");
		Category cat6 = new Category("Enciklopedije i atlasi");
		Category cat7 = new Category("Filozofija");
		Category cat8 = new Category("Istorija");
		Category cat9 = new Category("Klasicna knjizevnost");
		Category cat10 = new Category("Kompjuteri i internet");
		Category cat11 = new Category("Marketing i menadzment");
		Category cat12 = new Category("Poezija");
		Category cat13 = new Category("Psihologija");
		Category cat14 = new Category("Religija i mitologija");
		Category cat15 = new Category("Recnici i strani jezici");
		Category cat16 = new Category("Sociologija");
		Category cat17 = new Category("Teologija");
		Category cat18 = new Category("Udzbenici");
		Category cat19 = new Category("Umetnost");
		Category cat20 = new Category("Vodici i mape");
		Category cat21 = new Category("Zdravlje");
		Category cat22 = new Category("Sport");
		Category cat23 = new Category("Enciklopedije");
		Category cat24 = new Category("Hrana i kuvari");
		Category cat25 = new Category("Poljoprivreda i stocarstvo");
		Category cat26 = new Category("Muzika");
		Category cat27 = new Category("Prirucnici");
		Category cat28 = new Category("Kompleti");
		Category cat29 = new Category("Leksikoni");
		Category cat30 = new Category("Drama");
		Category cat31 = new Category("Strucna literatura");
		Category cat32 = new Category("Sport");
		catServ.save(cat0);
		catServ.save(cat1);
		catServ.save(cat2);
		catServ.save(cat3);
		catServ.save(cat4);
		catServ.save(cat5);
		catServ.save(cat6);
		catServ.save(cat7);
		catServ.save(cat8);
		catServ.save(cat9);
		catServ.save(cat10);
		catServ.save(cat11);
		catServ.save(cat12);
		catServ.save(cat13);
		catServ.save(cat14);
		catServ.save(cat15);
		catServ.save(cat16);
		catServ.save(cat17);
		catServ.save(cat18);
		catServ.save(cat19);
		catServ.save(cat20);
		catServ.save(cat21);
		catServ.save(cat22);
		catServ.save(cat23);
		catServ.save(cat24);
		catServ.save(cat25);
		catServ.save(cat26);
		catServ.save(cat27);
		catServ.save(cat28);
		catServ.save(cat29);
		catServ.save(cat30);
		catServ.save(cat31);
		catServ.save(cat32);
	
		//User
		User admin = new User("Pera", "Peric", "pera", "pera", UserType.ADMIN.getEnumValue(), null);
		User subsc = new User("Mika", "Becic", "mika", "mika", UserType.SUBSCRIBER.getEnumValue(), null);
		userServ.save(admin);
		userServ.save(subsc);
		
		return "Uspesno inicijalizovani podaci";
	}
	
	
	
}

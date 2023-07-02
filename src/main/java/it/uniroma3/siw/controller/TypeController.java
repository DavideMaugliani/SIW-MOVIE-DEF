package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Type;
import it.uniroma3.siw.repository.TypeRepository;
import it.uniroma3.siw.service.TypeService;
import it.uniroma3.siw.validator.TypeValidator;
import jakarta.validation.Valid;

@Controller
public class TypeController {
	
	@Autowired
	private TypeService typeService;
	
	@GetMapping(value="/admin/indexType")
	public String indexType() {
		return "admin/indexType.html";
	}
	
	@GetMapping(value="/admin/formNewType")
	public String formNewType(Model model) {
		model.addAttribute("type", new Type());
		return "admin/formNewType.html";
	}
	
	@PostMapping(value="/admin/type")
	public String newType(@Valid @ModelAttribute("type") Type type, BindingResult bindingResult, Model model) {
		
		try {
			model.addAttribute("type", this.typeService.saveType(type, bindingResult));
			return "admin/indexType.html";
		} 
		catch(IOException e) {
			return "admin/formNewType.html";
		}
	}
	
	@GetMapping(value="/admin/types")
	public String showTypes(Model model) {
		model.addAttribute("types", this.typeService.getAllType());
		return "admin/types.html";
	}

}

package it.uniroma3.siw.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.model.Type;
import it.uniroma3.siw.repository.TypeRepository;
import it.uniroma3.siw.validator.TypeValidator;

@Service
public class TypeService {
	
	@Autowired
	private TypeRepository typeRepository;
	
	@Autowired
	private TypeValidator typeValidator;
	
	@Transactional
	public Type saveType(Type type, BindingResult bindingResult) throws IOException {
		
		this.typeValidator.validate(type, bindingResult);
		if (!bindingResult.hasErrors()) {
			
			return this.typeRepository.save(type);
			
		} else {
			
			throw new IOException();
		}
	}
	
	@Transactional
	public Iterable<Type> getAllType() {
		return this.typeRepository.findAll();
	}
	
	@Transactional
	public Iterable<Type> findAll(){
		return this.typeRepository.findAll();
	}
	
	@Transactional
	public Iterable<Type> findTypesNotInMovie(Long typeId){
		return this.typeRepository.findTypesNotInMovie(typeId);
	}
	
	/*private List<Type> typesToAdd(Long movieId) {
	List<Type> typesToAdd = new ArrayList<>();

	for (Type t : typeService.findTypesNotInMovie(movieId)) {
		typesToAdd.add(t);
	}
	return typesToAdd;
}*/

}

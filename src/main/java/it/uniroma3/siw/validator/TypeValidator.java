package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Type;
import it.uniroma3.siw.repository.TypeRepository;

@Component
public class TypeValidator implements Validator{
	
	@Autowired
	private TypeRepository typeRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Type type=(Type)o;
		if(type.getName()!=null && typeRepository.existsByName(type.getName())) {
			errors.reject("type.duplicate");
		}
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Type.class.equals(aClass);
	}

}

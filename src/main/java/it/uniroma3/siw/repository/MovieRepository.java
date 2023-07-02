package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Type;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	public List<Movie> findByYear(int year);
	
	public List<Movie> findByMovieTypesContains(Type type);

	public boolean existsByTitleAndYear(String title, int year);	
}
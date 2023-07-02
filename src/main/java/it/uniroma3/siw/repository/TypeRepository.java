package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Type;

public interface TypeRepository extends CrudRepository<Type, Long>{
	
	public boolean existsByName(String name);
	
	@Query(value="select * "
			+ "from type t "
			+ "where t.id not in "
			+ "(select movie_types_id "
			+ "from movie_movie_types "
			+ "where movie_movie_types.movie_list_id = :movieId)", nativeQuery=true)
	public Iterable<Type> findTypesNotInMovie(@Param("movieId") Long id);

}

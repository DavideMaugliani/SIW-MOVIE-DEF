package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Type;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.repository.TypeRepository;
import it.uniroma3.siw.validator.MovieValidator;

@Service
public class MovieService {
	
	@Autowired 
	private MovieRepository movieRepository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private TypeRepository typeRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired 
	private MovieValidator movieValidator;
	
	@Transactional
	public Movie findById(Long id) {
		return  movieRepository.findById(id).get();
	}
	
	@Transactional
	public Iterable<Movie> findAll(){
		return this.movieRepository.findAll();
	}
	
	@Transactional
	public Movie setDirectorToMovie(Long directorId, Long movieId) {
		Artist director = this.artistRepository.findById(directorId).get();
		Movie movie = this.movieRepository.findById(movieId).get();
		movie.setDirector(director);
		/*return*/ this.movieRepository.save(movie);
		
		director.getDirectorOf().add(movie);
		this.artistRepository.save(director);
		
		return movie;
	}
	
	@Transactional
	public Movie saveMovie(Movie movie, BindingResult bindingResult, MultipartFile image) throws IOException{
		this.movieValidator.validate(movie, bindingResult);
		
		if (!bindingResult.hasErrors()) {
			try {
				String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
				movie.setUrlImage(base64Image);
			} catch (IOException e) {
			}
			return this.movieRepository.save(movie); 
		} else {
			throw new IOException(); 
		}
	}
	
	@Transactional
	public Iterable<Movie> removeMovie(Long movieId) {
		this.movieRepository.deleteById(movieId);
		return this.movieRepository.findAll();
	}
	
	@Transactional
	public List<Movie> findByYear(int year){
		return this.movieRepository.findByYear(year);
	}
	
	@Transactional
	public List<Movie> findByMovieTypesContains(Long typeId){
		return this.movieRepository.findByMovieTypesContains(this.typeRepository.findById(typeId).get());
	}
	
	@Transactional
	public Movie addActorToMovie(Long actorId, Long movieId) {
		
		Movie movie = this.movieRepository.findById(movieId).get();
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		
		return this.movieRepository.save(movie);
	}
	
	@Transactional
	public Movie removeActorFromMovie(Long actorId, Long movieId) {
		
		Movie movie = this.movieRepository.findById(movieId).get();
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		
		return this.movieRepository.save(movie);
	}
	
	@Transactional
	public Movie addTypeToMovie(Long typeId, Long movieId) {
		
		Movie movie = this.movieRepository.findById(movieId).get();
		Type type = this.typeRepository.findById(typeId).get();
		List<Type> types = movie.getMovieTypes();
		types.add(type);
		
		return this.movieRepository.save(movie);
	}
	
	@Transactional
	public Movie removeTypeToMovie(Long typeId, Long movieId) {
		
		Movie movie = this.movieRepository.findById(movieId).get();
		Type type = this.typeRepository.findById(typeId).get();
		List<Type> types = movie.getMovieTypes();
		types.remove(type);
		
		return this.movieRepository.save(movie);
	}
	
	@Transactional
	public Movie saveEditMovie(Movie newMovie, Long movieId, BindingResult bindingResult, MultipartFile image) 
			throws IOException {
        
		this.movieValidator.validate(newMovie, bindingResult);
		if (!bindingResult.hasFieldErrors()) {
			Movie movie=this.movieRepository.findById(movieId).get();
			movie.setTitle(newMovie.getTitle());
			movie.setYear(newMovie.getYear());
			movie.setUrlImage(newMovie.getUrlImage());
			
			try {
                if (image.getBytes().length != 0) {
                    String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                    movie.setUrlImage(base64Image);
                }
            } catch (IOException e) {
            }
			
			return this.movieRepository.save(movie); 
		} else {
			throw new IOException();
		}
	}
	
	@Transactional
	public Movie removeReviewFromMovie(Long movieId, Long reviewId) {
		this.reviewRepository.deleteById(reviewId);
		return this.movieRepository.findById(movieId).get();
	}
	
	/*private List<Artist> actorsToAdd(Long movieId) {
		List<Artist> actorsToAdd = new ArrayList<>();

		for (Artist a : artistRepository.findActorsNotInMovie(movieId)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}*/

}

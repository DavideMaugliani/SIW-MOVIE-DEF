package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Type;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.TypeService;
import jakarta.validation.Valid;

@Controller
public class MovieController {
	@Autowired 
	private MovieService movieService;
	
	@Autowired 
	private ArtistService artistService;
	
	@Autowired
	private TypeService typeService;

	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", movieService.findById(id));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/indexMovie")
	public String indexMovie() {
		return "admin/indexMovie.html";
	}
	
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return "admin/manageMovies.html";
	}
	
	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		model.addAttribute("movie", this.movieService.setDirectorToMovie(directorId, movieId));
		return "admin/formUpdateMovie.html";
	}
	
	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistService.findAll());
		model.addAttribute("movie", movieService.findById(id));
		return "admin/directorsToAdd.html";
	}

	@PostMapping(value="/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, 
			MultipartFile image, Model model) {
		try {
			model.addAttribute("movie", this.movieService.saveMovie(movie, bindingResult, image));
			return "admin/formUpdateMovie.html";
		} catch(IOException e) {
			return "admin/formNewMovie.html"; 
		}
	}
	
	@GetMapping(value="/admin/removeMovie/{movieId}")
	public String removeMovie(@PathVariable("movieId") Long movieId, Model model) {
		model.addAttribute("movies", this.movieService.removeMovie(movieId));
		return "admin/manageMovies.html";
	}
	
	@GetMapping("/movie")
	public String getMovies(Model model) {		
		model.addAttribute("movies", this.movieService.findAll());
		return "movies.html";
	}

	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return "movie.html";
	}
	
	@GetMapping(value="/admin/movie")
	public String getMoviesAdmin(Model model) {		
		model.addAttribute("movies", this.movieService.findAll());
		return "admin/moviesAdmin.html";
	}
	
	@GetMapping(value="/admin/movie/{id}")
	public String getMovieAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return "admin/movieAdmin.html";
	}
	
	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies", this.movieService.findByYear(year));
		return "foundMovies.html";
	}
	
	@GetMapping("/formSearchMoviesForType")
	public String formSearchMoviesForType(Model model) {
		model.addAttribute("types", this.typeService.findAll());
		return "formSearchMovieForType.html";
	}
	
	@GetMapping("/searchMoviesForType/{typeId}")
	public String searchMoviesForType(@PathVariable("typeId") Long typeId, Model model) {
		model.addAttribute("movies", this.movieService.findByMovieTypesContains(typeId));
		return "foundMovies.html";
	}
	
	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		model.addAttribute("actorsToAdd", this.artistService.findActorsNotInMovie(id));
		model.addAttribute("movie", this.movieService.findById(id));

		return "admin/actorsToAdd.html";
	}

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		
		model.addAttribute("movie", this.movieService.addActorToMovie(actorId, movieId));
		model.addAttribute("actorsToAdd", this.artistService.findActorsNotInMovie(movieId));

		return "admin/actorsToAdd.html";
	}
	
	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		
		model.addAttribute("movie", this.movieService.removeActorFromMovie(actorId, movieId));
		model.addAttribute("actorsToAdd", this.artistService.findActorsNotInMovie(movieId));

		return "admin/actorsToAdd.html";
	}
	
	@GetMapping("/admin/updateTypes/{id}")
	public String updateTypes(@PathVariable("id") Long id, Model model) {

		model.addAttribute("typesToAdd", this.typeService.findTypesNotInMovie(id));
		model.addAttribute("movie", this.movieService.findById(id));

		return "admin/typesToAdd.html";
	}
	
	@GetMapping(value="/admin/addTypeToMovie/{typeId}/{movieId}")
	public String addTypeToMovie(@PathVariable("typeId") Long typeId, @PathVariable("movieId") Long movieId, Model model) {
		
		model.addAttribute("movie", this.movieService.addTypeToMovie(typeId, movieId));
		model.addAttribute("typesToAdd", this.typeService.findTypesNotInMovie(movieId));

		return "admin/typesToAdd.html";
	}
	
	@GetMapping(value="/admin/removeTypeFromMovie/{typeId}/{movieId}")
	public String removeTypeFromMovie(@PathVariable("typeId") Long typeId, @PathVariable("movieId") Long movieId, Model model) {
		
		model.addAttribute("movie", this.movieService.removeTypeToMovie(typeId, movieId));
		model.addAttribute("typesToAdd", this.typeService.findTypesNotInMovie(movieId));

		return "admin/typesToAdd.html";
	}
	
	@GetMapping(value="/admin/editMovie/{movieId}")
	public String formEditMovie(@PathVariable("movieId") Long movieId, Model model) {
		model.addAttribute("movie", this.movieService.findById(movieId));
		return "admin/editMovie.html";
	}
	
	@PostMapping(value="/admin/editMovie/{movieId}")
	public String editMovie(@Valid @ModelAttribute("movie") Movie newMovie, @PathVariable("movieId") Long movieId,
			BindingResult bindingResult, MultipartFile image, Model model) {
		
		try {
			model.addAttribute("movie", this.movieService.saveEditMovie(newMovie, movieId, bindingResult, image));
			return "admin/formUpdateMovie.html";
		} catch(IOException e) {
			return "admin/editMovie.html";
		}
	}
	
	@GetMapping("/admin/removeReviewFromMovie/{movieId}/{reviewId}")
	public String removeReviewFromMovie(@PathVariable("movieId") Long movieId, @PathVariable("reviewId") Long reviewId, Model model) {
		model.addAttribute("movie", this.movieService.removeReviewFromMovie(movieId, reviewId));
		return "admin/formUpdateMovie.html";
	}
	
}

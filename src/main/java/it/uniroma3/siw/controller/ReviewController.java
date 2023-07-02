package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.Set;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.validator.ReviewValidator;
import jakarta.validation.Valid;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MovieService movieService;
	
	@GetMapping("/reviews/{reviewId}")
	public String review(@PathVariable("reviewId") Long reviewId, Model model) {
		model.addAttribute("review", this.reviewService.findById(reviewId));
		return "review.html";
	}
	
	@GetMapping("/default/formNewReview/{movieId}")
	public String newReview(@PathVariable("movieId") Long movieId, Model model) {
		Review review= new Review();
		model.addAttribute("review", review);
		model.addAttribute("movie", this.movieService.findById(movieId));
		return "default/newReview.html";
	}
	
	@PostMapping("/default/addReview/{movieId}")
	public String addReview(@Valid @ModelAttribute("review") Review review, @PathVariable("movieId") Long movieId, BindingResult bindingResult, Model model) {
		
		try {
			model.addAttribute("review", this.reviewService.saveReview(review, movieId, bindingResult));
			return "review.html";
		} catch(IOException e) {
			model.addAttribute("movie", this.movieService.findById(movieId));
			return "default/newReview.html";
		}	
	}
	
	@GetMapping(value="/default/reviewList")
	public String reviewList(Model model) {
		model.addAttribute("reviews", this.reviewService.findAllByUser());
		return "default/reviewList";
	}
	
	@GetMapping(value="/default/editReview/{reviewId}")
	public String formEditReview(@PathVariable("reviewId") Long reviewId, Model model) {
		model.addAttribute("review", this.reviewService.findById(reviewId));
		return "default/editReview.html";
	}
	
	@PostMapping("/default/editReview/{reviewId}")
	public String editReview(@Valid @ModelAttribute("review") Review newReview, @PathVariable("reviewId") Long reviewId, BindingResult bindingResult, Model model) {
		
		try {
			model.addAttribute("review", this.reviewService.saveEditReview(newReview, reviewId, bindingResult));
			model.addAttribute("reviews", this.reviewService.findAllByUser());
			return "default/reviewList.html";
		} catch(IOException e) {
			return "default/editReview.html";
		}	
	}
}

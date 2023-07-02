package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.validator.ReviewValidator;

@Service
public class ReviewService {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ReviewValidator reviewValidator;
	
	@Transactional
	public Review findById(Long id) {
		return this.reviewRepository.findById(id).get();
	}
	
	@Transactional
	public Review saveReview(Review review, Long movieId, BindingResult bindingResult) throws IOException {
		
		review.setMovie(this.movieRepository.findById(movieId).get());
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		review.setUser(credentials.getUser());
		
		this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			
			review.getUser().getWrittenReviews().add(review);
			review.getMovie().getMovieReviews().add(review);
			
			return this.reviewRepository.save(review);
		} else {
			throw new IOException();
		}	
	}
	
	@Transactional
	public List<Review> findAllByUser(){
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		return this.reviewRepository.findAllByUser(credentials.getUser());
	}
	
	@Transactional
	public Review saveEditReview(Review newReview, Long reviewId, BindingResult bindingResult) throws IOException {
		
		this.reviewValidator.validate(newReview, bindingResult);
		if(!bindingResult.hasFieldErrors()) {
			Review review=this.reviewRepository.findById(reviewId).get();
			
			review.setTitle(newReview.getTitle());
			review.setValutazione(newReview.getValutazione());
			review.setDescription(newReview.getDescription());
			
			return this.reviewRepository.save(review);
		} else {
			throw new IOException();
		}	
	}

}

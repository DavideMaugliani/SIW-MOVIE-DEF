package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.validator.ArtistValidator;

@Service
public class ArtistService {
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private ArtistValidator artistValidator;
	
	@Transactional
	public Artist saveArtist(Artist artist, BindingResult bindingResult, MultipartFile image) throws IOException {
		
		this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			
			try {
				String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
				artist.setUrlImage(base64Image);
			} catch (IOException e) {
			}
			return this.artistRepository.save(artist);
		} else {
			throw new IOException(); 
		}
	}
	
	@Transactional
	public Artist findById(Long artistId) {
		return this.artistRepository.findById(artistId).get();
	}
	
	@Transactional
	public Artist saveEditArtist(Artist newArtist, Long artistId,
			 BindingResult bindingResult, MultipartFile image) throws IOException{
        
		this.artistValidator.validate(newArtist, bindingResult);
		
		if (!bindingResult.hasFieldErrors()) {
			
			Artist artist=this.artistRepository.findById(artistId).get();
			
			artist.setName(newArtist.getName());
			artist.setSurname(newArtist.getSurname());
			artist.setDateOfBirth(newArtist.getDateOfBirth());
			artist.setDateOfDeath(newArtist.getDateOfDeath());
			artist.setUrlImage(newArtist.getUrlImage());
			
			try {
                if (image.getBytes().length != 0) {
                    String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                    artist.setUrlImage(base64Image);
                }
            } catch (IOException e) {
            }

			return this.artistRepository.save(artist);
		} else {
			throw new IOException(); 
		}
	}
	
	@Transactional
	public Iterable<Artist> removeArtist(Long artistId) {
		Artist artist=this.artistRepository.findById(artistId).get();
		for(Movie m:artist.getDirectorOf()) {
			m.setDirector(null);
		}
		for(Movie m:artist.getActorOf()) {
			m.setActors(null);
		}
		this.artistRepository.deleteById(artistId);
		
		return this.artistRepository.findAll();
	}
	
	@Transactional
	public Iterable<Artist> findAll() {
		return this.artistRepository.findAll();
	}
	
	@Transactional
	public Iterable<Artist> findActorsNotInMovie(Long movieId){
		return this.artistRepository.findActorsNotInMovie(movieId);
	}

}

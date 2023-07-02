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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;

@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistService artistService;
	
	@Autowired
	private MovieService movieService;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	
	@GetMapping("/artistsForMovie/{id}")
    public String showArtists(@PathVariable("id") Long id, Model model) {
        model.addAttribute("artists", this.movieService.findById(id).getActors());
        return "artists.html";
    }
	
	@PostMapping("/admin/artist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult,
			MultipartFile image, Model model) {
		try {
			model.addAttribute("artist", this.artistService.saveArtist(artist, bindingResult, image));
			return "admin/artistAdmin.html";
		} catch(IOException e) {
			return "admin/formNewArtist.html"; 
		}
	}
	
	@GetMapping(value="/admin/editArtist/{artistId}")
	public String formEditArtist(@PathVariable("artistId") Long artistId, Model model) {
		model.addAttribute("artist", this.artistService.findById(artistId));
		return "admin/editArtist.html";
	}
	
	@PostMapping(value="/admin/editArtist/{artistId}")
	public String editArtist(@Valid @ModelAttribute("artist") Artist newArtist, @PathVariable("artistId") Long artistId,
			MultipartFile image, BindingResult bindingResult, Model model) {
		
		try {
			model.addAttribute("artist", this.artistService.saveEditArtist(newArtist, artistId, bindingResult, image));
			return "admin/artistAdmin.html";
		} catch(IOException e) {
			return "admin/editArtist.html"; 
		}
	}
	
	@GetMapping(value="/admin/removeArtist/{artistId}")
	public String removeArtist(@PathVariable("artistId") Long artistId, Model model) {
		model.addAttribute("artists", this.artistService.removeArtist(artistId));
		return "admin/artistsAdmin.html";
	}

	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.findById(id));
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return "artists.html";
	}
	
	@GetMapping("/admin/artistAdmin/{id}")
	public String getArtistAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.findById(id));
		return "admin/artistAdmin.html";
	}

	@GetMapping("/admin/artistsAdmin")
	public String getArtistsAdmin(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return "admin/artistsAdmin.html";
	}
}

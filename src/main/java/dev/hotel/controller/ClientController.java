package dev.hotel.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hotel.entite.Client;
import dev.hotel.repository.ClientRepository;

@RestController
@RequestMapping("/clients")
public class ClientController {

	private ClientRepository clientRepository;

	/**
	 * Constructeur
	 * 
	 * @param clientRepository
	 */
	public ClientController(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}

	// GET /clients?start=X&size=Y
	@RequestMapping(method = RequestMethod.GET)
	public List<Client> listerClients(@RequestParam Integer start, @RequestParam Integer size) {
		return clientRepository.findAll(PageRequest.of(start, size)).toList();
	}

	// GET /clients/UUID
	@GetMapping("{uuid}")
	public ResponseEntity<Client> findClient(@PathVariable UUID uuid) {
		Optional<Client> opt = clientRepository.findById(uuid);

		if (opt.isPresent()) {
			return ResponseEntity.ok().body(opt.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// POST /clients
	@PostMapping()
	public ResponseEntity<Client> ajouterClient(@Valid @RequestBody Client client) {
		Optional<Client> opt = Optional.of(client);

		if (client.getNom().length() < 2 || client.getPrenoms().length() < 2) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.header("Le nom et le prénom doivent avoir au moins 2 caractères").build();
		} else {
			return ResponseEntity.ok().body(opt.get());
		}
	}

}

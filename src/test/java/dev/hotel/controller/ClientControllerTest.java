package dev.hotel.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.hotel.entite.Client;
import dev.hotel.repository.ClientRepository;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ClientRepository clientRepository;

	@Test
	void testListerClients() throws Exception {

		List<Client> clients = new ArrayList<>();
		Client client1 = new Client("Odd", "Ross");
		Client client2 = new Client("Don", "Duck");
		clients.add(client1);
		clients.add(client2);

		when(clientRepository.findAll(PageRequest.of(0, 2))).thenReturn(new PageImpl<Client>(clients));

		mockMvc.perform(MockMvcRequestBuilders.get("/clients?start=0&size=2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].nom").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].nom").value("Odd"));
	}

	@Test
	void testFindClient() throws Exception {

		UUID uuid = UUID.fromString("dcf129f1-a2f9-47dc-8265-1d844244b192");
		Client client = new Client("Odd", "Ross");
		Optional<Client> opt = Optional.of(client);

		when(clientRepository.findById(uuid)).thenReturn(opt);

		mockMvc.perform(MockMvcRequestBuilders.get("/clients/dcf129f1-a2f9-47dc-8265-1d844244b192"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nom").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nom").value("Odd"));
	}

	@Test
	void testAjouterClient() throws Exception {

		String jsonBody = "{\"nom\" : \"Dupont\",\"prenoms\" : \"Jean\"}";
		Client client = new Client("Dupont", "Jean");

		when(clientRepository.save(client)).thenReturn(new Client("Dupont", "Jean"));

		mockMvc.perform(
				MockMvcRequestBuilders.post("/clients").contentType(MediaType.APPLICATION_JSON).content(jsonBody))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nom").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nom").value("Dupont"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.prenoms").value("Jean"));
	}

}

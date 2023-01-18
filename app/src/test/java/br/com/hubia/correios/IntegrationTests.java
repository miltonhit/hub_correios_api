package br.com.hubia.correios;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.springtest.MockServerTest;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hubia.correios.model.Address;
import br.com.hubia.correios.service.CorreiosService;

@MockServerTest({"setup.origin.url=http://localhost:${mockServerPort}/ceps.csv"})
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

	@Autowired private MockMvc mockMvc;
	@Autowired private CorreiosService service;
	private MockServerClient mockServerClient;
	
	@Test
	@Order(1)
	public void testGetZipCodeWhenNotReady() throws Exception {
		mockMvc.perform(get("/zip/03358150")).andExpect(status().is(503));
	}
	
	@Test
	@Order(2)
	public void testSetup() throws Exception {
		String csvContent = "SP,Sao Paulo,Vila Formosa,3358150,Rua Ituri,,,,,,,,,,";  
		
		//
		// Mock SETUP Endpoint
		mockServerClient
			.when(request()
					.withMethod("GET")
			.withPath("/ceps.csv"))
				.respond(response()
						.withStatusCode(HttpStatusCode.OK_200.code())
						.withContentType(org.mockserver.model.MediaType.PLAIN_TEXT_UTF_8)
						.withBody(csvContent)
						);
		
		//
		// Setup it
		this.service.setup();
	}

	@Test
	@Order(3)
	void testGetZipCodeThatDoesntExist() throws Exception {
		mockMvc.perform(get("/zip/99999999")).andExpect(status().is(204));
	}

	@Test
	@Order(4)
	void tesGetCorrectZipCode() throws Exception {
		MvcResult result = mockMvc.perform(get("/zip/03358150"))
				.andExpect(status().is(200))
				.andReturn();

		String addressResultJson = result.getResponse().getContentAsString();
		String addressCorrectJson = new ObjectMapper().writeValueAsString(
				Address.builder()
					.zipcode("03358150")
					.street("Rua Ituri")
					.city("Sao Paulo")
					.state("SP")
					.district("Vila Formosa").build());
		//
		//
		JSONAssert.assertEquals(addressCorrectJson, addressResultJson, false);
	}
}
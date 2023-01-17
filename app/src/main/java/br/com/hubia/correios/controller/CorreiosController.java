package br.com.hubia.correios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.hubia.correios.exception.NotReadyException;
import br.com.hubia.correios.model.Address;
import br.com.hubia.correios.service.CorreiosService;

@RestController
public class CorreiosController {
	@Autowired
	private CorreiosService service;
	
	@GetMapping("status")
	public String get() {
		return "Correios Service is " + service.getStatus();
	}
	
	@GetMapping("zip/{zipcode}")
	public Address getByZipcode(
			@PathVariable("zipcode") String zipcode) throws NotReadyException {
		return this.service.getByZipcode(zipcode);
	}
}
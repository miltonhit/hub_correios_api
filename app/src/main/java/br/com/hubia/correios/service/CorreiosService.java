package br.com.hubia.correios.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.hubia.correios.SpringApp;
import br.com.hubia.correios.exception.NoContentException;
import br.com.hubia.correios.exception.NotReadyException;
import br.com.hubia.correios.model.Address;
import br.com.hubia.correios.model.AddressStatus;
import br.com.hubia.correios.model.Status;
import br.com.hubia.correios.repository.AddressRepository;
import br.com.hubia.correios.repository.AddressStatusRepository;
import br.com.hubia.correios.repository.SetupRepository;

@Service
public class CorreiosService {
	private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private AddressStatusRepository statusRepository;
	
	@Autowired
	private SetupRepository setupRepository;
	
	public Status getStatus() {
		return statusRepository.findById(AddressStatus.DEFAULT_ID)
				.orElse(AddressStatus.builder().status(Status.NEED_SETUP).build()).getStatus();
	}
	
	public Address getByZipcode(String zipcode) throws NotReadyException, NoContentException {
		if (!this.getStatus().equals(Status.READY))
			throw new NotReadyException();
		
		return addressRepository.findById(zipcode).orElseThrow(NoContentException::new);
	}
	
	private void saveServiceStatus(Status status) {
		statusRepository.save(AddressStatus.builder().id(AddressStatus.DEFAULT_ID).status(status).build());
	}
	
    @Async
    @EventListener(ApplicationStartedEvent.class)
	protected synchronized void setup() {
		logger.info("---");
		logger.info("---");
		logger.info("--- STARTING SETUP");
		logger.info("--- Please wait... This may take a few minutes");
		logger.info("---");
		logger.info("---");
		
		try {
			if (this.getStatus().equals(Status.NEED_SETUP)) { // If not running, starts it.
				this.saveServiceStatus(Status.SETUP_RUNNING);
				
				//
				// Download CSV content
				// From origin and saves it.
				this.addressRepository.saveAll(
						setupRepository.listAdressesFromOrigin());
				
				//
				// Set service READY!
				this.saveServiceStatus(Status.READY);
			}
			
			logger.info("---");
			logger.info("---");
			logger.info("--- READY TO USE");
			logger.info("--- Good luck my friend :)");
			logger.info("---");
			logger.info("---");
		} catch(Exception exc) {
			logger.error("Error to download/save addresses, closing the application....", exc);
			SpringApp.close(999);
		}
	}
}
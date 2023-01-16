package br.com.hubia.correios.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
	private static Logger LOGGER = LoggerFactory.getLogger(CorreiosService.class);
	
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
	protected synchronized void setup() throws Exception {
		LOGGER.info("---");
		LOGGER.info("---");
		LOGGER.info("--- STARTING SETUP");
		LOGGER.info("--- Please wait... This may take a few minutes");
		LOGGER.info("---");
		LOGGER.info("---");
		if (this.getStatus().equals(Status.NEED_SETUP)) { // If not running, starts it.
			this.saveServiceStatus(Status.SETUP_RUNNING);
			
			//
			// Download CSV content
			// From origin
			// And send it to MySQL
			this.addressRepository.saveAll(
					setupRepository.listAdressesFromOrigin());
			
			//
			// Set service READY!
			this.saveServiceStatus(Status.READY);
		}
		
		LOGGER.info("---");
		LOGGER.info("---");
		LOGGER.info("--- READY TO USE");
		LOGGER.info("--- Good luck my friend :)");
		LOGGER.info("---");
		LOGGER.info("---");
	}
}
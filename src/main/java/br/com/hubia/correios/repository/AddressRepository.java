package br.com.hubia.correios.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.hubia.correios.model.Address;

public interface AddressRepository extends CrudRepository<Address, String> {

}

package br.com.hubia.correios.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.hubia.correios.model.AddressStatus;

public interface AddressStatusRepository extends CrudRepository<AddressStatus, Integer> {

}
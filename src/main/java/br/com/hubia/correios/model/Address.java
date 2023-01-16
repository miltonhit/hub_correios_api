package br.com.hubia.correios.model;



import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Address {
	@Id
	private String zipcode;
	private String street;
	private String district;
	private String state;
	private String city;
}

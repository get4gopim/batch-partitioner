package com.howtodoinjava.batch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Customer 
{
	private Long id;
	private String firstName;
	private String lastName;
	private String birthdate;

	public Customer(Long id, String firstName, String lastName, String birthdate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
	}
}
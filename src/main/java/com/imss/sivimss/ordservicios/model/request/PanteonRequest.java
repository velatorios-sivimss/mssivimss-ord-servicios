package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PanteonRequest {

	private String desPanteon;

	private String desCalle;

	private String numExterior;

	private String numInterior;

	private CodigoPostalRequest cp;
	
	private String desContacto;

	private String numTelefono;

}

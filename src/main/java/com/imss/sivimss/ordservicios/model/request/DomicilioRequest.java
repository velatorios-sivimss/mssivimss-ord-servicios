package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomicilioRequest {
	
	private String desCalle;

	private String numExterior;

	private String numInterior;
	
	private String codigoPostal;

	private String desColonia;

	private String desMunicipio;

	private String desEstado;
	
	private String desCiudad;

}

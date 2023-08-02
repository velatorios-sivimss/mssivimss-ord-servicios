package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomicilioRequest {
	
	private Integer idDomicilio;
	
	private String desCalle;

	private String numExterior;

	private String numInterior;
	
	private String codigoPostal;

	private String desColonia;

	private String desMunicipio;

	private String desEstado;
	
	private String desCiudad;

	public DomicilioRequest(Integer idDomicilio) {
		this.idDomicilio = idDomicilio;
	}
	
	

}

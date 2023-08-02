package com.imss.sivimss.ordservicios.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PanteonResponse {
	
	private Integer idPanteon;
	
	private String nombrePanteon;

	private String desCalle;

	private String numExterior;

	private String numInterior;

	private Integer codigoPostal;

	private String desColonia;

	private String desMunicipio;

	private String desEstado;
	
	private String nombreContacto;

	private String numTelefono;
}

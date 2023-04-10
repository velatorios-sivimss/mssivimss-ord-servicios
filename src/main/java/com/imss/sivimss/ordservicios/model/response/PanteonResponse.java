package com.imss.sivimss.ordservicios.model.response;

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
public class PanteonResponse {
	
	private Integer idPanteon;
	
	private String desPanteon;

	private String desCalle;

	private String numExterior;

	private String numInterior;

	private Integer idCodigoPostal;

	private Integer codigoPostal;

	private String desColonia;

	private String desMunicipio;

	private String desEstado;
	
	private String desCiudad;
	
	private String desContacto;

	private String numTelefono;
}

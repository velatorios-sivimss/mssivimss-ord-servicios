package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformacionServicioVelacionRequest {
	
	private Integer idInformacionServicioVelacion;
	
	private String fechaInstalacion;
	
	private String horaInstalacion;
	
	private String fechaVelacion;
	
	private String horaVelacion;
	
	private Integer idCapilla;
	
	private String desCalle;

	private String numExterior;

	private String numInterior;

	private CodigoPostalRequest cp;
	
	private String desColonia;
}

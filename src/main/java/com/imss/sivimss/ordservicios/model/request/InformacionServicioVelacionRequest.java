package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


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

	private DomicilioRequest cp;

}

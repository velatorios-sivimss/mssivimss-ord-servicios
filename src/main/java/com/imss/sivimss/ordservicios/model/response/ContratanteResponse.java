package com.imss.sivimss.ordservicios.model.response;

import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratanteResponse extends Persona{

	private Integer idContratante;
	
	private String matricula;
	
	private String tipo;

	private DomicilioRequest cp;
}

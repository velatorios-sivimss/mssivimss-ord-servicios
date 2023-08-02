package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratanteRequest extends Persona{

	private Integer idContratante;
	
	private String matricula;

	private DomicilioRequest cp;
	
}

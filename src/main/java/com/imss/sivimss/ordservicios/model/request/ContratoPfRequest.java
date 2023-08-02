package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratoPfRequest {
	
	private String folio;
	
	private Integer idContrato;

	private Integer idContratante;

	private Integer idPersona;

}

package com.imss.sivimss.ordservicios.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratoPfResponse {

	private Integer idContratoPF;
	
	private Integer idTipoPrevision;
	
	private Integer idTipoContrato;

	private Integer idVelatorio;
	
	private String nombreVelatorio;

	private String vigencia;
	
	//private List<ContratantesContratoPfResponse>contratante; 
}

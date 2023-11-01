package com.imss.sivimss.ordservicios.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConvenioPAResponse {

	private Integer idConvenioPa;
	
	private String folio;
    
	private Integer idVelatorio;
	
	private String nombreVelatorio;
	
	private List<ContratanteResponse> contratante;
	
}

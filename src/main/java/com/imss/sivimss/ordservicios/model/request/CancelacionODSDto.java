package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelacionODSDto {
	
	private Integer idOrdenServicio;
	private Double costoCancelacion;
	private String  numeroFolio;
	private String motivoCancelacion;
	

}

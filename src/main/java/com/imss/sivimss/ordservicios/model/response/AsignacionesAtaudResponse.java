package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsignacionesAtaudResponse {

	private String idAsignacion;
	
	
}

package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreType(value = true)
public class PaqueteCaracteristicas {

	private Integer idPaquete;
	
	private String nombrePaquete;
	
}

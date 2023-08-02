package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreType(value = true)
public class CapillaResponse {
	
	private Integer idCapilla;
	
	private String nombreCapilla;
}

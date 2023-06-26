package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreType(value = true)
public class OrdenServicioResponse {

	private Integer idOrdenServicio;
	
	private String folio;

	private String contratante;

	private String finado;

}

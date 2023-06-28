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
public class ContratantesContratoPfResponse {

	private Integer idContratante;
	
	private Integer idPersona;
	
	private String nombreContratante;
	
	private Integer validaSiniestro;

	private Integer idTipoContratacion;

}

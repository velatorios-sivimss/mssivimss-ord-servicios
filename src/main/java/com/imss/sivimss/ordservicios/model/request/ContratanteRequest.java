package com.imss.sivimss.ordservicios.model.request;

import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ContratanteRequest extends Persona{

	private Integer idContratante;
	
	private String matricula;

	private String desCalle;

	private String numExterior;

	private String numInterior;

	private CodigoPostalRequest cp;
	
}

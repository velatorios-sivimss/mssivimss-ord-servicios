package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SituarServicio {

	private Integer idHistorialServicio;
	private String folio;
	private Integer idOrdenServicio;
	private Integer indCertificado;
	private Integer idTipoServicio;
	private String fechaSolicitud;
	private String desNotas;
	private Integer indActivo;
}

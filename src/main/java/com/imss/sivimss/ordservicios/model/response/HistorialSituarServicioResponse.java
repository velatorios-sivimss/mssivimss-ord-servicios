package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistorialSituarServicioResponse {

	private Integer idHistorialServicio;
	private String nombreServicio;
	private String fechaSolicitud;
	private String desNotas;
	private Integer estatus;
	private Integer indCertificado;
}

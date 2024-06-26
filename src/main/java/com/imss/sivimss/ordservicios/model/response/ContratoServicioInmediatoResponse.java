package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratoServicioInmediatoResponse {
	private String folioOds;
	private String nombreFibeso;
	private String nombreContratante;
	private String correoFibeso;
	private String telefonoQuejas;
	private String direccionCliente;
	private String rfcCliente;
	private String telefonoCliente;
	private String correoCliente;
	private String direccionVelatorio;
	private String fechaOds;
	private String horarioInicio;
	private String horarioFin;
	private String capillaVelatorio;
	private String nombreFinado;
	private String lugarExpedicion;
	private String horarioServicio;
	private String descripcionServicio; 
	private String totalOds;
	private String nombrePanteon;
	private String fechaServicio;

}

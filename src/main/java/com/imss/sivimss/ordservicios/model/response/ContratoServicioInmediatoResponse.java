package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratoServicioInmediatoResponse {
	private String nombreCliente;
	private String nacionalidadCliente;
	private String correoCliente;
	private String telefonoCliente;
	private String rfcCliente;
	private String calleCliente;
	private String numInteriorCliente;
	private String numExteriorCliente;
	private String coloniaCliente;
	private String cpCliente;
	private String municipioCliente;
	private String estadoCliente;
	private String importeTotalOds;
	private String nombreVelatorio;
	private String calleVelatorio;
	private String numInteriorVelatorio;
	private String numExteriorVelatorio;
	private String coloniaVelatorio;
	private String cpVelatorio;
	private String municipioVelatorio;
	private String estadoVelatorio;
	private String nombreFinado;
	private Integer diaOds;
	private Integer mesOds;
	private Integer anioOds;
	private String nombrePaquete;
	private String precioPaquete;

}

package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteDto {
	
	private Integer idOds;
	private Integer estatus;
	private Integer idVelatorio;
	private Integer idContratante;
	private Integer idFinado;
	private Integer idTipoODS;
	private Integer idUnidadMedica;
	private Integer idConvenio; 
	private Integer idOrdenServicio;
	private String tipoReporte;
	private String rutaNombreReporte;
	private String motivoCancelacion;
	private String idSalidaDona;
	private Integer idDonacion;
	private Integer idAtaudDonacion;
	private Integer idEstatusODS;
	private Integer idDelegacion;
	private String fechaIni;
	private String fechaFin;
	

}

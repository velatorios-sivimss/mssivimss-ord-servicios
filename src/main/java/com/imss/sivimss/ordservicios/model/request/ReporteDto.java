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
	private String cveFolio;
	private String nombreContratante;
	private String apPatContratante;
	private String apMatContratante;
	private String nombreFinado;
	private String apPatFinado;
	private String apMatFinado;
	private String cveConvenio; 
	private String fecha_inicial;
	private String fecha_final;
	private String id_delegacion;
	private String id_velatorio;
	private String id_tipo_reporte;

}

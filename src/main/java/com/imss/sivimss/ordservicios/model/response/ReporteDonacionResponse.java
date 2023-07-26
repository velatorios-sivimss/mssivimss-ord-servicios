package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReporteDonacionResponse {

	@JsonProperty
	private Double version=5.2;
	@JsonProperty
	private String ooadNom;
	@JsonProperty
	private Integer velatorioId;
	@JsonProperty
	private String numContrato;
	@JsonProperty
	private String modeloAtaud;
	@JsonProperty
	private String tipoAtaud;
	@JsonProperty
	private String numInventarios;
	@JsonProperty
	private String nomFinado;
	@JsonProperty
	private String nomResponsableAlmacen;
	@JsonProperty
	private String claveResponsableAlmacen;
	@JsonProperty
	private String nomContratante;
	@JsonProperty
	private String nomAdministrador;
	@JsonProperty
	private String claveAdministrador;
	@JsonProperty
	private String lugar;
	@JsonProperty
	private Integer dia;
	@JsonProperty
	private String mes;
	@JsonProperty
	private Integer anio;
	@JsonProperty
	private String tipoReporte;

}

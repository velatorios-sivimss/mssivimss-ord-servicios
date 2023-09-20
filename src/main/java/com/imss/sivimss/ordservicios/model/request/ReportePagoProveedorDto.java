package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@JsonIgnoreType(value = true)
public class ReportePagoProveedorDto {
	
	private String tipoReporte;
	private Integer id_velatorio;
	private Integer id_delegacion;
	private String fecha_inicial;
	private String fecha_final;
	private String fecInicioConsulta;
	private String fecFinConsulta;

}

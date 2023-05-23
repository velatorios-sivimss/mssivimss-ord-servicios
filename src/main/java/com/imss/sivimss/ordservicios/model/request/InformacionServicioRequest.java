package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformacionServicioRequest {

	private Integer idInformacionServicio;
	private String fechaCortejo;
	private String horaCortejo;
	private String fechaRecoger;
	private String horaRecoger;
	private Integer idPanteon;
	private Integer idSala;
	private String fechaCremacion;
	private String horaCremacion;
	private Integer idPromotores;
	private InformacionServicioVelacionRequest informacionServicioVelacion;
}

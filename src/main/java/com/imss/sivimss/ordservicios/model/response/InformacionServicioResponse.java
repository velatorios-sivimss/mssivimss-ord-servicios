package com.imss.sivimss.ordservicios.model.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformacionServicioResponse {
	private Integer idInformacionServicio;
	private String fechaCortejo;
	private String horaCortejo;
	private String fechaRecoger;
	private String horaRecoger;
	private Integer idPanteon;
	private Integer idSala;
	private String fechaCremacion;
	private String horaCremacion;
	private Integer idPromotor;
	private InformacionServicioVelacionResponse informacionServicioVelacion;
}

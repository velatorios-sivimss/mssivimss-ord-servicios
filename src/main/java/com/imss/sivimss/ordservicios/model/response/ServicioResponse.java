package com.imss.sivimss.ordservicios.model.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"idServicio", "nombreServicio"})
public class ServicioResponse {

	private Integer idServicio;

	private String nombreServicio;
}

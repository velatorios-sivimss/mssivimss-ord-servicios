package com.imss.sivimss.ordservicios.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdministrarOperacionODSResponse {
	
	private Integer idOrden;
	private String contrante;
	private String finado;
	private List<HistorialSituarServicioResponse>historialSituarServicioResponses;

}

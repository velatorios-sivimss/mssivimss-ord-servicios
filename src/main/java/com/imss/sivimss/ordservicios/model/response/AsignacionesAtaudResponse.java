package com.imss.sivimss.ordservicios.model.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsignacionesAtaudResponse {

	private String idAsignacion;
	//private List<Integer> idAsignacion;
	
}

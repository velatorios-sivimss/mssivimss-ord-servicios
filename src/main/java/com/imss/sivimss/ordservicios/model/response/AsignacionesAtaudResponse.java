package com.imss.sivimss.ordservicios.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AsignacionesAtaudResponse {

	List<ArticuloFunerarioResponse>consigando= new ArrayList<>();
	List<ArticuloFunerarioResponse>donado= new ArrayList<>();
	List<ArticuloFunerarioResponse>economico= new ArrayList<>();
}

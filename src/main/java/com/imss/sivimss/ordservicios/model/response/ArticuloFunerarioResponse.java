package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreType(value = true)
public class ArticuloFunerarioResponse {

	private Integer idArticulo;

	private Integer idInventario;
	
	private String nombreArticulo;
	
	private Double precio;

	private Integer idCategoria;
	
	private Integer idProveedor;
	
	private String nombreProveedor;
	
	private String grupo;
}

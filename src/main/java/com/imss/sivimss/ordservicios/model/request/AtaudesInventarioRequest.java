package com.imss.sivimss.ordservicios.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AtaudesInventarioRequest {
	private Integer idAsignacion;
	private Integer idArticulo;
	private Integer idProveedor;	
	private Integer idVelatorio;

}

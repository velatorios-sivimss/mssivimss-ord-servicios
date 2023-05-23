package com.imss.sivimss.ordservicios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Ataud {

	private static Ataud instancia;
	
	private Ataud() {}
	
	public static Ataud obtenerInstancia() {
		if (instancia == null) {
			instancia = new Ataud();
		}
		
		return instancia;
	}
	
	public DatosRequest obtenerAtaudes() {
		DatosRequest request = new DatosRequest();
		Map<String, Object>paramtero= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("STA.ID_ARTICULO AS idArticulo","STA.DES_MODELO_ARTICULO AS nombreArticulo")
		.from("SVT_ARTICULO STA")
		.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = STA .ID_CATEGORIA_ARTICULO")
		//.innerJoin("SVT_INVENTARIO STI", "STA.ID_ARTICULO = STI .ID_ARTICULO")
		.innerJoin("SVC_TIPO_ARTICULO STA2", "STA .ID_TIPO_ARTICULO = STA2.ID_TIPO_ARTICULO ")
		.where("STA.ID_CATEGORIA_ARTICULO = 1")
		//.and("STI.CAN_STOCK > 0")
		.and("STA.IND_ACTIVO =1")
		.and("STA.ID_TIPO_ARTICULO =1");
		String query= selectQueryUtil.build();
		String encoded= DatatypeConverter.printBase64Binary(query.getBytes());
		paramtero.put(AppConstantes.QUERY, encoded);
		request.setDatos(paramtero);
		return request;
	}
	
	public DatosRequest obtenerAtaudesTipoAsignacion(Integer idTipoAsignacion) {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		String query;
		switch (idTipoAsignacion) {
		case 1:
			query=obtenerAtaudConsignado(idTipoAsignacion);
			break;
			
		case 3:
			query=obtenerAtaudDonado(idTipoAsignacion);
			break;
		case 5:
			query=obtenerAtaudEconomico(idTipoAsignacion);
			break;
		

		default:
			query="";
			break;
		}
		String encoded= DatatypeConverter.printBase64Binary(query.getBytes());
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
		
	}
	
	public String obtenerAtaudConsignado(Integer idTipoAsignacion) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		
		return selectQueryUtil.build();
	}
	
	public String obtenerAtaudDonado(Integer idTipoAsignacion) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		return selectQueryUtil.build();
	}
	
	public String obtenerAtaudEconomico(Integer idTipoAsignacion) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		return selectQueryUtil.build();
	}
}

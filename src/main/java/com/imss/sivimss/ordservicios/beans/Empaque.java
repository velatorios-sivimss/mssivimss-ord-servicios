package com.imss.sivimss.ordservicios.beans;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Empaque {

	private static Empaque instancia;
	
	private Empaque() {}
	
	public static Empaque obtenerInstancia() {
		if (instancia == null) {
			instancia = new Empaque();
		}
		
		return instancia;
	}
	
	public DatosRequest obtenerEmpaque() {
		DatosRequest request = new DatosRequest();
		Map<String, Object>paramtero= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("STA.ID_ARTICULO AS idArticulo","STA.DES_MODELO_ARTICULO AS nombreArticulo")
		.from("SVT_ARTICULO STA")
		.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = STA .ID_CATEGORIA_ARTICULO")
		//.innerJoin("SVT_INVENTARIO STI", "STA.ID_ARTICULO = STI .ID_ARTICULO")
		.innerJoin("SVC_TIPO_ARTICULO STA2", "STA .ID_TIPO_ARTICULO = STA2.ID_TIPO_ARTICULO ")
		.where("STA.ID_CATEGORIA_ARTICULO = 4")
		//.and("STI.CAN_STOCK > 0")
		.and("STA.IND_ACTIVO =1")
		.and("STA.ID_TIPO_ARTICULO =1");
		String query= selectQueryUtil.build();
		String encoded= DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		paramtero.put(AppConstantes.QUERY, encoded);
		request.setDatos(paramtero);
		return request;
	}
	
}

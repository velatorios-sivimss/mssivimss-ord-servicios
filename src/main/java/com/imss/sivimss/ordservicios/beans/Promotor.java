package com.imss.sivimss.ordservicios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Promotor {

	private static Promotor instancia;
	
	private Promotor() {}
	
	public static Promotor getInstancia() {
		if (instancia==null) {
			instancia= new Promotor();
		}
		
		return instancia;
	}
	
	public DatosRequest consultarPromotores() {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SPR.ID_PROMOTOR AS idPromotor","concat(SPR.NOM_PROMOTOR,' ', SPR.NOM_PAPELLIDO,' ',SPR.NOM_SAPELLIDO) as nombrePromotor")
		.from("SVT_PROMOTOR SPR")
		.where("SPR.IND_ACTIVO = 1")
		.orderBy("nombrePromotor ASC");
		String query= selectQueryUtil.build();
		String encoded=DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}
}

package com.imss.sivimss.ordservicios.beans;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Kilometraje {
	
	private static Kilometraje instancia;
	
	private Kilometraje () {}
	
	public static Kilometraje instancia() {
		if (instancia==null) {
			instancia= new Kilometraje();
		}
		return instancia;
	}
	
	public DatosRequest obtenerKilometrajePorPaquete(Integer idPaquete) throws UnsupportedEncodingException {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		String query= selectQueryUtil.build();
		String encoded= selectQueryUtil.encrypt(query);
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
	
	public DatosRequest obtenerKilometrajePorServicio(Integer idProveedor) throws UnsupportedEncodingException {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		
		String query=selectQueryUtil.build();
		String encoded=selectQueryUtil.encrypt(query);
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}

}

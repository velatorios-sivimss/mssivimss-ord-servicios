package com.imss.sivimss.ordservicios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Servicio {

	private static Servicio instancia;
	
	private Servicio() {}
	
	public static Servicio getInstancia() {
		if (instancia==null) {
			instancia= new Servicio();
		} 
		
		return instancia;
	}
	
	public DatosRequest obtenerServicio(){
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SS.ID_SERVICIO AS idServicio","SS.NOM_SERVICIO AS nombreServicio")
		.from("SVT_SERVICIO SS")
		.where("SS.CVE_ESTATUS =1")
		.orderBy("SS.NOM_SERVICIO ASC");
		
		String query=selectQueryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
	
	public DatosRequest obtenerProveedorServicio(Integer idServicio){
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SP.ID_PROVEEDOR AS idProveedor","SP.NOM_PROVEEDOR AS nombreProveedor")
		.from("SVT_PROVEEDOR SP")
		.where("SP.CVE_ESTATUS =1")
		.orderBy("SP.NOM_PROVEEDOR ASC");
		
		String query=selectQueryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
}

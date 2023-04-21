package com.imss.sivimss.ordservicios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Paquete {

	private static Paquete instancia;
	
	private Paquete() {}
	
	public static Paquete getInstancia() {
		if (instancia==null) {
			instancia = new Paquete();
		}
		return instancia;
	}
	
	public DatosRequest obtenerPaquete() {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SP.ID_PAQUETE AS idPaquete","SP.NOM_PAQUETE AS nombrePaquete")
		.from("SVT_PAQUETE SP")
		.where("SP.CVE_ESTATUS =1")
		.orderBy("SP.NOM_PAQUETE ASC");
		
		String query=queryUtil.build();
		String encoded=DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}

	public DatosRequest obtenerServiciosPaquete(Integer idPaquete) {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SS.ID_SERVICIO AS idServicio","SS.NOM_SERVICIO AS nombreServicio")
		.from("SVT_SERVICIO SS")
		.innerJoin("SVT_PAQUETE_SERVICIO SPS", "SS.ID_SERVICIO =SPS.ID_SERVICIO")
		.innerJoin("SVT_PAQUETE SP", "SPS.ID_PAQUETE = SP.ID_PAQUETE")
		.where("SS.CVE_ESTATUS = 1","SP.ID_PAQUETE = :idVelatorio")
		.setParameter("idVelatorio", idPaquete)
		.orderBy("SS.NOM_SERVICIO ASC");
		
		String query=queryUtil.build();
		String encoded=DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}
}

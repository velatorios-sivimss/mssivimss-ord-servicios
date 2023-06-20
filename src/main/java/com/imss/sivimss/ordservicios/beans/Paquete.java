package com.imss.sivimss.ordservicios.beans;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Paquete {

	private static Paquete instancia;
	
	private static final Logger log = LoggerFactory.getLogger(Paquete.class);

	private Paquete() {}
	
	public static Paquete getInstancia() {
		if (instancia==null) {
			instancia = new Paquete();
		}
		return instancia;
	}
	
	public DatosRequest obtenerPaquetes() {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SP.ID_PAQUETE AS idPaquete","SP.DES_NOM_PAQUETE AS nombrePaquete")
		.from("SVT_PAQUETE SP")
		.innerJoin("SVT_PAQUETE_VELATORIO SPV", "SP.ID_PAQUETE=SPV.ID_PAQUETE")
		.where("SP.IND_ACTIVO = 1")
		.and("SPV.ID_VELATORIO = 1")
		.orderBy("SP.DES_NOM_PAQUETE ASC");
		SelectQueryUtil queryUnion= new SelectQueryUtil();
		
		
		String query=queryUnion.build();
		log.info(query);

		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}

	public DatosRequest obtenerServiciosPaquete(Integer idPaquete) {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SS.ID_SERVICIO AS idServicio","SS.DES_NOM_SERVICIO AS nombreServicio")
		.from("SVT_SERVICIO SS")
		.innerJoin("SVT_PAQUETE_SERVICIO SPS", "SS.ID_SERVICIO = SPS.ID_SERVICIO")
		.innerJoin("SVT_PAQUETE SPA", "SPS.ID_PAQUETE = SPA.ID_PAQUETE")
		.where("SPA.ID_PAQUETE = :idPaquete")
		.setParameter("idPaquete", idPaquete)
		.orderBy("nombreServicio ASC");
		
		String query=queryUtil.build();
		log.info(query);

		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}
	
	public DatosRequest obtenerCaracteristicasPaquete(Integer idPaquete) {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("");
		String query="";
		log.info(query);

		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametros.put(AppConstantes.QUERY, encoded);
		return datosRequest;
	}
}

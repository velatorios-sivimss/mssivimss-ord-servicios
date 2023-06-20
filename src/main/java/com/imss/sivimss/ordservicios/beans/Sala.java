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

public class Sala {

	private static Sala instancia;
	
	private static final Logger log = LoggerFactory.getLogger(Sala.class);

	private Sala() {}
	
	public static Sala getInstancia() {
		if (instancia==null) {
			instancia= new Sala();
		} 
		
		return instancia;
	}
	
	public DatosRequest obtenerSala(Integer idVelatorio){
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SS.ID_SALA AS idSala","SS.DES_SALA AS nombreSala")
		.from("SVC_SALA SS")
		.where("SS.IND_ACTIVO = 1","SS.ID_VELATORIO = :idVelatorio")
		.setParameter("idVelatorio", idVelatorio)
		.orderBy("SS.DES_SALA ASC");
		
		String query=selectQueryUtil.build();
		log.info(query);

		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
}

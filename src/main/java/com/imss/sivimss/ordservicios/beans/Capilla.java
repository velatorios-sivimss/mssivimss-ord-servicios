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

public class Capilla {

	private static Capilla instancia;
	
	
	private static final Logger log = LoggerFactory.getLogger(Capilla.class);

	private Capilla() {}
	
	public static Capilla getInstancia() {
		if (instancia==null) {
			instancia = new Capilla();
		}
		return instancia;
	}
	
	public DatosRequest obtenerCapillas(Integer idVelatorio) {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SC.ID_CAPILLA AS idCapilla","SC.DES_CAPILLA AS nombreCapilla")
		.from("SVC_CAPILLA SC")
		.where("SC.CVE_ESTATUS = 1","SC .ID_VELATORIO = :idVelatorio")
		.setParameter("idVelatorio", idVelatorio)
		.orderBy("SC.DES_CAPILLA ASC");
		String query=queryUtil.build();

		log.info(query);

		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}
}

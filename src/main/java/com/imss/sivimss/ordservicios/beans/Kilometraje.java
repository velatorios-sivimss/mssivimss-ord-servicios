package com.imss.sivimss.ordservicios.beans;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Kilometraje {
	
	private static Kilometraje instancia;
	
	
	private static final Logger log = LoggerFactory.getLogger(Kilometraje.class);

	private Kilometraje () {}
	
	public static Kilometraje getInstancia() {
		if (instancia==null) {
			instancia= new Kilometraje();
		}
		return instancia;
	}
	
	public DatosRequest obtenerKilometrajePorPaquete(Integer idPaquete, Integer idProveedor, Integer idVelatorio) throws UnsupportedEncodingException {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilCosto= new SelectQueryUtil();
		
		selectQueryUtilCosto.select("SCS.MON_PRECIO")
		.from("SVT_CONTRATO SC")
		.innerJoin("SVT_PROVEEDOR SP", "SC.ID_PROVEEDOR = SP.ID_PROVEEDOR")
		.innerJoin("SVT_CONTRATO_SERVICIO SCS", "SC.ID_CONTRATO = SCS.ID_CONTRATO")
		.innerJoin("SVT_SERVICIO SS", "SCS.ID_SERVICIO = SS.ID_SERVICIO")
		.where("SC.ID_PROVEEDOR ="+idProveedor).and("SC.ID_VELATORIO="+idVelatorio).and("SS.ID_TIPO_SERVICIO = 4 LIMIT 1");
		
		selectQueryUtil.select("DISTINCT SCT.NUM_KILOMETRO AS numKilometraje","("+selectQueryUtilCosto.build()+") AS costoPorKilometraje")
		.from("SVT_PAQUETE SPA")
		.innerJoin("SVT_PAQUETE_SERVICIO SPS", "SPA.ID_PAQUETE = SPS.ID_PAQUETE")
		.innerJoin("SVT_CONF_TRASLADO SCT", "SCT.ID_PAQUETE = SPA.ID_PAQUETE")
		.where("SCT.ID_PAQUETE = :idPaquete")
		.setParameter("idPaquete", idPaquete);
		String query= selectQueryUtil.build();
		log.info(query);

		String encoded= selectQueryUtil.encrypt(query);
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
	
	public DatosRequest obtenerKilometrajePorServicio(Integer idServicio, Integer idProveedor,Integer idVelatorio) throws UnsupportedEncodingException {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SCS.MON_PRECIO AS costoPorKilometraje")
		.from("SVT_CONTRATO SC ")
		.innerJoin("SVT_PROVEEDOR SP", "SC.ID_PROVEEDOR = SP.ID_PROVEEDOR")
		.innerJoin("SVT_CONTRATO_SERVICIO SCS", "SC.ID_CONTRATO = SCS.ID_CONTRATO")
		.innerJoin("SVT_SERVICIO SS", "SCS.ID_SERVICIO = SS.ID_SERVICIO")
		.where(" SC.ID_PROVEEDOR = :idProveedor")
		.and("SCS.ID_SERVICIO = :idServicio")
		.and("SC.ID_VELATORIO="+idVelatorio)
		.and("SS.ID_TIPO_SERVICIO =4 LIMIT 1")
		.setParameter("idProveedor", idProveedor)
		.setParameter("idServicio", idServicio);
		String query=selectQueryUtil.build();
		log.info(query);

		String encoded=selectQueryUtil.encrypt(query);
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}

}

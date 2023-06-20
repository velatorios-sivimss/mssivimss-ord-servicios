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

public class Urna {

	private static Urna instancia;
	
	private static final Logger log = LoggerFactory.getLogger(Urna.class);

	private Urna() {}
	
	public static Urna obtenerInstancia() {
		if (instancia == null) {
			instancia = new Urna();
		}
		
		return instancia;
	}
	
	public DatosRequest obtenerUrna() {
		DatosRequest request = new DatosRequest();
		Map<String, Object>paramtero= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("STA.ID_ARTICULO AS idArticulo","STA.DES_MODELO_ARTICULO AS nombreArticulo")
		.from("SVT_ARTICULO STA")
		.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = STA .ID_CATEGORIA_ARTICULO")
		.innerJoin("SVC_TIPO_ARTICULO ST", "STA.ID_TIPO_ARTICULO = ST.ID_TIPO_ARTICULO ")
		.where("STA.ID_CATEGORIA_ARTICULO = 2")
		.and("STA.IND_ACTIVO =1")
		.and("STA.ID_TIPO_ARTICULO =1");
		String query= selectQueryUtil.build();
		log.info(query);

		String encoded= DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		paramtero.put(AppConstantes.QUERY, encoded);
		request.setDatos(paramtero);
		return request;
	}
	
}

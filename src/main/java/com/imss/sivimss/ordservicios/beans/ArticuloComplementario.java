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

public class ArticuloComplementario {

	private static ArticuloComplementario instancia;
	
	private ArticuloComplementario() {}
	
	
	private static final Logger log = LoggerFactory.getLogger(ArticuloComplementario.class);

	
	public static ArticuloComplementario getInstancia() {
		if (instancia== null) {
			instancia= new ArticuloComplementario();
		}
		
		return instancia;
	}
	
	public DatosRequest obtenerArticulosComplementarios() {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("STA.ID_ARTICULO AS idArticulo","STA.DES_ARTICULO AS nombreArticulo")
		.from("SVT_ARTICULO STA")
		.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = STA .ID_CATEGORIA_ARTICULO")
		.where("STA.IND_ACTIVO=1")
		.and("STA.ID_TIPO_ARTICULO = 2 ")
		.orderBy("nombreArticulo ASC");
		String query=selectQueryUtil.build();
		
		log.info(query);
		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}
	
	public DatosRequest obtenerArticulosComplementariosPorId(Integer idArticulo) {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("STA.ID_ARTICULO AS idArticulo","STA.DES_ARTICULO AS nombreArticulo")
		.from("SVT_ARTICULO STA")
		.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = STA .ID_CATEGORIA_ARTICULO")
		//.innerJoin("SVT_INVENTARIO STI", "STA.ID_ARTICULO = STI .ID_ARTICULO")
		.innerJoin("SVC_TIPO_ARTICULO STA2", "STA .ID_TIPO_ARTICULO = STA2.ID_TIPO_ARTICULO")
		//.innerJoin("SVT_PROVEEDOR SP", "STI.ID_PROVEEDOR = SP.ID_PROVEEDOR")
		.where("STA.CVE_ESTATUS =1")
		//.and("STI.CAN_STOCK > 0")
		.and("STA.ID_TIPO_ARTICULO = 2 ")
		.orderBy("nombreArticulo ASC");
		String query=selectQueryUtil.build();

		log.info(query);

		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}
}

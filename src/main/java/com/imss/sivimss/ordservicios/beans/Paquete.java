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
	
	public DatosRequest obtenerPaquetes(Integer idVelatorio) {
		DatosRequest datosRequest = new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtilPaquete= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilUnionPaqueteRegion= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilUnionPaqueteVelatorio= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilUnionPaqueteServicio= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilUnionPaqueteArticulo= new SelectQueryUtil();
		
		selectQueryUtilUnionPaqueteVelatorio.select("SP.ID_PAQUETE","SP.DES_NOM_PAQUETE")
		.from("SVT_PAQUETE SP")
		.innerJoin("SVT_PAQUETE_VELATORIO SPV", "SP.ID_PAQUETE=SPV.ID_PAQUETE")
		.where("SP.IND_ACTIVO = 1")
		.and("SPV.ID_VELATORIO = "+idVelatorio);

		selectQueryUtilUnionPaqueteRegion.select("SP.ID_PAQUETE","SP.DES_NOM_PAQUETE")
		.from("SVT_PAQUETE SP")
		.where("SP.IND_ACTIVO =1 ")
		.and("SP.IND_REGION =1");
		
		selectQueryUtilUnionPaqueteServicio.select("SPS.ID_PAQUETE")
		.from("SVT_PROVEEDOR SP")
		.innerJoin("SVT_CONTRATO SC", "SC.ID_PROVEEDOR = SP.ID_PROVEEDOR")
		.innerJoin("SVT_CONTRATO_SERVICIO SCS", "SCS.ID_CONTRATO = SC.ID_CONTRATO")
		.innerJoin("SVT_SERVICIO SS", "SS.ID_SERVICIO = SCS.ID_SERVICIO")
		.innerJoin("SVT_PAQUETE_SERVICIO SPS", "SPS.ID_SERVICIO = SS.ID_SERVICIO")
		.where("SP.IND_ACTIVO =1 ")
		.and("SPS.IND_ACTIVO = 1")
		.and("SP.ID_TIPO_PROVEEDOR =1")
		.and("SC.FEC_FIN_VIG >= CURRENT_DATE()")
		.and("SP.FEC_VIGENCIA >= CURRENT_DATE()")
		.and("SC.IND_ACTIVO =1");
		
		selectQueryUtilUnionPaqueteArticulo.select("DISTINCT SPA.ID_PAQUETE")
		.from("SVT_INVENTARIO_ARTICULO STI")
		.innerJoin("SVT_ARTICULO STA", "STA.ID_ARTICULO =STI.ID_ARTICULO")
		.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = STA.ID_CATEGORIA_ARTICULO")
		.innerJoin("SVT_PAQUETE_ARTICULO SPA", "SPA.ID_CATEGORIA_ARTICULO = SCA.ID_CATEGORIA_ARTICULO")
		.where("STI.IND_ESTATUS = 0")
		.and("STI.ID_TIPO_ASIGNACION_ART IN (1,3)");
		
		String queryPaqueteRegion=selectQueryUtilUnionPaqueteVelatorio.union(selectQueryUtilUnionPaqueteRegion);
		String queryPaqueteServiciosArticulos=selectQueryUtilUnionPaqueteServicio.union(selectQueryUtilUnionPaqueteArticulo);
		
		selectQueryUtilPaquete.select("PAQUETES.ID_PAQUETE AS idPaquete","PAQUETES.DES_NOM_PAQUETE AS nombrePaquete")
		.from("("+queryPaqueteRegion+") PAQUETES")
		.where("PAQUETES.ID_PAQUETE IN("+queryPaqueteServiciosArticulos+")");
		
		String query=selectQueryUtilPaquete.build();
		log.info(query);

		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}

	public DatosRequest obtenerServiciosPaquete(Integer idPaquete) {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SS.ID_SERVICIO AS idServicio","SS.DES_SERVICIO AS nombreServicio")
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
		SelectQueryUtil selectQueryUtilServicio= new SelectQueryUtil();
		selectQueryUtilServicio.select("'' AS idCategoria","SPS.ID_SERVICIO AS idServicio","SS.ID_TIPO_SERVICIO AS idTipoServicio","STPS.DES_TIPO_SERVICIO AS grupo",
				"SS.DES_SERVICIO AS concepto","SP.MON_PRECIO AS importe","'1' AS cantidad","SP.MON_PRECIO AS totalPaquete");
		selectQueryUtilServicio.from("SVT_PAQUETE SP")
		.innerJoin("SVT_PAQUETE_SERVICIO SPS", "SPS.ID_PAQUETE = SP.ID_PAQUETE")
		.leftJoin("SVT_SERVICIO SS", "SS.ID_SERVICIO =SPS.ID_SERVICIO")
		.innerJoin("SVC_TIPO_SERVICIO STPS", "SS.ID_TIPO_SERVICIO = STPS.ID_TIPO_SERVICIO")
		.where("SP.ID_PAQUETE = :idPaquete")
		.and("SPS.IND_ACTIVO = 1")
		.setParameter("idPaquete", idPaquete);
		
		SelectQueryUtil selectQueryUtilArticulo= new SelectQueryUtil();
		selectQueryUtilArticulo.select("SCA.ID_CATEGORIA_ARTICULO AS idCategoria","'' AS idServicio","'' AS idTipoServicio","SCA.DES_CATEGORIA_ARTICULO AS grupo",
				"'' AS concepto","SP.MON_PRECIO AS importe","'1' AS cantidad","SP.MON_PRECIO AS totalPaquete");
		selectQueryUtilArticulo.from("SVT_PAQUETE SP")
		.innerJoin("SVT_PAQUETE_ARTICULO SPA", "SP.ID_PAQUETE= SPA.ID_PAQUETE ")
		.leftJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = SPA.ID_CATEGORIA_ARTICULO")
		.where("SP.ID_PAQUETE = :idPaquete")
		.and("SPA.IND_ACTIVO = 1")
		.setParameter("idPaquete", idPaquete);
		
		String query=selectQueryUtilServicio.union(selectQueryUtilArticulo);
		log.info(query);

		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
}

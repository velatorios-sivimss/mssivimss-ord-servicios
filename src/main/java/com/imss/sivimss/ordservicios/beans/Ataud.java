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

public class Ataud {

	private static Ataud instancia;
	
	
	private static final Logger log = LoggerFactory.getLogger(Ataud.class);

	private Ataud() {}
	
	public static Ataud obtenerInstancia() {
		if (instancia == null) {
			instancia = new Ataud();
		}
		
		return instancia;
	}
	
	public DatosRequest obtenerAtaudes(Integer idVelatorio) {
		DatosRequest request = new DatosRequest();
		Map<String, Object>paramtero= new HashMap<>();
		SelectQueryUtil selectQueryUtilArticulo= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilInventarioTemp= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilInventario= new SelectQueryUtil();
		
		selectQueryUtilInventarioTemp.select("IFNULL(STP.ID_INVE_ARTICULO,0)")
		.from("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP STP")
		.where("STP.IND_ACTIVO=1")
		.and("DATE_FORMAT(STP.FEC_ALTA,'YY-MM-DD')=DATE_FORMAT(CURRENT_DATE(),'YY-MM-DD')")
		.and("TIMESTAMPDIFF(MINUTE,DATE_ADD(STP.FEC_ALTA, INTERVAL 4 HOUR),CURRENT_TIMESTAMP()) <= 0");
		
		selectQueryUtilInventario.select("IFNULL(SDCP.ID_INVE_ARTICULO,0)")
		.from("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP")
		.where("SDCP.IND_ACTIVO=1");
		
		selectQueryUtilArticulo.select("DISTINCT STA.ID_ARTICULO AS idArticulo","STI.ID_INVE_ARTICULO AS idInventario","CONCAT(STI.FOLIO_ARTICULO,'-',STA.DES_MODELO_ARTICULO,' $',SCA.MON_PRECIO) AS nombreArticulo",
				"STA.ID_CATEGORIA_ARTICULO AS idCategoria","SC.ID_PROVEEDOR AS idProveedor",
				"STCA.DES_CATEGORIA_ARTICULO AS grupo",
				"SCA.MON_PRECIO AS precio")
		.from("SVT_INVENTARIO_ARTICULO STI")
		.innerJoin("SVT_ARTICULO STA", "STI.ID_ARTICULO = STA.ID_ARTICULO")
		.innerJoin("SVC_CATEGORIA_ARTICULO STCA", "STCA.ID_CATEGORIA_ARTICULO = STA.ID_CATEGORIA_ARTICULO")
		.innerJoin("SVT_ORDEN_ENTRADA SOE2", "SOE2.ID_ODE = STI.ID_ODE")
		.innerJoin("SVT_CONTRATO SC", "SC.ID_CONTRATO = SOE2.ID_CONTRATO")
		.innerJoin("SVT_PROVEEDOR SP", "SP .ID_PROVEEDOR = SC.ID_PROVEEDOR")
		.innerJoin("SVT_CONTRATO_ARTICULOS SCA", "SCA.ID_CONTRATO = SC.ID_CONTRATO AND STA.ID_ARTICULO=SCA.ID_ARTICULO")
		.where("STI.IND_ESTATUS NOT IN (1,2,3)")
		.and("STA.ID_CATEGORIA_ARTICULO = 1")
		.and("STI.ID_VELATORIO = "+idVelatorio)
		.and("STI.ID_TIPO_ASIGNACION_ART =1")
		.and("STI.ID_INVE_ARTICULO NOT IN ("+selectQueryUtilInventarioTemp.build()+")")
		.and("STI.ID_INVE_ARTICULO NOT IN("+selectQueryUtilInventario.build()+")");
		
		String query=selectQueryUtilArticulo.build();
		
		log.info(query);
		String encoded= DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));

		paramtero.put(AppConstantes.QUERY, encoded);
		request.setDatos(paramtero);
		return request;
	}
	

	
	public DatosRequest obtenerAsignacionAtaudPaquete(Integer idPaquete) {
		DatosRequest request = new DatosRequest();
		Map<String, Object>paramtero= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("ifnull(group_concat(STPA.ID_TIPO_ASIGNACION_ARTICULO),'0') AS idAsignacion")
		.from("SVT_PAQUETE SPA")
		.innerJoin("SVT_PAQUETE_ARTICULO STA", "STA.ID_PAQUETE=SPA.ID_PAQUETE")
		.innerJoin("SVT_PAQUETE_ARTICULO_TIPO STPA", "STPA.ID_PAQUETE_ARTICULO = STA.ID_PAQUETE_ARTICULO")
		.where("SPA.ID_PAQUETE = :idPaquete")
		.setParameter("idPaquete", idPaquete);
		String query=selectQueryUtil.build();
	
		log.info(query);
		String encoded= DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));

		paramtero.put(AppConstantes.QUERY, encoded);
		request.setDatos(paramtero);
		return request;
	}
	
	public DatosRequest obtenerAtaudTipoAsignacion(Integer idVelatorio, Integer idTipoAsignacion) {
		DatosRequest datosRequest = new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtilArticulo= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilInventarioTemp= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilInventario= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilCosto= new SelectQueryUtil();
		
		selectQueryUtilInventarioTemp.select("IFNULL(STP.ID_INVE_ARTICULO,0)")
		.from("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP STP")
		.where("STP.IND_ACTIVO=1")
		.and("DATE_FORMAT(STP.FEC_ALTA,'YY-MM-DD')=DATE_FORMAT(CURRENT_DATE(),'YY-MM-DD')")
		.and("TIMESTAMPDIFF(MINUTE,DATE_ADD(STP.FEC_ALTA, INTERVAL 4 HOUR),CURRENT_TIMESTAMP()) <= 0");
		
		selectQueryUtilInventario.select("IFNULL(SDCP.ID_INVE_ARTICULO,0)")
		.from("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP")
		.where("SDCP.IND_ACTIVO=1");
		
		selectQueryUtilArticulo.select("DISTINCT SA.ID_ARTICULO AS idArticulo","SA.DES_ARTICULO AS nombreArticulo")
		.from("SVT_ARTICULO SA")
		.innerJoin("SVT_INVENTARIO_ARTICULO STI", "SA.ID_ARTICULO = STI.ID_ARTICULO AND STI.IND_ESTATUS NOT IN (2,3) AND "
				+ "STI.ID_INVE_ARTICULO NOT IN ("+selectQueryUtilInventarioTemp.build()+") AND STI.ID_INVE_ARTICULO NOT IN ("+selectQueryUtilInventario.build()+")")
		.innerJoin("SVT_ORDEN_ENTRADA SOE2", "SOE2.ID_ODE = STI.ID_ODE")
		.innerJoin("SVT_CONTRATO SC", "SC.ID_CONTRATO = SOE2.ID_CONTRATO")
		.innerJoin("SVT_CONTRATO_ARTICULOS SCA", "SCA.ID_CONTRATO = SC.ID_CONTRATO AND STI.ID_ARTICULO = SCA.ID_ARTICULO ");
		if (idTipoAsignacion==5) {
			selectQueryUtilArticulo.where("STI.ID_TIPO_ASIGNACION_ART in (1,3)");

		}else {
			selectQueryUtilArticulo.where("STI.ID_TIPO_ASIGNACION_ART = "+idTipoAsignacion);
		}
		selectQueryUtilArticulo.and("SA.ID_CATEGORIA_ARTICULO = 1")
		.and("STI.ID_VELATORIO ="+idVelatorio);
		
		
		
		selectQueryUtilCosto.select("CAST(IFNULL(SPS.TIP_PARAMETRO,0) AS DECIMAL(10,2))")
				.from("SVC_PARAMETRO_SISTEMA SPS")
				.where("SPS.DES_PARAMETRO='COSTO ATAUD'");
		
		if (idTipoAsignacion==5) {
			selectQueryUtilArticulo.and("SCA.MON_PRECIO <= (".concat(selectQueryUtilCosto.build()).concat(")"));
		}
		String query= selectQueryUtilArticulo.build();
		log.info(query);
		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}

	public DatosRequest obtenerProveedorAtaud(Integer idAtaud) {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SP.ID_PROVEEDOR as idProveedor","SP.NOM_PROVEEDOR as nombreProveedor")
		.from("SVT_PROVEEDOR SP")
		.innerJoin("SVT_CONTRATO SC", "SP .ID_PROVEEDOR = SC.ID_PROVEEDOR")
		.innerJoin("SVT_CONTRATO_ARTICULOS SCA", "SCA.ID_CONTRATO =SC.ID_CONTRATO")
		.where("SCA.ID_ARTICULO  = :idAtaud")
		.setParameter("idAtaud", idAtaud)
		.and("SP.IND_ACTIVO=1")
		.groupBy("nombreProveedor");
		
		String query = selectQueryUtil.build();
		log.info(query);
		String encoded=DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
	
	public DatosRequest obtenerListadoAtaudesInventario(Integer idAsignacion, Integer idArticulo, Integer idProveedor, Integer idVelatorio) {
		DatosRequest request = new DatosRequest();
		Map<String, Object>paramtero= new HashMap<>();
		SelectQueryUtil selectQueryUtilArticulo= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilInventarioTemp= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilInventario= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilCosto= new SelectQueryUtil();
		
		selectQueryUtilInventarioTemp.select("IFNULL(STP.ID_INVE_ARTICULO,0)")
		.from("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP STP")
		.where("STP.IND_ACTIVO=1")
		.and("DATE_FORMAT(STP.FEC_ALTA,'YY-MM-DD')=DATE_FORMAT(CURRENT_DATE(),'YY-MM-DD')")
		.and("TIMESTAMPDIFF(MINUTE,DATE_ADD(STP.FEC_ALTA, INTERVAL 4 HOUR),CURRENT_TIMESTAMP()) <= 0");
		
		selectQueryUtilInventario.select("IFNULL(SDCP.ID_INVE_ARTICULO,0)")
		.from("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP")
		.where("SDCP.IND_ACTIVO=1");
		
		selectQueryUtilCosto.select("CAST(IFNULL(SPS.TIP_PARAMETRO,0) AS DECIMAL(10,2))")
		.from("SVC_PARAMETRO_SISTEMA SPS")
		.where("SPS.DES_PARAMETRO='COSTO ATAUD'");
		
		selectQueryUtilArticulo.select("STI.ID_INVE_ARTICULO AS idInventario","STI.FOLIO_ARTICULO AS idFolioArticulo")
		.from("SVT_INVENTARIO_ARTICULO STI")
		.innerJoin("SVT_ORDEN_ENTRADA SOE2", "SOE2.ID_ODE = STI.ID_ODE")
		.innerJoin("SVT_CONTRATO SC", "SC.ID_CONTRATO = SOE2.ID_CONTRATO")
		.innerJoin("SVT_CONTRATO_ARTICULOS SCA", "SCA.ID_CONTRATO = SC.ID_CONTRATO")
		.innerJoin("SVT_PROVEEDOR SP", "SP.ID_PROVEEDOR = SC.ID_PROVEEDOR")
		.where("SC.ID_PROVEEDOR = "+idProveedor)
		.and("STI.IND_ESTATUS NOT IN (1,2,3) AND SCA.ID_ARTICULO = "+idArticulo);
		
		if (idAsignacion==5) {
			selectQueryUtilArticulo.and("STI.ID_TIPO_ASIGNACION_ART in(1,3)");
		}else {
			selectQueryUtilArticulo.and("STI.ID_TIPO_ASIGNACION_ART = "+idAsignacion);
		}
		selectQueryUtilArticulo.and("STI.ID_ARTICULO = "+idArticulo)
		.and("STI.ID_VELATORIO = "+idVelatorio)
		.and("STI.ID_INVE_ARTICULO NOT IN ("+selectQueryUtilInventarioTemp.build()+")")
		.and("STI.ID_INVE_ARTICULO NOT IN("+selectQueryUtilInventario.build()+")");
		if (idAsignacion==5) {
			selectQueryUtilArticulo.and("SCA.MON_PRECIO <=("+selectQueryUtilCosto.build()+")");
		}
		
		
		String query=selectQueryUtilArticulo.build();
		
		log.info(query);
		String encoded= DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));

		paramtero.put(AppConstantes.QUERY, encoded);
		request.setDatos(paramtero);
		return request;
	}
	
}

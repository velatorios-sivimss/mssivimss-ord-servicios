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

public class Empaque {

	private static Empaque instancia;
	
	
	private static final Logger log = LoggerFactory.getLogger(Empaque.class);

	private Empaque() {}
	
	public static Empaque obtenerInstancia() {
		if (instancia == null) {
			instancia = new Empaque();
		}
		
		return instancia;
	}
	
	public DatosRequest obtenerEmpaque(Integer idVelatorio) {
		DatosRequest request = new DatosRequest();
		Map<String, Object>paramtero= new HashMap<>();
		SelectQueryUtil selectQueryUtilArticulo= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilInventarioTemp= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilInventario= new SelectQueryUtil();
		
		selectQueryUtilInventarioTemp.select("STP.ID_INVE_ARTICULO")
		.from("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP STP")
		.where("STP.IND_ACTIVO=1")
		.and("DATE_FORMAT(STP.FEC_ALTA,'YY-MM-DD')=DATE_FORMAT(CURRENT_DATE(),'YY-MM-DD')")
		.and("TIMESTAMPDIFF(MINUTE,DATE_ADD(STP.FEC_ALTA, INTERVAL 4 HOUR),CURRENT_TIMESTAMP()) <= 0");
		
		selectQueryUtilInventario.select("SDCP.ID_INVE_ARTICULO")
		.from("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP")
		.where("SDCP.IND_ACTIVO=1");
		
		selectQueryUtilArticulo.select("STI.ID_INVE_ARTICULO AS idArticulo","CONCAT(STI.FOLIO_ARTICULO,'-',STA.DES_MODELO_ARTICULO,' $',SCA.MON_PRECIO) AS nombreArticulo","SCA.MON_PRECIO AS precio")
		.from("SVT_INVENTARIO_ARTICULO STI")
		.innerJoin("SVT_ARTICULO STA", "STI.ID_ARTICULO = STA.ID_ARTICULO")
		.innerJoin("SVT_ORDEN_ENTRADA SOE2", "SOE2.ID_ODE = STI.ID_ODE")
		.innerJoin("SVT_CONTRATO SC", "SC.ID_CONTRATO = SOE2.ID_CONTRATO")
		.innerJoin("SVT_PROVEEDOR SP", "SP .ID_PROVEEDOR = SC.ID_PROVEEDOR")
		.innerJoin("SVT_CONTRATO_ARTICULOS SCA", "SCA.ID_CONTRATO = SC.ID_CONTRATO AND STA.ID_ARTICULO = SCA.ID_ARTICULO")
		.where("STA.ID_CATEGORIA_ARTICULO = 4")
		.and("STI.ID_VELATORIO = "+idVelatorio)
		.and("STI.IND_ESTATUS NOT IN (2,3)")
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
	
}

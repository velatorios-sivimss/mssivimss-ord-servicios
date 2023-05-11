package com.imss.sivimss.ordservicios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Servicio {

	private static Servicio instancia;
	
	private Servicio() {}
	
	public static Servicio getInstancia() {
		if (instancia==null) {
			instancia= new Servicio();
		} 
		
		return instancia;
	}
	
	public DatosRequest obtenerServiciosVigentes() {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SS.ID_SERVICIO AS idServicio","SS.NOM_SERVICIO AS nombreServicio")
		.from("SVT_SERVICIO SS")
		.innerJoin("SVT_CONTRATO_SERVICIO SCS", "SS.ID_SERVICIO = SCS.ID_SERVICIO")
		.innerJoin("SVT_CONTRATO SC", "SCS.ID_CONTRATO =SC.ID_CONTRATO")
		.where("SS.IND_ACTIVO =1")
		.and("DATE_FORMAT(SC.FEC_FIN_VIG,\"%Y-%M-%D\") <= DATE_FORMAT(CURRENT_DATE(),\"%Y-%M-%D\")")
		.orderBy("SS.NOM_SERVICIO ASC");
		
		String query=selectQueryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());

		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
	
	public DatosRequest obtenerProveedorServicio(Integer idServicio){
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SP.ID_PROVEEDOR AS idProveedor","SP.NOM_PROVEEDOR AS nombreProveedor")
		.from("SVT_PROVEEDOR SP")
		.innerJoin("SVT_CONTRATO SC", "SP.ID_PROVEEDOR = SC.ID_PROVEEDOR")
		.innerJoin("SVT_CONTRATO_SERVICIO SCS", "SC.ID_CONTRATO = SCS.ID_CONTRATO")
		.innerJoin("SVT_SERVICIO SS", "SCS.ID_SERVICIO = SS.ID_SERVICIO")
		.where("SP.IND_ACTIVO =1")
		.and("SS.ID_SERVICIO = "+idServicio)
		.orderBy("SP.NOM_PROVEEDOR ASC");
		
		String query=selectQueryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametros.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
}

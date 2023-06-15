package com.imss.sivimss.ordservicios.beans;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.imss.sivimss.ordservicios.model.request.ContratoPfRequest;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContratoPF {
	
	private static ContratoPF instancia;
	
	private ContratoPF() {}
	
	public static ContratoPF getInstancia() {
		if (instancia==null) {
			instancia= new ContratoPF();
		}
		
		return instancia;
	}

	public DatosRequest consultarSiniestros(ContratoPfRequest contratoPfRequest) throws UnsupportedEncodingException{
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("COUNT(svo.ID_ORDEN_SERVICIO) as siniestros")
		.from("SVT_CONVENIO_PF scp")
		.innerJoin("SVC_FINADO svi", "scp.ID_CONVENIO_PF = svi.ID_CONTRATO_PREVISION")
		.innerJoin("SVC_ORDEN_SERVICIO svo", "svi.ID_ORDEN_SERVICIO =svo.ID_ORDEN_SERVICIO and svo.ID_ESTATUS_ORDEN_SERVICIO =2")
		.where("scp.DES_FOLIO = :folio")
		.and("scp.ID_ESTATUS_CONVENIO = 2")
		.and("scp .ID_TIPO_PREVISION =1")
		.setParameter("folio", contratoPfRequest.getFolio());
		String query= queryUtil.build();
		String encoded=queryUtil.encrypt(query, "utf-8");
		parametro.put(AppConstantes.QUERY, encoded);
		datosRequest.setDatos(parametro);
		return datosRequest;
	}
}

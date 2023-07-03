package com.imss.sivimss.ordservicios.beans;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.model.request.ContratoServicioInmediatoRequest;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Reporte {
	
	public DatosRequest consultarContratoServicioInmediato(DatosRequest request, ContratoServicioInmediatoRequest contratoServicioInmediatoRequest) {
		log.info(" INICIO - consultarContratoServicioInmediato");
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SOE.CVE_FOLIO as folioOds","CONCAT_WS(' ',SP.NOM_PERSONA,SP.NOM_PRIMER_APELLIDO,SP.NOM_SEGUNDO_APELLIDO ) AS  nombreContratante",
				"CONCAT_WS(' ',NULLIF(SD.DES_CALLE,''), NULLIF(SD.NUM_EXTERIOR ,''),NULLIF(SD.NUM_INTERIOR ,''), CONCAT_WS(', ',NULLIF(SD.DES_COLONIA,' '),NULLIF(SD.DES_CP,' '),NULLIF(SD.DES_MUNICIPIO,' '),NULLIF(SD.DES_ESTADO,' ')) ) AS direccionCliente",
				"SP.CVE_RFC AS rfcCliente","SP.DES_TELEFONO AS telefonoCliente","SP.DES_CORREO  AS correoCliente","CONCAT_WS(' ',NULLIF(SDV.DES_CALLE,''), NULLIF(SDV.NUM_EXTERIOR ,''),NULLIF(SDV.NUM_INTERIOR ,''), CONCAT_WS(', ',NULLIF(SDV.DES_COLONIA,' '),NULLIF(SDV.DES_CP,' '),NULLIF(SDV.DES_MUNICIPIO,' '),NULLIF(SDV.DES_ESTADO,' ')) ) AS direccionVelatorio",
				"CONCAT_WS(' ',NULLIF(SV.DES_VELATORIO,''),NULLIF(SDL.DES_DELEGACION,'')) AS capillaVelatorio","date_format(SOE.FEC_ALTA,'%d/%m/%Y') AS fechaOds",
				"SIS.TIM_HORA_RECOGER AS horarioInicio","date_format(SISV.FEC_INSTALACION ,'%d/%m/%Y') AS fechaServicio","SISV.TIM_HORA_VELACION AS horarioFin",
				"CONCAT_WS(' ',SPA.NOM_PERSONA,SPA.NOM_PRIMER_APELLIDO,SPA.NOM_SEGUNDO_APELLIDO ) AS nombreFinado","SDL.DES_DELEGACION AS lugarExpedicion",
				"SIS.TIM_HORA_CREMACION AS horarioServicio","SS.DES_SERVICIO AS descripcionServicio","SPN.DES_PANTEON AS nombrePanteon","SCP.CAN_PRESUPUESTO AS totalOds")
		.from("SVC_ORDEN_SERVICIO SOE")
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO SCP", "SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP", "SDCP.ID_CARACTERISTICAS_PRESUPUESTO = SCP.ID_CARACTERISTICAS_PRESUPUESTO")
		.innerJoin("SVT_SERVICIO SS", "SDCP.ID_SERVICIO = SS.ID_SERVICIO")
		.innerJoin("SVC_CONTRATANTE SC", "SC.ID_CONTRATANTE = SOE.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SP.ID_PERSONA = SC.ID_PERSONA")
		.innerJoin("SVT_DOMICILIO SD", "SC.ID_DOMICILIO = SD.ID_DOMICILIO")
		.innerJoin("SVC_VELATORIO SV", "SOE.ID_VELATORIO = SV.ID_VELATORIO")
		.innerJoin("SVT_DOMICILIO SDV", "SV.ID_DOMICILIO = SDV.ID_DOMICILIO")
		.innerJoin("SVC_DELEGACION SDL", "SV.ID_DELEGACION = SDL.ID_DELEGACION")
		.innerJoin("SVC_INFORMACION_SERVICIO SIS", "SOE.ID_ORDEN_SERVICIO = SIS.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_INFORMACION_SERVICIO_VELACION SISV", "SIS.ID_INFORMACION_SERVICIO = SISV.ID_INFORMACION_SERVICIO")
		.innerJoin("SVT_PANTEON SPN", "SIS.ID_PANTEON = SPN.ID_PANTEON")
		.innerJoin("SVC_FINADO SFO", "SOE.ID_ORDEN_SERVICIO = SFO.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_PERSONA SPA", "SPA.ID_PERSONA = SFO.ID_PERSONA")
		.where("SOE.ID_ORDEN_SERVICIO = :idOrdenServicio").setParameter("idOrdenServicio", contratoServicioInmediatoRequest.getIdOrdenServicio());
		
		final String query = queryUtil.build();
		log.info(" consultarOrdenEntradaPorVelatorio: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		
		log.info(" TERMINO - consultarOrdenEntradaPorVelatorio");
		return request;
	}

}

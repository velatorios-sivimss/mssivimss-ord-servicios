package com.imss.sivimss.ordservicios.beans;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.model.request.ContratoServicioInmediatoRequest;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConsultaConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Reporte {
	
	
	String query;
	
	public DatosRequest consultarContratoServicioInmediatoTemp(DatosRequest request, ContratoServicioInmediatoRequest contratoServicioInmediatoRequest) {
		log.info(" INICIO - consultarContratoServicioInmediatoTemp");
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SOE.CVE_FOLIO as folioOds","CONCAT_WS(' ',SP.NOM_PERSONA,SP.NOM_PRIMER_APELLIDO,SP.NOM_SEGUNDO_APELLIDO ) AS  nombreContratante",
				"CONCAT_WS(' ',NULLIF(SD.DES_CALLE,''), NULLIF(SD.NUM_EXTERIOR ,''),NULLIF(SD.NUM_INTERIOR ,''), CONCAT_WS(', ',NULLIF(SD.DES_COLONIA,' '),NULLIF(SD.DES_CP,' '),NULLIF(SD.DES_MUNICIPIO,' '),NULLIF(SD.DES_ESTADO,' ')) ) AS direccionCliente",
				"SP.CVE_RFC AS rfcCliente","SP.DES_TELEFONO AS telefonoCliente","SP.DES_CORREO  AS correoCliente","CONCAT_WS(' ',NULLIF(SDV.DES_CALLE,''), NULLIF(SDV.NUM_EXTERIOR ,''),NULLIF(SDV.NUM_INTERIOR ,''), CONCAT_WS(', ',NULLIF(SDV.DES_COLONIA,' '),NULLIF(SDV.DES_CP,' '),NULLIF(SDV.DES_MUNICIPIO,' '),NULLIF(SDV.DES_ESTADO,' ')) ) AS direccionVelatorio",
				"CONCAT_WS(' ',NULLIF(SV.DES_VELATORIO,''),NULLIF(SDL.DES_DELEGACION,'')) AS capillaVelatorio","date_format(SOE.FEC_ALTA,'%d/%m/%Y') AS fechaOds",
				"SIS.TIM_HORA_RECOGER AS horarioInicio","date_format(SISV.FEC_INSTALACION ,'%d/%m/%Y') AS fechaServicio","SISV.TIM_HORA_VELACION AS horarioFin",
				"CONCAT_WS(' ',SPA.NOM_PERSONA,SPA.NOM_PRIMER_APELLIDO,SPA.NOM_SEGUNDO_APELLIDO ) AS nombreFinado","SDL.DES_DELEGACION AS lugarExpedicion",
				"SIS.TIM_HORA_CREMACION AS horarioServicio","(SELECT GROUP_CONCAT(SS.DES_SERVICIO SEPARATOR ', ') FROM SVC_ORDEN_SERVICIO SOE INNER JOIN SVC_CARACTERISTICAS_PRESUPUESTO_TEMP SCP ON SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO AND SCP.IND_ACTIVO = 1 INNER JOIN SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP SDCP ON SDCP.ID_CARACTERISTICAS_PRESUPUESTO = SCP.ID_CARACTERISTICAS_PRESUPUESTO AND SDCP.IND_ACTIVO = 1 "
						.concat("LEFT JOIN SVT_SERVICIO SS ON SDCP.ID_SERVICIO = SS.ID_SERVICIO WHERE SOE.ID_ORDEN_SERVICIO = "+ contratoServicioInmediatoRequest.getIdOrdenServicio()+")AS descripcionServicio"),
				"SPN.DES_PANTEON AS nombrePanteon","SCP.CAN_PRESUPUESTO AS totalOds","(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 12 AND SPS.ID_FUNCIONALIDAD = 20) AS nombreFibeso","(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 13 AND SPS.ID_FUNCIONALIDAD = 20) AS correoFibeso",
				"(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 14 AND SPS.ID_FUNCIONALIDAD = 20) AS telefonoQuejas")
		.from("SVC_ORDEN_SERVICIO SOE")
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO_TEMP SCP", "SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO").and("SCP.IND_ACTIVO = 1")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, "SC.ID_CONTRATANTE = SOE.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SP.ID_PERSONA = SC.ID_PERSONA")
		.innerJoin("SVT_DOMICILIO SD", "SC.ID_DOMICILIO = SD.ID_DOMICILIO")
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, "SOE.ID_VELATORIO = SV.ID_VELATORIO")
		.innerJoin("SVT_DOMICILIO SDV", "SV.ID_DOMICILIO = SDV.ID_DOMICILIO")
		.innerJoin("SVC_DELEGACION SDL", "SV.ID_DELEGACION = SDL.ID_DELEGACION")
		.leftJoin("SVC_INFORMACION_SERVICIO SIS", "SOE.ID_ORDEN_SERVICIO = SIS.ID_ORDEN_SERVICIO").and("SIS.IND_ACTIVO = 1")
		.leftJoin ("SVC_INFORMACION_SERVICIO_VELACION SISV", "SIS.ID_INFORMACION_SERVICIO = SISV.ID_INFORMACION_SERVICIO")
		.leftJoin("SVT_PANTEON SPN", "SIS.ID_PANTEON = SPN.ID_PANTEON")
		.innerJoin("SVC_FINADO SFO", "SOE.ID_ORDEN_SERVICIO = SFO.ID_ORDEN_SERVICIO")
		.leftJoin("SVC_PERSONA SPA", "SPA.ID_PERSONA = SFO.ID_PERSONA")
		.where("SOE.ID_ORDEN_SERVICIO = :idOrdenServicio").setParameter("idOrdenServicio", contratoServicioInmediatoRequest.getIdOrdenServicio());
		
		final String query = queryUtil.build();
		log.info(" consultarContratoServicioInmediatoTemp: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		
		log.info(" TERMINO - consultarContratoServicioInmediatoTemp");
		return request;
	}
	
	public DatosRequest consultarContratoServicioInmediato(DatosRequest request, ContratoServicioInmediatoRequest contratoServicioInmediatoRequest) {
		log.info(" INICIO - consultarContratoServicioInmediato");
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SOE.CVE_FOLIO as folioOds","CONCAT_WS(' ',SP.NOM_PERSONA,SP.NOM_PRIMER_APELLIDO,SP.NOM_SEGUNDO_APELLIDO ) AS  nombreContratante",
				"CONCAT_WS(' ',NULLIF(SD.DES_CALLE,''), NULLIF(SD.NUM_EXTERIOR ,''),NULLIF(SD.NUM_INTERIOR ,''), CONCAT_WS(', ',NULLIF(SD.DES_COLONIA,' '),NULLIF(SD.DES_CP,' '),NULLIF(SD.DES_MUNICIPIO,' '),NULLIF(SD.DES_ESTADO,' ')) ) AS direccionCliente",
				"SP.CVE_RFC AS rfcCliente","SP.DES_TELEFONO AS telefonoCliente","SP.DES_CORREO  AS correoCliente","CONCAT_WS(' ',NULLIF(SDV.DES_CALLE,''), NULLIF(SDV.NUM_EXTERIOR ,''),NULLIF(SDV.NUM_INTERIOR ,''), CONCAT_WS(', ',NULLIF(SDV.DES_COLONIA,' '),NULLIF(SDV.DES_CP,' '),NULLIF(SDV.DES_MUNICIPIO,' '),NULLIF(SDV.DES_ESTADO,' ')) ) AS direccionVelatorio",
				"CONCAT_WS(' ',NULLIF(SV.DES_VELATORIO,''),NULLIF(SDL.DES_DELEGACION,'')) AS capillaVelatorio","date_format(SOE.FEC_ALTA,'%d/%m/%Y') AS fechaOds",
				"SIS.TIM_HORA_RECOGER AS horarioInicio","date_format(SISV.FEC_INSTALACION ,'%d/%m/%Y') AS fechaServicio","SISV.TIM_HORA_VELACION AS horarioFin",
				"CONCAT_WS(' ',SPA.NOM_PERSONA,SPA.NOM_PRIMER_APELLIDO,SPA.NOM_SEGUNDO_APELLIDO ) AS nombreFinado","SDL.DES_DELEGACION AS lugarExpedicion",
				"SIS.TIM_HORA_CREMACION AS horarioServicio","(SELECT GROUP_CONCAT(SS.DES_SERVICIO SEPARATOR ', ') FROM SVC_ORDEN_SERVICIO SOE INNER JOIN SVC_CARACTERISTICAS_PRESUPUESTO SCP ON SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO AND SCP.IND_ACTIVO = 1 INNER JOIN SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP ON SDCP.ID_CARACTERISTICAS_PRESUPUESTO = SCP.ID_CARACTERISTICAS_PRESUPUESTO AND SDCP.IND_ACTIVO = 1 "
						.concat("LEFT JOIN SVT_SERVICIO SS ON SDCP.ID_SERVICIO = SS.ID_SERVICIO WHERE SOE.ID_ORDEN_SERVICIO = "+ contratoServicioInmediatoRequest.getIdOrdenServicio()+")AS descripcionServicio"),
				"SPN.DES_PANTEON AS nombrePanteon","SCP.CAN_PRESUPUESTO AS totalOds","(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 12 AND SPS.ID_FUNCIONALIDAD = 20) AS nombreFibeso","(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 13 AND SPS.ID_FUNCIONALIDAD = 20) AS correoFibeso",
				"(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 14 AND SPS.ID_FUNCIONALIDAD = 20) AS telefonoQuejas")
		.from("SVC_ORDEN_SERVICIO SOE")
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO SCP", "SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO").and("SCP.IND_ACTIVO = 1")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, "SC.ID_CONTRATANTE = SOE.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SP.ID_PERSONA = SC.ID_PERSONA")
		.innerJoin("SVT_DOMICILIO SD", "SC.ID_DOMICILIO = SD.ID_DOMICILIO")
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, "SOE.ID_VELATORIO = SV.ID_VELATORIO")
		.innerJoin("SVT_DOMICILIO SDV", "SV.ID_DOMICILIO = SDV.ID_DOMICILIO")
		.innerJoin("SVC_DELEGACION SDL", "SV.ID_DELEGACION = SDL.ID_DELEGACION")
		.leftJoin("SVC_INFORMACION_SERVICIO SIS", "SOE.ID_ORDEN_SERVICIO = SIS.ID_ORDEN_SERVICIO").and("SIS.IND_ACTIVO = 1")
		.leftJoin ("SVC_INFORMACION_SERVICIO_VELACION SISV", "SIS.ID_INFORMACION_SERVICIO = SISV.ID_INFORMACION_SERVICIO")
		.leftJoin("SVT_PANTEON SPN", "SIS.ID_PANTEON = SPN.ID_PANTEON")
		.innerJoin("SVC_FINADO SFO", "SOE.ID_ORDEN_SERVICIO = SFO.ID_ORDEN_SERVICIO")
		.leftJoin("SVC_PERSONA SPA", "SPA.ID_PERSONA = SFO.ID_PERSONA")
		.where("SOE.ID_ORDEN_SERVICIO = :idOrdenServicio").setParameter("idOrdenServicio", contratoServicioInmediatoRequest.getIdOrdenServicio());
		
		final String query = queryUtil.build();
		log.info(" consultarContratoServicioInmediato: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		
		log.info(" TERMINO - consultarContratoServicioInmediato");
		return request;
	}
	
	// consultar reporte salida donacion
	public String consultarReporteSalidaDonacion(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("SD.DES_DELEGACION AS ooadNom","SV.ID_VELATORIO AS velatorioId","SV.DES_VELATORIO AS velatorioNom","SSD.NUM_TOTAL_ATAUDES numAtaudes",
		"CONCAT_WS('-',STA.FOLIO_ARTICULO,STVA.DES_MODELO_ARTICULO ) AS  modeloAtaud","TM.DES_TIPO_MATERIAL AS tipoAtaud","STA.FOLIO_ARTICULO AS numInventarios",
		"CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS  nomSolicitantes","CONCAT_WS(' ',SPE2.NOM_PERSONA,SPE2.NOM_PRIMER_APELLIDO,SPE2.NOM_SEGUNDO_APELLIDO ) AS nomFinados",
		"date_format(SSD.FEC_SOLICITUD ,'%d/%m/%Y') AS fecSolicitud","CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS  nomSolicitante","CONCAT_WS(' ',SU.NOM_USUARIO,SU.NOM_APELLIDO_PATERNO,SU.NOM_APELLIDO_MATERNO) AS nomAdministrador",
		"SU.CVE_MATRICULA AS claveAdministrador","CONCAT_WS(',',SV.DES_VELATORIO,SD.DES_DELEGACION) AS lugar","DAY(NOW()) as dia", "ELT(MONTH(NOW()), \"ENERO\", \"FEBRERO\", \"MARZO\", \"ABRIL\", \"MAYO\", \"JUNIO\", \"JULIO\", \"AGOSTO\", \"SEPTIEMBRE\", \"OCTUBRE\", \"NOVIEMBRE\", \"DICIEMBRE\") as mes",
		"YEAR(NOW()) as anio")
		.from("SVC_SALIDA_DONACION SSD")
		.innerJoin("SVC_ORDEN_SERVICIO SOS", "SOS.ID_ORDEN_SERVICIO = SSD.ID_ORDEN_SERVICIO ")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, "SC.ID_CONTRATANTE = SOS.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SPE", "SPE.ID_PERSONA = SC.ID_PERSONA")
		.innerJoin("SVC_FINADO STF", "STF.ID_ORDEN_SERVICIO = SOS.ID_ORDEN_SERVICIO")
		.leftJoin("SVC_PERSONA SPE2", "SPE2.ID_PERSONA =STF.ID_PERSONA")
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO SCPT", "SOS.ID_ORDEN_SERVICIO = SCPT.ID_ORDEN_SERVICIO").and("SCPT.IND_ACTIVO = 1")
		.innerJoin("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCPT", "SDCPT.ID_CARACTERISTICAS_PRESUPUESTO = SCPT.ID_CARACTERISTICAS_PRESUPUESTO").and("SDCPT.IND_ACTIVO = 1")
		.innerJoin("SVC_SALIDA_DONACION_ATAUDES STDA", "STDA.ID_INVE_ARTICULO = SDCPT.ID_INVE_ARTICULO")
		.innerJoin("SVT_INVENTARIO_ARTICULO STA", "STA.ID_INVE_ARTICULO = STDA.ID_INVE_ARTICULO")
		.innerJoin("SVT_ARTICULO STVA", "STVA.ID_ARTICULO  = SDCPT.ID_ARTICULO").and("STVA.ID_CATEGORIA_ARTICULO = 1")
		.innerJoin("SVC_TIPO_MATERIAL TM", "STVA.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL")
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, "SOS.ID_VELATORIO = SV.ID_VELATORIO")
		.innerJoin("SVC_DELEGACION SD", "SD.ID_DELEGACION = SV.ID_DELEGACION")
		.leftJoin("SVT_USUARIOS SU", "SU.ID_USUARIO = SV.ID_USUARIO_ADMIN")
		.where("SOS.ID_ORDEN_SERVICIO = :idOrdenServicio").setParameter("idOrdenServicio", idOrdenServicio).and("SSD.IND_ACTIVO = 1");
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	// consultar reporte salida donacion
	public String consultarReporteSalidaDonacionTemp(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("SD.DES_DELEGACION AS ooadNom","SV.ID_VELATORIO AS velatorioId","SV.DES_VELATORIO AS velatorioNom","SSD.NUM_TOTAL_ATAUDES numAtaudes",
		"CONCAT_WS('-',STA.FOLIO_ARTICULO,STVA.DES_MODELO_ARTICULO ) AS  modeloAtaud","TM.DES_TIPO_MATERIAL AS tipoAtaud","STA.FOLIO_ARTICULO AS numInventarios",
		"CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS  nomSolicitantes","CONCAT_WS(' ',SPE2.NOM_PERSONA,SPE2.NOM_PRIMER_APELLIDO,SPE2.NOM_SEGUNDO_APELLIDO ) AS nomFinados",
		"date_format(SSD.FEC_SOLICITUD ,'%d/%m/%Y') AS fecSolicitud","CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS  nomSolicitante","CONCAT_WS(' ',SU.NOM_USUARIO,SU.NOM_APELLIDO_PATERNO,SU.NOM_APELLIDO_MATERNO) AS nomAdministrador",
		"SU.CVE_MATRICULA AS claveAdministrador","CONCAT_WS(',',SV.DES_VELATORIO,SD.DES_DELEGACION) AS lugar","DAY(NOW()) as dia", "ELT(MONTH(NOW()), \"ENERO\", \"FEBRERO\", \"MARZO\", \"ABRIL\", \"MAYO\", \"JUNIO\", \"JULIO\", \"AGOSTO\", \"SEPTIEMBRE\", \"OCTUBRE\", \"NOVIEMBRE\", \"DICIEMBRE\") as mes",
		"YEAR(NOW()) as anio")
		.from("SVC_SALIDA_DONACION_TEMP SSD")
		.innerJoin("SVC_ORDEN_SERVICIO SOS", "SOS.ID_ORDEN_SERVICIO = SSD.ID_ORDEN_SERVICIO ")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, "SC.ID_CONTRATANTE = SOS.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SPE", "SPE.ID_PERSONA = SC.ID_PERSONA")
		.innerJoin("SVC_FINADO STF", "STF.ID_ORDEN_SERVICIO = SOS.ID_ORDEN_SERVICIO")
		.leftJoin("SVC_PERSONA SPE2", "SPE2.ID_PERSONA =STF.ID_PERSONA")
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO_TEMP SCPT", "SOS.ID_ORDEN_SERVICIO = SCPT.ID_ORDEN_SERVICIO").and("SCPT.IND_ACTIVO = 1")
		.innerJoin("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP SDCPT", "SDCPT.ID_CARACTERISTICAS_PRESUPUESTO = SCPT.ID_CARACTERISTICAS_PRESUPUESTO").and("SDCPT.IND_ACTIVO = 1")
		.innerJoin("SVC_SALIDA_DONACION_ATAUDES_TEMP STDA", "STDA.ID_INVE_ARTICULO = SDCPT.ID_INVE_ARTICULO")
		.innerJoin("SVT_INVENTARIO_ARTICULO STA", "STA.ID_INVE_ARTICULO = STDA.ID_INVE_ARTICULO")
		.innerJoin("SVT_ARTICULO STVA", "STVA.ID_ARTICULO  = SDCPT.ID_ARTICULO").and("STVA.ID_CATEGORIA_ARTICULO = 1")
		.innerJoin("SVC_TIPO_MATERIAL TM", "STVA.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL")
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, "SOS.ID_VELATORIO = SV.ID_VELATORIO")
		.innerJoin("SVC_DELEGACION SD", "SD.ID_DELEGACION = SV.ID_DELEGACION")
		.leftJoin("SVT_USUARIOS SU", "SU.ID_USUARIO = SV.ID_USUARIO_ADMIN")
		.where("SOS.ID_ORDEN_SERVICIO = :idOrdenServicio").setParameter("idOrdenServicio", idOrdenServicio).and("SSD.IND_ACTIVO = 1");
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}

}

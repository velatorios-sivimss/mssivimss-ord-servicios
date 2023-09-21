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
				"SIS.TIM_HORA_CREMACION AS horarioServicio","(SELECT GROUP_CONCAT(SS.DES_SERVICIO SEPARATOR ', ') FROM SVC_ORDEN_SERVICIO SOE INNER JOIN SVC_CARAC_PRESUP_TEMP SCP ON SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO AND SCP.IND_ACTIVO = 1 INNER JOIN SVC_DETALLE_CARAC_PRESUP_TEMP SDCP ON SDCP.ID_CARAC_PRESUPUESTO = SCP.ID_CARAC_PRESUPUESTO AND SDCP.IND_ACTIVO = 1 "
						.concat("LEFT JOIN SVT_SERVICIO SS ON SDCP.ID_SERVICIO = SS.ID_SERVICIO WHERE SOE.ID_ORDEN_SERVICIO = "+ contratoServicioInmediatoRequest.getIdOrdenServicio()+")AS descripcionServicio"),
				"SPN.DES_PANTEON AS nombrePanteon","SCP.CAN_PRESUPUESTO AS totalOds","(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 12 AND SPS.ID_FUNCIONALIDAD = 20) AS nombreFibeso","(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 13 AND SPS.ID_FUNCIONALIDAD = 20) AS correoFibeso",
				"(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 14 AND SPS.ID_FUNCIONALIDAD = 20) AS telefonoQuejas")
		.from("SVC_ORDEN_SERVICIO SOE")
		.innerJoin("SVC_CARAC_PRESUP_TEMP SCP", "SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO").and("SCP.IND_ACTIVO = 1")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, "SC.ID_CONTRATANTE = SOE.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SP.ID_PERSONA = SC.ID_PERSONA")
		.innerJoin("SVT_DOMICILIO SD", "SC.ID_DOMICILIO = SD.ID_DOMICILIO")
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, "SOE.ID_VELATORIO = SV.ID_VELATORIO")
		.innerJoin("SVT_DOMICILIO SDV", "SV.ID_DOMICILIO = SDV.ID_DOMICILIO")
		.innerJoin("SVC_DELEGACION SDL", "SV.ID_DELEGACION = SDL.ID_DELEGACION")
		.leftJoin("SVC_INFORMACION_SERVICIO SIS", "SOE.ID_ORDEN_SERVICIO = SIS.ID_ORDEN_SERVICIO").and("SIS.IND_ACTIVO = 1")
		.leftJoin ("SVC_INF_SERVICIO_VELACION SISV", "SIS.ID_INFORMACION_SERVICIO = SISV.ID_INFORMACION_SERVICIO")
		.leftJoin("SVT_PANTEON SPN", "SIS.ID_PANTEON = SPN.ID_PANTEON")
		.innerJoin("SVC_FINADO SFO", "SOE.ID_ORDEN_SERVICIO = SFO.ID_ORDEN_SERVICIO")
		.leftJoin("SVC_PERSONA SPA", "SPA.ID_PERSONA = SFO.ID_PERSONA")
		.where("SOE.ID_ORDEN_SERVICIO = :idOrdenServicio").setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, contratoServicioInmediatoRequest.getIdOrdenServicio());
		
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
				"SIS.TIM_HORA_CREMACION AS horarioServicio","(SELECT GROUP_CONCAT(SS.DES_SERVICIO SEPARATOR ', ') FROM SVC_ORDEN_SERVICIO SOE INNER JOIN SVC_CARAC_PRESUPUESTO SCP ON SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO AND SCP.IND_ACTIVO = 1 INNER JOIN SVC_DETALLE_CARAC_PRESUP SDCP ON SDCP.ID_CARAC_PRESUPUESTO = SCP.ID_CARAC_PRESUPUESTO AND SDCP.IND_ACTIVO = 1 "
						.concat("LEFT JOIN SVT_SERVICIO SS ON SDCP.ID_SERVICIO = SS.ID_SERVICIO WHERE SOE.ID_ORDEN_SERVICIO = "+ contratoServicioInmediatoRequest.getIdOrdenServicio()+")AS descripcionServicio"),
				"SPN.DES_PANTEON AS nombrePanteon","SCP.CAN_PRESUPUESTO AS totalOds","(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 12 AND SPS.ID_FUNCIONALIDAD = 20) AS nombreFibeso","(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 13 AND SPS.ID_FUNCIONALIDAD = 20) AS correoFibeso",
				"(SELECT SPS.TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA SPS WHERE SPS.ID_PARAMETRO = 14 AND SPS.ID_FUNCIONALIDAD = 20) AS telefonoQuejas")
		.from("SVC_ORDEN_SERVICIO SOE")
		.innerJoin("SVC_CARAC_PRESUPUESTO SCP", "SCP.ID_ORDEN_SERVICIO = SOE.ID_ORDEN_SERVICIO").and("SCP.IND_ACTIVO = 1")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, "SC.ID_CONTRATANTE = SOE.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SP.ID_PERSONA = SC.ID_PERSONA")
		.innerJoin("SVT_DOMICILIO SD", "SC.ID_DOMICILIO = SD.ID_DOMICILIO")
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, "SOE.ID_VELATORIO = SV.ID_VELATORIO")
		.innerJoin("SVT_DOMICILIO SDV", "SV.ID_DOMICILIO = SDV.ID_DOMICILIO")
		.innerJoin("SVC_DELEGACION SDL", "SV.ID_DELEGACION = SDL.ID_DELEGACION")
		.leftJoin("SVC_INFORMACION_SERVICIO SIS", "SOE.ID_ORDEN_SERVICIO = SIS.ID_ORDEN_SERVICIO").and("SIS.IND_ACTIVO = 1")
		.leftJoin ("SVC_INF_SERVICIO_VELACION SISV", "SIS.ID_INFORMACION_SERVICIO = SISV.ID_INFORMACION_SERVICIO")
		.leftJoin("SVT_PANTEON SPN", "SIS.ID_PANTEON = SPN.ID_PANTEON")
		.innerJoin("SVC_FINADO SFO", "SOE.ID_ORDEN_SERVICIO = SFO.ID_ORDEN_SERVICIO")
		.leftJoin("SVC_PERSONA SPA", "SPA.ID_PERSONA = SFO.ID_PERSONA")
		.where("SOE.ID_ORDEN_SERVICIO = :idOrdenServicio").setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, contratoServicioInmediatoRequest.getIdOrdenServicio());
		
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
		selectQueryUtil.select(ConsultaConstantes.SD_DES_DELEGACION_AS_OOAD_NOM,ConsultaConstantes.SV_ID_VELATORIO_AS_VELATORIO_ID,"SV.DES_VELATORIO AS velatorioNom","SSD.NUM_TOTAL_ATAUDES numAtaudes",
		"CONCAT_WS('-',STA. CVE_FOLIO_ARTICULO,STVA.DES_MODELO_ARTICULO ) AS  modeloAtaud",ConsultaConstantes.TM_DES_TIPO_MATERIAL_AS_TIPO_ATAUD,ConsultaConstantes.STA_CVE_FOLIO_ARTICULO_AS_NUM_INVENTARIOS,
		"CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS  nomSolicitantes","CONCAT_WS(' ',SPE2.NOM_PERSONA,SPE2.NOM_PRIMER_APELLIDO,SPE2.NOM_SEGUNDO_APELLIDO ) AS nomFinados",
		"date_format(SSD.FEC_SOLICITUD ,'%d/%m/%Y') AS fecSolicitud","CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS  nomSolicitante", ConsultaConstantes.CONCAT_WS_SU_NOM_USUARIO_SU_NOM_APELLIDO_PATERNO_SU_NOM_APELLIDO_MATERNO_AS_NOM_ADMINISTRADOR,
		ConsultaConstantes.SU_CVE_MATRICULA_AS_CLAVE_ADMINISTRADOR, ConsultaConstantes.CONCAT_WS_SV_DES_VELATORIO_SD_DES_DELEGACION_AS_LUGAR, ConsultaConstantes.DAY_NOW_AS_DIA, ConsultaConstantes.ELT_MONTH_NOW_ENERO_FEBRERO_MARZO_ABRIL_MAYO_JUNIO_JULIO_AGOSTO_SEPTIEMBRE_OCTUBRE_NOVIEMBRE_DICIEMBRE_AS_MES,
		ConsultaConstantes.YEAR_NOW_AS_ANIO)
		.from("SVC_SALIDA_DONACION SSD")
		.innerJoin(ConsultaConstantes.SVC_ORDEN_SERVICIO_SOS, "SOS.ID_ORDEN_SERVICIO = SSD.ID_ORDEN_SERVICIO ")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, ConsultaConstantes.SC_ID_CONTRATANTE_SOS_ID_CONTRATANTE)
		.innerJoin(ConsultaConstantes.SVC_PERSONA_SPE, ConsultaConstantes.SPE_ID_PERSONA_SC_ID_PERSONA)
		.innerJoin(ConsultaConstantes.SVC_FINADO_STF, ConsultaConstantes.STF_ID_ORDEN_SERVICIO_SOS_ID_ORDEN_SERVICIO)
		.leftJoin(ConsultaConstantes.SVC_PERSONA_SPE2, ConsultaConstantes.SPE2_ID_PERSONA_STF_ID_PERSONA)
		.innerJoin("SVC_CARAC_PRESUPUESTO SCPT", "SOS.ID_ORDEN_SERVICIO = SCPT.ID_ORDEN_SERVICIO").and("SCPT.IND_ACTIVO = 1")
		.innerJoin("SVC_DETALLE_CARAC_PRESUP SDCPT", "SDCPT.ID_CARAC_PRESUPUESTO = SCPT.ID_CARAC_PRESUPUESTO").and("SDCPT.IND_ACTIVO = 1")
		.innerJoin("SVC_SALIDA_DONACION_ATAUDES STDA", "STDA.ID_INVE_ARTICULO = SDCPT.ID_INVE_ARTICULO")
		.innerJoin(ConsultaConstantes.SVT_INVENTARIO_ARTICULO_STA, "STA.ID_INVE_ARTICULO = STDA.ID_INVE_ARTICULO")
		.innerJoin(ConsultaConstantes.SVT_ARTICULO_STVA, "STVA.ID_ARTICULO  = SDCPT.ID_ARTICULO").and(ConsultaConstantes.STVA_ID_CATEGORIA_ARTICULO_1)
		.innerJoin(ConsultaConstantes.SVC_TIPO_MATERIAL_TM, ConsultaConstantes.STVA_ID_TIPO_MATERIAL_TM_ID_TIPO_MATERIAL)
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, ConsultaConstantes.SOS_ID_VELATORIO_SV_ID_VELATORIO)
		.innerJoin(ConsultaConstantes.SVC_DELEGACION_SD, ConsultaConstantes.SD_ID_DELEGACION_SV_ID_DELEGACION)
		.leftJoin(ConsultaConstantes.SVT_USUARIOS_SU, ConsultaConstantes.SU_ID_USUARIO_SV_ID_USUARIO_ADMIN)
		.where(ConsultaConstantes.SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO).setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, idOrdenServicio).and("SSD.IND_ACTIVO = 1");
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	// consultar reporte salida donacion
	public String consultarReporteSalidaDonacionTemp(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select(ConsultaConstantes.SD_DES_DELEGACION_AS_OOAD_NOM, ConsultaConstantes.SV_ID_VELATORIO_AS_VELATORIO_ID,"SV.DES_VELATORIO AS velatorioNom","SSD.NUM_TOTAL_ATAUDES numAtaudes",
		"CONCAT_WS('-',STA. CVE_FOLIO_ARTICULO,STVA.DES_MODELO_ARTICULO ) AS  modeloAtaud",ConsultaConstantes.TM_DES_TIPO_MATERIAL_AS_TIPO_ATAUD, ConsultaConstantes.STA_CVE_FOLIO_ARTICULO_AS_NUM_INVENTARIOS,
		"CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS  nomSolicitantes","CONCAT_WS(' ',SPE2.NOM_PERSONA,SPE2.NOM_PRIMER_APELLIDO,SPE2.NOM_SEGUNDO_APELLIDO ) AS nomFinados",
		"date_format(SSD.FEC_SOLICITUD ,'%d/%m/%Y') AS fecSolicitud","CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS  nomSolicitante", ConsultaConstantes.CONCAT_WS_SU_NOM_USUARIO_SU_NOM_APELLIDO_PATERNO_SU_NOM_APELLIDO_MATERNO_AS_NOM_ADMINISTRADOR,
		ConsultaConstantes.SU_CVE_MATRICULA_AS_CLAVE_ADMINISTRADOR, ConsultaConstantes.CONCAT_WS_SV_DES_VELATORIO_SD_DES_DELEGACION_AS_LUGAR, ConsultaConstantes.DAY_NOW_AS_DIA, ConsultaConstantes.ELT_MONTH_NOW_ENERO_FEBRERO_MARZO_ABRIL_MAYO_JUNIO_JULIO_AGOSTO_SEPTIEMBRE_OCTUBRE_NOVIEMBRE_DICIEMBRE_AS_MES,
		ConsultaConstantes.YEAR_NOW_AS_ANIO)
		.from("SVC_SALIDA_DONACION_TEMP SSD")
		.innerJoin(ConsultaConstantes.SVC_ORDEN_SERVICIO_SOS, "SOS.ID_ORDEN_SERVICIO = SSD.ID_ORDEN_SERVICIO ")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, ConsultaConstantes.SC_ID_CONTRATANTE_SOS_ID_CONTRATANTE)
		.innerJoin(ConsultaConstantes.SVC_PERSONA_SPE, ConsultaConstantes.SPE_ID_PERSONA_SC_ID_PERSONA)
		.innerJoin(ConsultaConstantes.SVC_FINADO_STF, ConsultaConstantes.STF_ID_ORDEN_SERVICIO_SOS_ID_ORDEN_SERVICIO)
		.leftJoin(ConsultaConstantes.SVC_PERSONA_SPE2, ConsultaConstantes.SPE2_ID_PERSONA_STF_ID_PERSONA)
		.innerJoin("SVC_CARAC_PRESUP_TEMP SCPT", "SOS.ID_ORDEN_SERVICIO = SCPT.ID_ORDEN_SERVICIO").and("SCPT.IND_ACTIVO = 1")
		.innerJoin("SVC_DETALLE_CARAC_PRESUP_TEMP SDCPT", "SDCPT.ID_CARAC_PRESUPUESTO = SCPT.ID_CARAC_PRESUPUESTO").and("SDCPT.IND_ACTIVO = 1")
		.innerJoin("SVC_SALIDA_DONACION_ATAUDES_TEMP STDA", "STDA.ID_INVE_ARTICULO = SDCPT.ID_INVE_ARTICULO")
		.innerJoin(ConsultaConstantes.SVT_INVENTARIO_ARTICULO_STA, "STA.ID_INVE_ARTICULO = STDA.ID_INVE_ARTICULO")
		.innerJoin(ConsultaConstantes.SVT_ARTICULO_STVA, "STVA.ID_ARTICULO  = SDCPT.ID_ARTICULO").and(ConsultaConstantes.STVA_ID_CATEGORIA_ARTICULO_1)
		.innerJoin(ConsultaConstantes.SVC_TIPO_MATERIAL_TM, ConsultaConstantes.STVA_ID_TIPO_MATERIAL_TM_ID_TIPO_MATERIAL)
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, ConsultaConstantes.SOS_ID_VELATORIO_SV_ID_VELATORIO)
		.innerJoin(ConsultaConstantes.SVC_DELEGACION_SD,ConsultaConstantes. SD_ID_DELEGACION_SV_ID_DELEGACION)
		.leftJoin(ConsultaConstantes.SVT_USUARIOS_SU, ConsultaConstantes.SU_ID_USUARIO_SV_ID_USUARIO_ADMIN)
		.where(ConsultaConstantes.SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO).setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, idOrdenServicio).and("SSD.IND_ACTIVO = 1");
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	// consultar reporte donacion
	public String consultarReporteDonacion(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select(ConsultaConstantes.SD_DES_DELEGACION_AS_OOAD_NOM, ConsultaConstantes.SV_ID_VELATORIO_AS_VELATORIO_ID,"SOS.CVE_FOLIO AS numContrato", "CONCAT_WS('-',STA. CVE_FOLIO_ARTICULO,STVA.DES_MODELO_ARTICULO ) AS modeloAtaud",
				ConsultaConstantes.TM_DES_TIPO_MATERIAL_AS_TIPO_ATAUD, ConsultaConstantes.STA_CVE_FOLIO_ARTICULO_AS_NUM_INVENTARIOS,"CONCAT_WS(' ',SPE2.NOM_PERSONA,SPE2.NOM_PRIMER_APELLIDO,SPE2.NOM_SEGUNDO_APELLIDO ) AS nomFinado",
				"CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS nomContratante", ConsultaConstantes.CONCAT_WS_SU_NOM_USUARIO_SU_NOM_APELLIDO_PATERNO_SU_NOM_APELLIDO_MATERNO_AS_NOM_ADMINISTRADOR,
				ConsultaConstantes.SU_CVE_MATRICULA_AS_CLAVE_ADMINISTRADOR, ConsultaConstantes.CONCAT_WS_SV_DES_VELATORIO_SD_DES_DELEGACION_AS_LUGAR, ConsultaConstantes.DAY_NOW_AS_DIA, ConsultaConstantes.ELT_MONTH_NOW_ENERO_FEBRERO_MARZO_ABRIL_MAYO_JUNIO_JULIO_AGOSTO_SEPTIEMBRE_OCTUBRE_NOVIEMBRE_DICIEMBRE_AS_MES,
				ConsultaConstantes.YEAR_NOW_AS_ANIO)
		.from("SVC_DONACION SDN")
		.innerJoin("SVC_ATAUDES_DONADOS SAD", "SDN.ID_DONACION = SAD.ID_DONACION")
		.innerJoin(ConsultaConstantes.SVC_ORDEN_SERVICIO_SOS, "SOS.ID_ORDEN_SERVICIO = SDN.ID_ORDEN_SERVICIO ")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, ConsultaConstantes.SC_ID_CONTRATANTE_SOS_ID_CONTRATANTE)
		.innerJoin(ConsultaConstantes.SVC_PERSONA_SPE, ConsultaConstantes.SPE_ID_PERSONA_SC_ID_PERSONA)
		.innerJoin(ConsultaConstantes.SVC_FINADO_STF, ConsultaConstantes.STF_ID_ORDEN_SERVICIO_SOS_ID_ORDEN_SERVICIO)
		.leftJoin(ConsultaConstantes.SVC_PERSONA_SPE2, ConsultaConstantes.SPE2_ID_PERSONA_STF_ID_PERSONA)
		.innerJoin(ConsultaConstantes.SVT_INVENTARIO_ARTICULO_STA, "STA.ID_INVE_ARTICULO = SAD.ID_INVE_ARTICULO")
		.innerJoin(ConsultaConstantes.SVT_ARTICULO_STVA, "STVA.ID_ARTICULO  = STA.ID_ARTICULO").and(ConsultaConstantes.STVA_ID_CATEGORIA_ARTICULO_1)
		.innerJoin(ConsultaConstantes.SVC_TIPO_MATERIAL_TM, ConsultaConstantes.STVA_ID_TIPO_MATERIAL_TM_ID_TIPO_MATERIAL)
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, ConsultaConstantes.SOS_ID_VELATORIO_SV_ID_VELATORIO)
		.innerJoin(ConsultaConstantes.SVC_DELEGACION_SD, ConsultaConstantes.SD_ID_DELEGACION_SV_ID_DELEGACION)
		.leftJoin(ConsultaConstantes.SVT_USUARIOS_SU, ConsultaConstantes.SU_ID_USUARIO_SV_ID_USUARIO_ADMIN)
		.where(ConsultaConstantes.SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO).setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, idOrdenServicio).and("SDN.IND_ACTIVO = 1");
		
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	public String consultarReporteDonacionTemp(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select(ConsultaConstantes.SD_DES_DELEGACION_AS_OOAD_NOM, ConsultaConstantes.SV_ID_VELATORIO_AS_VELATORIO_ID,"SOS.CVE_FOLIO AS numContrato", "CONCAT_WS('-',STA. CVE_FOLIO_ARTICULO,STVA.DES_MODELO_ARTICULO ) AS modeloAtaud",
				ConsultaConstantes.TM_DES_TIPO_MATERIAL_AS_TIPO_ATAUD, ConsultaConstantes.STA_CVE_FOLIO_ARTICULO_AS_NUM_INVENTARIOS,"CONCAT_WS(' ',SPE2.NOM_PERSONA,SPE2.NOM_PRIMER_APELLIDO,SPE2.NOM_SEGUNDO_APELLIDO ) AS nomFinado",
				"CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS nomContratante",ConsultaConstantes.CONCAT_WS_SU_NOM_USUARIO_SU_NOM_APELLIDO_PATERNO_SU_NOM_APELLIDO_MATERNO_AS_NOM_ADMINISTRADOR,
				ConsultaConstantes.SU_CVE_MATRICULA_AS_CLAVE_ADMINISTRADOR,ConsultaConstantes.CONCAT_WS_SV_DES_VELATORIO_SD_DES_DELEGACION_AS_LUGAR,ConsultaConstantes.DAY_NOW_AS_DIA, ConsultaConstantes.ELT_MONTH_NOW_ENERO_FEBRERO_MARZO_ABRIL_MAYO_JUNIO_JULIO_AGOSTO_SEPTIEMBRE_OCTUBRE_NOVIEMBRE_DICIEMBRE_AS_MES,
				ConsultaConstantes.YEAR_NOW_AS_ANIO)
		.from("SVC_DONACION_ORDEN_SERVICIO_TEMP SDN")
		.innerJoin(ConsultaConstantes.SVC_ORDEN_SERVICIO_SOS, "SOS.ID_ORDEN_SERVICIO = SDN.ID_ORDEN_SERVICIO ")
		.innerJoin(ConsultaConstantes.SVC_CONTRATANTE_SC, ConsultaConstantes.SC_ID_CONTRATANTE_SOS_ID_CONTRATANTE)
		.innerJoin(ConsultaConstantes.SVC_PERSONA_SPE, ConsultaConstantes.SPE_ID_PERSONA_SC_ID_PERSONA)
		.innerJoin(ConsultaConstantes.SVC_FINADO_STF,ConsultaConstantes. STF_ID_ORDEN_SERVICIO_SOS_ID_ORDEN_SERVICIO)
		.leftJoin(ConsultaConstantes.SVC_PERSONA_SPE2, ConsultaConstantes.SPE2_ID_PERSONA_STF_ID_PERSONA)
		.innerJoin(ConsultaConstantes.SVT_INVENTARIO_ARTICULO_STA, "STA.ID_INVE_ARTICULO = SDN.ID_INVE_ARTICULO")
		.innerJoin(ConsultaConstantes.SVT_ARTICULO_STVA, "STVA.ID_ARTICULO  = STA.ID_ARTICULO").and(ConsultaConstantes.STVA_ID_CATEGORIA_ARTICULO_1)
		.innerJoin(ConsultaConstantes.SVC_TIPO_MATERIAL_TM, ConsultaConstantes.STVA_ID_TIPO_MATERIAL_TM_ID_TIPO_MATERIAL)
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, ConsultaConstantes.SOS_ID_VELATORIO_SV_ID_VELATORIO)
		.innerJoin(ConsultaConstantes.SVC_DELEGACION_SD, ConsultaConstantes.SD_ID_DELEGACION_SV_ID_DELEGACION)
		.leftJoin(ConsultaConstantes.SVT_USUARIOS_SU, ConsultaConstantes.SU_ID_USUARIO_SV_ID_USUARIO_ADMIN)
		.where(ConsultaConstantes.SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO).setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, idOrdenServicio).and("SDN.IND_ACTIVO = 1");
		
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
		

}

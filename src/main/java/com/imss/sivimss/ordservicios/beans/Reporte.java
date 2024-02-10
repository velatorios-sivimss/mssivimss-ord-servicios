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
		final String query = "SELECT IFNULL(CONCAT(SP.NOM_PERSONA,' ', SP.NOM_PRIMER_APELLIDO ,' ', SP.NOM_SEGUNDO_APELLIDO ), '') as nombreCliente, "
				+ "CASE WHEN PA.ID_PAIS  is null or PA.ID_PAIS=119 THEN 'México' ELSE PA.DES_PAIS end as nacionalidadCliente, CASE WHEN  SP.REF_CORREO ='null' "
				+ "THEN '' ELSE IFNULL( SP.REF_CORREO, '') END AS correoCliente, CASE WHEN SP.REF_TELEFONO ='null' THEN '' ELSE IFNULL( SP.REF_TELEFONO, '') END AS telefonoCliente, "
				+ "IFNULL( SP.CVE_RFC, '') as rfcCliente, IFNULL( SD.REF_CALLE, '') as calleCliente, IFNULL( SD.NUM_INTERIOR, '') as numInteriorCliente, IFNULL(SD.NUM_EXTERIOR, '') as numExteriorCliente, "
				+ "IFNULL( SD.REF_COLONIA , '') as coloniaCliente, IFNULL(SD.REF_CP, '') as cpCliente, IFNULL( SD.REF_MUNICIPIO, '') as municipioCliente, IFNULL( SD.REF_ESTADO, '') as estadoCliente, "
				+ "IFNULL(FORMAT(SCP.CAN_PRESUPUESTO, 2) , '0.00') as importeTotalOds, IFNULL(SV.DES_VELATORIO, '') as nombreVelatorio, IFNULL( SD2.REF_CALLE, '') as calleVelatorio, "
				+ "IFNULL( SD2.NUM_INTERIOR, '') as numInteriorVelatorio, IFNULL(SD2.NUM_EXTERIOR, '') as numExteriorVelatorio, IFNULL( SD2.REF_COLONIA , '') as coloniaVelatorio, "
				+ "IFNULL(SD2.REF_CP, '') as cpVelatorio, IFNULL( SD2.REF_MUNICIPIO, '') as municipioVelatorio, IFNULL( SD2.REF_ESTADO, '') as estadoVelatorio, "
				+ "IFNULL( CONCAT(SP2.NOM_PERSONA ,' ', SP2.NOM_PRIMER_APELLIDO ,' ', SP2.NOM_SEGUNDO_APELLIDO ), '') as nombreFinado, DATE_FORMAT(SOS.FEC_ALTA,'%d') as diaOds, "
				+ "ELT(MONTH(SOS.FEC_ALTA), 'ENERO', 'FEBRERO', 'MARZO', 'ABRIL', 'MAYO', 'JUNIO', 'JULIO', 'AGOSTO', 'SEPTIEMBRE', 'OCTUBRE', 'NOVIEMBRE', 'DICIEMBRE') as mesOds, DATE_FORMAT(SOS.FEC_ALTA,'%Y') as anioOds, IFNULL(PAQ.REF_PAQUETE_NOMBRE, '') as nombrePaquete, "
				+ "IFNULL(CONCAT('$',FORMAT(PAQ.MON_PRECIO,2)),'') as precioPaquete from SVC_ORDEN_SERVICIO SOS left join SVC_CONTRATANTE SC on SOS.ID_CONTRATANTE = SC.ID_CONTRATANTE "
				+ "left join SVC_PERSONA SP on SC.ID_PERSONA = SP.ID_PERSONA left join SVC_PAIS PA on SP.ID_PAIS = PA.ID_PAIS left join SVT_DOMICILIO SD on SC.ID_DOMICILIO = SD.ID_DOMICILIO "
				+ "left join SVC_VELATORIO SV on SOS.ID_VELATORIO = SV.ID_VELATORIO left join SVT_DOMICILIO SD2 on SV.ID_DOMICILIO = SD2.ID_DOMICILIO left join SVC_FINADO SF on "
				+ "SOS.ID_ORDEN_SERVICIO = SF.ID_ORDEN_SERVICIO left join SVC_UNIDAD_MEDICA UM on SF.ID_UNIDAD_PROCEDENCIA = UM.ID_UNIDAD_MEDICA left join SVC_PERSONA SP2 on "
				+ "SF.ID_PERSONA = SP2.ID_PERSONA left join SVC_PAIS PA2 on SP2.ID_PAIS = PA2.ID_PAIS left join SVC_INFORMACION_SERVICIO SIS on SOS.ID_ORDEN_SERVICIO = SIS.ID_ORDEN_SERVICIO "
				+ "and SIS.IND_ACTIVO = 1 left join SVC_CARAC_PRESUP_TEMP SCP on SOS.ID_ORDEN_SERVICIO = SCP.ID_ORDEN_SERVICIO and SCP.IND_ACTIVO = 1 left join SVT_PAQUETE PAQ on "
				+ "SCP.ID_PAQUETE = PAQ.ID_PAQUETE left join SVT_PANTEON PAN on SIS.ID_PANTEON = PAN.ID_PANTEON left join SVC_INF_SERVICIO_VELACION IV on "
				+ "SIS.ID_INFORMACION_SERVICIO = IV.ID_INFORMACION_SERVICIO left join SVT_DOMICILIO DOM on IV.ID_DOMICILIO = DOM.ID_DOMICILIO left join SVC_TIPO_PENSION ST ON "
				+ "SF.ID_TIPO_PENSION = ST.ID_TIPO_PENSION left join SVC_CAPILLA SC2 ON IV.ID_CAPILLA = SC2.ID_CAPILLA where SOS.ID_ORDEN_SERVICIO = " + contratoServicioInmediatoRequest.getIdOrdenServicio();
		log.info(" consultarContratoServicioInmediatoTemp: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		
		log.info(" TERMINO - consultarContratoServicioInmediatoTemp");
		return request;
	}
	
	public DatosRequest consultarContratoServicioInmediato(DatosRequest request, ContratoServicioInmediatoRequest contratoServicioInmediatoRequest) {
		log.info(" INICIO - consultarContratoServicioInmediato");
		String query ="SELECT IFNULL(CONCAT(SP.NOM_PERSONA, ' ', SP.NOM_PRIMER_APELLIDO , ' ', SP.NOM_SEGUNDO_APELLIDO ), '') AS nombreCliente, "
				+ "CASE WHEN PA.ID_PAIS  IS NULL OR PA.ID_PAIS=119 THEN 'México' ELSE PA.DES_PAIS end AS nacionalidadCliente, CASE WHEN  SP.REF_CORREO ='null' "
				+ "THEN '' ELSE IFNULL( SP.REF_CORREO, '') END AS correoCliente, CASE WHEN SP.REF_TELEFONO ='null' THEN '' ELSE IFNULL( SP.REF_TELEFONO, '') END AS telefonoCliente, "
				+ "IFNULL( SP.CVE_RFC, '') AS rfcCliente, IFNULL( SD.REF_CALLE, '') AS calleCliente, IFNULL( SD.NUM_INTERIOR, '') AS numInteriorCliente, IFNULL(SD.NUM_EXTERIOR, '') AS numExteriorCliente, "
				+ "IFNULL( SD.REF_COLONIA , '') AS coloniaCliente, IFNULL(SD.REF_CP, '') AS cpCliente, IFNULL( SD.REF_MUNICIPIO, '') AS municipioCliente, IFNULL( SD.REF_ESTADO, '') AS estadoCliente, "
				+ "IFNULL(FORMAT(SCP.CAN_PRESUPUESTO, 2) , '0.00') AS importeTotalOds, IFNULL(SV.DES_VELATORIO, '') as nombreVelatorio, IFNULL( SD2.REF_CALLE, '') AS calleVelatorio, "
				+ "IFNULL( SD2.NUM_INTERIOR, '') AS numInteriorVelatorio, IFNULL(SD2.NUM_EXTERIOR, '') AS numExteriorVelatorio, IFNULL( SD2.REF_COLONIA , '') AS coloniaVelatorio, "
				+ "IFNULL(SD2.REF_CP, '') AS cpVelatorio, IFNULL( SD2.REF_MUNICIPIO, '') AS municipioVelatorio, IFNULL( SD2.REF_ESTADO, '') AS estadoVelatorio, "
				+ "IFNULL( CONCAT(SP2.NOM_PERSONA , ' ', SP2.NOM_PRIMER_APELLIDO , ' ', SP2.NOM_SEGUNDO_APELLIDO ), '') AS nombreFinado, DATE_FORMAT(SOS.FEC_ALTA,'%d') AS diaOds, "
				+ "ELT(MONTH(SOS.FEC_ALTA), 'ENERO', 'FEBRERO', 'MARZO', 'ABRIL', 'MAYO', 'JUNIO', 'JULIO', 'AGOSTO', 'SEPTIEMBRE', 'OCTUBRE', 'NOVIEMBRE', 'DICIEMBRE') AS mesOds, DATE_FORMAT(SOS.FEC_ALTA,'%Y') AS anioOds, IFNULL(PAQ.REF_PAQUETE_NOMBRE, '') AS nombrePaquete, "
				+ "IFNULL(CONCAT('$',FORMAT(PAQ.MON_PRECIO,2)),'') AS precioPaquete from SVC_ORDEN_SERVICIO SOS left join SVC_CONTRATANTE SC on SOS.ID_CONTRATANTE = SC.ID_CONTRATANTE "
				+ "left join SVC_PERSONA SP on SC.ID_PERSONA = SP.ID_PERSONA left join SVC_PAIS PA on SP.ID_PAIS = PA.ID_PAIS left join SVT_DOMICILIO SD on SC.ID_DOMICILIO = SD.ID_DOMICILIO "
				+ "left join SVC_VELATORIO SV on SOS.ID_VELATORIO = SV.ID_VELATORIO left join SVT_DOMICILIO SD2 on SV.ID_DOMICILIO = SD2.ID_DOMICILIO left join SVC_FINADO SF on "
				+ "SOS.ID_ORDEN_SERVICIO = SF.ID_ORDEN_SERVICIO left join SVC_UNIDAD_MEDICA UM on SF.ID_UNIDAD_PROCEDENCIA = UM.ID_UNIDAD_MEDICA left join "
				+ "SVC_PERSONA SP2 on SF.ID_PERSONA = SP2.ID_PERSONA left join SVC_PAIS PA2 on SP2.ID_PAIS = PA2.ID_PAIS left join SVC_INFORMACION_SERVICIO SIS on "
				+ "SOS.ID_ORDEN_SERVICIO = SIS.ID_ORDEN_SERVICIO and SIS.IND_ACTIVO = 1 left join SVC_CARAC_PRESUPUESTO SCP on SOS.ID_ORDEN_SERVICIO = SCP.ID_ORDEN_SERVICIO "
				+ "and SCP.IND_ACTIVO = 1 left join SVT_PAQUETE PAQ on SCP.ID_PAQUETE = PAQ.ID_PAQUETE left join SVT_PANTEON PAN on SIS.ID_PANTEON = PAN.ID_PANTEON left join "
				+ "SVC_INF_SERVICIO_VELACION IV on SIS.ID_INFORMACION_SERVICIO = IV.ID_INFORMACION_SERVICIO left join SVT_DOMICILIO DOM on IV.ID_DOMICILIO = DOM.ID_DOMICILIO "
				+ "left join SVC_TIPO_PENSION ST ON SF.ID_TIPO_PENSION = ST.ID_TIPO_PENSION left join SVC_CAPILLA SC2 ON IV.ID_CAPILLA = SC2.ID_CAPILLA where SOS.ID_ORDEN_SERVICIO = " +contratoServicioInmediatoRequest.getIdOrdenServicio();
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
		"CONCAT_WS('-',STA. CVE_FOLIO_ARTICULO,STVA.REF_MODELO_ARTICULO ) AS  modeloAtaud",ConsultaConstantes.TM_DES_TIPO_MATERIAL_AS_TIPO_ATAUD,ConsultaConstantes.STA_CVE_FOLIO_ARTICULO_AS_NUM_INVENTARIOS,
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
		.innerJoin("SVC_PERSONA PE", "SU.ID_PERSONA=PE.ID_PERSONA")
		.where(ConsultaConstantes.SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO).setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, idOrdenServicio).and("SSD.IND_ACTIVO = 1");
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	// consultar reporte salida donacion
	public String consultarReporteSalidaDonacionTemp(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select(ConsultaConstantes.SD_DES_DELEGACION_AS_OOAD_NOM, ConsultaConstantes.SV_ID_VELATORIO_AS_VELATORIO_ID,"SV.DES_VELATORIO AS velatorioNom","SSD.NUM_TOTAL_ATAUDES numAtaudes",
		"CONCAT_WS('-',STA. CVE_FOLIO_ARTICULO,STVA.REF_MODELO_ARTICULO ) AS  modeloAtaud",ConsultaConstantes.TM_DES_TIPO_MATERIAL_AS_TIPO_ATAUD, ConsultaConstantes.STA_CVE_FOLIO_ARTICULO_AS_NUM_INVENTARIOS,
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
		.innerJoin("SVC_SALIDA_DON_ATAUDES_TEMP STDA", "STDA.ID_INVE_ARTICULO = SDCPT.ID_INVE_ARTICULO")
		.innerJoin(ConsultaConstantes.SVT_INVENTARIO_ARTICULO_STA, "STA.ID_INVE_ARTICULO = STDA.ID_INVE_ARTICULO")
		.innerJoin(ConsultaConstantes.SVT_ARTICULO_STVA, "STVA.ID_ARTICULO  = SDCPT.ID_ARTICULO").and(ConsultaConstantes.STVA_ID_CATEGORIA_ARTICULO_1)
		.innerJoin(ConsultaConstantes.SVC_TIPO_MATERIAL_TM, ConsultaConstantes.STVA_ID_TIPO_MATERIAL_TM_ID_TIPO_MATERIAL)
		.innerJoin(ConsultaConstantes.SVC_VELATORIO_SV, ConsultaConstantes.SOS_ID_VELATORIO_SV_ID_VELATORIO)
		.innerJoin(ConsultaConstantes.SVC_DELEGACION_SD,ConsultaConstantes. SD_ID_DELEGACION_SV_ID_DELEGACION)
		.leftJoin(ConsultaConstantes.SVT_USUARIOS_SU, ConsultaConstantes.SU_ID_USUARIO_SV_ID_USUARIO_ADMIN)
		.innerJoin("SVC_PERSONA PE", "SU.ID_PERSONA=PE.ID_PERSONA")

		.where(ConsultaConstantes.SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO).setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, idOrdenServicio).and("SSD.IND_ACTIVO = 1");
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	// consultar reporte donacion
	public String consultarReporteDonacion(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select(ConsultaConstantes.SD_DES_DELEGACION_AS_OOAD_NOM, ConsultaConstantes.SV_ID_VELATORIO_AS_VELATORIO_ID,"SOS.CVE_FOLIO AS numContrato", "CONCAT_WS('-',STA. CVE_FOLIO_ARTICULO,STVA.REF_MODELO_ARTICULO ) AS modeloAtaud",
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
		.innerJoin("SVC_PERSONA PE", "SU.ID_PERSONA=PE.ID_PERSONA")

		.where(ConsultaConstantes.SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO).setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, idOrdenServicio).and("SDN.IND_ACTIVO = 1");
		
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	public String consultarReporteDonacionTemp(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select(ConsultaConstantes.SD_DES_DELEGACION_AS_OOAD_NOM, ConsultaConstantes.SV_ID_VELATORIO_AS_VELATORIO_ID,"SOS.CVE_FOLIO AS numContrato", "CONCAT_WS('-',STA. CVE_FOLIO_ARTICULO,STVA.REF_MODELO_ARTICULO ) AS modeloAtaud",
				ConsultaConstantes.TM_DES_TIPO_MATERIAL_AS_TIPO_ATAUD, ConsultaConstantes.STA_CVE_FOLIO_ARTICULO_AS_NUM_INVENTARIOS,"CONCAT_WS(' ',SPE2.NOM_PERSONA,SPE2.NOM_PRIMER_APELLIDO,SPE2.NOM_SEGUNDO_APELLIDO ) AS nomFinado",
				"CONCAT_WS(' ',SPE.NOM_PERSONA,SPE.NOM_PRIMER_APELLIDO,SPE.NOM_SEGUNDO_APELLIDO ) AS nomContratante",ConsultaConstantes.CONCAT_WS_SU_NOM_USUARIO_SU_NOM_APELLIDO_PATERNO_SU_NOM_APELLIDO_MATERNO_AS_NOM_ADMINISTRADOR,
				ConsultaConstantes.SU_CVE_MATRICULA_AS_CLAVE_ADMINISTRADOR,ConsultaConstantes.CONCAT_WS_SV_DES_VELATORIO_SD_DES_DELEGACION_AS_LUGAR,ConsultaConstantes.DAY_NOW_AS_DIA, ConsultaConstantes.ELT_MONTH_NOW_ENERO_FEBRERO_MARZO_ABRIL_MAYO_JUNIO_JULIO_AGOSTO_SEPTIEMBRE_OCTUBRE_NOVIEMBRE_DICIEMBRE_AS_MES,
				ConsultaConstantes.YEAR_NOW_AS_ANIO)
		.from("SVC_DONACION_ORDEN_SERV_TEMP SDN")
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
		.innerJoin("SVC_PERSONA PE", "SU.ID_PERSONA=PE.ID_PERSONA")
		.where(ConsultaConstantes.SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO).setParameter(ConsultaConstantes.ID_ORDEN_SERVICIO, idOrdenServicio).and("SDN.IND_ACTIVO = 1");
		
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
		

}

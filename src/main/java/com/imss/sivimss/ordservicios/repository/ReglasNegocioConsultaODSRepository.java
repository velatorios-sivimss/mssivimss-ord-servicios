package com.imss.sivimss.ordservicios.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetallePresupuestoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleTrasladoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaquetePresupuestoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPresupuestoRequest;
import com.imss.sivimss.ordservicios.model.request.ContratanteRequest;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.request.FinadoRequest;
import com.imss.sivimss.ordservicios.model.request.InformacionServicioRequest;
import com.imss.sivimss.ordservicios.model.request.InformacionServicioVelacionRequest;
import com.imss.sivimss.ordservicios.model.request.PersonaRequest;
import com.imss.sivimss.ordservicios.util.QueryHelper;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

@Service
public class ReglasNegocioConsultaODSRepository {

	private static final String ID_USUARIO_ALTA="ID_USUARIO_ALTA";
	
	private static final String FEC_ALTA="FEC_ALTA";
	
	private static final String ID_DOMICILIO="ID_DOMICILIO";

	private static final String IND_ACTIVO="IND_ACTIVO";

	private static final String PARAM_FOLIO="folio";
	
	private static final String WHERE_CVE_FOLIO_FOLIO="sos.CVE_FOLIO = :folio";

	private static final String TABLA_SVC_VELATORIO_SV = "SVC_VELATORIO sv";
	private static final String TABLA_SVC_DELEGACION = "SVC_DELEGACION";
	private static final String TABLA_SVC_ORDEN_SERVICIO_SOS = "SVC_ORDEN_SERVICIO sos";
	private static final String TABLA_SVC_PERSONA_SP = "SVC_PERSONA sp";
	private static final String TABLA_SVC_PERSONA_SP2 = "SVC_PERSONA sp2";
	private static final String TABLA_SVC_CONTRATANTE_SC = "SVC_CONTRATANTE sc";
	private static final String TABLA_SVC_FINADO_SF = "SVC_FINADO sf";
	private static final String TABLA_SVC_TIPO_ORDEN_SERVICIO_STOS = "SVC_TIPO_ORDEN_SERVICIO stos";
	private static final String TABLA_SVC_UNIDAD_MEDICA_SUM2 = "SVC_UNIDAD_MEDICA sum2";
	private static final String TABLA_SVT_CONVENIO_PF_SCP = "SVT_CONVENIO_PF scp";
	private static final String TABLA_SVC_ESTATUS_ORDEN_SERVICIO_SEOS = "SVC_ESTATUS_ORDEN_SERVICIO seos";
	private static final String TABLA_SVT_OPERADORES_SO = "SVT_OPERADORES so";
	private static final String TABLA_SVT_DOMICILIO_SD = "SVT_DOMICILIO sd";
	private static final String TABLA_SVC_INFORMACION_SERVICIO_SIS = "SVC_INFORMACION_SERVICIO sis";
	private static final String TABLA_SVT_USUARIOS_SU = "SVT_USUARIOS su";



	private static final Logger log = LoggerFactory.getLogger(ReglasNegocioConsultaODSRepository.class);

	String query;
	
	public String obtenerVelatorios(String idDel) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sv.ID_VELATORIO AS idVelatorio","sv.DES_VELATORIO")
		.from(TABLA_SVC_VELATORIO_SV)
		.join(TABLA_SVC_DELEGACION + " sd", "sd.ID_DELEGACION = sv.ID_DELEGACION")
		.where("sd.ID_DELEGACION = :idDel")
		.setParameter("idDel", idDel);
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerFolioODS(Integer idVel) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sos.ID_ORDEN_SERVICIO AS idODS","sos.CVE_FOLIO AS folioODS")
		.from(TABLA_SVC_ORDEN_SERVICIO_SOS)
		.where(" so.ID_VELATORIO = :idVel")
		.setParameter("idVel", idVel);
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}
	public String obtenerContratante(String folio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nombreContratante")
		.from(TABLA_SVC_PERSONA_SP)
		.join(TABLA_SVC_CONTRATANTE_SC, "sc.ID_PERSONA  = sp.ID_PERSONA")
		.join(TABLA_SVC_ORDEN_SERVICIO_SOS , "sos.ID_CONTRATANTE = sc.ID_CONTRATANTE")
		.where(WHERE_CVE_FOLIO_FOLIO)
		.setParameter(PARAM_FOLIO, folio);		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerFinado(String folio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sf.id_finado AS idFinado"
				,"CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nombreFinado")
		.from(TABLA_SVC_PERSONA_SP)
		.join(TABLA_SVC_FINADO_SF, "sf.ID_PERSONA = sp.ID_PERSONA")
		.join(TABLA_SVC_ORDEN_SERVICIO_SOS, "sos.ID_ORDEN_SERVICIO = sf.ID_ORDEN_SERVICIO")
		.where(WHERE_CVE_FOLIO_FOLIO)
		.setParameter(PARAM_FOLIO, folio);		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerTipoOrden() {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("stos.ID_TIPO_ORDEN_SERVICIO AS idTipoODS","stos.DES_TIPO_ORDEN_SERVICIO AS tipoODS")
		.from(TABLA_SVC_TIPO_ORDEN_SERVICIO_STOS);
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerUnidadMedica(Integer idFinado) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sum2.ID_UNIDAD_MEDICA AS idUnidad","sum2.DES_UNIDAD_MEDICA AS nombreUnidad")
		.from(TABLA_SVC_UNIDAD_MEDICA_SUM2)
		.join(TABLA_SVC_FINADO_SF, "sf.ID_UNIDAD_PROCEDENCIA = sum2.ID_UNIDAD_MEDICA")
		.where("sf.ID_FINADO = :idFinado")
		.setParameter("idFinado", idFinado);		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerContratoConvenio(Integer idFinado){
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("scp.ID_CONVENIO_PF AS idConvenio","scp.DES_FOLIO AS convenio")
		.from(TABLA_SVT_CONVENIO_PF_SCP)
		.join(TABLA_SVC_FINADO_SF, "sf.ID_CONTRATO_PREVISION  = scp.ID_CONVENIO_PF ")
		.where("sf.ID_FINADO = :idFinado")
		.setParameter("idFinado", idFinado);				
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerEstadoODS(String folio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("seos.ID_ESTATUS_ORDEN_SERVICIO AS idODS"," seos.DES_ESTATUS AS estatus")
		.from(TABLA_SVC_ESTATUS_ORDEN_SERVICIO_SEOS) ;
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String generaTarjetaIden(String idOper) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sos.CVE_FOLIO AS noFolioODS","DATE_FORMAT(sos.FEC_ALTA,'%d-%m-%Y') as fecha"
				,"CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nombreFinado"
				,"CONCAT(sv.DES_VELATORIO,' / ',sd.DES_CALLE,', ',sd.NUM_EXTERIOR,IFNULL(CONCAT(', INT ',sd.NUM_INTERIOR),''),IFNULL(CONCAT(', COL. ',sd.DES_COLONIA),''),IFNULL(CONCAT(', DEL. ',sd.DES_MUNICIPIO),''),IFNULL(CONCAT(', ',sd.DES_ESTADO),''))	AS velatorio"
				, "DATE_FORMAT(sis.FEC_CORTEJO,'%d-%m-%Y') AS fechaCortejo"
				, "TIME_FORMAT(sis.TIM_HORA_CORTEJO ,'%H:%i') AS horaCortejo"
				//+ " -- "
				, "'Modelo Ataud' AS modAtaud"
				, "'Numero Folio' AS noFolioAtaud"
				// + " -- "
				, "CONCAT(su.NOM_USUARIO, ' ', su.NOM_APELLIDO_PATERNO, ' ', su.NOM_APELLIDO_MATERNO) AS operador"
				, "su.CVE_MATRICULA AS matriculaOperador")
		.from(TABLA_SVT_OPERADORES_SO)
		.join(TABLA_SVC_ORDEN_SERVICIO_SOS, "sos.ID_OPERADOR = so.ID_OPERADOR")
		.join(TABLA_SVC_FINADO_SF, "sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO")
		.join(TABLA_SVC_PERSONA_SP, "sp.ID_PERSONA = sf.ID_PERSONA")
		.join(TABLA_SVC_VELATORIO_SV, "sv.ID_VELATORIO = sos.ID_VELATORIO")
		.join(TABLA_SVT_DOMICILIO_SD, "sd.ID_DOMICILIO = sv.ID_DOMICILIO ")
		.join(TABLA_SVC_INFORMACION_SERVICIO_SIS, "sis.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO")
		.leftJoin(TABLA_SVT_USUARIOS_SU, "su.id_usuario = so.ID_USUARIO")
		.where("so.ID_OPERADOR = :idOper")
		.setParameter("idOper", idOper);
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerODS(String folio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sv.DES_VELATORIO AS velatorio","sos.CVE_FOLIO AS numeroFolio"
				,"CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nombreContratante"
				,"CONCAT(sp2.NOM_PERSONA, ' ', sp2.NOM_PRIMER_APELLIDO, ' ', sp2.NOM_SEGUNDO_APELLIDO) AS nombreFinado"
				,"stos.DES_TIPO_ORDEN_SERVICIO AS tipoOrden"
				,"sum2.DES_UNIDAD_MEDICA AS unidadProcedencia"
				,"scp.DES_FOLIO AS contratoConvenio"
				,"seos.DES_ESTATUS AS estatus")
		.from(TABLA_SVC_ORDEN_SERVICIO_SOS)
		.join(TABLA_SVC_VELATORIO_SV, "sv.ID_VELATORIO = sos.ID_VELATORIO")
		.join(TABLA_SVC_CONTRATANTE_SC,"sc.ID_CONTRATANTE = sos.ID_CONTRATANTE")
		.leftJoin(TABLA_SVC_PERSONA_SP,"sp.ID_PERSONA = sc.ID_PERSONA") 
		.join(TABLA_SVC_FINADO_SF ,"sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO")
		.leftJoin(TABLA_SVC_PERSONA_SP2 ,"sp2.ID_PERSONA = sf.ID_PERSONA")
		.join(TABLA_SVC_TIPO_ORDEN_SERVICIO_STOS ,"stos.ID_TIPO_ORDEN_SERVICIO = sf.ID_TIPO_ORDEN") 
		.join(TABLA_SVC_UNIDAD_MEDICA_SUM2 ,"sum2.ID_UNIDAD_MEDICA = sf.ID_UNIDAD_PROCEDENCIA")
		.join(TABLA_SVT_CONVENIO_PF_SCP ,"scp.ID_CONVENIO_PF = sf.ID_CONTRATO_PREVISION")
		.join(TABLA_SVC_ESTATUS_ORDEN_SERVICIO_SEOS ,"seos.ID_ESTATUS_ORDEN_SERVICIO = sos.ID_ESTATUS_ORDEN_SERVICIO")  
		.where(WHERE_CVE_FOLIO_FOLIO)
		.setParameter(PARAM_FOLIO, folio);

		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerCancelarODS(Integer idVelatorio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

}

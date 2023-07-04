package com.imss.sivimss.ordservicios.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.util.SelectQueryUtil;
import com.imss.sivimss.ordservicios.model.request.ReporteDto;

@Service
public class ReglasNegocioConsultaODSRepository {


	private static final String PARAM_FOLIO="folio";
	private static final String JOIN = " JOIN ";
	private static final String LEFT_JOIN= " LEFT JOIN ";
	
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
	
	public String obtenerVelatorios(Integer idDel) {
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

	public String obtenerFolioODS() {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sos.ID_ORDEN_SERVICIO AS idODS","sos.CVE_FOLIO AS folioODS")
		.from(TABLA_SVC_ORDEN_SERVICIO_SOS);
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}
	public String obtenerContratante() {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sc.ID_CONTRATANTE AS idContratante, sc.CVE_MATRICULA AS matricula, sp.CVE_RFC AS rfc, sp.CVE_CURP AS curp"
				+ ", sp.NOM_PERSONA AS nombreContratante, sp.NOM_PRIMER_APELLIDO AS primerApellido, sp.NOM_SEGUNDO_APELLIDO AS segundoApellido"
				+ ", IFNULL(sp.NUM_SEXO,sp.DES_OTRO_SEXO) AS sexo, sp.FEC_NAC AS fechaNacimiento, sp3.DES_PAIS  AS nacionalidad, sp3.DES_PAIS  AS pais"
				+ ", se.DES_ESTADO  AS lugarNacimiento, sp.DES_TELEFONO AS telefono, sp.DES_CORREO AS correoElectronico, sp2.DES_PARENTESCO AS  parentesco"
				+ ", sd.DES_CALLE AS calle, sd.NUM_EXTERIOR AS numExterior, sd.NUM_INTERIOR AS numInterior, sd.DES_CP AS cp"
				+ ", sd.DES_COLONIA AS colonia, sd.DES_MUNICIPIO AS municipio, sd.DES_ESTADO AS estado"
				+ ", CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nombreCompletoContratante")
		.from(TABLA_SVC_PERSONA_SP)
		.join(TABLA_SVC_CONTRATANTE_SC, "sc.ID_PERSONA  = sp.ID_PERSONA")
		.join(TABLA_SVC_ORDEN_SERVICIO_SOS , "sos.ID_CONTRATANTE = sc.ID_CONTRATANTE")
		.join("SVC_PARENTESCO sp2","sos.ID_PARENTESCO = sp2.ID_PARENTESCO") 
		.join("SVT_DOMICILIO sd","sd.ID_DOMICILIO = sc.ID_DOMICILIO") 
		.join("SVC_PAIS sp3","sp3.ID_PAIS = sp.ID_PAIS")
		.join("SVC_ESTADO se","se.ID_ESTADO = sp.ID_ESTADO");
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerFinado() {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("sf.id_finado AS idFinado, CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nombreCompletoFinado"
				+ ", stos.DES_TIPO_ORDEN_SERVICIO AS tipoOrden, sf.DES_EXTREMIDAD AS servicioExtremidad, sf.DES_OBITO AS esObito, sf.CVE_MATRICULA AS matricula"
				+ ", scp.DES_FOLIO AS contratoConvenio, sv.DES_VELATORIO AS velatorioPrevisionsp, sp.CVE_CURP AS curp, sp.CVE_NSS AS nss, sp.NOM_PERSONA AS nombres"
				+ ", sp.NOM_PRIMER_APELLIDO AS primerApellido, sp.NOM_SEGUNDO_APELLIDO AS segundoApellido, IFNULL(sp.NUM_SEXO,sp.DES_OTRO_SEXO) AS sexo"
				+ ", sp.FEC_NAC AS fechaNacimiento, YEAR (CURDATE()) - YEAR(sp.FEC_NAC) as edad, sp3.DES_PAIS AS nacionalidad"
				+ ", sp3.DES_PAIS AS paisNacimiento, se.DES_ESTADO  AS lugarNacimiento, sd.DES_CALLE AS calle, sd.NUM_EXTERIOR AS numExterior"
				+ ", sd.NUM_INTERIOR AS numInterior, sd.DES_CP AS cp, sd.DES_COLONIA AS colonia, sd.DES_MUNICIPIO AS municipio"
				+ ", sd.DES_ESTADO AS estado, sf.FEC_DECESO AS fechaDefuncion, sf.DES_CAUSA_DECESO AS causaDeceso"
				+ ", sf.DES_LUGAR_DECESO AS lugarDeceso, sf.TIM_HORA AS horaDeceso, sum2.DES_UNIDAD_MEDICA AS clinicaAdscripcion"
				+ ", sum3.DES_UNIDAD_MEDICA AS unidadProcedencia, sf.DES_PROCEDENCIA_FINADO AS procedenciaFinado, stp.DES_PENSION AS pension ")
		.from(TABLA_SVC_PERSONA_SP)
		.join(TABLA_SVC_FINADO_SF, "sf.ID_PERSONA = sp.ID_PERSONA")
		.join(TABLA_SVC_ORDEN_SERVICIO_SOS, "sos.ID_ORDEN_SERVICIO = sf.ID_ORDEN_SERVICIO")
		.join("SVC_TIPO_ORDEN_SERVICIO stos","stos.ID_TIPO_ORDEN_SERVICIO = sf.ID_TIPO_ORDEN")
		.join("SVT_CONVENIO_PF scp","scp.ID_CONVENIO_PF = sf.ID_CONTRATO_PREVISION") 
		.join("SVC_VELATORIO sv","sv.ID_VELATORIO = sf.ID_VELATORIO") 
		.join("SVT_DOMICILIO sd","sd.ID_DOMICILIO = sf.ID_DOMICILIO") 
		.join("SVC_PAIS sp3 ","sp3.ID_PAIS = sp.ID_PAIS")
		.join("SVC_ESTADO se","se.ID_ESTADO = sp.ID_ESTADO")
		.join("SVC_UNIDAD_MEDICA sum2","sum2.ID_UNIDAD_MEDICA = sf.ID_CLINICA_ADSCRIPCION")  
		.join("SVC_UNIDAD_MEDICA sum3","sum3.ID_UNIDAD_MEDICA = sf.ID_UNIDAD_PROCEDENCIA") 
		.join("SVC_TIPO_PENSION stp","stp.ID_TIPO_PENSION = sf.ID_TIPO_PENSION");
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

	public String obtenerUnidadMedica(Integer idDel) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("DISTINCT(sum2.ID_UNIDAD_MEDICA) AS idUnidadMedica","sum2.DES_UNIDAD_MEDICA AS nombreUnidad")
		.from(TABLA_SVC_UNIDAD_MEDICA_SUM2)
		.join(TABLA_SVC_FINADO_SF, "sf.ID_UNIDAD_PROCEDENCIA = sum2.ID_UNIDAD_MEDICA")
		.join("SVC_VELATORIO sv","sv.ID_VELATORIO  = sf.ID_VELATORIO")
		.where("sum2.IND_ACTIVO = 1")
		.and("sv.ID_DELEGACION = :idDel")
		.setParameter("idDel", idDel);		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerContratoConvenio(){
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("scp.ID_CONVENIO_PF AS idConvenio","scp.DES_FOLIO AS convenio")
		.from(TABLA_SVT_CONVENIO_PF_SCP)
		.join(TABLA_SVC_FINADO_SF, "sf.ID_CONTRATO_PREVISION  = scp.ID_CONVENIO_PF ");
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerEstadoODS() {
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

	public String obtenerODS(ReporteDto reporteDto) {
		String str= "SELECT sos.ID_ORDEN_SERVICIO AS idOrdenServicio , sv.DES_VELATORIO AS velatorio , sos.CVE_FOLIO AS numeroFolio "
				+ ", CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nombreContratante"
				+ ", CONCAT(sp2.NOM_PERSONA, ' ', sp2.NOM_PRIMER_APELLIDO, ' ', sp2.NOM_SEGUNDO_APELLIDO) AS nombreFinado"
				+ ", stos.DES_TIPO_ORDEN_SERVICIO AS tipoOrden, sum2.DES_UNIDAD_MEDICA AS unidadProcedencia"
				+ ", scp.DES_FOLIO AS contratoConvenio, seos.DES_ESTATUS AS estatus"
				+ " FROM " + TABLA_SVC_ORDEN_SERVICIO_SOS 
				+ JOIN + TABLA_SVC_VELATORIO_SV + " ON sv.ID_VELATORIO = sos.ID_VELATORIO"
				+ JOIN + TABLA_SVC_CONTRATANTE_SC +" ON sc.ID_CONTRATANTE = sos.ID_CONTRATANTE"
				+ LEFT_JOIN + TABLA_SVC_PERSONA_SP + " ON sp.ID_PERSONA = sc.ID_PERSONA"
				+ JOIN + TABLA_SVC_FINADO_SF + " ON sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO"
				+ LEFT_JOIN + TABLA_SVC_PERSONA_SP2 + " ON sp2.ID_PERSONA = sf.ID_PERSONA"
				+ JOIN + TABLA_SVC_TIPO_ORDEN_SERVICIO_STOS + " ON stos.ID_TIPO_ORDEN_SERVICIO = sf.ID_TIPO_ORDEN" 
				+ JOIN + TABLA_SVC_UNIDAD_MEDICA_SUM2 + " ON sum2.ID_UNIDAD_MEDICA = sf.ID_UNIDAD_PROCEDENCIA"
				+ JOIN + TABLA_SVT_CONVENIO_PF_SCP + " ON scp.ID_CONVENIO_PF = sf.ID_CONTRATO_PREVISION"
				+ JOIN + TABLA_SVC_ESTATUS_ORDEN_SERVICIO_SEOS + " ON seos.ID_ESTATUS_ORDEN_SERVICIO = sos.ID_ESTATUS_ORDEN_SERVICIO ";
		str = str + generaReporteConsultaODS(reporteDto);
		log.info(str);
		return str;
	}

	public String cancelarODS(Integer idVelatorio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}
	public String generaReporteConsultaODS(ReporteDto reporteDto) {
		String  condicion = "";
		if (reporteDto.getIdVelatorio() != null) {
			condicion = "WHERE sv.ID_VELATORIO = " + reporteDto.getIdVelatorio();
			if (reporteDto.getIdOds() != null) {
				condicion = condicion + " AND sos.ID_ORDEN_SERVICIo = " + reporteDto.getIdOds();
			}
			if (reporteDto.getIdContratante() != null) {
				condicion = condicion + " AND sc.ID_CONTRATANTE = " + reporteDto.getIdContratante();
			}
			if (reporteDto.getIdFinado() != null) {
				condicion = condicion + " AND sf.ID_FINADO = " + reporteDto.getIdFinado();
			}
			if (reporteDto.getIdTipoODS() != null) {
				condicion = condicion + " AND stos.ID_TIPO_ORDEN_SERVICIO = " + reporteDto.getIdTipoODS();
			}
			if (reporteDto.getIdUnidadMedica() != null) {
				condicion = condicion + " AND sum2.ID_UNIDAD_MEDICA = " + reporteDto.getIdUnidadMedica();
			}
			if (reporteDto.getIdConvenio() != null) {
				condicion = condicion + " AND scp.ID_CONVENIO_PF = " + reporteDto.getIdConvenio();
			}
		} else if (reporteDto.getIdOds() != null || reporteDto.getIdOrdenServicio() != null) {
			condicion = "WHERE sos.ID_ORDEN_SERVICIO = " + ((reporteDto.getIdOds()!=null) ? reporteDto.getIdOds(): reporteDto.getIdOrdenServicio()) ;
			if (reporteDto.getIdContratante() != null) {
				condicion = condicion + " AND sc.ID_CONTRATANTE = " + reporteDto.getIdContratante();
			}
			if (reporteDto.getIdFinado() != null) {
				condicion = condicion + " AND sf.ID_FINADO = " + reporteDto.getIdFinado();
			}
			if (reporteDto.getIdTipoODS() != null) {
				condicion = condicion + " AND stos.ID_TIPO_ORDEN_SERVICIO = " + reporteDto.getIdTipoODS();
			}
			if (reporteDto.getIdUnidadMedica() != null) {
				condicion = condicion + " AND sum2.ID_UNIDAD_MEDICA = " + reporteDto.getIdUnidadMedica();
			}
			if (reporteDto.getIdConvenio() != null) {
				condicion = condicion + " AND scp.ID_CONVENIO_PF = " + reporteDto.getIdConvenio();
			}
		} else if (reporteDto.getIdContratante() != null) {
			condicion = "WHERE sc.ID_CONTRATANTE = " + reporteDto.getIdContratante();
			if (reporteDto.getIdFinado() != null) {
				condicion = condicion + " AND sf.ID_FINADO = " + reporteDto.getIdFinado();
			}
			if (reporteDto.getIdTipoODS() != null) {
				condicion = condicion + " AND stos.ID_TIPO_ORDEN_SERVICIO = " + reporteDto.getIdTipoODS();
			}
			if (reporteDto.getIdUnidadMedica() != null) {
				condicion = condicion + " AND sum2.ID_UNIDAD_MEDICA = " + reporteDto.getIdUnidadMedica();
			}
			if (reporteDto.getIdConvenio() != null) {
				condicion = condicion + " AND scp.ID_CONVENIO_PF = " + reporteDto.getIdConvenio();
			}
		} else if (reporteDto.getIdFinado() != null) {
			condicion = "WHERE sf.ID_FINADO = " + reporteDto.getIdFinado();
			if (reporteDto.getIdTipoODS() != null) {
				condicion = condicion + " AND stos.ID_TIPO_ORDEN_SERVICIO = " + reporteDto.getIdTipoODS();
			}
			if (reporteDto.getIdUnidadMedica() != null) {
				condicion = condicion + " AND sum2.ID_UNIDAD_MEDICA = " + reporteDto.getIdUnidadMedica();
			}
			if (reporteDto.getIdConvenio() != null) {
				condicion = condicion + " AND scp.ID_CONVENIO_PF = " + reporteDto.getIdConvenio();
			}
		} else if (reporteDto.getIdTipoODS() != null) {
			condicion = "WHERE stos.ID_TIPO_ORDEN_SERVICIO = " + reporteDto.getIdTipoODS();
			if (reporteDto.getIdUnidadMedica() != null) {
				condicion = condicion + " AND sum2.ID_UNIDAD_MEDICA = " + reporteDto.getIdUnidadMedica();
			}
			if (reporteDto.getIdConvenio() != null) {
				condicion = condicion + " AND scp.ID_CONVENIO_PF = " + reporteDto.getIdConvenio();
			}
		} else if (reporteDto.getIdUnidadMedica() != null) {
			condicion = "WHERE sum2.ID_UNIDAD_MEDICA = " +	reporteDto.getIdUnidadMedica();

			if (reporteDto.getIdConvenio() != null) {
				condicion = condicion + " AND scp.ID_CONVENIO_PF = " + reporteDto.getIdConvenio();
			}
		} else if (reporteDto.getIdConvenio() != null) {
			condicion = "WHERE scp.ID_CONVENIO_PF = " + reporteDto.getIdConvenio();
		}
		log.info(condicion);
		return condicion;
	}

}

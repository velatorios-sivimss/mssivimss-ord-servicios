package com.imss.sivimss.ordservicios.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.util.SelectQueryUtil;
import com.imss.sivimss.ordservicios.model.request.ReporteDto;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;

@Service
public class ReglasNegocioConsultaODSRepository {


	private static final String JOIN = " JOIN ";
	private static final String LEFT_JOIN= " LEFT JOIN ";
	

	private static final String TABLA_SVC_VELATORIO_SV = "SVC_VELATORIO sv";
	private static final String TABLA_SVC_DELEGACION = "SVC_DELEGACION";
	private static final String TABLA_SVC_ORDEN_SERVICIO_SOS = "SVC_ORDEN_SERVICIO sos";
	private static final String TABLA_SVC_PERSONA_SP = "SVC_PERSONA sp";
	private static final String TABLA_SVC_PERSONA_SP2 = "SVC_PERSONA sp2";
	private static final String TABLA_SVC_CONTRATANTE_SC = "SVC_CONTRATANTE sc";
	private static final String TABLA_SVC_FINADO_SF = "SVC_FINADO sf";
	private static final String TABLA_SVC_TIPO_ORDEN_SERVICIO_STOS = "SVC_TIPO_ORDEN_SERVICIO stos";
	private static final String TABLA_SVC_UNIDAD_MEDICA_SUM2 = "SVC_UNIDAD_MEDICA sum2";
	private static final String TABLA_SVC_UNIDAD_MEDICA_SUM3 = "SVC_UNIDAD_MEDICA sum3";
	private static final String TABLA_SVT_CONVENIO_PF_SCP = "SVT_CONVENIO_PF scp";
	private static final String TABLA_SVC_ESTATUS_ORDEN_SERVICIO_SEOS = "SVC_ESTATUS_ORDEN_SERVICIO seos";
	private static final String TABLA_SVT_OPERADORES_SO = "SVT_OPERADORES so";
	private static final String TABLA_SVT_DOMICILIO_SD = "SVT_DOMICILIO sd";
	private static final String TABLA_SVC_INFORMACION_SERVICIO_SIS = "SVC_INFORMACION_SERVICIO sis";
	private static final String TABLA_SVT_USUARIOS_SU = "SVT_USUARIOS su";
	private static final String TABLA_SVC_CARACTERISTICAS_PRESUPUESTO_SCP2 = "SVC_CARACTERISTICAS_PRESUPUESTO scp2";
	private static final String TABLA_SVC_PARENTESCO_SP2 = "SVC_PARENTESCO sp2";
	private static final String TABLA_SVC_PAIS_SP3 = "SVC_PAIS sp3";
	private static final String TABLA_SVC_ESTADO_SE = "SVC_ESTADO se";
	private static final String TABLA_SVC_TIPO_PENSION_STP = "SVC_TIPO_PENSION stp";

	private static final String SET_CAMPO_FEC_BAJA = " FEC_BAJA = CURRENT_TIMESTAMP() ";
	private static final String WHERE_ID_ORDEN_SERVICIO = " WHERE ID_ORDEN_SERVICIO = ";
	
	private static final String AND_ID_FINADO = " AND sf.ID_FINADO = ";
	private static final String AND_ID_TIPO_ORDEN_SERVICIO = " AND stos.ID_TIPO_ORDEN_SERVICIO = ";
	private static final String AND_ID_UNIDAD_MEDICA = " AND sum2.ID_UNIDAD_MEDICA = ";
	private static final String AND_ID_CONVENIO_PF = " AND scp.ID_CONVENIO_PF = "; 
	
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
		.join(TABLA_SVC_PARENTESCO_SP2,"sos.ID_PARENTESCO = sp2.ID_PARENTESCO") 
		.join(TABLA_SVT_DOMICILIO_SD,"sd.ID_DOMICILIO = sc.ID_DOMICILIO") 
		.join(TABLA_SVC_PAIS_SP3,"sp3.ID_PAIS = sp.ID_PAIS")
		.join(TABLA_SVC_ESTADO_SE,"se.ID_ESTADO = sp.ID_ESTADO");
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
		.join(TABLA_SVC_TIPO_ORDEN_SERVICIO_STOS,"stos.ID_TIPO_ORDEN_SERVICIO = sf.ID_TIPO_ORDEN")
		.join(TABLA_SVT_CONVENIO_PF_SCP,"scp.ID_CONVENIO_PF = sf.ID_CONTRATO_PREVISION") 
		.join(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO = sf.ID_VELATORIO") 
		.join(TABLA_SVT_DOMICILIO_SD,"sd.ID_DOMICILIO = sf.ID_DOMICILIO") 
		.join(TABLA_SVC_PAIS_SP3,"sp3.ID_PAIS = sp.ID_PAIS")
		.join(TABLA_SVC_ESTADO_SE,"se.ID_ESTADO = sp.ID_ESTADO")
		.join(TABLA_SVC_UNIDAD_MEDICA_SUM2,"sum2.ID_UNIDAD_MEDICA = sf.ID_CLINICA_ADSCRIPCION")  
		.join(TABLA_SVC_UNIDAD_MEDICA_SUM3,"sum3.ID_UNIDAD_MEDICA = sf.ID_UNIDAD_PROCEDENCIA") 
		.join(TABLA_SVC_TIPO_PENSION_STP,"stp.ID_TIPO_PENSION = sf.ID_TIPO_PENSION");
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
		.join(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO  = sf.ID_VELATORIO")
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
	public String obtenerOperadores() {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("so.ID_OPERADOR as idOperador","concat(su.NOM_USUARIO, ' ', su.NOM_APELLIDO_PATERNO, ' ', su.NOM_APELLIDO_MATERNO) as nombreOperador")
		.from(TABLA_SVT_OPERADORES_SO)
		.join(TABLA_SVT_USUARIOS_SU,"su.ID_USUARIO = so.ID_USUARIO");
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String generaTarjetaIden(Integer idOper, Integer idOrdenServicio, UsuarioDto usuario) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("DISTINCT(sos.CVE_FOLIO) AS noFolioODS","DATE_FORMAT(sos.FEC_ALTA,'%d-%m-%Y') as fecha"
				,"CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nombreFinado"
				,"CONCAT(sv.DES_VELATORIO,' / ',sd.DES_CALLE,', ',sd.NUM_EXTERIOR,IFNULL(CONCAT(', INT ',sd.NUM_INTERIOR),''),IFNULL(CONCAT(', COL. ',sd.DES_COLONIA),''),IFNULL(CONCAT(', DEL. ',sd.DES_MUNICIPIO),''),IFNULL(CONCAT(', ',sd.DES_ESTADO),''))	AS velatorio"
				, "DATE_FORMAT(sis.FEC_CORTEJO,'%d-%m-%Y') AS fechaCortejo"
				, "TIME_FORMAT(sis.TIM_HORA_CORTEJO ,'%H:%i') AS horaCortejo"
				, "sa.DES_MODELO_ARTICULO  AS modAtaud"
				, "sia.FOLIO_ARTICULO  AS noFolioAtaud"
				, "CONCAT(su.NOM_USUARIO, ' ', su.NOM_APELLIDO_PATERNO, ' ', su.NOM_APELLIDO_MATERNO) AS operador"
				, "su.CVE_MATRICULA AS matriculaOperador"
				, "'" + usuario.getNombre() + "' AS nombreAdmin")
		.from(TABLA_SVT_OPERADORES_SO)
		.join(TABLA_SVC_ORDEN_SERVICIO_SOS, "sos.ID_OPERADOR = so.ID_OPERADOR")
		.join(TABLA_SVC_FINADO_SF, "sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO")
		.join(TABLA_SVC_PERSONA_SP, "sp.ID_PERSONA = sf.ID_PERSONA")
		.join(TABLA_SVC_VELATORIO_SV, "sv.ID_VELATORIO = sos.ID_VELATORIO")
		.join(TABLA_SVT_DOMICILIO_SD, "sd.ID_DOMICILIO = sv.ID_DOMICILIO ")
		.join(TABLA_SVC_INFORMACION_SERVICIO_SIS, "sis.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO")
		.leftJoin(TABLA_SVT_USUARIOS_SU, "su.id_usuario = so.ID_USUARIO")  
		.join("SVT_INVENTARIO_ARTICULO sia","ID_INVE_ARTICULO  = (SELECT sdcp.ID_INVE_ARTICULO FROM SVC_CARACTERISTICAS_PRESUPUESTO scp "
				+ JOIN + "  SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO sdcp ON sdcp.ID_CARACTERISTICAS_PRESUPUESTO = scp.ID_CARACTERISTICAS_PRESUPUESTO "
				+ JOIN + "  SVC_ORDEN_SERVICIO sos ON sos.ID_ORDEN_SERVICIO = scp.ID_ORDEN_SERVICIO WHERE sos.ID_ORDEN_SERVICIO  = " + idOrdenServicio + ")")
		.join("SVT_ARTICULO sa ","sa.ID_ARTICULO = sia.ID_ARTICULO") 
		.where("so.ID_OPERADOR = :idOper")
		.setParameter("idOper", idOper)
		.and("sos.ID_ORDEN_SERVICIO = :idODS")
		.setParameter("idODS", idOrdenServicio);
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerCostoCancelacionODS() {
		String str= "SELECT TIP_PARAMETRO AS costoCancelacion FROM SVC_PARAMETRO_SISTEMA WHERE ID_FUNCIONALIDAD = 24 AND DES_PARAMETRO = 'COSTO POR CANCELACION'";
		log.info(str);
		return str;
	}
	public String obtenerODS(ReporteDto reporteDto) {
		String str= "SELECT DISTINCT (sos.ID_ORDEN_SERVICIO ) as idOrdenServicio ,"
				+ "CASE WHEN sad.ID_INVE_ARTICULO is not null then 1 else 0 end as EntradaDonacion,"
				+ "CASE WHEN ssda.ID_INVE_ARTICULO is not null then 1 else 0 end as SalidaDonacion,"
				+ "sv.DES_VELATORIO as velatorio ,"
				+ "sos.CVE_FOLIO as numeroFolio ,"
				+ "CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) as nombreContratante,"
				+ "CONCAT(sp2.NOM_PERSONA, ' ', sp2.NOM_PRIMER_APELLIDO, ' ', sp2.NOM_SEGUNDO_APELLIDO) as nombreFinado,"
				+ "stos.DES_TIPO_ORDEN_SERVICIO as tipoOrden,"
				+ "IFNULL(sum2.DES_UNIDAD_MEDICA, '') as unidadProcedencia,"
				+ "IFNULL(scp.DES_FOLIO, '') as contratoConvenio,"
				+ " seos.ID_ESTATUS_ORDEN_SERVICIO as idEstatus, "
				+ " seos.DES_ESTATUS as estatus, "
				+ "IFNULL(scp2.DES_NOTAS_SERVICIO, scpt.DES_NOTAS_SERVICIO) as notasServicio ,"
				+ "TIMESTAMPDIFF(hour , sos.FEC_ALTA, now()) as tiempoGeneracionODSHrs "
				+ " FROM SVC_ORDEN_SERVICIO sos "
				+ JOIN + TABLA_SVC_VELATORIO_SV + " on sv.ID_VELATORIO = sos.ID_VELATORIO "
				+ JOIN + TABLA_SVC_CONTRATANTE_SC + " on sc.ID_CONTRATANTE = sos.ID_CONTRATANTE "
				+ LEFT_JOIN + TABLA_SVC_PERSONA_SP + " on sp.ID_PERSONA = sc.ID_PERSONA "
				+ JOIN + TABLA_SVC_FINADO_SF + " on sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO "
				+ LEFT_JOIN + TABLA_SVC_PERSONA_SP2 +" on sp2.ID_PERSONA = sf.ID_PERSONA "
				+ JOIN + TABLA_SVC_TIPO_ORDEN_SERVICIO_STOS + " on stos.ID_TIPO_ORDEN_SERVICIO = sf.ID_TIPO_ORDEN "
				+ LEFT_JOIN + TABLA_SVC_UNIDAD_MEDICA_SUM2 +" on sum2.ID_UNIDAD_MEDICA = IFNULL(sf.ID_UNIDAD_PROCEDENCIA, sf.ID_CLINICA_ADSCRIPCION) "
				+ LEFT_JOIN + TABLA_SVT_CONVENIO_PF_SCP + " on scp.ID_CONVENIO_PF = sf.ID_CONTRATO_PREVISION "
				+ JOIN + TABLA_SVC_ESTATUS_ORDEN_SERVICIO_SEOS + " on seos.ID_ESTATUS_ORDEN_SERVICIO = sos.ID_ESTATUS_ORDEN_SERVICIO "
				+ LEFT_JOIN + TABLA_SVC_CARACTERISTICAS_PRESUPUESTO_SCP2 + " on scp2.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO "
				+ LEFT_JOIN + "SVC_CARACTERISTICAS_PRESUPUESTO_TEMP scpt on scpt.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO AND scpt.IND_ACTIVO = 1 "
				+ LEFT_JOIN + "SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO sdcp on scp2.ID_CARACTERISTICAS_PRESUPUESTO = sdcp.ID_CARACTERISTICAS_PRESUPUESTO "
				+ LEFT_JOIN + "SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP sdcpt on sdcpt.ID_CARACTERISTICAS_PRESUPUESTO = scpt.ID_CARACTERISTICAS_PRESUPUESTO "
				+ LEFT_JOIN + "SVT_INVENTARIO_ARTICULO sia on sdcp.ID_INVE_ARTICULO = sia.ID_INVE_ARTICULO "
				+ LEFT_JOIN + "SVT_INVENTARIO_ARTICULO sia2 on sia2.ID_INVE_ARTICULO = sdcpt.ID_INVE_ARTICULO "
				+ LEFT_JOIN + "SVC_ATAUDES_DONADOS sad on sad.ID_INVE_ARTICULO = sia.ID_INVE_ARTICULO OR sad.ID_INVE_ARTICULO = sia2.ID_INVE_ARTICULO "
				+ LEFT_JOIN + "SVC_SALIDA_DONACION_ATAUDES ssda on sia.ID_INVE_ARTICULO = ssda.ID_INVE_ARTICULO OR ssda.ID_INVE_ARTICULO = sia2.ID_INVE_ARTICULO "
				+ LEFT_JOIN + "SVC_SALIDA_DONACION ssd on ssd.ID_SALIDA_DONACION = ssda.ID_SALIDA_DONACION ";
		str = str + generaReporteConsultaODS(reporteDto);
		log.info(str);
		return str;
	}
	// Bloque Cancelacion ODS
	public String cancelarODS(ReporteDto idODS, UsuarioDto usuario) {		
		String str = "UPDATE SVC_ORDEN_SERVICIO SET  ID_ESTATUS_ORDEN_SERVICIO = 0, DES_MOTIVO_CANCELACION ='" +idODS.getMotivoCancelacion() + "'"
				+ ", ID_USUARIO_MODIFICA = " + usuario.getIdUsuario() + ", " + SET_CAMPO_FEC_BAJA + WHERE_ID_ORDEN_SERVICIO + idODS.getIdOrdenServicio();
		log.info(str);
		return str;
	}
	public String cancelarCaracteristicasPaquete(Integer idODS, UsuarioDto usuario) {		
		String str = "UPDATE SVC_CARACTERISTICAS_PAQUETE SET  IND_ACTIVO = 0, ID_USUARIO_MODIFICA = " + usuario.getIdUsuario() 
		+ ", " + SET_CAMPO_FEC_BAJA + WHERE_ID_ORDEN_SERVICIO + idODS;
		log.info(str);
		return str;
	}
	public String cancelarDetalleCaracteristicasPaquete(Integer idODS, UsuarioDto usuario) {		
		String str = "UPDATE SVC_DETALLE_CARACTERISTICAS_PAQUETE SET  IND_ACTIVO = 0, ID_USUARIO_MODIFICA = " + usuario.getIdUsuario() + ", "
		+ SET_CAMPO_FEC_BAJA + " WHERE ID_CARACTERISTICAS_PAQUETE IN (SELECT ID_CARACTERISTICAS_PAQUETE FROM SVC_CARACTERISTICAS_PAQUETE " + WHERE_ID_ORDEN_SERVICIO + idODS + ")";
		log.info(str);
		return str;
	}
	public String cancelarCaracteristicasPresupuesto(Integer idODS, UsuarioDto usuario) {		
		String str = "UPDATE SVC_CARACTERISTICAS_PRESUPUESTO SET  IND_ACTIVO = 0, ID_USUARIO_MODIFICA = " + usuario.getIdUsuario() 
		+ "," + SET_CAMPO_FEC_BAJA + WHERE_ID_ORDEN_SERVICIO + idODS;
		log.info(str);
		return str;
	}
	public String cancelarDetalleCaracteristicasPresupuesto(Integer idODS, UsuarioDto usuario) {		
		String str = "UPDATE SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SET  IND_ACTIVO = 0, ID_USUARIO_MODIFICA = " + usuario.getIdUsuario() + ", "
		+ SET_CAMPO_FEC_BAJA
		+ " WHERE ID_CARACTERISTICAS_PRESUPUESTO IN (SELECT ID_CARACTERISTICAS_PRESUPUESTO  FROM SVC_CARACTERISTICAS_PRESUPUESTO " + WHERE_ID_ORDEN_SERVICIO + idODS + ") ";
		log.info(str);
		return str;
	}
	public String cancelarDonacion(Integer idODS, UsuarioDto usuario) {		
		String str = "UPDATE SVC_DONACION SET  IND_ACTIVO = 0, ID_USUARIO_MODIFICA = " + usuario.getIdUsuario() 
		+ ", " + SET_CAMPO_FEC_BAJA + WHERE_ID_ORDEN_SERVICIO + idODS ;
		log.info(str);
		return str;
	}
	public String cancelarInventarioArticulo(Integer idODS, UsuarioDto usuario) {		
		String str = "UPDATE SVT_INVENTARIO_ARTICULO SET  IND_ESTATUS = 0, ID_TIPO_ASIGNACION_ART = 1, ID_USUARIO_MODIFICA = " + usuario.getIdUsuario() + ", " + SET_CAMPO_FEC_BAJA
				+ " WHERE ID_INVE_ARTICULO IN (" + queryIdInvArticulo(idODS) +")";
		log.info(str);
		return str;
	}
	private String queryIdInvArticulo(Integer idODS) {
		return " SELECT sdcp.ID_INVE_ARTICULO FROM SVC_CARACTERISTICAS_PRESUPUESTO scp "
		+ JOIN + "  SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO sdcp ON sdcp.ID_CARACTERISTICAS_PRESUPUESTO = scp.ID_CARACTERISTICAS_PRESUPUESTO"
		+ JOIN + "  SVC_ORDEN_SERVICIO sos ON sos.ID_ORDEN_SERVICIO = scp.ID_ORDEN_SERVICIO "
		+ " WHERE sos.ID_ORDEN_SERVICIO  = " + idODS ;
	}
	// Bloque Generacion Reportes
	public String generaReporteConsultaODS(ReporteDto reporteDto) {
		String  condicion = " WHERE sos.ID_ESTATUS_ORDEN_SERVICIO IS NOT NULL ";
		if (reporteDto.getIdVelatorio() != null)
			condicion = condicion + " AND sv.ID_VELATORIO = " + reporteDto.getIdVelatorio();
		if (reporteDto.getIdOds() != null) 
			condicion = condicion + " AND sos.ID_ORDEN_SERVICIo = " + reporteDto.getIdOds();
		if (reporteDto.getIdContratante() != null) 
			condicion = condicion + " AND sc.ID_CONTRATANTE = " + reporteDto.getIdContratante();
		if (reporteDto.getIdFinado() != null) 
			condicion = condicion + AND_ID_FINADO + reporteDto.getIdFinado();
		if (reporteDto.getIdTipoODS() != null) 
			condicion = condicion + AND_ID_TIPO_ORDEN_SERVICIO + reporteDto.getIdTipoODS();
		if (reporteDto.getIdUnidadMedica() != null) 
			condicion = condicion + AND_ID_UNIDAD_MEDICA + reporteDto.getIdUnidadMedica();
		if (reporteDto.getIdConvenio() != null) 
			condicion = condicion + AND_ID_CONVENIO_PF + reporteDto.getIdConvenio();
		log.info(condicion);
		return condicion;
	}

}

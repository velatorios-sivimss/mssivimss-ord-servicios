package com.imss.sivimss.ordservicios.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetallePresupuestoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleTrasladoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaquetePresupuestoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteRequest;
import com.imss.sivimss.ordservicios.model.request.ContratanteRequest;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.request.FinadoRequest;
import com.imss.sivimss.ordservicios.model.request.InformacionServicioRequest;
import com.imss.sivimss.ordservicios.model.request.InformacionServicioVelacionRequest;
import com.imss.sivimss.ordservicios.util.QueryHelper;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

@Service
public class ReglasNegocioRepository {

	private static final String ID_USUARIO_ALTA = "ID_USUARIO_ALTA";

	private static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";

	private static final String FEC_ALTA = "FEC_ALTA";

	private static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";

	private static final String ID_DOMICILIO = "ID_DOMICILIO";

	private static final String IND_ACTIVO = "IND_ACTIVO";

	private static final String ID_ORDEN_SERVICIO = "ID_ORDEN_SERVICIO";

	private static final String CURRENT_DATE = "CURRENT_DATE()";
	
	@Value("${formato_fecha}")
	private String fecha;

	private static final Logger log = LoggerFactory.getLogger(ReglasNegocioRepository.class);

	String query;

	public String obtenerDatosContratanteRfc(String rfc) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil
				.select("SVC.ID_CONTRATANTE AS idContratante", "SPE.ID_PERSONA AS idPersona",
						"IFNULL(SPE.CVE_RFC,'') AS rfc", "IFNULL(SPE.CVE_CURP,'') AS curp",
						"IFNULL(SPE.CVE_NSS,'') AS nss", "IFNULL(SPE.NOM_PERSONA,'') AS nombre",
						"IFNULL(SPE.NOM_PRIMER_APELLIDO,'') AS primerApellido",
						"IFNULL(SPE.NOM_SEGUNDO_APELLIDO,'') AS segundoApellido", "IFNULL(SPE.NUM_SEXO,'') AS sexo",
						"IFNULL(SPE.REF_OTRO_SEXO,'') AS otroSexo", "IFNULL(SPE .FEC_NAC,'') AS fechaNac",
						"IFNULL(SVP.ID_PAIS,'') AS idPais", "IFNULL(SVE.ID_ESTADO,'') AS idEstado",
						"IFNULL(SPE.REF_TELEFONO,'') AS telefono", "IFNULL(SPE.REF_CORREO,'') AS correo",
						"IFNULL(SVC.ID_DOMICILIO,'') AS idDomicilio","IFNULL(SVD.REF_CALLE,'') AS calle", "IFNULL(SVD.NUM_EXTERIOR,'') AS numExterior",
						"IFNULL(SVD.NUM_INTERIOR,'') AS numInterior", "IFNULL(SVD.REF_CP,'') AS cp",
						"IFNULL(SVD.REF_COLONIA,'') AS colonia", "IFNULL(SVD.REF_MUNICIPIO,'') AS municipio",
						"IFNULL(SVD.REF_ESTADO,'') AS estado")
				.from("SVC_PERSONA SPE").leftJoin("SVC_PAIS SVP", "SPE.ID_PAIS = SVP.ID_PAIS")
				.leftJoin("SVC_ESTADO SVE", "SPE.ID_ESTADO = SVE.ID_ESTADO")
				.innerJoin("SVC_CONTRATANTE SVC", "SPE.ID_PERSONA = SVC.ID_PERSONA")
				.innerJoin("SVT_DOMICILIO SVD", "SVC.ID_DOMICILIO = SVD.ID_DOMICILIO").where("SPE.CVE_RFC = :rfc")
				.setParameter("rfc", rfc);
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerDatosContratanteCurp(String curp) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil
				.select("SVC.ID_CONTRATANTE AS idContratante", "SPE.ID_PERSONA AS idPersona",
						"IFNULL(SPE.CVE_RFC,'') AS rfc", "IFNULL(SPE.CVE_CURP,'') AS curp",
						"IFNULL(SPE.CVE_NSS,'') AS nss", "IFNULL(SPE.NOM_PERSONA,'') AS nombre",
						"IFNULL(SPE.NOM_PRIMER_APELLIDO,'') AS primerApellido",
						"IFNULL(SPE.NOM_SEGUNDO_APELLIDO,'') AS segundoApellido", "IFNULL(SPE.NUM_SEXO,'') AS sexo",
						"IFNULL(SPE.REF_OTRO_SEXO,'') AS otroSexo",  "IFNULL(SPE .FEC_NAC,'') AS fechaNac",
						"IFNULL(SVP.ID_PAIS,'') AS idPais", "IFNULL(SVE.ID_ESTADO,'') AS idEstado",
						"IFNULL(SPE.REF_TELEFONO,'') AS telefono", "IFNULL(SPE.REF_CORREO,'') AS correo",
						"IFNULL(SVC.ID_DOMICILIO,'') AS idDomicilio","IFNULL(SVD.REF_CALLE,'') AS calle", "IFNULL(SVD.NUM_EXTERIOR,'') AS numExterior",
						"IFNULL(SVD.NUM_INTERIOR,'') AS numInterior", "IFNULL(SVD.REF_CP,'') AS cp",
						"IFNULL(SVD.REF_COLONIA,'') AS colonia", "IFNULL(SVD.REF_MUNICIPIO,'') AS municipio",
						"IFNULL(SVD.REF_ESTADO,'') AS estado")
				.from("SVC_PERSONA SPE").leftJoin("SVC_PAIS SVP", "SPE.ID_PAIS = SVP.ID_PAIS")
				.leftJoin("SVC_ESTADO SVE", "SPE.ID_ESTADO = SVE.ID_ESTADO")
				.innerJoin("SVC_CONTRATANTE SVC", "SPE.ID_PERSONA = SVC.ID_PERSONA")
				.innerJoin("SVT_DOMICILIO SVD", "SVC.ID_DOMICILIO = SVD.ID_DOMICILIO").where("SPE.CVE_CURP = :curp")
				.setParameter("curp", curp);
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}

	// obtener folio
	public String obtenerFolio(Integer idVelatorio) {
		SelectQueryUtil velatorio = new SelectQueryUtil();
		velatorio.select("SUBSTRING(sv.DES_VELATORIO ,1,3)").from("SVC_VELATORIO sv")
				.where("sv.ID_VELATORIO = :idVelatorio").setParameter("idVelatorio", idVelatorio);

		SelectQueryUtil ordenServicio = new SelectQueryUtil();
		ordenServicio.select("COUNT(*)").from("SVC_ORDEN_SERVICIO").where("ID_VELATORIO = :idVelatorio")
				.and("ID_ESTATUS_ORDEN_SERVICIO not in (1)").and("CVE_FOLIO IS NOT NULL ").setParameter("idVelatorio", idVelatorio);

		SelectQueryUtil ordenServicioCount = new SelectQueryUtil();
		ordenServicioCount.select("COUNT(*)+1").from("SVC_ORDEN_SERVICIO").where("ID_VELATORIO = :idVelatorio")
				.and("ID_ESTATUS_ORDEN_SERVICIO not in (1)").and("CVE_FOLIO IS NOT NULL ").setParameter("idVelatorio", idVelatorio);

		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil
				.select("CONCAT((" + velatorio.build() + ")", "'-'", "LPAD((case when (" + ordenServicio.build()
						+ ") = 0 then 1 else (" + ordenServicioCount.build() + ") end)" + ",7,'0')" + ") as folio")
				.from("dual");
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}

	// insertar persona
	public String insertarPersona(Persona personaRequest, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_PERSONA");
		q.agregarParametroValues("CVE_RFC", setValor(personaRequest.getRfc()));
		q.agregarParametroValues("CVE_CURP", setValor(personaRequest.getCurp()));
		if (personaRequest.getNss()==null || personaRequest.getNss().equalsIgnoreCase("null")) {
			q.agregarParametroValues("CVE_NSS", "NULL");
		}else {
			q.agregarParametroValues("CVE_NSS", "'"+personaRequest.getNss()+"'");
		}
		
		q.agregarParametroValues("NOM_PERSONA", setValor(personaRequest.getNomPersona()));
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", setValor(personaRequest.getPrimerApellido()));
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", setValor(personaRequest.getSegundoApellido()));
		q.agregarParametroValues("NUM_SEXO", "" + setValor(personaRequest.getSexo()));
		q.agregarParametroValues("REF_OTRO_SEXO", setValor(personaRequest.getOtroSexo()));
		q.agregarParametroValues("FEC_NAC", setValor(personaRequest.getFechaNac()));
		q.agregarParametroValues("ID_PAIS", setValor(personaRequest.getIdPais()));
		q.agregarParametroValues("ID_ESTADO", setValor(personaRequest.getIdEstado()));
		q.agregarParametroValues("DES_TELEFONO", setValor(personaRequest.getTelefono()));
		q.agregarParametroValues("DES_CORREO", setValor(personaRequest.getCorreo()));
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar contratante
	public String insertarContratante(ContratanteRequest contratanteRequest, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", "" + contratanteRequest.getIdPersona() + "");
		q.agregarParametroValues("CVE_MATRICULA", setValor(contratanteRequest.getMatricula()));
		q.agregarParametroValues(ID_DOMICILIO, "" + contratanteRequest.getCp().getIdDomicilio() + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	public String insertarDomicilio(DomicilioRequest domicilioRequest, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("REF_CALLE", setValor(domicilioRequest.getDesCalle()));
		q.agregarParametroValues("NUM_EXTERIOR", setValor(domicilioRequest.getNumExterior()));
		q.agregarParametroValues("NUM_INTERIOR", setValor(domicilioRequest.getNumInterior()));
		q.agregarParametroValues("REF_CP", setValor(domicilioRequest.getCodigoPostal()));
		q.agregarParametroValues("REF_COLONIA", setValor( domicilioRequest.getDesColonia()));
		q.agregarParametroValues("REF_MUNICIPIO", setValor( domicilioRequest.getDesMunicipio()));
		q.agregarParametroValues("REF_ESTADO", setValor( domicilioRequest.getDesEstado()));
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar orden de servicio
	public String insertarOrdenServicio(String folio, Integer idContratante, Integer idParentesco, Integer idVelatorio,
			Integer idContratantePf, Integer idEstatus, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_ORDEN_SERVICIO");
		q.agregarParametroValues("CVE_FOLIO", setValor( folio ));
		q.agregarParametroValues("ID_CONTRATANTE", "" + idContratante + "");
		q.agregarParametroValues("ID_PARENTESCO", "" + idParentesco + "");
		q.agregarParametroValues("ID_VELATORIO", "" + idVelatorio + "");
		q.agregarParametroValues("ID_CONTRATANTE_PF", "" + idContratantePf + "");
		q.agregarParametroValues("ID_ESTATUS_ORDEN_SERVICIO", "" + idEstatus + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar finado
	public String insertarFinado(FinadoRequest finadoRequest, Integer idOrdenServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_FINADO");
		if (finadoRequest.getIdPersona()!=null) {
			q.agregarParametroValues("ID_PERSONA", "" + finadoRequest.getIdPersona() + "");
		}else {
			q.agregarParametroValues("ID_PERSONA", "NULL");
		}
		
		q.agregarParametroValues("ID_TIPO_ORDEN", "" + finadoRequest.getIdTipoOrden() + "");
		q.agregarParametroValues("REF_EXTREMIDAD", setValor( finadoRequest.getExtremidad()));
		q.agregarParametroValues("REF_OBITO", setValor( finadoRequest.getEsobito()));
		q.agregarParametroValues("CVE_MATRICULA", setValor( finadoRequest.getMatricula()));
		q.agregarParametroValues("ID_CONTRATO_PREVISION", "" + finadoRequest.getIdContratoPrevision() + "");
		q.agregarParametroValues("ID_VELATORIO", "" + finadoRequest.getIdVelatorioContratoPrevision() + "");
		if (finadoRequest.getCp()==null) {
			q.agregarParametroValues(ID_DOMICILIO, "NULL");
		}else {
		    q.agregarParametroValues(ID_DOMICILIO, "" + finadoRequest.getCp().getIdDomicilio() + "");
		}
		
		q.agregarParametroValues("FEC_DECESO", setValor(finadoRequest.getFechaDeceso()));
		q.agregarParametroValues("REF_CAUSA_DECESO", setValor( finadoRequest.getCausaDeceso()));
		q.agregarParametroValues("REF_LUGAR_DECESO", setValor( finadoRequest.getLugarDeceso()));
		q.agregarParametroValues("TIM_HORA", setValor(finadoRequest.getHora()));
		q.agregarParametroValues("ID_CLINICA_ADSCRIPCION", "" + finadoRequest.getIdClinicaAdscripcion() + "");
		q.agregarParametroValues("ID_UNIDAD_PROCEDENCIA", "" + finadoRequest.getIdUnidadProcedencia() + "");
		q.agregarParametroValues("REF_PROCEDENCIA_FINADO", setValor( finadoRequest.getProcedenciaFinado()));
		q.agregarParametroValues("ID_TIPO_PENSION", "" + finadoRequest.getIdTipoPension() + "");
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}
	
	// insertar finado pa
	public String insertarFinadoPa(FinadoRequest finadoRequest, Integer idOrdenServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_FINADO");
		if (finadoRequest.getIdPersona()!=null) {
			q.agregarParametroValues("ID_PERSONA", "" + finadoRequest.getIdPersona() + "");
		}else {
			q.agregarParametroValues("ID_PERSONA", "NULL");
		}
		
		q.agregarParametroValues("ID_TIPO_ORDEN", "" + finadoRequest.getIdTipoOrden() + "");
		q.agregarParametroValues("CVE_MATRICULA", setValor( finadoRequest.getMatricula()));
		q.agregarParametroValues("ID_CONTRATO_PREVISION_PA", "" + finadoRequest.getIdContratoPrevision() + "");
		q.agregarParametroValues("ID_VELATORIO", "" + finadoRequest.getIdVelatorioContratoPrevision() + "");
		if (finadoRequest.getCp()==null) {
			q.agregarParametroValues(ID_DOMICILIO, "NULL");
		}else {
		    q.agregarParametroValues(ID_DOMICILIO, "" + finadoRequest.getCp().getIdDomicilio() + "");
		}
		
		q.agregarParametroValues("FEC_DECESO", setValor(finadoRequest.getFechaDeceso()));
		q.agregarParametroValues("REF_CAUSA_DECESO", setValor( finadoRequest.getCausaDeceso()));
		q.agregarParametroValues("REF_LUGAR_DECESO", setValor( finadoRequest.getLugarDeceso()));
		q.agregarParametroValues("TIM_HORA", setValor(finadoRequest.getHora()));
		q.agregarParametroValues("ID_CLINICA_ADSCRIPCION", "" + finadoRequest.getIdClinicaAdscripcion() + "");
		q.agregarParametroValues("ID_UNIDAD_PROCEDENCIA", "" + finadoRequest.getIdUnidadProcedencia() + "");
		q.agregarParametroValues("REF_PROCEDENCIA_FINADO", setValor( finadoRequest.getProcedenciaFinado()));
		q.agregarParametroValues("ID_TIPO_PENSION", "" + finadoRequest.getIdTipoPension() + "");
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar finado venta de articulo
	public String insertarFinadoVentaArticulo(FinadoRequest finadoRequest, Integer idOrdenServicio,
			Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_FINADO");
		q.agregarParametroValues("ID_TIPO_ORDEN", "" + finadoRequest.getIdTipoOrden() + "");
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar caracteristicas paquete
	// SVC_CARACTERISTICAS_PAQUETE_TEMP/SVC_CARACTERISTICAS_PAQUETE
	public String insertarCaracteristicasPaquete(String from,
			CaracteristicasPaqueteRequest caracteristicasPaqueteRequest, Integer idOrdenServicio,
			Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO " + from);
		q.agregarParametroValues("ID_PAQUETE", "" + caracteristicasPaqueteRequest.getIdPaquete() + "");
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(IND_ACTIVO, "1");
		if (caracteristicasPaqueteRequest.getOtorgamiento()==null) {
			q.agregarParametroValues("IND_OTORGAMIENTO",
					"NULL");
		}else {
			q.agregarParametroValues("IND_OTORGAMIENTO",
				"" + Integer.parseInt(caracteristicasPaqueteRequest.getOtorgamiento()) + "");
		}
		
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar detalle caracteristicas paquete
	// SVC_DETALLE_CARACTERISTICAS_PAQUETE_TEMP/SVC_DETALLE_CARACTERISTICAS_PAQUETE
	public String insertarDetalleCaracteristicasPaquete(String from,
			CaracteristicasPaqueteDetalleRequest detalleCaracteristicasPaqueteRequest, Integer idCaracteristicasPaquete,
			Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO " + from);
		q.agregarParametroValues("ID_ARTICULO", "" + detalleCaracteristicasPaqueteRequest.getIdArticulo() + "");
		q.agregarParametroValues("ID_SERVICIO", "" + detalleCaracteristicasPaqueteRequest.getIdServicio() + "");
		q.agregarParametroValues("DES_MOTIVO", setValor( detalleCaracteristicasPaqueteRequest.getDesmotivo()));
		q.agregarParametroValues(IND_ACTIVO, "" + detalleCaracteristicasPaqueteRequest.getActivo() + "");
		q.agregarParametroValues("CAN_CARAC_PAQ", "" + detalleCaracteristicasPaqueteRequest.getCantidad() + "");
		q.agregarParametroValues("IMP_CARAC_PAQ", "" + detalleCaracteristicasPaqueteRequest.getImporteMonto() + "");
		q.agregarParametroValues("ID_PROVEEDOR", "" + detalleCaracteristicasPaqueteRequest.getIdProveedor() + "");
		q.agregarParametroValues("ID_CARAC_PAQUETE", "" + idCaracteristicasPaquete + "");
		q.agregarParametroValues("ID_CATEGORIA_PAQUETE", "" + detalleCaracteristicasPaqueteRequest.getIdCategoriaPaquete() + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar detalle caracteristicas paquete traslado
	// SVC_CARACTERISTICA_PAQUETE_TRASLADO_TEMP/ SVC_CARACTERISTICA_PAQUETE_TRASLADO
	public String insertarDetalleCaracteristicasPaqueteTraslado(String from,
			CaracteristicasPaqueteDetalleTrasladoRequest detalleCaracteristicasPaqueteTrasladoRequest,
			Integer idDetalleCaracteristicasPaquete, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO " + from);
		q.agregarParametroValues("REF_ORIGEN", setValor( detalleCaracteristicasPaqueteTrasladoRequest.getOrigen()));
		q.agregarParametroValues("REF_DESTINO", setValor( detalleCaracteristicasPaqueteTrasladoRequest.getDestino()));
		q.agregarParametroValues("NUM_LATITUD_INICIAL",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getLatitudInicial() + "");
		q.agregarParametroValues("NUM_LATITUD_FINAL",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getLatitudFinal() + "");
		q.agregarParametroValues("NUM_LONGITUD_INICIAL",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getLongitudInicial() + "");
		q.agregarParametroValues("NUM_LONGITUD_FINAL",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getLongitudFinal() + "");
		q.agregarParametroValues("CAN_TOTAL_KILOMETROS",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getTotalKilometros());
		q.agregarParametroValues("ID_DETALLE_CARACTERISTICAS", "" + idDetalleCaracteristicasPaquete + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar caracteristicas presupuesto SVC_CARACTERISTICAS_PRESUPUESTO_TEMP/
	// SVC_CARACTERISTICAS_PRESUPUESTO
	public String insertarCaracteristicasPresupuesto(String from,
			CaracteristicasPaquetePresupuestoRequest caracteristicasPaquetePresupuestoRequest, Integer idOrdenServicio,
			Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO " + from);
		q.agregarParametroValues("ID_PAQUETE", "" + caracteristicasPaquetePresupuestoRequest.getIdPaquete() + "");
		q.agregarParametroValues("CAN_PRESUPUESTO",
				"" + caracteristicasPaquetePresupuestoRequest.getTotalPresupuesto() + "");
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(IND_ACTIVO, "1");
		q.agregarParametroValues("REF_OBSERVACIONES",
				setValor( caracteristicasPaquetePresupuestoRequest.getObservaciones()));
		q.agregarParametroValues("REF_NOTAS_SERVICIO",
				setValor( caracteristicasPaquetePresupuestoRequest.getNotasServicio()));
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar detalle caracteristicas presupuesto
	// SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP/
	// SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO
	public String insertarDetalleCaracteristicasPresupuesto(String from,
			CaracteristicasPaqueteDetallePresupuestoRequest caracteristicasPaqueteRespuestoRequest,
			Integer idCaracteristicasPaquetePresupuesto, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO " + from);
		q.agregarParametroValues("ID_ARTICULO", "" + caracteristicasPaqueteRespuestoRequest.getIdArticulo() + "");
		q.agregarParametroValues("ID_INVE_ARTICULO",
				"" + caracteristicasPaqueteRespuestoRequest.getIdInventario() + "");
		q.agregarParametroValues("ID_SERVICIO", "" + caracteristicasPaqueteRespuestoRequest.getIdServicio() + "");
		q.agregarParametroValues(IND_ACTIVO, "1");
		q.agregarParametroValues("CAN_DET_PRESUP", "" + caracteristicasPaqueteRespuestoRequest.getCantidad() + "");
		q.agregarParametroValues("IMP_CARAC_PRESUP", "" + caracteristicasPaqueteRespuestoRequest.getImporteMonto() + "");
		q.agregarParametroValues("ID_PROVEEDOR", "" + caracteristicasPaqueteRespuestoRequest.getIdProveedor() + "");
		q.agregarParametroValues("ID_CARAC_PRESUPUESTO", "" + idCaracteristicasPaquetePresupuesto + "");
		q.agregarParametroValues("DES_PROVIENE", "'" + caracteristicasPaqueteRespuestoRequest.getProviene() + "'");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar detalle caracteristicas presupuesto traslado
	// SVC_CARACTERISTICA_PRESUPUESTO_TRASLADO_TEMP/
	// SVC_CARACTERISTICA_PRESUPUESTO_TRASLADO
	public String insertarDetalleCaracteristicasPresupuestoTraslado(String from,
			CaracteristicasPaqueteDetalleTrasladoRequest detalleCaracteristicasPresupuestoTrasladoRequest,
			Integer idDetalleCaracteristicasPaquete, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO " + from);
		q.agregarParametroValues("REF_ORIGEN",
				setValor( detalleCaracteristicasPresupuestoTrasladoRequest.getOrigen()));
		q.agregarParametroValues("REF_DESTINO",
				setValor( detalleCaracteristicasPresupuestoTrasladoRequest.getDestino()));
		q.agregarParametroValues("NUM_LATITUD_INICIAL",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getLatitudInicial() + "");
		q.agregarParametroValues("NUM_LATITUD_FINAL",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getLatitudFinal() + "");
		q.agregarParametroValues("NUM_LONGITUD_INICIAL",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getLongitudInicial() + "");
		q.agregarParametroValues("NUM_LONGITUD_FINAL",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getLongitudFinal() + "");
		q.agregarParametroValues("CAN_TOTAL_KILOMETROS",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getTotalKilometros());
		q.agregarParametroValues("ID_DETALLE_CARACTERISTICAS", "" + idDetalleCaracteristicasPaquete + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar informacion servicio
	public String insertarInformacionServicio(InformacionServicioRequest informacionServicioRequest,
			Integer idOrdenServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_INFORMACION_SERVICIO");
		q.agregarParametroValues("FEC_CORTEJO", setValor(informacionServicioRequest.getFechaCortejo()));
		q.agregarParametroValues("TIM_HORA_CORTEJO", setValor(informacionServicioRequest.getHoraCortejo()));
		q.agregarParametroValues("FEC_RECOGER", setValor(informacionServicioRequest.getFechaRecoger()));
		q.agregarParametroValues("TIM_HORA_RECOGER", setValor(informacionServicioRequest.getHoraRecoger()));
		q.agregarParametroValues("ID_PANTEON", "" + informacionServicioRequest.getIdPanteon() + "");
		q.agregarParametroValues("ID_SALA", "" + informacionServicioRequest.getIdSala() + "");
		q.agregarParametroValues("FEC_CREMACION", setValor(informacionServicioRequest.getFechaCremacion()) );
		q.agregarParametroValues("TIM_HORA_CREMACION", setValor(informacionServicioRequest.getHoraCremacion()));
		q.agregarParametroValues("ID_PROMOTORES", "" + informacionServicioRequest.getIdPromotor());
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar informacion servicio velacion
	public String insertarInformacionServicioVelacion(InformacionServicioVelacionRequest informacionServicioRequest,
			Integer idInformacionServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_INF_SERVICIO_VELACION");
		
		if (informacionServicioRequest.getCp()==null) {
			q.agregarParametroValues(ID_DOMICILIO, "NULL");
		}else {
			q.agregarParametroValues(ID_DOMICILIO, "" + informacionServicioRequest.getCp().getIdDomicilio() + "");
		}
		
		q.agregarParametroValues("FEC_INSTALACION", setValor(informacionServicioRequest.getFechaInstalacion()));
		q.agregarParametroValues("TIM_HORA_INSTALACION", setValor(informacionServicioRequest.getHoraInstalacion()));
		q.agregarParametroValues("FEC_VELACION", setValor(informacionServicioRequest.getFechaVelacion()));
		q.agregarParametroValues("TIM_HORA_VELACION", setValor(informacionServicioRequest.getHoraVelacion()));
		q.agregarParametroValues("ID_CAPILLA", "" + informacionServicioRequest.getIdCapilla() + "");
		q.agregarParametroValues("ID_INFORMACION_SERVICIO", "" + idInformacionServicio);
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar ataud donado temp
	public String insertarAtaudDonadoTemp(Integer ordenServicio, Integer idInventario, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_DONACION_ORDEN_SERV_TEMP");
		q.agregarParametroValues("ID_ORDEN_SERVICIO", "" + ordenServicio + "");
		q.agregarParametroValues("ID_INVE_ARTICULO", "" + idInventario + "");
		q.agregarParametroValues(IND_ACTIVO, "1");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar ataud donado
	public String insertarDonacion(Integer ordenServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_DONACION");
		q.agregarParametroValues("ID_ORDEN_SERVICIO", "" + ordenServicio + "");
		q.agregarParametroValues("NUM_TOTAL_ATAUDES", "1");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar ataud donado
	public String insertarAtaudDonado(Integer donacion, Integer idInventario, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_ATAUDES_DONADOS");
		q.agregarParametroValues("ID_DONACION", "" + donacion + "");
		q.agregarParametroValues("ID_INVE_ARTICULO", "" + idInventario + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}
	
	// insertar SVC_SALIDA_DONACION/SVC_SALIDA_DONACION_TEMP
	public String insertarSalidaDonacion(String from,Integer idOrdenSServcio, Integer cantidad, String otorgamiento, Integer idContratante, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO "+from);
		q.agregarParametroValues("ID_ORDEN_SERVICIO", "" + idOrdenSServcio + "");
		q.agregarParametroValues("NUM_TOTAL_ATAUDES", "" + cantidad + "");
		if (otorgamiento!=null) {
			
			Integer indOtorgamiento=Integer.parseInt(otorgamiento);
			if (indOtorgamiento==1) {
				q.agregarParametroValues("IND_ESTUDIO_SOCIECONOMICO", "1");
			}else if(indOtorgamiento==2) {
				q.agregarParametroValues("IND_ESTUDIO_LIBRE", "1");

			}
		}
		
		q.agregarParametroValues("FEC_SOLICITUD",CURRENT_DATE);
		q.agregarParametroValues("ID_CONTRATANTE", "" + idContratante + "");
		q.agregarParametroValues(IND_ACTIVO, "1");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues("FEC_SOLICITUD", CURRENT_DATE);
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}
	
	// insertar SVC_SALIDA_DONACION_ATAUDES/SVC_SALIDA_DON_ATAUDES_TEMP
	public String insertarSalidaDonacionAtaud(String from, Integer idSalidaDonacion, Integer idInventario, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO "+from);
		q.agregarParametroValues("ID_SALIDA_DONACION", "" + idSalidaDonacion + "");
		q.agregarParametroValues("ID_INVE_ARTICULO", "" + idInventario + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}
	
	// insertar SVC_SALIDA_DONACION_FINADOS/SVC_SALIDA_DON_FINADOS_TEMP
	public String insertarSalidaDonacionFinado(String from, String nombreFinado, String primerApellidoFinado, String segundoApellidoFinado, Integer idSalidaDonacion, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO "+from);
		q.agregarParametroValues("NOM_FINADO", setValor(nombreFinado));
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", setValor(primerApellidoFinado));
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", setValor(segundoApellidoFinado));
		q.agregarParametroValues("ID_SALIDA_DONACION", "" + idSalidaDonacion + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}
	

	// actualizar inventario ataud
	public String actualizarAtaudTipoAsignacion(Integer idInventarioArticulo, Integer idAsignacion,
			Integer idUsuarioModifica, Integer estatus) {
		final QueryHelper q = new QueryHelper("UPDATE SVT_INVENTARIO_ARTICULO ");
		q.agregarParametroValues("ID_TIPO_ASIGNACION_ART", "" + idAsignacion + "");
		q.agregarParametroValues("IND_ESTATUS", "" + estatus + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioModifica + "");
		q.addWhere("ID_INVE_ARTICULO = " + idInventarioArticulo);
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	// actualizar CveTarea
	public String actualizarCveTarea(Integer ordenServicio, String cveTarea) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_ORDEN_SERVICIO ");
		q.agregarParametroValues("CVE_TAREA", "'" + cveTarea + "'");
		q.addWhere("ID_ORDEN_SERVICIO = " + ordenServicio);
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}
	
	// consultar tipo de asignacion ataud
	public String consultarAsignacionInventario(Integer idInventario) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("ID_TIPO_ASIGNACION_ART AS idAsignacion").from("SVT_INVENTARIO_ARTICULO ")
					.where("ID_INVE_ARTICULO = " + idInventario);
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}

	// consultar orden de servicio
	public String consultarOrdenServicio(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("STO.ID_ORDEN_SERVICIO AS idOrdenServicio", "STO.CVE_FOLIO AS folio",
				"IFNULL(CONCAT(SP.NOM_PERSONA,' ',SP.NOM_PRIMER_APELLIDO,' ',SP.NOM_SEGUNDO_APELLIDO),'') AS contratante",
				"IFNULL(CONCAT(SP2.NOM_PERSONA,' ',SP2.NOM_PRIMER_APELLIDO,' ',SP2.NOM_SEGUNDO_APELLIDO),'') AS finado",
				"STO.ID_ESTATUS_ORDEN_SERVICIO AS idEstatus",
				"STF.ID_TIPO_ORDEN AS idTipoOrdenServicio"
				).from("SVC_ORDEN_SERVICIO STO")
				.leftJoin("SVC_CONTRATANTE SC", "STO.ID_CONTRATANTE =SC.ID_CONTRATANTE")
				.leftJoin("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
				.leftJoin("SVC_FINADO STF", "STO.ID_ORDEN_SERVICIO = STF.ID_ORDEN_SERVICIO")
				.leftJoin("SVC_PERSONA SP2", "STF.ID_PERSONA =SP2.ID_PERSONA")
				.where("STO.ID_ORDEN_SERVICIO = " + idOrdenServicio);
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}

	
	// actualizar inventario ataud
	public String actualizarEstatusAtaud(Integer idInventarioArticulo, Integer idUsuarioModifica, Integer estatus) {
		final QueryHelper q = new QueryHelper("UPDATE SVT_INVENTARIO_ARTICULO ");
		q.agregarParametroValues("IND_ESTATUS", "" + estatus + "");
		q.agregarParametroValues("FEC_ACTUALIZACION", CURRENT_DATE);
		q.agregarParametroValues("ID_USUARIO_MODIFICA", "" + idUsuarioModifica + "");
		q.addWhere("ID_INVE_ARTICULO = " + idInventarioArticulo);
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	// insertar bitacora pago
	public String insertarBitacoraPago(Integer idRegistro, Integer idVelatorio, String contratante, String folio,
			String valor, Integer estatus, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_PAGO_BITACORA");
		q.agregarParametroValues("ID_REGISTRO", "" + idRegistro + "");
		q.agregarParametroValues("ID_FLUJO_PAGOS", "1");
		q.agregarParametroValues("ID_VELATORIO", "" + idVelatorio + "");
		q.agregarParametroValues("FEC_ODS", CURRENT_DATE);
		q.agregarParametroValues("NOM_CONTRATANTE", "'" + contratante + "'");
		q.agregarParametroValues("CVE_FOLIO", "'" + folio + "'");
		q.agregarParametroValues("IMP_VALOR", "'" + valor + "'");
		q.agregarParametroValues("CVE_ESTATUS_PAGO", "" + estatus + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_DATE);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	////////////////////////////////// desactivar
	////////////////////////////////// temporales//////////////////////////////////

	public String actualizarCaracteristicasPaqueteTemporal(Integer idOrden) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_CARAC_PAQUETE_TEMP ");
		q.agregarParametroValues("IND_ACTIVO", "0");
		q.addWhere("ID_ORDEN_SERVICIO = " + idOrden);
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	public String actualizarCaracteristicasPaqueteDetalleTemp(Integer idOrden) {

		query = " UPDATE SVC_DETALLE_CARAC_PAQ_TEMP SET IND_ACTIVO = 0"
				+ " WHERE ID_CARAC_PAQUETE IN " + " (SELECT DISTINCT ID_CARAC_PAQUETE "
				+ " FROM SVC_CARAC_PAQUETE_TEMP" + " WHERE ID_ORDEN_SERVICIO =" + idOrden + ")";
		log.info(query);
		return query;

	}

	public String actualizarCaracteristicasPresupuestoTemporal(Integer idOrden) {
		final QueryHelper queryHelper = new QueryHelper("UPDATE SVC_CARAC_PRESUP_TEMP ");
		queryHelper.agregarParametroValues("IND_ACTIVO", "0");
		queryHelper.addWhere(" ID_ORDEN_SERVICIO = " + idOrden);

		query = queryHelper.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	public String actualizarCaracteristicasPresuestoDetalleTemp(Integer idOrden) {

		query = " UPDATE SVC_DETALLE_CARAC_PRESUP_TEMP SET IND_ACTIVO = 0"
				+ " WHERE ID_CARAC_PRESUPUESTO IN " + " (SELECT DISTINCT ID_CARAC_PRESUPUESTO "
				+ " FROM SVC_CARAC_PRESUP_TEMP" + " WHERE ID_ORDEN_SERVICIO =" + idOrden + ")";
		log.info(query);
		return query;

	}

	public String actualizarDonacionTemporal(Integer idOrden) {
		final QueryHelper queryHelper = new QueryHelper("UPDATE SVC_DONACION_ORDEN_SERV_TEMP ");
		queryHelper.agregarParametroValues("IND_ACTIVO", "0");
		queryHelper.addWhere(" ID_ORDEN_SERVICIO = " + idOrden);

		query = queryHelper.obtenerQueryActualizar();
		log.info(query);
		return query;
	}
	
	public String actualizarSalidaDonacionTemporal(Integer idSalida) {
		final QueryHelper queryHelper = new QueryHelper("UPDATE SVC_SALIDA_DONACION_TEMP ");
		queryHelper.agregarParametroValues("IND_ACTIVO", "0");
		queryHelper.addWhere(" ID_SALIDA_DONACION = " + idSalida);

		query = queryHelper.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	// actualizar EstatusOrden
	public String actualizarEstatusOrden(Integer idOrdenServicio) {

		query = "UPDATE SVC_ORDEN_SERVICIO  SET ID_CONTRATANTE_PF = NULL, "
				+ "ID_ESTATUS_ORDEN_SERVICIO = NULL WHERE ID_ORDEN_SERVICIO =" + idOrdenServicio + ";";
		log.info(query);
		return query;
	}
    // actualizar persona
	public String actualizarPersona(Persona personaRequest, Integer idUsuario) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_PERSONA ");
		q.agregarParametroValues("CVE_RFC", setValor(personaRequest.getRfc()));
		q.agregarParametroValues("CVE_CURP", setValor(personaRequest.getCurp()));
		if (personaRequest.getNss()==null || personaRequest.getNss().equalsIgnoreCase("null")) {
			q.agregarParametroValues("CVE_NSS", "NULL");
		}else {
			q.agregarParametroValues("CVE_NSS", "'"+personaRequest.getNss()+"'");
		}
		q.agregarParametroValues("NOM_PERSONA", "'" + personaRequest.getNomPersona() + "'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" + personaRequest.getPrimerApellido() + "'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" + personaRequest.getSegundoApellido() + "'");
		q.agregarParametroValues("NUM_SEXO", "" + personaRequest.getSexo() + "");
		q.agregarParametroValues("REF_OTRO_SEXO", setValor(personaRequest.getOtroSexo()));
		q.agregarParametroValues("FEC_NAC", "'" + personaRequest.getFechaNac() + "'");
		q.agregarParametroValues("ID_PAIS", "" + personaRequest.getIdPais() + "");
		q.agregarParametroValues("ID_ESTADO", "" + personaRequest.getIdEstado() + "");
		q.agregarParametroValues("DES_TELEFONO", "'" + personaRequest.getTelefono() + "'");
		q.agregarParametroValues("DES_CORREO", "'" + personaRequest.getCorreo() + "'");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuario + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
		q.addWhere("ID_PERSONA = " + personaRequest.getIdPersona());
		query = q.obtenerQueryActualizarSinCoalesce();
		log.info(query);
		return query;
	}

	// actualizar domicilio
	public String actualizarDomicilio(DomicilioRequest domicilioRequest, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVT_DOMICILIO ");
		q.agregarParametroValues("REF_CALLE", "'" + domicilioRequest.getDesCalle() + "'");
		q.agregarParametroValues("NUM_EXTERIOR", setValor(domicilioRequest.getNumExterior()));
		q.agregarParametroValues("NUM_INTERIOR",  setValor(domicilioRequest.getNumInterior()));
		q.agregarParametroValues("REF_CP", "'" + domicilioRequest.getCodigoPostal() + "'");
		q.agregarParametroValues("REF_COLONIA", "'" + domicilioRequest.getDesColonia() + "'");
		q.agregarParametroValues("REF_MUNICIPIO", "'" + domicilioRequest.getDesMunicipio() + "'");
		q.agregarParametroValues("REF_ESTADO", "'" + domicilioRequest.getDesEstado() + "'");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
		q.addWhere("ID_DOMICILIO = "+domicilioRequest.getIdDomicilio());
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}
	
	// actualizar orden de servicio
	public String actualizarOrdenServicio(Integer idOrdenServicio,String folio, Integer idParentesco, Integer idContratantePf,
			Integer idEstatus, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_ORDEN_SERVICIO ");
		q.agregarParametroValues("CVE_FOLIO", setValor(folio));
		q.agregarParametroValues("ID_PARENTESCO", "" + idParentesco + "");
		q.agregarParametroValues("ID_CONTRATANTE_PF", "" + idContratantePf + "");
		q.agregarParametroValues("ID_ESTATUS_ORDEN_SERVICIO", "" + idEstatus + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
		q.addWhere("ID_ORDEN_SERVICIO="+idOrdenServicio);

		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	// actualizar finado
	public String actualizarFinado(FinadoRequest finadoRequest,Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_FINADO ");
		q.agregarParametroValues("ID_PERSONA", "" + finadoRequest.getIdPersona() + "");
		q.agregarParametroValues("ID_TIPO_ORDEN", "" + finadoRequest.getIdTipoOrden() + "");
		q.agregarParametroValues("REF_EXTREMIDAD", setValor(finadoRequest.getExtremidad() ));
		q.agregarParametroValues("REF_OBITO", setValor(finadoRequest.getEsobito()));
		q.agregarParametroValues("CVE_MATRICULA", setValor(finadoRequest.getMatricula()));
		q.agregarParametroValues("ID_CONTRATO_PREVISION", "" + finadoRequest.getIdContratoPrevision() + "");
		q.agregarParametroValues("ID_VELATORIO", "" + finadoRequest.getIdVelatorioContratoPrevision() + "");
		if (finadoRequest.getCp()==null) {
			q.agregarParametroValues(ID_DOMICILIO, "NULL");
		}else {
			q.agregarParametroValues(ID_DOMICILIO, "" +finadoRequest.getCp().getIdDomicilio() + "");
		}
		q.agregarParametroValues("FEC_DECESO", setValor(finadoRequest.getFechaDeceso()));
		q.agregarParametroValues("REF_CAUSA_DECESO",setValor(finadoRequest.getCausaDeceso()));
		q.agregarParametroValues("REF_LUGAR_DECESO", setValor(finadoRequest.getLugarDeceso()));
		q.agregarParametroValues("TIM_HORA", setValor(finadoRequest.getHora()));
		q.agregarParametroValues("ID_CLINICA_ADSCRIPCION", "" + finadoRequest.getIdClinicaAdscripcion() + "");
		q.agregarParametroValues("ID_UNIDAD_PROCEDENCIA", "" + finadoRequest.getIdUnidadProcedencia() + "");
		q.agregarParametroValues("REF_PROCEDENCIA_FINADO", setValor(finadoRequest.getProcedenciaFinado()));
		q.agregarParametroValues("ID_TIPO_PENSION", "" + finadoRequest.getIdTipoPension() + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
		q.addWhere("ID_FINADO="+finadoRequest.getIdFinado());

		query = q.obtenerQueryActualizarSinCoalesce();
		log.info(query);
		return query;
	}
	
	
	// actualizar informacion servicio
	public String actualizarInformacionServicio(InformacionServicioRequest informacionServicioRequest,
			Integer idOrdenServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_INFORMACION_SERVICIO ");
		q.agregarParametroValues("FEC_CORTEJO", setValor(informacionServicioRequest.getFechaCortejo()));
		q.agregarParametroValues("TIM_HORA_CORTEJO", setValor(informacionServicioRequest.getHoraCortejo()));
		q.agregarParametroValues("FEC_RECOGER", setValor(informacionServicioRequest.getFechaRecoger() ));
		q.agregarParametroValues("TIM_HORA_RECOGER", setValor(informacionServicioRequest.getHoraRecoger()));
		q.agregarParametroValues("ID_PANTEON", "" + informacionServicioRequest.getIdPanteon() + "");
		q.agregarParametroValues("ID_SALA", "" + informacionServicioRequest.getIdSala() + "");
		q.agregarParametroValues("FEC_CREMACION", setValor(informacionServicioRequest.getFechaCremacion()));
		q.agregarParametroValues("TIM_HORA_CREMACION", setValor(informacionServicioRequest.getHoraCremacion() ));
		q.agregarParametroValues("ID_PROMOTORES", "" + informacionServicioRequest.getIdPromotor());
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
		q.addWhere("ID_INFORMACION_SERVICIO="+informacionServicioRequest.getIdInformacionServicio());
		query = q.obtenerQueryActualizarSinCoalesce();
		log.info(query);
		return query;
	}
	
	// actualizar informacion servicio
	public String desactivarInformacionServicio(Integer idOrdenServicio, Integer idUsuarioAlta) {
			final QueryHelper q = new QueryHelper("UPDATE SVC_INFORMACION_SERVICIO ");
			q.agregarParametroValues(IND_ACTIVO, "0");
			q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
			q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
			q.addWhere(ID_ORDEN_SERVICIO+"="+idOrdenServicio);
			query = q.obtenerQueryActualizar();
			log.info(query);
			return query;
	}
	
	// actualizar salida donacion
	public String desactivarSalidaDonacionTemp(Integer idOrdenServicio, Integer idUsuarioAlta) {
				final QueryHelper q = new QueryHelper("UPDATE SVC_SALIDA_DONACION_TEMP ");
				q.agregarParametroValues(IND_ACTIVO, "0");
				q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
				q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
				q.addWhere(ID_ORDEN_SERVICIO+"="+idOrdenServicio);
				query = q.obtenerQueryActualizar();
				log.info(query);
				return query;
	}
	
	// actualizar informacion servicio velacion
	public String actualizarInformacionServicioVelacion(InformacionServicioVelacionRequest informacionServicioRequest,
			Integer idInformacionServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_INF_SERVICIO_VELACION ");
		q.agregarParametroValues(ID_DOMICILIO, "" + informacionServicioRequest.getCp().getIdDomicilio() + "");
		q.agregarParametroValues("FEC_INSTALACION", "'" + informacionServicioRequest.getFechaInstalacion() + "'");
		q.agregarParametroValues("TIM_HORA_INSTALACION", "'" + informacionServicioRequest.getHoraInstalacion() + "'");
		q.agregarParametroValues("FEC_VELACION", "'" + informacionServicioRequest.getFechaVelacion() + "'");
		q.agregarParametroValues("TIM_HORA_VELACION", "'" + informacionServicioRequest.getHoraVelacion() + "'");
		q.agregarParametroValues("ID_CAPILLA", "" + informacionServicioRequest.getIdCapilla() + "");
		q.agregarParametroValues("ID_INFORMACION_SERVICIO", "" + idInformacionServicio);
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_DATE);
		q.addWhere("ID_INF_SERVICIO_VELACION="+informacionServicioRequest.getIdInformacionServicioVelacion());
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}
	
	
	///////////////////////consultas////////////////////////////
	// consultar orden de servicio
	public String consultarOrdenServicios(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("STO.ID_ORDEN_SERVICIO AS idOrdenServicio", 
				"STO.CVE_FOLIO AS folio",
				"STO.ID_PARENTESCO AS idParentesco",
				"STO.ID_VELATORIO AS idVelatorio",
				"STO.ID_OPERADOR AS idOperador",
				"STO.ID_ESTATUS_ORDEN_SERVICIO AS idEstatus",
				"STO.ID_CONTRATANTE_PF AS idContratantePf")
		.from("SVC_ORDEN_SERVICIO STO")
		.where("STO.ID_ORDEN_SERVICIO = " + idOrdenServicio);
		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	// consultar orden de servicio
	public String consultarPersona(String curp) {
			SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
			selectQueryUtil.select("SPE.ID_PERSONA AS idPersona ")
			.from("SVC_PERSONA SPE ")
			.where("SPE.CVE_CURP = "+setValor(curp)+"");
			query = selectQueryUtil.build();
			log.info(query);
			return query;
	}
	
	// consultar contratante
	public String consultarContratanteOrdenServicios(Integer idOrdenServicio) {
			SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
			selectQueryUtil.select("SPC.ID_PERSONA AS idPersona",
					"STO .ID_CONTRATANTE AS idContratante",
					"IFNULL(SC.CVE_MATRICULA,'') AS matricula",
					"IFNULL(SPC.CVE_RFC,'') AS rfc",
					"IFNULL(SPC.CVE_CURP,'') AS curp",
					"IFNULL(SPC.NOM_PERSONA,'') AS nomPersona",
					"IFNULL(SPC.NOM_PRIMER_APELLIDO,'') AS primerApellido",
					"IFNULL(SPC.NOM_SEGUNDO_APELLIDO,'') AS segundoApellido",
					"IFNULL(SPC.NUM_SEXO,'') AS sexo",
					"IFNULL(SPC.REF_OTRO_SEXO,'') AS otroSexo",
					"DATE_FORMAT(SPC.FEC_NAC,'"+fecha+"') AS fechaNac",
					"(CASE WHEN SPC.ID_PAIS = NULL OR SPC.ID_PAIS = 119  THEN 1 ELSE 2 END) AS nacionalidad",
					"SPC.ID_PAIS AS idPais",
					"SPC.ID_ESTADO AS idEstado",
					"SPC.REF_TELEFONO AS telefono",
					"SPC.REF_CORREO AS correo",
					"SC.ID_DOMICILIO AS idDomicilio",
					"SCD.REF_CALLE AS desCalle",
					"SCD.NUM_EXTERIOR AS numExterior",
					"SCD.NUM_INTERIOR AS numInterior",
					"SCD.REF_CP AS codigoPostal",
					"SCD.REF_COLONIA AS desColonia",
					"SCD.REF_MUNICIPIO AS desMunicipio",
					"SCD.REF_ESTADO AS desEstado")
			.from("SVC_ORDEN_SERVICIO STO")
			.leftJoin("SVC_CONTRATANTE SC", "STO.ID_CONTRATANTE =SC.ID_CONTRATANTE")
			.leftJoin("SVC_PERSONA SPC", "SC.ID_PERSONA = SPC.ID_PERSONA")
			.leftJoin("SVT_DOMICILIO SCD", "SC.ID_DOMICILIO = SCD.ID_DOMICILIO")
			.where("STO.ID_ORDEN_SERVICIO = " + idOrdenServicio);
	
			query = selectQueryUtil.build();
			log.info(query);
			return query;
	}
	
	// consultar finado
	public String consultarFinadoOrdenServicios(Integer idOrdenServicio) {
				SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
				selectQueryUtil.select(
						"STF.ID_FINADO AS idFinado",
						"SPF.ID_PERSONA AS idPersona",
						"STF.ID_TIPO_ORDEN AS idTipoOrden",
						"STF.REF_EXTREMIDAD AS extremidad",
						"STF.REF_OBITO AS esobito",
						"IFNULL(STF.CVE_MATRICULA,'') AS matricula",
						"IFNULL(SPF.CVE_RFC,'') AS rfc",
						"IFNULL(SPF.CVE_CURP,'') AS curp",
						"IFNULL(SPF.CVE_NSS,'') AS nss",
						"IFNULL(SPF.NOM_PERSONA,'') AS nomPersona",
						"IFNULL(SPF.NOM_PRIMER_APELLIDO,'') AS primerApellido",
						"IFNULL(SPF.NOM_SEGUNDO_APELLIDO,'') AS segundoApellido",
						"SPF.NUM_SEXO AS sexo",
						"SPF.REF_OTRO_SEXO AS otroSexo",
						"DATE_FORMAT(SPF.FEC_NAC,'"+fecha+"') AS fechaNac",
						"(CASE WHEN SPF.ID_PAIS = NULL OR SPF.ID_PAIS = 119 THEN 1 ELSE 2 END) AS nacionalidad",
						"SPF.ID_PAIS AS idPais",
						"SPF.ID_ESTADO AS idEstado",
						"SPF.REF_TELEFONO AS telefono",
						"SPF.REF_CORREO AS correo",
						"SCD.ID_DOMICILIO AS idDomicilio",
						"SCD.REF_CALLE AS desCalle",
						"SCD.NUM_EXTERIOR AS numExterior",
						"SCD.NUM_INTERIOR AS numInterior",
						"SCD.REF_CP AS codigoPostal",
						"SCD.REF_COLONIA AS desColonia",
						"SCD.REF_MUNICIPIO AS desMunicipio",
						"SCD.REF_ESTADO AS desEstado",
						"DATE_FORMAT(STF.FEC_DECESO,'"+fecha+"') AS fechaDeceso",
						"STF.REF_CAUSA_DECESO AS causaDeceso",
						"STF.REF_LUGAR_DECESO AS lugarDeceso",
						"TIME_FORMAT(STF.TIM_HORA,'%H:%i') AS hora",
						"STF.ID_CLINICA_ADSCRIPCION AS idClinicaAdscripcion",
						"STF.ID_UNIDAD_PROCEDENCIA AS idUnidadProcedencia",
						"STF.REF_LUGAR_DECESO AS procedenciaFinado",
						"STF.ID_TIPO_PENSION AS idTipoPension",
						"STF.ID_CONTRATO_PREVISION AS idContratoPrevision",
						"STF.ID_VELATORIO AS idVelatorioContratoPrevision",
						"STF.ID_CONTRATO_PREVISION_PA AS idConvenioPrevision",
						"SPC.DES_FOLIO AS folioContrato",
						"SPLA.NUM_FOLIO_PLAN_SFPA AS folioConvenioPa"
						)
				.from("SVC_ORDEN_SERVICIO STO")
				.leftJoin("SVC_FINADO STF ", "STO.ID_ORDEN_SERVICIO = STF.ID_ORDEN_SERVICIO")
				.leftJoin("SVC_PERSONA SPF ", "STF.ID_PERSONA =SPF.ID_PERSONA ")
				.leftJoin("SVT_DOMICILIO SCD", "STF.ID_DOMICILIO = SCD.ID_DOMICILIO")
				.leftJoin("SVT_CONVENIO_PF SPC", "SPC.ID_CONVENIO_PF = STF.ID_CONTRATO_PREVISION")
				.leftJoin("SVT_PLAN_SFPA SPLA", "SPLA.ID_PLAN_SFPA = STF.ID_CONTRATO_PREVISION_PA")
				.where("STO.ID_ORDEN_SERVICIO = " + idOrdenServicio);
		
				query = selectQueryUtil.build();
				log.info(query);
				return query;
	}
	
	// consultar CaracteristicasPresupuestoPaqueteTempOrdenServicios
	public String consultarCaracteristicasPresupuestoPaqueteTempOrdenServicios(Integer idOrdenServicio) {
					SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
					selectQueryUtil.select(
							"SCP.ID_CARAC_PAQUETE AS idCaracteristicasPaquete",
							"SCP.ID_PAQUETE AS idPaquete",
							"SCP.IND_OTORGAMIENTO AS  otorgamiento")
					.from("SVC_ORDEN_SERVICIO STO")
					.innerJoin("SVC_CARAC_PAQUETE_TEMP SCP ", "SCP.ID_ORDEN_SERVICIO = STO.ID_ORDEN_SERVICIO ")
					.innerJoin("SVT_PAQUETE SP", "SCP.ID_PAQUETE=SP.ID_PAQUETE")
					.where("STO.ID_ORDEN_SERVICIO = " + idOrdenServicio)
					.and("SCP.IND_ACTIVO = 1");
			
				query = selectQueryUtil.build();
				log.info(query);
				return query;
	}
	
	// consultar CaracteristicasPresupuestoDetallePaqueteTempOrdenServicios
	public String consultarCaracteristicasPresupuestoDetallePaqueteTempOrdenServicios(Integer idCaracteristicasPaquete) {
						SelectQueryUtil selectQueryUtilServicios = new SelectQueryUtil();
						SelectQueryUtil selectQueryUtilArticulos = new SelectQueryUtil();
						SelectQueryUtil selectQueryAgregadoServicio = new SelectQueryUtil();
						SelectQueryUtil selectQueryAgregadoArticulo = new SelectQueryUtil();
						
						selectQueryAgregadoServicio.select("COUNT(SDCP.ID_DETALLE_CARACTERISTICAS)")
						.from("SVC_CARAC_PRESUP_TEMP SCPT")
						.innerJoin("SVC_DETALLE_CARAC_PRESUP_TEMP SDCP", "SDCP.ID_CARAC_PRESUPUESTO =SCPT.ID_CARAC_PRESUPUESTO ")
						.where("SCPT.ID_ORDEN_SERVICIO =SCP.ID_ORDEN_SERVICIO ");
						selectQueryAgregadoArticulo.select("COUNT(SDCP.ID_DETALLE_CARACTERISTICAS)")
						.from("SVC_CARAC_PRESUP_TEMP SCPT")
						.innerJoin("SVC_DETALLE_CARAC_PRESUP_TEMP SDCP", "SDCP.ID_CARAC_PRESUPUESTO =SCPT.ID_CARAC_PRESUPUESTO ")
						.where("SCPT.ID_ORDEN_SERVICIO =SCP.ID_ORDEN_SERVICIO ");
						
						selectQueryUtilServicios.select("SDCPT.ID_DETALLE_CARAC AS idPaqueteDetalle",
								"0 AS idArticulo",
								"SDCPT.ID_SERVICIO AS idServicio",
								"SS.ID_TIPO_SERVICIO AS idTipoServicio",
								"STS.DES_TIPO_SERVICIO AS grupo",
								"SS.DES_SERVICIO AS concepto",
								"SDCPT.DES_MOTIVO AS desmotivo",
								"SDCPT.IND_ACTIVO AS activo",
								"SDCPT.CAN_CARAC_PAQ AS cantidad",
								"SDCPT.ID_PROVEEDOR AS idProveedor",
								"SP.REF_PROVEEDOR AS nombreProveedor",
								"SDCPT.IMP_CARAC_PAQ AS importeMonto",
								"SDCPT.IMP_CARAC_PAQ AS totalPaquete",
								"CASE WHEN ("+selectQueryAgregadoServicio.and("SDCP.ID_SERVICIO = SDCPT.ID_SERVICIO ").build() + " ) > 0 THEN TRUE "+
										" ELSE  FALSE END AS agregado ",
										"0 AS idCategoriaPaquete "		
								)
						.from("SVC_DETALLE_CARAC_PAQ_TEMP SDCPT ")
						.innerJoin("SVC_CARAC_PAQUETE_TEMP SCP ", "SCP.ID_CARAC_PAQUETE = SDCPT.ID_CARAC_PAQUETE  ")
						.leftJoin("SVT_SERVICIO SS ", "SS.ID_SERVICIO = SDCPT.ID_SERVICIO  ")
						.leftJoin("SVC_TIPO_SERVICIO STS", "SS.ID_TIPO_SERVICIO =STS.ID_TIPO_SERVICIO ")
						.leftJoin("SVT_PROVEEDOR SP", "SP.ID_PROVEEDOR = SDCPT.ID_PROVEEDOR  ")
						.where("SDCPT.ID_CARAC_PAQUETE = " + idCaracteristicasPaquete)
						.and("SDCPT.IND_ACTIVO = 1").and("SDCPT.ID_SERVICIO IS NOT NULL");
						
						selectQueryUtilArticulos.select(
								"SDCPT.ID_DETALLE_CARAC AS idPaqueteDetalle",
								"SDCPT.ID_ARTICULO AS idArticulo",
								"0 AS idServicio",
								"0 AS idTipoServicio",
								"SCA.DES_CATEGORIA_ARTICULO AS grupo",
								"STA.REF_MODELO_ARTICULO AS concepto",
								"SDCPT.DES_MOTIVO AS desmotivo",
								"SDCPT.IND_ACTIVO AS activo",
								"SDCPT.CAN_CARAC_PAQ AS cantidad",
								"SDCPT.ID_PROVEEDOR AS idProveedor",
								"SP.REF_PROVEEDOR AS nombreProveedor",
								"SDCPT.IMP_CARAC_PAQ AS importeMonto",
								"SDCPT.IMP_CARAC_PAQ AS totalPaquete",
								"CASE WHEN ("+selectQueryAgregadoArticulo.and("SDCP.ID_ARTICULO = SDCPT.ID_ARTICULO  ").build() + " ) > 0 THEN TRUE "+
										" ELSE  FALSE END AS agregado ",
										"SDCPT.ID_CATEGORIA_PAQUETE AS idCategoriaPaquete ")
						.from("SVC_DETALLE_CARAC_PAQ_TEMP SDCPT ")
						.innerJoin("SVC_CARAC_PAQUETE_TEMP SCP ", "SCP.ID_CARAC_PAQUETE = SDCPT.ID_CARAC_PAQUETE  ")
						.leftJoin("SVT_ARTICULO STA", "STA.ID_ARTICULO = SDCPT.ID_ARTICULO ")
						.leftJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = SDCPT.ID_CATEGORIA_PAQUETE ")
						.leftJoin("SVT_PROVEEDOR SP", "SP.ID_PROVEEDOR = SDCPT.ID_PROVEEDOR  ")
						.where("SDCPT.ID_CARAC_PAQUETE = " + idCaracteristicasPaquete)
						.and("SDCPT.IND_ACTIVO = 1").and("SDCPT.ID_CATEGORIA_PAQUETE IS NOT NULL");
				
				query = selectQueryUtilServicios.union(selectQueryUtilArticulos);
				log.info(query);
				return query;
	}
	
	
	// consultar CaracteristicasPresupuestoPaqueteTrasladoTempOrdenServicios
	public String consultarCaracteristicasPresupuestoDetallePaqueteTrasladoTempOrdenServicios(Integer idDetalleCaracteristicasPaquete) {
							SelectQueryUtil selectQueryUtilTrasladoServicio = new SelectQueryUtil();
							selectQueryUtilTrasladoServicio.select("SCPTT.ID_CARAC_PRESU_TRASLADO AS idCaracteristicasPaqueteDetalleTraslado",
									"SCPTT.REF_ORIGEN AS origen",
									"SCPTT.REF_DESTINO AS destino",
									"SCPTT.CAN_TOTAL_KILOMETROS AS totalKilometros ",
									"SCPTT.NUM_LATITUD_INICIAL AS latitudInicial",
									"SCPTT.NUM_LATITUD_FINAL AS latitudFinal",
									"SCPTT.NUM_LONGITUD_INICIAL AS longitudInicial",
									"SCPTT.NUM_LONGITUD_FINAL AS longitudFinal")
							.from("SVC_CARAC_PAQ_TRAS_TEMP SCPTT")
							.where("SCPTT.ID_CARAC_PRESU_TRASLADO = " + idDetalleCaracteristicasPaquete);
							
						
					
				query = selectQueryUtilTrasladoServicio.build();
				log.info(query);
				return query;
	}
	
	// consultar CaracteristicasPresupuestoPresupuestoTempOrdenServicios
	public String consultarCaracteristicasPresupuestoPresupuestoTempOrdenServicios(Integer idOrdenServicio) {
					SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
					selectQueryUtil.select("SCPT.ID_CARAC_PRESUPUESTO AS idCaracteristicasPresupuesto",
							"SCPT.ID_PAQUETE AS idPaquete",
							"SCPT.CAN_PRESUPUESTO AS  totalPresupuesto",
							"SCPT.REF_OBSERVACIONES AS observaciones",
							"SCPT.REF_NOTAS_SERVICIO AS notasServicio")
					.from("SVC_ORDEN_SERVICIO STO")
					.innerJoin("SVC_CARAC_PRESUP_TEMP SCPT ", "SCPT.ID_ORDEN_SERVICIO = STO.ID_ORDEN_SERVICIO  ")
					.innerJoin("SVT_PAQUETE SP", "SCPT.ID_PAQUETE=SP.ID_PAQUETE")
					.where("STO.ID_ORDEN_SERVICIO = " + idOrdenServicio)
					.and("SCPT.IND_ACTIVO = 1");
			
				query = selectQueryUtil.build();
				log.info(query);
				return query;
	}
	
	// consultar CaracteristicasPresupuestoDetallePaqueteTempOrdenServicios
		public String consultarCaracteristicasPresupuestoDetallePresupuestoTempOrdenServicios(Integer idCaracteristicasPresupuesto, Integer idOrdenServicio) {
							SelectQueryUtil selectQueryUtilServicios = new SelectQueryUtil();
							SelectQueryUtil selectQueryUtilArticulos = new SelectQueryUtil();
								
							
							selectQueryUtilServicios.select("SDCPT.ID_DETALLE_CARACTERISTICAS AS idPaqueteDetallePresupuesto",
									"0 AS idCategoria",
									"0 AS idArticulo",
									"0 AS idInventario",
									"SDCPT.ID_SERVICIO AS idServicio",
									"STS.DES_TIPO_SERVICIO AS grupo",
									"SS.DES_SERVICIO AS concepto",
									"SDCPT.CAN_DET_PRESUP AS cantidad",
									"SDCPT.ID_PROVEEDOR AS idProveedor",
									"SP.REF_PROVEEDOR AS nombreProveedor",
									"0 AS esDonado",
									"SDCPT.IMP_CARAC_PRESUP AS importeMonto",
									"SDCPT.DES_PROVIENE AS proviene")
							.from("SVC_DETALLE_CARAC_PRESUP_TEMP SDCPT  ")
							.innerJoin("SVC_CARAC_PRESUP_TEMP SCP ", "SCP.ID_CARAC_PRESUPUESTO = SDCPT.ID_CARAC_PRESUPUESTO  ")
							.leftJoin("SVT_SERVICIO SS", "SS.ID_SERVICIO = SDCPT.ID_SERVICIO ")
							.innerJoin("SVC_TIPO_SERVICIO STS", "SS.ID_TIPO_SERVICIO =STS.ID_TIPO_SERVICIO  ")
							.leftJoin("SVT_PROVEEDOR SP", "SP.ID_PROVEEDOR = SDCPT.ID_PROVEEDOR ")
							.where("SDCPT.ID_CARAC_PRESUPUESTO = " + idCaracteristicasPresupuesto)
							.and("SDCPT.IND_ACTIVO = 1");
							
							selectQueryUtilArticulos.select("SDCPT.ID_DETALLE_CARACTERISTICAS AS idPaqueteDetallePresupuesto",
									"STA.ID_CATEGORIA_ARTICULO AS idCategoria",
									"SDCPT.ID_ARTICULO AS idArticulo",
									"SDCPT.ID_INVE_ARTICULO AS idInventario",
									"0 AS idServicio",
									"SCA.DES_CATEGORIA_ARTICULO AS grupo",
									"STA.REF_MODELO_ARTICULO AS concepto",
									"SDCPT.CAN_DET_PRESUP AS cantidad",
									"SDCPT.ID_PROVEEDOR AS idProveedor",
									"SP.REF_PROVEEDOR AS nombreProveedor",
									"(SELECT IFNULL(SDO.ID_DONACION_ORDEN_SERVICIO,0)  FROM SVC_DONACION_ORDEN_SERV_TEMP SDO "+ 
									"WHERE SDO.ID_INVE_ARTICULO = SDCPT.ID_INVE_ARTICULO AND SDO.ID_ORDEN_SERVICIO="+idOrdenServicio+" AND SDO.IND_ACTIVO=1)"+ 
									"AS esDonado",
									"SDCPT.IMP_CARAC_PRESUP AS importeMonto",
									"SDCPT.DES_PROVIENE AS proviene")
							.from("SVC_DETALLE_CARAC_PRESUP_TEMP SDCPT  ")
							.innerJoin("SVC_CARAC_PRESUP_TEMP SCP  ", "SCP.ID_CARAC_PRESUPUESTO = SDCPT.ID_CARAC_PRESUPUESTO ")
							.leftJoin("SVT_INVENTARIO_ARTICULO STIA ", "STIA.ID_INVE_ARTICULO = SDCPT.ID_INVE_ARTICULO ")
							.innerJoin("SVT_ARTICULO STA", "STA.ID_ARTICULO = SDCPT.ID_ARTICULO ")
							.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO =STA.ID_CATEGORIA_ARTICULO ")
							.leftJoin("SVT_PROVEEDOR SP", "SP.ID_PROVEEDOR = SDCPT.ID_PROVEEDOR   ")
							.where("SDCPT.ID_CARAC_PRESUPUESTO = " + idCaracteristicasPresupuesto)
							.and("SDCPT.IND_ACTIVO = 1");
					
					query = selectQueryUtilServicios.union(selectQueryUtilArticulos);
					log.info(query);
					return query;
		}
		
		// consultar CaracteristicasPresupuestoPTrasladoTempOrdenServicios
		public String consultarCaracteristicasPresupuestoDetallePresupuestoTrasladoTempOrdenServicios(Integer idDetalleCaracteristicasPresupuesto) {
								SelectQueryUtil selectQueryUtilTrasladoServicio = new SelectQueryUtil();
								selectQueryUtilTrasladoServicio.select("SCPTT.ID_CARAC_PRESU_TRASLADO AS idCaracteristicasPaqueteDetalleTraslado",
										"SCPTT.REF_ORIGEN AS origen",
										"SCPTT.REF_DESTINO AS destino",
										"SCPTT.CAN_TOTAL_KILOMETROS AS totalKilometros ",
										"SCPTT.NUM_LATITUD_INICIAL AS latitudInicial",
										"SCPTT.NUM_LATITUD_FINAL AS latitudFinal",
										"SCPTT.NUM_LONGITUD_INICIAL AS longitudInicial",
										"SCPTT.NUM_LONGITUD_FINAL AS longitudFinal")
								.from("SVC_CARAC_PRESUP_TRAS_TEMP SCPTT")
								.where("SCPTT.ID_DETALLE_CARACTERISTICAS = " + idDetalleCaracteristicasPresupuesto);
								
							
						
					query = selectQueryUtilTrasladoServicio.build();
					log.info(query);
					return query;
		}
		
		// consultar InformacionServicioOrdenServicios
		public String consultarInformacionServicioOrdenServicios(Integer idOrdenServicio) {
										SelectQueryUtil selectQueryUtilTrasladoServicio = new SelectQueryUtil();
										selectQueryUtilTrasladoServicio.select("SI.ID_INFORMACION_SERVICIO AS idInformacionServicio",
												"DATE_FORMAT(SI.FEC_CORTEJO,'"+fecha+"') AS fechaCortejo",
												"TIME_FORMAT(SI.TIM_HORA_CORTEJO,'%H:%i') AS horaCortejo",
												"DATE_FORMAT(SI.FEC_RECOGER,'"+fecha+"') AS fechaRecoger",
												"TIME_FORMAT(SI.TIM_HORA_RECOGER,'%H:%i') AS horaRecoger",
												"SI.ID_PANTEON AS idPanteon",
												"SI.ID_SALA AS idSala",
												"DATE_FORMAT(SI.FEC_CREMACION,'"+fecha+"') AS fechaCremacion",
												"TIME_FORMAT(SI.TIM_HORA_CREMACION,'%H:%i') AS horaCremacion",
												"SI.ID_PROMOTORES AS idPromotor")
										.from("SVC_INFORMACION_SERVICIO SI ")
										.where("SI.ID_ORDEN_SERVICIO = " + idOrdenServicio)
										.and(IND_ACTIVO+"="+1);
										
									
								
					 query = selectQueryUtilTrasladoServicio.build();
					 log.info(query);
					 return query;
		}
		
		// consultar InformacionServicioOrdenServicios
		public String consultarInformacionServicioVelacionOrdenServicios(Integer idInformacionServicio) {
												SelectQueryUtil selectQueryUtilTrasladoServicio = new SelectQueryUtil();
												selectQueryUtilTrasladoServicio.select("SISV.ID_INF_SERVICIO_VELACION AS idInformacionServicioVelacion",
														"DATE_FORMAT(SISV.FEC_INSTALACION ,'"+fecha+"') AS fechaInstalacion",
														"TIME_FORMAT(SISV.TIM_HORA_INSTALACION,'%H:%i') AS horaInstalacion",
														"DATE_FORMAT(SISV.FEC_VELACION,'"+fecha+"') AS fechaVelacion",
														"TIME_FORMAT(SISV.TIM_HORA_VELACION,'%H:%i') AS horaVelacion",
														"SISV.ID_CAPILLA AS idCapilla",
														"SISV.ID_DOMICILIO AS idDomicilio",
														"SCD.REF_CALLE AS desCalle",
														"SCD.NUM_EXTERIOR AS numExterior",
														"SCD.NUM_INTERIOR AS numInterior",
														"SCD.REF_CP AS codigoPostal",
														"SCD.REF_COLONIA AS desColonia",
														"SCD.REF_MUNICIPIO AS desMunicipio",
														"SCD.REF_ESTADO AS desEstado")
												.from("SVC_INF_SERVICIO_VELACION SISV ")
												.leftJoin("SVT_DOMICILIO SCD","SCD.ID_DOMICILIO = SISV.ID_DOMICILIO")
												.where("SISV.ID_INFORMACION_SERVICIO = " + idInformacionServicio);
												
											
										
						query = selectQueryUtilTrasladoServicio.build();
						log.info(query);
						return query;
		}
		
		private String setValor(String valor) {
			if (valor==null || valor.equals("")) {
				return "NULL";
			}else {
				return "'"+valor+"'";
			}
		}
		
		
}

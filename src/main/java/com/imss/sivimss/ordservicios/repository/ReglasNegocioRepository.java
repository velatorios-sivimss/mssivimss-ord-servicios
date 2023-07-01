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

	private static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP()";

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
						"IFNULL(SPE.DES_OTRO_SEXO,'') AS otroSexo", "IFNULL(SPE .FEC_NAC,'') AS fechaNac",
						"IFNULL(SVP.ID_PAIS,'') AS idPais", "IFNULL(SVE.ID_ESTADO,'') AS idEstado",
						"IFNULL(SPE.DES_TELEFONO,'') AS telefono", "IFNULL(SPE.DES_CORREO,'') AS correo",
						"IFNULL(SVD.DES_CALLE,'') AS calle", "IFNULL(SVD.NUM_EXTERIOR,'') AS numExterior",
						"IFNULL(SVD.NUM_INTERIOR,'') AS numInterior", "IFNULL(SVD.DES_CP,'') AS cp",
						"IFNULL(SVD.DES_COLONIA,'') AS colonia", "IFNULL(SVD.DES_MUNICIPIO,'') AS municipio",
						"IFNULL(SVD.DES_ESTADO,'') AS estado")
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
						"IFNULL(SPE.DES_OTRO_SEXO,'') AS otroSexo", "IFNULL(SPE .FEC_NAC,'') AS fechaNac",
						"IFNULL(SVP.ID_PAIS,'') AS idPais", "IFNULL(SVE.ID_ESTADO,'') AS idEstado",
						"IFNULL(SPE.DES_TELEFONO,'') AS telefono", "IFNULL(SPE.DES_CORREO,'') AS correo",
						"IFNULL(SVD.DES_CALLE,'') AS calle", "IFNULL(SVD.NUM_EXTERIOR,'') AS numExterior",
						"IFNULL(SVD.NUM_INTERIOR,'') AS numInterior", "IFNULL(SVD.DES_CP,'') AS cp",
						"IFNULL(SVD.DES_COLONIA,'') AS colonia", "IFNULL(SVD.DES_MUNICIPIO,'') AS municipio",
						"IFNULL(SVD.DES_ESTADO,'') AS estado")
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
				.and("ID_ESTATUS_ORDEN_SERVICIO not in (0,1)").setParameter("idVelatorio", idVelatorio);

		SelectQueryUtil ordenServicioCount = new SelectQueryUtil();
		ordenServicioCount.select("COUNT(*)+1").from("SVC_ORDEN_SERVICIO").where("ID_VELATORIO = :idVelatorio")
				.and("ID_ESTATUS_ORDEN_SERVICIO not in (0,1)").setParameter("idVelatorio", idVelatorio);

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
		q.agregarParametroValues("CVE_RFC", "'" + personaRequest.getRfc() + "'");
		q.agregarParametroValues("CVE_CURP", "'" + personaRequest.getCurp() + "'");
		q.agregarParametroValues("CVE_NSS", "'" + personaRequest.getNss() + "'");
		q.agregarParametroValues("NOM_PERSONA", "'" + personaRequest.getNomPersona() + "'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" + personaRequest.getPrimerApellido() + "'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" + personaRequest.getSegundoApellido() + "'");
		q.agregarParametroValues("NUM_SEXO", "" + personaRequest.getSexo() + "");
		q.agregarParametroValues("DES_OTRO_SEXO", "'" + personaRequest.getOtroSexo() + "'");
		q.agregarParametroValues("FEC_NAC", "'" + personaRequest.getFechaNac() + "'");
		q.agregarParametroValues("ID_PAIS", "" + personaRequest.getIdPais() + "");
		q.agregarParametroValues("ID_ESTADO", "" + personaRequest.getIdEstado() + "");
		q.agregarParametroValues("DES_TELEFONO", "'" + personaRequest.getTelefono() + "'");
		q.agregarParametroValues("DES_CORREO", "'" + personaRequest.getCorreo() + "'");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar contratante
	public String insertarContratante(ContratanteRequest contratanteRequest, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", "" + contratanteRequest.getIdPersona() + "");
		q.agregarParametroValues("CVE_MATRICULA", "'" + contratanteRequest.getMatricula() + "'");
		q.agregarParametroValues(ID_DOMICILIO, "" + contratanteRequest.getCp().getIdDomicilio() + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	public String insertarDomicilio(DomicilioRequest domicilioRequest, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", "'" + domicilioRequest.getDesCalle() + "'");
		q.agregarParametroValues("NUM_EXTERIOR", "'" + domicilioRequest.getNumExterior() + "'");
		q.agregarParametroValues("NUM_INTERIOR", "'" + domicilioRequest.getNumInterior() + "'");
		q.agregarParametroValues("DES_CP", "'" + domicilioRequest.getCodigoPostal() + "'");
		q.agregarParametroValues("DES_COLONIA", "'" + domicilioRequest.getDesColonia() + "'");
		q.agregarParametroValues("DES_MUNICIPIO", "'" + domicilioRequest.getDesMunicipio() + "'");
		q.agregarParametroValues("DES_ESTADO", "'" + domicilioRequest.getDesEstado() + "'");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar orden de servicio
	public String insertarOrdenServicio(String folio, Integer idContratante, Integer idParentesco, Integer idVelatorio,
			Integer idContratantePf, Integer idEstatus, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_ORDEN_SERVICIO");
		q.agregarParametroValues("CVE_FOLIO", "'" + folio + "'");
		q.agregarParametroValues("ID_CONTRATANTE", "" + idContratante + "");
		q.agregarParametroValues("ID_PARENTESCO", "" + idParentesco + "");
		q.agregarParametroValues("ID_VELATORIO", "" + idVelatorio + "");
		q.agregarParametroValues("ID_CONTRATANTE_PF", "" + idContratantePf + "");
		q.agregarParametroValues("ID_ESTATUS_ORDEN_SERVICIO", "" + idEstatus + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar finado
	public String insertarFinado(FinadoRequest finadoRequest, Integer idOrdenServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_FINADO");
		q.agregarParametroValues("ID_PERSONA", "" + finadoRequest.getIdPersona() + "");
		q.agregarParametroValues("ID_TIPO_ORDEN", "" + finadoRequest.getIdTipoOrden() + "");
		q.agregarParametroValues("DES_EXTREMIDAD", "'" + finadoRequest.getExtremidad() + "'");
		q.agregarParametroValues("DES_OBITO", "'" + finadoRequest.getEsobito() + "'");
		q.agregarParametroValues("CVE_MATRICULA", "'" + finadoRequest.getMatricula() + "'");
		q.agregarParametroValues("ID_CONTRATO_PREVISION", "" + finadoRequest.getIdContratoPrevision() + "");
		q.agregarParametroValues("ID_VELATORIO", "" + finadoRequest.getIdVelatorioContratoPrevision() + "");
		q.agregarParametroValues(ID_DOMICILIO, "" + finadoRequest.getCp().getIdDomicilio() + "");
		q.agregarParametroValues("FEC_DECESO", "'" + finadoRequest.getFechaDeceso() + "'");
		q.agregarParametroValues("DES_CAUSA_DECESO", "'" + finadoRequest.getCausaDeceso() + "'");
		q.agregarParametroValues("DES_LUGAR_DECESO", "'" + finadoRequest.getLugarDeceso() + "'");
		q.agregarParametroValues("TIM_HORA", "'" + finadoRequest.getHora() + "'");
		q.agregarParametroValues("ID_CLINICA_ADSCRIPCION", "" + finadoRequest.getIdClinicaAdscripcion() + "");
		q.agregarParametroValues("ID_UNIDAD_PROCEDENCIA", "" + finadoRequest.getIdUnidadProcedencia() + "");
		q.agregarParametroValues("DES_PROCEDENCIA_FINADO", "'" + finadoRequest.getProcedenciaFinado() + "'");
		q.agregarParametroValues("ID_TIPO_PENSION", "" + finadoRequest.getIdTipoPension() + "");
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("IND_OTORGAMIENTO",
				"" + Integer.parseInt(caracteristicasPaqueteRequest.getOtorgamiento()) + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("DES_MOTIVO", "'" + detalleCaracteristicasPaqueteRequest.getDesmotivo() + "'");
		q.agregarParametroValues(IND_ACTIVO, "" + detalleCaracteristicasPaqueteRequest.getActivo() + "");
		q.agregarParametroValues("CAN_CANTIDAD", "" + detalleCaracteristicasPaqueteRequest.getCantidad() + "");
		q.agregarParametroValues("IMP_IMPORTE", "" + detalleCaracteristicasPaqueteRequest.getImporteMonto() + "");
		q.agregarParametroValues("ID_PROVEEDOR", "" + detalleCaracteristicasPaqueteRequest.getIdProveedor() + "");
		q.agregarParametroValues("ID_CARACTERISTICAS_PAQUETE", "" + idCaracteristicasPaquete + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("DES_ORIGEN", "'" + detalleCaracteristicasPaqueteTrasladoRequest.getOrigen() + "'");
		q.agregarParametroValues("DES_DESTINO", "'" + detalleCaracteristicasPaqueteTrasladoRequest.getDestino() + "'");
		q.agregarParametroValues("LATITUD_INICIAL",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getLatitudInicial() + "");
		q.agregarParametroValues("LATITUD_FINAL",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getLatitudFinal() + "");
		q.agregarParametroValues("LONGITUD_INICIAL",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getLongitudInicial() + "");
		q.agregarParametroValues("LONGITUD_FINAL",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getLongitudFinal() + "");
		q.agregarParametroValues("CAN_TOTAL_KILOMETROS",
				"" + detalleCaracteristicasPaqueteTrasladoRequest.getTotalKilometros());
		q.agregarParametroValues("ID_DETALLE_CARACTERISTICAS", "" + idDetalleCaracteristicasPaquete + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("DES_OBSERVACIONES",
				"'" + caracteristicasPaquetePresupuestoRequest.getObservaciones() + "'");
		q.agregarParametroValues("DES_NOTAS_SERVICIO",
				"'" + caracteristicasPaquetePresupuestoRequest.getNotasServicio() + "'");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("CAN_CANTIDAD", "" + caracteristicasPaqueteRespuestoRequest.getCantidad() + "");
		q.agregarParametroValues("IMP_IMPORTE", "" + caracteristicasPaqueteRespuestoRequest.getImporteMonto() + "");
		q.agregarParametroValues("ID_PROVEEDOR", "" + caracteristicasPaqueteRespuestoRequest.getIdProveedor() + "");
		q.agregarParametroValues("ID_CARACTERISTICAS_PRESUPUESTO", "" + idCaracteristicasPaquetePresupuesto + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("DES_ORIGEN",
				"'" + detalleCaracteristicasPresupuestoTrasladoRequest.getOrigen() + "'");
		q.agregarParametroValues("DES_DESTINO",
				"'" + detalleCaracteristicasPresupuestoTrasladoRequest.getDestino() + "'");
		q.agregarParametroValues("LATITUD_INICIAL",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getLatitudInicial() + "");
		q.agregarParametroValues("LATITUD_FINAL",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getLatitudFinal() + "");
		q.agregarParametroValues("LONGITUD_INICIAL",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getLongitudInicial() + "");
		q.agregarParametroValues("LONGITUD_FINAL",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getLongitudFinal() + "");
		q.agregarParametroValues("CAN_TOTAL_KILOMETROS",
				"" + detalleCaracteristicasPresupuestoTrasladoRequest.getTotalKilometros());
		q.agregarParametroValues("ID_DETALLE_CARACTERISTICAS", "" + idDetalleCaracteristicasPaquete + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar informacion servicio
	public String insertarInformacionServicio(InformacionServicioRequest informacionServicioRequest,
			Integer idOrdenServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_INFORMACION_SERVICIO");
		q.agregarParametroValues("FEC_CORTEJO", "'" + informacionServicioRequest.getFechaCortejo() + "'");
		q.agregarParametroValues("TIM_HORA_CORTEJO", "'" + informacionServicioRequest.getHoraCortejo() + "'");
		q.agregarParametroValues("FEC_RECOGER", "'" + informacionServicioRequest.getFechaRecoger() + "'");
		q.agregarParametroValues("TIM_HORA_RECOGER", "'" + informacionServicioRequest.getHoraRecoger() + "'");
		q.agregarParametroValues("ID_PANTEON", "" + informacionServicioRequest.getIdPanteon() + "");
		q.agregarParametroValues("ID_SALA", "" + informacionServicioRequest.getIdSala() + "");
		q.agregarParametroValues("FEC_CREMACION", "'" + informacionServicioRequest.getFechaCremacion() + "'");
		q.agregarParametroValues("TIM_HORA_CREMACION", "'" + informacionServicioRequest.getHoraCremacion() + "'");
		q.agregarParametroValues("ID_PROMOTORES", "" + informacionServicioRequest.getIdPromotor());
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar informacion servicio velacion
	public String insertarInformacionServicioVelacion(InformacionServicioVelacionRequest informacionServicioRequest,
			Integer idInformacionServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_INFORMACION_SERVICIO_VELACION");
		q.agregarParametroValues(ID_DOMICILIO, "" + informacionServicioRequest.getCp().getIdDomicilio() + "");
		q.agregarParametroValues("FEC_INSTALACION", "'" + informacionServicioRequest.getFechaInstalacion() + "'");
		q.agregarParametroValues("TIM_HORA_INSTALACION", "'" + informacionServicioRequest.getHoraInstalacion() + "'");
		q.agregarParametroValues("FEC_VELACION", "'" + informacionServicioRequest.getFechaVelacion() + "'");
		q.agregarParametroValues("TIM_HORA_VELACION", "'" + informacionServicioRequest.getHoraVelacion() + "'");
		q.agregarParametroValues("ID_CAPILLA", "" + informacionServicioRequest.getIdCapilla() + "");
		q.agregarParametroValues("ID_INFORMACION_SERVICIO", "" + idInformacionServicio);
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	// insertar ataud donado temp
	public String insertarAtaudDonadoTemp(Integer ordenServicio, Integer idInventario, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_DONACION_ORDEN_SERVICIO_TEMP");
		q.agregarParametroValues("ID_ORDEN_SERVICIO", "" + ordenServicio + "");
		q.agregarParametroValues("ID_INVE_ARTICULO", "" + idInventario + "");
		q.agregarParametroValues(IND_ACTIVO, "1");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("FEC_ACTUALIZACION", CURRENT_TIMESTAMP);
		q.agregarParametroValues("ID_USUARIO_MODIFICA", "" + idUsuarioModifica + "");
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

	// consultar orden de servicio
	public String consultarOrdenServicio(Integer idOrdenServicio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("STO.ID_ORDEN_SERVICIO AS idOrdenServicio", "STO.CVE_FOLIO AS folio",
				"IFNULL(CONCAT(SP.NOM_PERSONA,' ',SP.NOM_PRIMER_APELLIDO,' ',SP.NOM_SEGUNDO_APELLIDO),'') AS contratante",
				"IFNULL(CONCAT(SP2.NOM_PERSONA,' ',SP2.NOM_PRIMER_APELLIDO,' ',SP2.NOM_SEGUNDO_APELLIDO),'') AS finado",
				"STF.ID_TIPO_ORDEN AS idEstatusOrdenServicio").from("SVC_ORDEN_SERVICIO STO")
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
		q.agregarParametroValues("FEC_ACTUALIZACION", CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("FEC_ODS", CURRENT_TIMESTAMP);
		q.agregarParametroValues("NOM_CONTRATANTE", "'" + contratante + "'");
		q.agregarParametroValues("CVE_FOLIO", "'" + folio + "'");
		q.agregarParametroValues("DESC_VALOR", "'" + valor + "'");
		q.agregarParametroValues("CVE_ESTATUS_PAGO", "" + estatus + "");
		q.agregarParametroValues(ID_USUARIO_ALTA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		query = q.obtenerQueryInsertar();
		log.info(query);
		return query;
	}

	////////////////////////////////// desactivar
	////////////////////////////////// temporales//////////////////////////////////

	public String actualizarCaracteristicasPaqueteTemporal(Integer idOrden) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_CARACTERISTICAS_PAQUETE_TEMP ");
		q.agregarParametroValues("IND_ACTIVO", "0");
		q.addWhere("ID_ORDEN_SERVICIO = " + idOrden);
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	public String actualizarCaracteristicasPaqueteDetalleTemp(Integer idOrden) {

		query = " UPDATE SVC_DETALLE_CARACTERISTICAS_PAQUETE_TEMP SET IND_ACTIVO = 0"
				+ " WHERE ID_CARACTERISTICAS_PAQUETE IN " + " (SELECT DISTINCT ID_CARACTERISTICAS_PAQUETE "
				+ " FROM SVC_CARACTERISTICAS_PAQUETE_TEMP" + " WHERE ID_ORDEN_SERVICIO =" + idOrden + ")";
		log.info(query);
		return query;

	}

	public String actualizarCaracteristicasPresupuestoTemporal(Integer idOrden) {
		final QueryHelper queryHelper = new QueryHelper("UPDATE SVC_CARACTERISTICAS_PRESUPUESTO_TEMP ");
		queryHelper.agregarParametroValues("IND_ACTIVO", "0");
		queryHelper.addWhere(" ID_ORDEN_SERVICIO = " + idOrden);

		query = queryHelper.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	public String actualizarCaracteristicasPresuestoDetalleTemp(Integer idOrden) {

		query = " UPDATE SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP SET IND_ACTIVO = 0"
				+ " WHERE ID_CARACTERISTICAS_PRESUPUESTO IN " + " (SELECT DISTINCT ID_CARACTERISTICAS_PRESUPUESTO "
				+ " FROM SVC_CARACTERISTICAS_PRESUPUESTO_TEMP" + " WHERE ID_ORDEN_SERVICIO =" + idOrden + ")";
		log.info(query);
		return query;

	}

	public String actualizarDonacionTemporal(Integer idOrden) {
		final QueryHelper queryHelper = new QueryHelper("UPDATE SVC_DONACION_ORDEN_SERVICIO_TEMP ");
		queryHelper.agregarParametroValues("IND_ACTIVO", "0");
		queryHelper.addWhere(" ID_ORDEN_SERVICIO = " + idOrden);

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
		q.agregarParametroValues("CVE_RFC", "'" + personaRequest.getRfc() + "'");
		q.agregarParametroValues("CVE_CURP", "'" + personaRequest.getCurp() + "'");
		q.agregarParametroValues("CVE_NSS", "'" + personaRequest.getNss() + "'");
		q.agregarParametroValues("NOM_PERSONA", "'" + personaRequest.getNomPersona() + "'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" + personaRequest.getPrimerApellido() + "'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" + personaRequest.getSegundoApellido() + "'");
		q.agregarParametroValues("NUM_SEXO", "" + personaRequest.getSexo() + "");
		q.agregarParametroValues("DES_OTRO_SEXO", "'" + personaRequest.getOtroSexo() + "'");
		q.agregarParametroValues("FEC_NAC", "'" + personaRequest.getFechaNac() + "'");
		q.agregarParametroValues("ID_PAIS", "" + personaRequest.getIdPais() + "");
		q.agregarParametroValues("ID_ESTADO", "" + personaRequest.getIdEstado() + "");
		q.agregarParametroValues("DES_TELEFONO", "'" + personaRequest.getTelefono() + "'");
		q.agregarParametroValues("DES_CORREO", "'" + personaRequest.getCorreo() + "'");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuario + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere("ID_PERSONA = " + personaRequest.getIdPersona());
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	// actualizar domicilio
	public String actualizarDomicilio(DomicilioRequest domicilioRequest, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVT_DOMICILIO ");
		q.agregarParametroValues("DES_CALLE", "'" + domicilioRequest.getDesCalle() + "'");
		q.agregarParametroValues("NUM_EXTERIOR", "'" + domicilioRequest.getNumExterior() + "'");
		q.agregarParametroValues("NUM_INTERIOR", "'" + domicilioRequest.getNumInterior() + "'");
		q.agregarParametroValues("DES_CP", "'" + domicilioRequest.getCodigoPostal() + "'");
		q.agregarParametroValues("DES_COLONIA", "'" + domicilioRequest.getDesColonia() + "'");
		q.agregarParametroValues("DES_MUNICIPIO", "'" + domicilioRequest.getDesMunicipio() + "'");
		q.agregarParametroValues("DES_ESTADO", "'" + domicilioRequest.getDesEstado() + "'");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere("ID_DOMICILIO = "+domicilioRequest.getIdDomicilio());
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}
	
	// actualizar orden de servicio
	public String actualizarOrdenServicio(Integer idOrdenServicio,String folio, Integer idParentesco, Integer idContratantePf,
			Integer idEstatus, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_ORDEN_SERVICIO ");
		q.agregarParametroValues("CVE_FOLIO", "'" + folio + "'");
		q.agregarParametroValues("ID_PARENTESCO", "" + idParentesco + "");
		q.agregarParametroValues("ID_CONTRATANTE_PF", "" + idContratantePf + "");
		q.agregarParametroValues("ID_ESTATUS_ORDEN_SERVICIO", "" + idEstatus + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
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
		q.agregarParametroValues("DES_EXTREMIDAD", "'" + finadoRequest.getExtremidad() + "'");
		q.agregarParametroValues("DES_OBITO", "'" + finadoRequest.getEsobito() + "'");
		q.agregarParametroValues("CVE_MATRICULA", "'" + finadoRequest.getMatricula() + "'");
		q.agregarParametroValues("ID_CONTRATO_PREVISION", "" + finadoRequest.getIdContratoPrevision() + "");
		q.agregarParametroValues("ID_VELATORIO", "" + finadoRequest.getIdVelatorioContratoPrevision() + "");
		q.agregarParametroValues(ID_DOMICILIO, "" + finadoRequest.getCp().getIdDomicilio() + "");
		q.agregarParametroValues("FEC_DECESO", "'" + finadoRequest.getFechaDeceso() + "'");
		q.agregarParametroValues("DES_CAUSA_DECESO", "'" + finadoRequest.getCausaDeceso() + "'");
		q.agregarParametroValues("DES_LUGAR_DECESO", "'" + finadoRequest.getLugarDeceso() + "'");
		q.agregarParametroValues("TIM_HORA", "'" + finadoRequest.getHora() + "'");
		q.agregarParametroValues("ID_CLINICA_ADSCRIPCION", "" + finadoRequest.getIdClinicaAdscripcion() + "");
		q.agregarParametroValues("ID_UNIDAD_PROCEDENCIA", "" + finadoRequest.getIdUnidadProcedencia() + "");
		q.agregarParametroValues("DES_PROCEDENCIA_FINADO", "'" + finadoRequest.getProcedenciaFinado() + "'");
		q.agregarParametroValues("ID_TIPO_PENSION", "" + finadoRequest.getIdTipoPension() + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere("ID_FINADO="+finadoRequest.getIdFinado());

		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}
	
	
	// insertar informacion servicio
	public String actualizarInformacionServicio(InformacionServicioRequest informacionServicioRequest,
			Integer idOrdenServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_INFORMACION_SERVICIO ");
		q.agregarParametroValues("FEC_CORTEJO", "'" + informacionServicioRequest.getFechaCortejo() + "'");
		q.agregarParametroValues("TIM_HORA_CORTEJO", "'" + informacionServicioRequest.getHoraCortejo() + "'");
		q.agregarParametroValues("FEC_RECOGER", "'" + informacionServicioRequest.getFechaRecoger() + "'");
		q.agregarParametroValues("TIM_HORA_RECOGER", "'" + informacionServicioRequest.getHoraRecoger() + "'");
		q.agregarParametroValues("ID_PANTEON", "" + informacionServicioRequest.getIdPanteon() + "");
		q.agregarParametroValues("ID_SALA", "" + informacionServicioRequest.getIdSala() + "");
		q.agregarParametroValues("FEC_CREMACION", "'" + informacionServicioRequest.getFechaCremacion() + "'");
		q.agregarParametroValues("TIM_HORA_CREMACION", "'" + informacionServicioRequest.getHoraCremacion() + "'");
		q.agregarParametroValues("ID_PROMOTORES", "" + informacionServicioRequest.getIdPromotor());
		q.agregarParametroValues(ID_ORDEN_SERVICIO, "" + idOrdenServicio + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere("ID_INFORMACION_SERVICIO="+informacionServicioRequest.getIdInformacionServicio());
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}

	// insertar informacion servicio velacion
	public String actualizarInformacionServicioVelacion(InformacionServicioVelacionRequest informacionServicioRequest,
			Integer idInformacionServicio, Integer idUsuarioAlta) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_INFORMACION_SERVICIO_VELACION ");
		q.agregarParametroValues(ID_DOMICILIO, "" + informacionServicioRequest.getCp().getIdDomicilio() + "");
		q.agregarParametroValues("FEC_INSTALACION", "'" + informacionServicioRequest.getFechaInstalacion() + "'");
		q.agregarParametroValues("TIM_HORA_INSTALACION", "'" + informacionServicioRequest.getHoraInstalacion() + "'");
		q.agregarParametroValues("FEC_VELACION", "'" + informacionServicioRequest.getFechaVelacion() + "'");
		q.agregarParametroValues("TIM_HORA_VELACION", "'" + informacionServicioRequest.getHoraVelacion() + "'");
		q.agregarParametroValues("ID_CAPILLA", "" + informacionServicioRequest.getIdCapilla() + "");
		q.agregarParametroValues("ID_INFORMACION_SERVICIO", "" + idInformacionServicio);
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuarioAlta + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere("ID_INFORMACION_SERVICIO_VELACION="+informacionServicioRequest.getIdInformacionServicioVelacion());
		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}
}

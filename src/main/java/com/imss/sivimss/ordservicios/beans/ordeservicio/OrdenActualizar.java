package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleTrasladoRequest;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.request.OrdenesServicioRequest;
import com.imss.sivimss.ordservicios.model.request.TareasDTO;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPaqueteDetallePresupuestoResponse;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPaqueteDetalleResponse;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPaquetePresupuestoResponse;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPaqueteResponse;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPresupuestoResponse;
import com.imss.sivimss.ordservicios.model.response.ContratanteResponse;
import com.imss.sivimss.ordservicios.model.response.DetalleOrdenesServicioResponse;
import com.imss.sivimss.ordservicios.model.response.FinadoResponse;
import com.imss.sivimss.ordservicios.model.response.InformacionServicioResponse;
import com.imss.sivimss.ordservicios.model.response.InformacionServicioVelacionResponse;
import com.imss.sivimss.ordservicios.model.response.OrdenServicioResponse;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class OrdenActualizar {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;

	@Value("${endpoints.ms-procesos}")
	private String urlProceso;

	@Value("${tipoHoraMinuto}")
	private String tipoHoraMinuto;

	@Value("${totalHoraMinuto}")
	private String totalHoraMinuto;

	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;

	@Autowired
	private ProviderServiceRestTemplate resTemplateProviderServiceRestTemplate;

	@Autowired
	private Contratante contratante;

	@Autowired
	private Finado finado;

	@Autowired
	private CaracteristicasPresupuesto caracteristicasPresupuesto;

	@Autowired
	private InformacionServicio informacionServicio;

	@Autowired
	private LogUtil logUtil;

	private Connection connection;

	private ResultSet rs;

	private Statement statement;

	@Autowired
	private Database database;

	private Response<Object> response;

	private String cveTarea;

	private static final String EXITO = "47"; // La Orden de Servicio se ha generado exitosamente.

	private Integer idEstatusTipoOrden;

	private Boolean desactivado = false;

	private static final Logger log = LoggerFactory.getLogger(OrdenActualizar.class);

	public Response<Object> actualizar(DatosRequest datosRequest, Authentication authentication)
			throws IOException, SQLException {
		String query = "El tipo orden de servicio es incorrecto.";

		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "actualizarOrden", AppConstantes.ALTA, authentication);

			Gson gson = new Gson();
			UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
			String datosJson = datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			OrdenesServicioRequest ordenesServicioRequest = gson.fromJson(datosJson, OrdenesServicioRequest.class);
			switch (ordenesServicioRequest.getFinado().getIdTipoOrden()) {
			case 1:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "servicioInmediato", AppConstantes.ALTA,
						authentication);
				response = guardarOrdenServicio(ordenesServicioRequest, usuario, authentication);
				break;
			case 2:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "contratoPF", AppConstantes.ALTA, authentication);

				response = guardarOrdenServicio(ordenesServicioRequest, usuario, authentication);
				break;
			case 3:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "articulosComplementarios", AppConstantes.ALTA,
						authentication);

				response = guardarVentaArticulos(ordenesServicioRequest, datosJson, usuario, authentication);
				break;
			case 4:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "convenioPF", AppConstantes.ALTA, authentication);

				query = convenioPF(ordenesServicioRequest);
				break;
			default:
				throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
			}

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.ALTA,
					authentication);
			throw new IOException(AppConstantes.ERROR_GUARDAR, e.getCause());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	public Response<Object> consultarDetallePreOrden(DatosRequest datosRequest,
			Authentication authentication) throws SQLException {
		ResultSet resultSetDetallePresupuesto =null;
		ResultSet resultSetDetallePresupuestoTraslado=null;
		ResultSet resultSetDetalle=null;
		ResultSet resultSetDetalleTraslado=null;
		try {
			connection = database.getConnection();
			connection.setAutoCommit(false);
			Gson gson = new Gson();
			String datosJson = datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			OrdenesServicioRequest ordenesServicioRequest=gson.fromJson(datosJson, OrdenesServicioRequest.class);
			DetalleOrdenesServicioResponse servicioResponse= new DetalleOrdenesServicioResponse();
			ContratanteResponse contratanteResponse= null;
			DomicilioRequest domicilioRequest;
			FinadoResponse finadoResponse=null;
			CaracteristicasPresupuestoResponse caracteristicasPresupuestoResponse=null;
			InformacionServicioResponse informacionServicioResponse=null;
			statement = connection.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.consultarOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));
		
			// orden de servicio
			if (rs.next()) {
				servicioResponse.setIdOrdenServicio(rs.getInt(1));
				servicioResponse.setFolio(rs.getString(2));
				servicioResponse.setIdParentesco(rs.getInt(3));
				servicioResponse.setIdVelatorio(rs.getInt(4));
				servicioResponse.setIdOperador(rs.getInt(5));
				servicioResponse.setIdEstatus(rs.getInt(6));
				servicioResponse.setIdContratantePf(rs.getInt(7));
				
				// contratante
				rs = statement.executeQuery(reglasNegocioRepository.consultarContratanteOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));
				if (rs.next()) {
					contratanteResponse= new ContratanteResponse();
					domicilioRequest= new DomicilioRequest();
					contratanteResponse.setIdPersona(rs.getInt(1));
					contratanteResponse.setIdContratante(rs.getInt(2));
					contratanteResponse.setMatricula(rs.getString(3));
					contratanteResponse.setRfc(rs.getString(4));
					contratanteResponse.setCurp(rs.getString(5));
					contratanteResponse.setNomPersona(rs.getString(6));
					contratanteResponse.setPrimerApellido(rs.getString(7));
					contratanteResponse.setSegundoApellido(rs.getString(8));
					contratanteResponse.setSexo(rs.getString(9));
					contratanteResponse.setOtroSexo(rs.getString(10));
					contratanteResponse.setFechaNac(rs.getString(11));
					contratanteResponse.setNacionalidad(rs.getString(12));
					contratanteResponse.setIdPais(rs.getString(13));
					contratanteResponse.setIdEstado(rs.getString(14));
					contratanteResponse.setTelefono(rs.getString(15));
					contratanteResponse.setCorreo(rs.getString(16));
					domicilioRequest.setIdDomicilio(rs.getInt(17));
					domicilioRequest.setDesCalle(rs.getString(18));
					domicilioRequest.setNumExterior(rs.getString(19));
					domicilioRequest.setNumInterior(rs.getString(20));
					domicilioRequest.setCodigoPostal(rs.getString(21));
					domicilioRequest.setDesColonia(rs.getString(22));
					domicilioRequest.setDesMunicipio(rs.getString(23));
					domicilioRequest.setDesEstado(rs.getString(24));
					
					contratanteResponse.setCp(domicilioRequest.getIdDomicilio()==0 || domicilioRequest.getIdDomicilio()==null?null:domicilioRequest);
					
					servicioResponse.setContratante(contratanteResponse);
				}
				
				rs = statement.executeQuery(reglasNegocioRepository.consultarFinadoOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));
				
				// finado
				
				if (rs.next()) {
					finadoResponse= new FinadoResponse();
					domicilioRequest= new DomicilioRequest();
					finadoResponse.setIdFinado(rs.getInt(1));
					finadoResponse.setIdPersona(rs.getInt(2));
					finadoResponse.setIdTipoOrden(rs.getInt(3));
					finadoResponse.setExtremidad(rs.getString(4));;
					finadoResponse.setEsobito(rs.getString(5));
					finadoResponse.setMatricula(rs.getString(6));
					finadoResponse.setRfc(rs.getString(7));
					finadoResponse.setCurp(rs.getString(8));
					finadoResponse.setNss(rs.getInt(9));
					finadoResponse.setNomPersona(rs.getString(10));
					finadoResponse.setPrimerApellido(rs.getString(11));
					finadoResponse.setSegundoApellido(rs.getString(12));
					finadoResponse.setSexo(rs.getString(13));
					finadoResponse.setOtroSexo(rs.getString(14));
					finadoResponse.setFechaNac(rs.getString(15));
					finadoResponse.setNacionalidad(rs.getString(16));
					finadoResponse.setIdPais(rs.getString(17));
					finadoResponse.setIdEstado(rs.getString(18));
					finadoResponse.setTelefono(rs.getString(19));
					finadoResponse.setCorreo(rs.getString(20));
					domicilioRequest.setIdDomicilio(rs.getInt(21));
					domicilioRequest.setDesCalle(rs.getString(22));
					domicilioRequest.setNumExterior(rs.getString(23));
					domicilioRequest.setNumInterior(rs.getString(24));
					domicilioRequest.setCodigoPostal(rs.getString(25));
					domicilioRequest.setDesColonia(rs.getString(26));
					domicilioRequest.setDesMunicipio(rs.getString(27));
					domicilioRequest.setDesEstado(rs.getString(28));
					finadoResponse.setCp(domicilioRequest.getIdDomicilio()==0 || domicilioRequest.getIdDomicilio()==null?null:domicilioRequest);
					finadoResponse.setFechaDeceso(rs.getString(29));
					finadoResponse.setCausaDeceso(rs.getString(30));
					finadoResponse.setLugarDeceso(rs.getString(31));
					finadoResponse.setHora(rs.getString(32));
					finadoResponse.setIdClinicaAdscripcion(rs.getString(33));
					finadoResponse.setIdUnidadProcedencia(rs.getString(34));
					finadoResponse.setProcedenciaFinado(rs.getString(35));
					finadoResponse.setIdTipoPension(rs.getInt(36));

					servicioResponse.setFinado(finadoResponse);
			}
			
			// caracteristicas presupuesto paquete
				CaracteristicasPaqueteResponse caracteristicasPaqueteResponse=null;
				List<CaracteristicasPaqueteDetalleResponse> caracteristicasPaqueteDetalleResponse=null;
				CaracteristicasPaqueteDetalleTrasladoRequest caracteristicasPaqueteDetalleTrasladoRequest=null;
				caracteristicasPresupuestoResponse= new CaracteristicasPresupuestoResponse();
			rs = statement.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoPaqueteTempOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));
			if (rs.next()) {
				
				caracteristicasPaqueteResponse= new CaracteristicasPaqueteResponse();
				caracteristicasPaqueteResponse.setIdCaracteristicasPaquete(rs.getInt(1));
				caracteristicasPaqueteResponse.setIdPaquete(rs.getInt(2));
				caracteristicasPaqueteResponse.setOtorgamiento(rs.getString(3));
				
				resultSetDetalle = statement.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoDetallePaqueteTempOrdenServicios(caracteristicasPaqueteResponse.getIdCaracteristicasPaquete()));
				caracteristicasPaqueteDetalleResponse= new ArrayList<>();
				CaracteristicasPaqueteDetalleResponse detalleResponse=null;
				
				while (resultSetDetalle.next()) {
					detalleResponse= new CaracteristicasPaqueteDetalleResponse();
					detalleResponse.setIdPaqueteDetalle(resultSetDetalle.getInt(1));
					detalleResponse.setIdArticulo(resultSetDetalle.getInt(2)==0?null:resultSetDetalle.getInt(2));
					detalleResponse.setIdServicio(resultSetDetalle.getInt(3)==0?null:resultSetDetalle.getInt(3));
					detalleResponse.setIdTipoServicio(resultSetDetalle.getInt(4)==0?null:resultSetDetalle.getInt(4));
					detalleResponse.setGrupo(resultSetDetalle.getString(5));
					detalleResponse.setConcepto(resultSetDetalle.getString(6));
					detalleResponse.setDesmotivo(resultSetDetalle.getString(7));
					detalleResponse.setActivo(resultSetDetalle.getInt(8));
					detalleResponse.setCantidad(resultSetDetalle.getInt(9));
					detalleResponse.setIdProveedor(resultSetDetalle.getInt(10));
					detalleResponse.setNombreProveedor(resultSetDetalle.getString(11));
					detalleResponse.setImporteMonto(resultSetDetalle.getDouble(12));
					detalleResponse.setTotalPaquete(resultSetDetalle.getDouble(13));
					resultSetDetalleTraslado = statement.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoDetallePaqueteTrasladoTempOrdenServicios(detalleResponse.getIdPaqueteDetalle()));
					caracteristicasPaqueteDetalleTrasladoRequest= null;
					if (resultSetDetalleTraslado.next()) {
						caracteristicasPaqueteDetalleTrasladoRequest= new CaracteristicasPaqueteDetalleTrasladoRequest();
						caracteristicasPaqueteDetalleTrasladoRequest.setIdCaracteristicasPaqueteDetalleTraslado(resultSetDetalleTraslado.getInt(1));
						caracteristicasPaqueteDetalleTrasladoRequest.setOrigen(resultSetDetalleTraslado.getString(2));
						caracteristicasPaqueteDetalleTrasladoRequest.setDestino(resultSetDetalleTraslado.getString(3));
						caracteristicasPaqueteDetalleTrasladoRequest.setTotalKilometros(resultSetDetalleTraslado.getString(4));
						caracteristicasPaqueteDetalleTrasladoRequest.setLatitudInicial(resultSetDetalleTraslado.getDouble(5));
						caracteristicasPaqueteDetalleTrasladoRequest.setLatitudFinal(resultSetDetalleTraslado.getDouble(6));
						caracteristicasPaqueteDetalleTrasladoRequest.setLongitudInicial(resultSetDetalleTraslado.getDouble(7));
						caracteristicasPaqueteDetalleTrasladoRequest.setLongitudFinal(resultSetDetalleTraslado.getDouble(8));
						caracteristicasPaqueteDetalleTrasladoRequest.setIdDetalleCaracteristicas(detalleResponse.getIdPaqueteDetalle());
					}
					detalleResponse.setServicioDetalleTraslado(Objects.isNull(caracteristicasPaqueteDetalleTrasladoRequest)?null:caracteristicasPaqueteDetalleTrasladoRequest);
					caracteristicasPaqueteDetalleResponse.add(detalleResponse);
				}
				caracteristicasPaqueteResponse.setDetallePaquete(caracteristicasPaqueteDetalleResponse);
				caracteristicasPresupuestoResponse.setCaracteristicasPaqueteResponse(Objects.isNull(caracteristicasPaqueteResponse)?null:caracteristicasPaqueteResponse);
			}
			// caracteristicas presupuesto presupuesto
			CaracteristicasPaquetePresupuestoResponse caracteristicasPaquetePresupuestoResponse=null;
			List<CaracteristicasPaqueteDetallePresupuestoResponse> caracteristicasPaqueteDetallePresupuestoResponse= new ArrayList<>();
			CaracteristicasPaqueteDetallePresupuestoResponse paqueteDetallePresupuesto;
			CaracteristicasPaqueteDetalleTrasladoRequest caracteristicasPresupuestoDetalleTrasladoRequest=null;

			
			rs = statement.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoPresupuestoTempOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));
			if (rs.next()) {
				caracteristicasPaquetePresupuestoResponse= new CaracteristicasPaquetePresupuestoResponse();
				caracteristicasPaquetePresupuestoResponse.setIdCaracteristicasPresupuesto(rs.getInt(1));
				caracteristicasPaquetePresupuestoResponse.setIdPaquete(rs.getInt(2));
				caracteristicasPaquetePresupuestoResponse.setNotasServicio(rs.getString(3));
				caracteristicasPaquetePresupuestoResponse.setObservaciones(rs.getString(4));
				caracteristicasPaquetePresupuestoResponse.setTotalPresupuesto(rs.getString(5));
				resultSetDetallePresupuesto = statement.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoDetallePresupuestoTempOrdenServicios(caracteristicasPaquetePresupuestoResponse.getIdCaracteristicasPresupuesto(),ordenesServicioRequest.getIdOrdenServicio()));
				while (resultSetDetallePresupuesto.next()) {
					paqueteDetallePresupuesto= new CaracteristicasPaqueteDetallePresupuestoResponse();
					paqueteDetallePresupuesto.setIdPaqueteDetallePresupuesto(resultSetDetallePresupuesto.getInt(1));
					paqueteDetallePresupuesto.setIdCategoria(resultSetDetallePresupuesto.getInt(2)==0?null:resultSetDetallePresupuesto.getInt(2));
					paqueteDetallePresupuesto.setIdArticulo(resultSetDetallePresupuesto.getInt(3)==0?null:resultSetDetallePresupuesto.getInt(3));
					paqueteDetallePresupuesto.setIdInventario(resultSetDetallePresupuesto.getInt(4)==0?null:resultSetDetallePresupuesto.getInt(4));
					paqueteDetallePresupuesto.setIdServicio(resultSetDetallePresupuesto.getInt(5)==0?null:resultSetDetallePresupuesto.getInt(5));
					paqueteDetallePresupuesto.setGrupo(resultSetDetallePresupuesto.getString(6));
					paqueteDetallePresupuesto.setConcepto(resultSetDetallePresupuesto.getString(7));
					paqueteDetallePresupuesto.setCantidad(resultSetDetallePresupuesto.getInt(8));
					paqueteDetallePresupuesto.setIdProveedor(resultSetDetallePresupuesto.getInt(9));
					paqueteDetallePresupuesto.setNombreProveedor(resultSetDetallePresupuesto.getString(10));
					paqueteDetallePresupuesto.setEsDonado(resultSetDetallePresupuesto.getInt(11));
					paqueteDetallePresupuesto.setImporteMonto(resultSetDetallePresupuesto.getDouble(12));
					resultSetDetallePresupuestoTraslado = statement.executeQuery(
							reglasNegocioRepository.consultarCaracteristicasPresupuestoDetallePresupuestoTrasladoTempOrdenServicios(paqueteDetallePresupuesto.getIdPaqueteDetallePresupuesto()));
					caracteristicasPresupuestoDetalleTrasladoRequest=null;
					if (resultSetDetallePresupuestoTraslado.next()) {
						caracteristicasPresupuestoDetalleTrasladoRequest= new CaracteristicasPaqueteDetalleTrasladoRequest();
						caracteristicasPresupuestoDetalleTrasladoRequest.setIdCaracteristicasPaqueteDetalleTraslado(resultSetDetallePresupuestoTraslado.getInt(1));
						caracteristicasPresupuestoDetalleTrasladoRequest.setOrigen(resultSetDetallePresupuestoTraslado.getString(2));
						caracteristicasPresupuestoDetalleTrasladoRequest.setDestino(resultSetDetallePresupuestoTraslado.getString(3));
						caracteristicasPresupuestoDetalleTrasladoRequest.setTotalKilometros(resultSetDetallePresupuestoTraslado.getString(4));
						caracteristicasPresupuestoDetalleTrasladoRequest.setLatitudInicial(resultSetDetallePresupuestoTraslado.getDouble(5));
						caracteristicasPresupuestoDetalleTrasladoRequest.setLatitudFinal(resultSetDetallePresupuestoTraslado.getDouble(6));
						caracteristicasPresupuestoDetalleTrasladoRequest.setLongitudInicial(resultSetDetallePresupuestoTraslado.getDouble(7));
						caracteristicasPresupuestoDetalleTrasladoRequest.setLongitudFinal(resultSetDetallePresupuestoTraslado.getDouble(8));
						caracteristicasPresupuestoDetalleTrasladoRequest.setIdDetalleCaracteristicas(paqueteDetallePresupuesto.getIdPaqueteDetallePresupuesto());
					}
					paqueteDetallePresupuesto.setServicioDetalleTraslado(Objects.isNull(caracteristicasPresupuestoDetalleTrasladoRequest)?null:caracteristicasPresupuestoDetalleTrasladoRequest);
					caracteristicasPaqueteDetallePresupuestoResponse.add(paqueteDetallePresupuesto);
				}
				
				caracteristicasPaquetePresupuestoResponse.setDetallePresupuesto(caracteristicasPaqueteDetallePresupuestoResponse);
				caracteristicasPresupuestoResponse.setCaracteristicasDelPresupuesto(Objects.isNull(caracteristicasPaquetePresupuestoResponse)?null:caracteristicasPaquetePresupuestoResponse);
			}
			rs = statement.executeQuery(reglasNegocioRepository.consultarInformacionServicioOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));
			
			// informacion de servicio
			
			if (rs.next()) {
				informacionServicioResponse= new InformacionServicioResponse();
				informacionServicioResponse.setIdInformacionServicio(rs.getInt(1));
				informacionServicioResponse.setFechaCortejo(rs.getString(2));
				informacionServicioResponse.setHoraCortejo(rs.getString(3));
				informacionServicioResponse.setFechaRecoger(rs.getString(4));
				informacionServicioResponse.setHoraRecoger(rs.getString(5));
				informacionServicioResponse.setIdPanteon(rs.getInt(6));
				informacionServicioResponse.setIdSala(rs.getInt(7));
				informacionServicioResponse.setFechaCremacion(rs.getString(8));
				informacionServicioResponse.setHoraCremacion(rs.getString(9));
				informacionServicioResponse.setIdPromotor(rs.getInt(10));
				
				InformacionServicioVelacionResponse informacionServicioVelacionResponse=null;
				
				
				// informacion de servicio velacion 
				rs = statement.executeQuery(reglasNegocioRepository.consultarInformacionServicioVelacionOrdenServicios(informacionServicioResponse.getIdInformacionServicio()));
				if (rs.next()) {
					domicilioRequest= new DomicilioRequest();
					informacionServicioVelacionResponse= new InformacionServicioVelacionResponse();
					informacionServicioVelacionResponse.setIdInformacionServicioVelacion(rs.getInt(1));
					informacionServicioVelacionResponse.setFechaInstalacion(rs.getString(2));
					informacionServicioVelacionResponse.setHoraInstalacion(rs.getString(3));
					informacionServicioVelacionResponse.setFechaVelacion(rs.getString(4));
					informacionServicioVelacionResponse.setHoraVelacion(rs.getString(5));
					informacionServicioVelacionResponse.setIdCapilla(rs.getInt(6));
					domicilioRequest.setIdDomicilio(rs.getInt(7));
					domicilioRequest.setDesCalle(rs.getString(8));
					domicilioRequest.setNumExterior(rs.getString(9));
					domicilioRequest.setNumInterior(rs.getString(10));
					domicilioRequest.setCodigoPostal(rs.getString(11));
					domicilioRequest.setDesColonia(rs.getString(12));
					domicilioRequest.setDesMunicipio(rs.getString(13));
					domicilioRequest.setDesEstado(rs.getString(14));
					informacionServicioVelacionResponse.setCp(domicilioRequest.getIdDomicilio()==0 || domicilioRequest.getIdDomicilio()==null?null:domicilioRequest);
				}
				informacionServicioResponse.setInformacionServicioVelacion(Objects.isNull(informacionServicioVelacionResponse)?null:informacionServicioVelacionResponse);
			  }
			

			}
			servicioResponse.setContratante(contratanteResponse);
			servicioResponse.setFinado(Objects.isNull(finadoResponse)?null:finadoResponse);
			servicioResponse.setCaracteristicasPresupuesto(Objects.isNull(caracteristicasPresupuestoResponse)?null:caracteristicasPresupuestoResponse);
			servicioResponse.setInformacionServicio(Objects.isNull(informacionServicioResponse)?null:informacionServicioResponse);
			response= new Response<>(false, 200, AppConstantes.EXITO, ConvertirGenerico.convertInstanceOfObject(servicioResponse));
			
			return response;
		} finally {
			if (connection != null) {
				connection.close();
			}
			
			if (statement != null) {
				statement.close();
			}
			if (rs != null || resultSetDetalle != null || resultSetDetallePresupuesto !=null || resultSetDetallePresupuestoTraslado !=null || resultSetDetalleTraslado !=null) {
				rs.close();
			}
		}
	}
	


	private Response<Object> guardarOrdenServicio(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,
			Authentication authentication) throws IOException, SQLException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// desactiva registros si tipo de orden es diferente
		desactivarRegistros(ordenesServicioRequest, usuario, authentication);

		if (Boolean.TRUE.equals(desactivado)) {
			Integer idOrden = ordenesServicioRequest.getIdOrdenServicio();

			response = insertarOrdenServicios(ordenesServicioRequest, usuario);

			connection.commit();

			// mandar a llamar el job con el id de la orden para cancelarlo
			Object datosDesactivar = "{\"idODS\":" + idOrden + "}";
			TareasDTO tareasDesactivar = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"CANCELAR", datosDesactivar);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareasDesactivar,
					urlProceso.concat(AppConstantes.PROCESO), authentication);
			
			// mandar a llamar el job con la clave tarea
			if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
				Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
				TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
						"INSERT", datos);
				resTemplateProviderServiceRestTemplate
						.consumirServicioProceso(tareas, urlProceso.concat(AppConstantes.PROCESO), authentication);

				return response;

			}
		} else {
			// actualizar registro actual
			actualizarOrdenServicios(ordenesServicioRequest, usuario,authentication);
			connection.commit();
		}
		return response;
	}

	private Response<Object> guardarVentaArticulos(OrdenesServicioRequest ordenesServicioRequest, String datosJson,
			UsuarioDto usuario, Authentication authentication) throws IOException, SQLException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// desactiva registros si tipo de orden es diferente
		desactivarRegistros(ordenesServicioRequest, usuario, authentication);
		if (Boolean.TRUE.equals(desactivado)) {
			Integer idOrden = ordenesServicioRequest.getIdOrdenServicio();
			response = insertarVentaArticulo(ordenesServicioRequest, usuario);
			connection.commit();

			// mandar a llamar el job de desactivar
			Object datosDesactivar = "{\"idODS\":" + idOrden + "}";
			TareasDTO tareasDesactivar = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"CANCELAR", datosDesactivar);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareasDesactivar,
					urlProceso.concat(AppConstantes.PROCESO), authentication);
			// mandar a llamar el job con la clave tarea
			if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
				Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
				TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
						"INSERT", datos);
				resTemplateProviderServiceRestTemplate
						.consumirServicioProceso(tareas, urlProceso.concat(AppConstantes.PROCESO), authentication);
				return response;

			}
		} else {
			// actualizar registro actual
			actualizarVentaArticulo(ordenesServicioRequest, usuario,authentication);
			connection.commit();
		}
		return response;
	}

	private String convenioPF(OrdenesServicioRequest ordenesServicioRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	////////////////////// insertar/////////////////////////////////////////////////

	private Response<Object> insertarOrdenServicios(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario)
			throws SQLException {
		// contratante
		if (ordenesServicioRequest.getContratante() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
		}
		ordenesServicioRequest.getContratante().setIdContratante(contratante
				.insertarContratante(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));

		// orden de servicio
		// generar folio
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(), connection));
		}
		insertarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);

		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// cve tarea
			cveTarea = generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}

		// finado
		if (ordenesServicioRequest.getFinado() != null) {
			finado.insertarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);
		}

		// caracteristicas presupuesto
		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// temporales
			caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);

		} else {
			// buenas buenas
			caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);

		}

		// informacion servicio
		if (ordenesServicioRequest.getInformacionServicio() != null) {
			informacionServicio.insertarInformacionServicio(ordenesServicioRequest.getInformacionServicio(),
					ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}

		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);

		return response;
	}

	public Response<Object> insertarVentaArticulo(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario)
			throws SQLException {
		// contratante
		if (ordenesServicioRequest.getContratante() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
		}

		ordenesServicioRequest.getContratante().setIdContratante(contratante
				.insertarContratante(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));

		// orden de servicio
		// generar folio
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(), connection));
		}
		insertarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);

		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// cve tarea
			cveTarea = generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}

		// finado
		if (ordenesServicioRequest.getFinado() != null) {
			finado.insertarFinadoVentaArticulo(ordenesServicioRequest.getFinado(),
					ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}

		// caracteristicas presupuesto
		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// temporales
			caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);

		} else {
			// buenas buenas
			caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);

		}

		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);
		return response;

	}

	////////////////////////////// actualizar////////////////////////////////////

	private Response<Object> actualizarOrdenServicios(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,Authentication authentication)
			throws SQLException, IOException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// contratante
		if (ordenesServicioRequest.getContratante() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
		}
		ordenesServicioRequest.getContratante().setIdContratante(contratante
				.actualizarContacto(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));

		// orden de servicio
		// generar folio
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(), connection));
		}

		actualizarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);

		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// cve tarea
			cveTarea = generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}

		// finado
		if (ordenesServicioRequest.getFinado() != null) {
			finado.actualizarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);
		}
		
		// caracteristicas presupuesto 
		if (ordenesServicioRequest.getIdEstatus() ==1) { // temporales
		  desactivarRegistrosTemp(ordenesServicioRequest, usuario, authentication);
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(),
		  connection);
		  
		  } else { // buenas buenas
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(),
		  connection);
		  
		  }
		 
		// informacion servicio
		if (ordenesServicioRequest.getInformacionServicio() != null) {
			informacionServicio.actualizarInformacionServicio(ordenesServicioRequest.getInformacionServicio(),
					ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}

		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);

		// mandar a llamar el job de desactivar
		Object datosDesactivar = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
		TareasDTO tareasDesactivar = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"CANCELAR", datosDesactivar);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareasDesactivar,
		urlProceso.concat(AppConstantes.PROCESO), authentication);
		
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
			
			// mandar a llamar el job con la clave tarea
			Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
			TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"ACTUALIZAR", datos);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareas,
					urlProceso.concat(AppConstantes.PROCESO), authentication);
			return response;

		}

		return response;
	}
	
	private Response<Object> actualizarVentaArticulo(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,Authentication authentication)
			throws SQLException, IOException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// contratante
		if (ordenesServicioRequest.getContratante() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
		}
		ordenesServicioRequest.getContratante().setIdContratante(contratante
				.actualizarContacto(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));

		// orden de servicio
		// generar folio
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(), connection));
		}

		actualizarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);

		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// cve tarea
			cveTarea = generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}

		// finado
		if (ordenesServicioRequest.getFinado() != null) {
			finado.actualizarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);
		}
		
		// caracteristicas presupuesto 
		if (ordenesServicioRequest.getIdEstatus() ==1) { // temporales
		  desactivarRegistrosTemp(ordenesServicioRequest, usuario, authentication);
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(),
		  connection);
		  
		  } else { // buenas buenas
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(),
		  connection);  
		}
		 
		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);

		// mandar a llamar el job de desactivar
		Object datosDesactivar = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
		TareasDTO tareasDesactivar = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"CANCELAR", datosDesactivar);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareasDesactivar,
		urlProceso.concat(AppConstantes.PROCESO), authentication);
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
			
		
			// mandar a llamar el job con la clave tarea
			Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
			TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"ACTUALIZAR", datos);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareas,
					urlProceso.concat(AppConstantes.PROCESO), authentication);
			return response;

		}

		return response;
	}

	private void desactivarRegistros(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,
			Authentication authentication) throws SQLException {
		consultarEstatusOrden(ordenesServicioRequest.getIdOrdenServicio());
		try {
			if (!(Objects.equals(ordenesServicioRequest.getFinado().getIdTipoOrden(), idEstatusTipoOrden))) {
				statement = connection.createStatement();
				desactivarRegistrosTemp(ordenesServicioRequest, usuario, authentication);
				statement.executeUpdate(
						reglasNegocioRepository.actualizarEstatusOrden(ordenesServicioRequest.getIdOrdenServicio()));
				desactivado = true;
			}
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}

	}
	
	private void desactivarRegistrosTemp(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,
			Authentication authentication) throws SQLException {
		consultarEstatusOrden(ordenesServicioRequest.getIdOrdenServicio());
		try {
			statement = connection.createStatement();
			statement.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPaqueteTemporal(ordenesServicioRequest.getIdOrdenServicio()));
			statement.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPaqueteDetalleTemp(ordenesServicioRequest.getIdOrdenServicio()));
			statement.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPresupuestoTemporal(ordenesServicioRequest.getIdOrdenServicio()));
			statement.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPresuestoDetalleTemp(ordenesServicioRequest.getIdOrdenServicio()));
			statement.executeUpdate(reglasNegocioRepository
					.actualizarDonacionTemporal(ordenesServicioRequest.getIdOrdenServicio()));
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}

	}

	public void consultarEstatusOrden(Integer idOrdenServicio) throws SQLException {
		try {

			statement = connection.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.consultarOrdenServicio(idOrdenServicio));

			if (rs.next()) {
				idEstatusTipoOrden = rs.getInt("idEstatusOrdenServicio");
			}
		
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	////////////////////// insertar
	private String generarFolio(Integer idVelatorio, Connection con) throws SQLException {
		String folio = null;
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.obtenerFolio(idVelatorio));

			if (rs.next()) {
				folio = rs.getString("folio");
			}
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return folio;
	}

	private String generarCveTarea(Integer ordenServicio, Connection con) throws SQLException {
		try {

			// generar una funcion de 10 caracteres random
			String cveTarea = "OD" + ordenServicio + OrdenesServicioUtil.cadenaAleatoria(8);
			// concatenar el id de la orden mas los 10 caracteres

			// hacer update en la tabla orden de servicio al campo cve_tarea
			statement = con.createStatement();
			statement.executeQuery(reglasNegocioRepository.actualizarCveTarea(ordenServicio, cveTarea));
			return cveTarea;
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}

	}

	private void insertarOrdenServicio(OrdenesServicioRequest ordenesServicioRequest, Integer idUsuarioAlta,
			Connection con) throws SQLException {
		try {
			statement = con.createStatement();
			statement.executeUpdate(reglasNegocioRepository.insertarOrdenServicio(ordenesServicioRequest.getFolio(),
					ordenesServicioRequest.getContratante().getIdContratante(),
					ordenesServicioRequest.getIdParentesco(), ordenesServicioRequest.getIdVelatorio(),
					ordenesServicioRequest.getIdContratantePf(), ordenesServicioRequest.getIdEstatus(), idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				ordenesServicioRequest.setIdOrdenServicio(rs.getInt(1));
			}
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void actualizarOrdenServicio(OrdenesServicioRequest ordenesServicioRequest, Integer idUsuarioAlta,
			Connection con) throws SQLException {
		try {
			statement = con.createStatement();
			statement.executeUpdate(reglasNegocioRepository.actualizarOrdenServicio(
					ordenesServicioRequest.getIdOrdenServicio(), ordenesServicioRequest.getFolio(),
					ordenesServicioRequest.getIdParentesco(), ordenesServicioRequest.getIdContratantePf(),
					ordenesServicioRequest.getIdEstatus(), idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void insertarPagoBitacora(OrdenesServicioRequest ordenesServicioRequest, Integer idUsuarioAlta,
			Connection con) throws SQLException {
		try {
			statement = con.createStatement();
			statement.executeUpdate(
					reglasNegocioRepository.insertarBitacoraPago(ordenesServicioRequest.getIdOrdenServicio(),
							ordenesServicioRequest.getIdVelatorio(),
							ordenesServicioRequest.getContratante().getNomPersona() + " "
									+ ordenesServicioRequest.getContratante().getPrimerApellido() + " "
									+ ordenesServicioRequest.getContratante().getSegundoApellido(),
							ordenesServicioRequest.getFolio(),
							ordenesServicioRequest.getCaracteristicasPresupuesto().getCaracteristicasDelPresupuesto()
									.getTotalPresupuesto(),
							ordenesServicioRequest.getIdEstatus(), idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();

		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	private Response<Object> consultarOrden(Integer idOrdenServicio, Connection con) throws SQLException {
		try {
			OrdenServicioResponse ordenServicioResponse;
			statement = con.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.consultarOrdenServicio(idOrdenServicio));

			if (rs.next()) {
				ordenServicioResponse = new OrdenServicioResponse(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4));
				response = new Response<>(false, 200, EXITO,
						ConvertirGenerico.convertInstanceOfObject(ordenServicioResponse));

			}
			return response;
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}
	

	
	

}

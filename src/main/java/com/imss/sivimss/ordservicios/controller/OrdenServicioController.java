package com.imss.sivimss.ordservicios.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.ordservicios.service.OrdenServicioService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/ordenes-servicio")
public class OrdenServicioController {
	
	private final OrdenServicioService ordenServicioService;
	
	private final ProviderServiceRestTemplate providerRestTemplate;
	
	private final String RESILIENCIA = "Resiliencia";
	
	private final  LogUtil logUtil;
	
	@PostMapping("/consultar/rfc")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarRfc(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "buscarRfc");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/consultar/curp")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarCurp(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "buscarCurp");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/guardar")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>agregar(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "guardar");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
//
	@PostMapping("/consultar/velatorio")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarVelatorio(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarVelatorio");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/folio-ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarFolioODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarFolioODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/contratante")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarContratante(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarContratante");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}

	@PostMapping("/consultar/nombreContratante")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarNombreContratante(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarNombreContratante");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/finado")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarFinado(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarFinado");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/nombreFinado")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarNombreFinado(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarNombreFinado");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/tipo-ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarTipoODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarTipoODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/unidad-medica")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarUnidadMedica(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarUnidadMedica");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/contrato-convenio")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarContratoConvenio(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarContratoConvenio");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/consultar/estado-ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarEstadoODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarEstadoODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/consultar/operadores")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarOperadores(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarOperadores");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/consultar/tarjeta-identificacion")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>generaTarjetaIdentificacion(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "generaTarjetaIdentificacion");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/consultar/costo-cancelar-ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarCostoCancelarODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarCostoCancelarODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/cancelar/ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarCancelarODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "cancelarODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/consultar/ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/generar/tarjeta-identificacion")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generaReporteTarjetaIdentificacion(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "generaReporteTarjetaIdentificacion");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/generar/reporte-consulta-ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generaReporteConsulta(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "generaReporteConsultaODS");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/consultar/detalle")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarDetalleOrdenServicio(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "detalle-preorden");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/generar/reporte-contrato-serv-inmediato")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generaReporteServicioInmediato(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "generaReporteServicioInmediato");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/generar/reporte-orden-servicio")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generaReporteOrdenServicio(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "generaReporteOrdenServicio");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/generar/reporte-ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generaReporteODS(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "generaReporteODS");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/consulta/reporte-ods")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> consultaReporteODS(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "consultaReporteODS");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/actualizar")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>actualizarOrdenServicio(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "actualizar");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/generar/reporte-salida-donacion")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generaReporteSalidaDonacion(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "generaReporteSalidaDonacion");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/generar/reporte-donacion")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generaReporteDonacion(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "generaReporteDonacion");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/generar/reporte-detalle-is")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generaReporteDetalleIS(@RequestBody DatosRequest request,Authentication authentication) throws IOException, SQLException {
		Response<Object> response =  ordenServicioService.peticionOrden(request, authentication, "generaReporteDetalleIS");
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	/**
	 * fallbacks generico
	 * 
	 * @return respuestas
	 * @throws IOException 
	 */
	private CompletableFuture<Object> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
	    logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), RESILIENCIA, AppConstantes.CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<Object> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			RuntimeException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
	    logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), RESILIENCIA, AppConstantes.CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<Object> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			NumberFormatException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
	    logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), RESILIENCIA, AppConstantes.CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
}

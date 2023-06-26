package com.imss.sivimss.ordservicios.controller;

import java.io.IOException;
import java.sql.SQLDataException;
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
	public CompletableFuture<Object>consultarRfc(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "buscarRfc");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/consultar/curp")
	public CompletableFuture<Object>consultarCurp(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "buscarCurp");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/guardar")
	public CompletableFuture<Object>agregar(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "guardar");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
//
	@PostMapping("/consultar/velatorio")
	public CompletableFuture<Object>consultarVelatorio(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarVelatorio");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/folioODS")
	public CompletableFuture<Object>consultarFolioODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarFolioODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/contratante")
	public CompletableFuture<Object>consultarContratante(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarContratante");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/finado")
	public CompletableFuture<Object>consultarFinado(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarFinado");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/tipoODS")
	public CompletableFuture<Object>consultarTipoODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarTipoODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/unidad")
	public CompletableFuture<Object>consultarUnidadMedica(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarUnidadMedica");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	@PostMapping("/consultar/contratoConvenio")
	public CompletableFuture<Object>consultarContratoConvenio(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarContratoConvenio");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/consultar/estadoODS")
	public CompletableFuture<Object>consultarEstadoODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarEstadoODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/generaTarjetaIden")
	public CompletableFuture<Object>generaTarjetaIdentificacion(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "generaTarjetaIdentificacion");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/cancelarODS")
	public CompletableFuture<Object>consultarCancelarODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "cancelarODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/consultar/ods")
	public CompletableFuture<Object>consultarODS(@RequestBody DatosRequest request, Authentication authentication) throws IOException, SQLException{
		Response<?>response=ordenServicioService.peticionOrden(request, authentication, "consultarODS");
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
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

package com.imss.sivimss.ordservicios.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.ordservicios.service.PaqueteService;
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
@RequestMapping("/paquete")
public class PaqueteController {
	
	private final PaqueteService paqueteService;
	
	private final ProviderServiceRestTemplate providerRestTemplate;
	
	private final LogUtil logUtil;
	
	@PostMapping("/consultar")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>obtenerPaquetes(@RequestBody DatosRequest request, Authentication authentication) throws IOException{
		Response<?>response=paqueteService.consultarPaquetes(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/consultar/servicios")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>obtenerServiciosPaquete(@RequestBody DatosRequest request, Authentication authentication) throws IOException{
		Response<?>response=paqueteService.consultarServiciosPaquete(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/consultar/caracteristicas")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>obtenerCaracteristicasPaquete(@RequestBody DatosRequest request, Authentication authentication) throws IOException{
		Response<?>response=paqueteService.consultarCaracteristicasPaquete(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/consultar/ataud/asignacion")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>obtenerAtaudAsignacion(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
		Response<?>response= paqueteService.consultarTipoAsignacionAtaud(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/consultar/ataud")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>obtenerAtaud(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
		Response<?>response= paqueteService.consultarAtaud(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/consultar/ataud/proveedor")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo",fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>obtenerProveedorAtaud(@RequestBody DatosRequest request, Authentication authentication) throws IOException{
		Response<?>response=paqueteService.consultarProveedorAtaud(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/consultar/ataud/inventario")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo",fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>obtenerAtaudInventario(@RequestBody DatosRequest request, Authentication authentication) throws IOException{
		Response<?>response=paqueteService.consultarAtaudInventario(request, authentication);
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
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", AppConstantes.CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<Object> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			RuntimeException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", AppConstantes.CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<Object> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			NumberFormatException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", AppConstantes.CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
}

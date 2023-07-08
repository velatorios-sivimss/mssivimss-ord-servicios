package com.imss.sivimss.ordservicios.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.model.request.TareasDTO;
import com.imss.sivimss.ordservicios.security.jwt.JwtTokenProvider;




@Service
public class ProviderServiceRestTemplate {

	@Autowired
	private RestTemplateUtil restTemplateUtil;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private static Logger log = LogManager.getLogger(ProviderServiceRestTemplate.class);
	
	private static final String ERROR_RECUPERAR_INFORMACION = "Ha ocurrido un error al recuperar la informacion";

	public Response<Object> consumirServicio(Map<String, Object> dato, String url, Authentication authentication)
			throws IOException {
		try {
			return restTemplateUtil.sendPostRequestByteArrayToken(url,
					new EnviarDatosRequest(dato), jwtTokenProvider.createToken((String) authentication.getPrincipal()),
					Response.class);
		} catch (IOException exception) {
			log.error(ERROR_RECUPERAR_INFORMACION);
			throw exception;
		}
	}

	public Response<Object> consumirServicioFlujo(Map<String, Object> dato, String url, Authentication authentication)
			throws IOException {
		try {
			return restTemplateUtil.sendPostRequestByteArrayToken(url,
					new EnviarDatosRequest(dato), jwtTokenProvider.createTokenFlujo((String) authentication.getPrincipal()),
					Response.class);
		} catch (IOException exception) {
			log.error(ERROR_RECUPERAR_INFORMACION);
			throw exception;
		}
	}
	public Response<Object> consumirServicioReportes(Map<String, Object> dato,
												String url, Authentication authentication) throws IOException {
		try {
			return (Response<Object>) restTemplateUtil.sendPostRequestByteArrayReportesToken(url,
					new DatosReporteDTO(dato),
					jwtTokenProvider.createToken((String) authentication.getPrincipal()), Response.class);
		} catch (IOException exception) {
			log.error(ERROR_RECUPERAR_INFORMACION);
			throw exception;
		}
	}
	
	public Response<Object> consumirServicioExternoGet(String url)
			throws IOException {
		try {
			return restTemplateUtil.sendGetRequest(url);
		} catch (IOException exception) {
			log.error(ERROR_RECUPERAR_INFORMACION);
			throw exception;
		}
	}

	public Response<Object> consumirServicioProceso(TareasDTO body, String url, Authentication authentication )
			throws IOException {
		try {
			
			return (Response<Object>) restTemplateUtil.sendPostRequestByteArrayTokenProcesos(url,
					new TareasDTO(body.getTipoHoraMinuto(), body.getCveTarea(), body.getTotalHoraMinuto(), body.getTipoEjecucion(), body.getValidacion(), body.getDatos()), 
					jwtTokenProvider.createToken((String) authentication.getPrincipal()),
					Response.class);
		} catch (IOException exception) {
			log.error(ERROR_RECUPERAR_INFORMACION);
			throw exception;
		}
	}

	public Response<?> respuestaProvider(String e) {
		  StringTokenizer exeception = new StringTokenizer(e, ":");
	        Gson gson = new Gson();
	        int totalToken = exeception.countTokens();
	        StringBuilder error = new StringBuilder("");
	        int i = 0;
	        int codigoError = HttpStatus.INTERNAL_SERVER_ERROR.value();

	        int isExceptionResponseMs = 0;
	        while (exeception.hasMoreTokens()) {
	            String str = exeception.nextToken();
	            i++;

	            if (i == 2) {
	                String[] palabras = str.split("\\.");
	                for (String palabra : palabras) {
	                    if ("BadRequestException".contains(palabra)) {
	                        codigoError = HttpStatus.BAD_REQUEST.value();
	                    } else if ("ResourceAccessException".contains(palabra)) {
	                        codigoError = HttpStatus.REQUEST_TIMEOUT.value();

	                    }
	                }
	            } else if (i == 3) {

	                if (str.trim().chars().allMatch(Character::isDigit)) {
	                    isExceptionResponseMs = 1;
	                }

	                error.append(codigoError == HttpStatus.REQUEST_TIMEOUT.value() ? AppConstantes.CIRCUITBREAKER : str);

	            } else if (i >= 4 && isExceptionResponseMs == 1) {
	                if (i == 4) {
	                    error.delete(0, error.length());
	                }
	                error.append(str).append(i != totalToken ? ":" : "");

	            }
	        }
	        Response<?> response;
	        try {
	            if (error.length() < 2)
	                response = new Response<>(true, codigoError, error.toString().trim(), Collections.emptyList());
	            else {
	                if (isExceptionResponseMs == 1) {
	                    if (error.length() == 1) {
	                        response = gson.fromJson(error.substring(1, error.length() - 1), Response.class);
	                    } else if (error.length() == 2) {
	                        response = gson.fromJson(error.substring(2, error.length() - 1), Response.class);
	                    } else {
	                        response = new Response<>(true, codigoError, error.toString().trim(), Collections.emptyList());
	                    }
	                } else {
	                    response = new Response<>(true, codigoError, error.toString().trim(), Collections.emptyList());
	                }
	            }
	        } catch (Exception e2) {
	            log.error("ex ->" + e2.getMessage());
	            return new Response<>(true, HttpStatus.REQUEST_TIMEOUT.value(), AppConstantes.CIRCUITBREAKER, Collections.emptyList());
	        }
	        return response;
	}

}
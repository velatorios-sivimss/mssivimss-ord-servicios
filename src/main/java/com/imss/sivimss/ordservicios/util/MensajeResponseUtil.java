package com.imss.sivimss.ordservicios.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MensajeResponseUtil {
	
	private static final Logger log = LoggerFactory.getLogger(MensajeResponseUtil.class);

	private MensajeResponseUtil() {
		super();
	}

	public static Response<Object> mensajeResponse(Response<Object> respuestaGenerado, String numeroMensaje) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200) {
			respuestaGenerado.setMensaje(numeroMensaje);
		} else {
			log.error("Error.. {}", respuestaGenerado.getMensaje());
			respuestaGenerado.setMensaje(AppConstantes.ERROR_GUARDAR);
		}
		return respuestaGenerado;
	}

	public static Response<?> mensajeConsultaResponse(Response<?> respuestaGenerado, String numeroMensaje) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200 && (!respuestaGenerado.getDatos().toString().contains("id"))) {
			respuestaGenerado.setMensaje(numeroMensaje);
		}
		return respuestaGenerado;
	}
	
	public static Response<Object> mensajeResponseExterno(Response<Object> respuestaGenerado, String numeroMensaje,
			String numeroMensajeError) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 400) {
			respuestaGenerado.setCodigo(200);
			respuestaGenerado.setMensaje(numeroMensaje);
		} else if (codigo == 404 || codigo == 500) {
			respuestaGenerado.setMensaje(numeroMensajeError);
		}
		return respuestaGenerado;
	}
	
	public  static Response<Object>mensajeConsultaResponseObject(Response<Object> respuestaGenerado, String numeroMensaje) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200 &&  !(respuestaGenerado.getDatos().toString().contains("[]")) ){
			respuestaGenerado.setMensaje(AppConstantes.EXITO);
		}else if (codigo == 400 || codigo == 404 || codigo == 500 ) {
			log.error("Error.. {}", respuestaGenerado.getMensaje());
			respuestaGenerado.setMensaje(numeroMensaje);
		}
		return respuestaGenerado;
	}
}

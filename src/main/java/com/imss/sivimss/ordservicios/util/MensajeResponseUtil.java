package com.imss.sivimss.ordservicios.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MensajeResponseUtil {
	
	private static final Logger log = LoggerFactory.getLogger(MensajeResponseUtil.class);

	private MensajeResponseUtil() {
		super();
	}

	public static Response<?> mensajeResponse(Response<?> respuestaGenerado, String numeroMensaje) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200) {
			respuestaGenerado.setMensaje(numeroMensaje);
		} else {
			log.error("Error.. {}", respuestaGenerado.getMensaje());
			respuestaGenerado.setMensaje("5");
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
	
	public static Response<?> mensajeResponseExterno(Response<?> respuestaGenerado, String numeroMensaje,
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
}

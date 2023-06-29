package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.util.concurrent.ThreadLocalRandom;

public class OrdenesServicioUtil {

	private static int numeroAleatorioEnRango(int minimo, int maximo) {

		// nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos

		// 1

		return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);

	}

	public static String cadenaAleatoria(int longitud) {

		String caracteres = "abcdefghijklmnñopqrsyuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYA1234567890";

		StringBuilder salida = new StringBuilder();

		for (int x = 0; x < longitud; x++) {

			int indiceAleatorio = numeroAleatorioEnRango(0, caracteres.length() - 1);

			char caracterAleatorio = caracteres.charAt(indiceAleatorio);

			salida.append(caracterAleatorio);

		}

		return salida.toString();

	}
}

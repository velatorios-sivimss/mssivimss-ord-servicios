package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinadoRequest extends Persona{

	private String idFinado;
	private Integer idTipoOrden;
	private String extremidad;
	private String esobito;
	private String matricula;
	private String idContratoPrevision;
	private String descalle;
	private String desnumexterior;
	private String numInterior;
	private DomicilioRequest cp;
	private String fechaDeceso;
	private String causaDeceso;
	private String lugarDeceso;
	private String hora;
	private String idClinicaAdscripcion;
	private String idUnidadProcedencia;
	private String procedenciaFinado;
	private String idTipoPension;
}

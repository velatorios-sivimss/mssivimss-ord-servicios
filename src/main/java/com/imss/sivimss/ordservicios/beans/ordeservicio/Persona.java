package com.imss.sivimss.ordservicios.beans.ordeservicio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Persona {

	private Integer idPersona;
	private String rfc;
	private String curp;
	private Integer nss;
	private String nomPersona;
	private String primerApellido;
	private String segundoApellido;
	private String sexo;
	private String otroSexo;
	private String fechaNac;
	private String nacionalidad;
	private String idPais;
	private String idEstado;
	private String telefono;
	private String correo;


	
}

package com.imss.sivimss.ordservicios.beans;

import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContratoPF {

	private ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private static ContratoPF instancia;
	
	private ContratoPF() {}
	
	public static ContratoPF getInstancia() {
		if (instancia==null) {
			instancia= new ContratoPF();
		}
		
		return instancia;
	}

	public ContratoPF(ProviderServiceRestTemplate providerServiceRestTemplate) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
	}
	
	public Response<?> consultarContrato(DatosRequest request,UsuarioDto usuario){
		return null;
	}
}

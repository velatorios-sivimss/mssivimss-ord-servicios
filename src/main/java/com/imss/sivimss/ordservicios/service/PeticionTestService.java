package com.imss.sivimss.ordservicios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.imss.sivimss.ordservicios.util.DatosArchivosRequest;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface PeticionTestService {

	Response<?>consultarOoad(DatosRequest request, Authentication authentication ) throws IOException;
	Response<?>agregarOoad(DatosRequest request, Authentication authentication ) throws IOException;
	Response<?> agregarOoadConArchivo(MultipartFile [] files,String datos, Authentication authentication) throws IOException;
}

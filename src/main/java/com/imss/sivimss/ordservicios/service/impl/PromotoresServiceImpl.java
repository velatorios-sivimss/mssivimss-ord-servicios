package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.Promotor;
import com.imss.sivimss.ordservicios.model.response.PromotorResponse;
import com.imss.sivimss.ordservicios.service.PromotorService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class PromotoresServiceImpl implements PromotorService {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;

	private final ProviderServiceRestTemplate providerServiceRestTemplate;

	private final ModelMapper modelMapper;

	private final Promotor promotor = Promotor.getInstancia();

	private static final Logger log = LoggerFactory.getLogger(PromotoresServiceImpl.class);

	private final LogUtil logUtil;

	public PromotoresServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper,
			LogUtil logUtil) {
		super();
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
		this.logUtil = logUtil;
	}

	@Override
	public Response<Object> consultarPromotores(DatosRequest request, Authentication authentication) throws IOException {
		Response<Object> response;
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarPromotores", AppConstantes.CONSULTA, authentication);
			response = providerServiceRestTemplate.consumirServicio(
					promotor.consultarPromotores().getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR),
					authentication);
			List<PromotorResponse> promotorResponses;
			if (response.getCodigo() == 200 && !response.getDatos().toString().contains("[]")) {
				promotorResponses = Arrays.asList(modelMapper.map(response.getDatos(), PromotorResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(promotorResponses));
			}
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = promotor.consultarPromotores().getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        log.error(e.getMessage());
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}

	}

}

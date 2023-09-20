package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.ReportePagoProveedor;
import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.model.request.ReportePagoProveedorDto;
import com.imss.sivimss.ordservicios.service.ReportePagoProveedorService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class ReportePagoProveedorImpl implements ReportePagoProveedorService{
	
	@Autowired
	private LogUtil logUtil;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Value("${reporte.generar_reporte_pago_proveedor}")
	private String reportePagoProv;
	
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";
	private static final String EXITO = "EXITO";
	private static final String IMPRIMIR = "IMPRIMIR";
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	ReportePagoProveedor pagoProveedor = new ReportePagoProveedor();
	
	Gson gson = new Gson();
	
	

	@Override
	public Response<?> generarReportePagoProveedor(DatosRequest request, Authentication authentication)
			throws IOException, ParseException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ReportePagoProveedorDto reporte= gson.fromJson(datosJson, ReportePagoProveedorDto.class);
		if(reporte.getFecha_inicial()!=null) {
			reporte.setFecInicioConsulta(formatFecha(reporte.getFecha_inicial()));
   	   		reporte.setFecFinConsulta(formatFecha(reporte.getFecha_final()));
		}
		if(reporte.getTipoReporte()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		Map<String, Object> envioDatos = pagoProveedor.generarReporte(reporte, reportePagoProv);
		return  providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes,
				authentication);
	//	logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"SE GENERO CORRECTAMENTE EL REPORTE SERVICIOS VELATORIOS", IMPRIMIR, authentication);
	//	return response;
	}
	
	 public String formatFecha(String fecha) throws ParseException {
			Date dateF = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
			DateFormat fecForma = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
			return fecForma.format(dateF);       
		}

}

package com.imss.sivimss.ordservicios.util;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.imss.sivimss.ordservicios.model.request.CorreoRequest;

@Component
public class GeneraCredencialesUtil {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Value("${endpoints.envio-correo}")
	private String urlEnvioCorreo;
	
	
	private static final Logger log = LoggerFactory.getLogger(GeneraCredencialesUtil.class);

	private static final String ERROR_RECUPERAR_INFORMACION = "Ha ocurrido un error al recuperar la informacion";

	public String generarContrasenia(String nombreCompleto, String paterno) {
		SecureRandom random = new SecureRandom();
        String caracteres = "#$^+=!*()@%&";
        int randomInt =random.nextInt(caracteres.length());
        String[] obtieneNombre = nombreCompleto.toLowerCase().split(" ");
        String nombre = obtieneNombre[0];
        for(int i=1; nombre.length()>i; i++  ) {
        	if(nombre.charAt(i)==nombre.charAt(i-1)) {
        		char letra = nombre.charAt(i);
        		char caracter = caracteres.charAt(randomInt);
        		nombre =nombre.replaceFirst(String.valueOf(letra), "?");
        	    nombre=nombre.replace(letra, caracter);
                nombre = nombre.replace("?", String.valueOf(letra));
            caracteres = caracteres.replace(String.valueOf(caracter), "");
        	}
        }
      
        char randomChar = caracteres.charAt(randomInt);
        char[] apellido= paterno.toLowerCase().toCharArray();
        
        char pLetra = apellido[0];//paterno 0
        String pLetraS = String.valueOf(pLetra); 
        char sLetra= apellido[1];
        if(pLetra==sLetra) { //SEGUNDA LETRA PATERNO
        	sLetra = caracteres.charAt(randomInt);
        }
        SimpleDateFormat fecActual = new SimpleDateFormat("MM");
		String numMes = fecActual.format(new Date());
		
		return nombre+randomChar+"."+pLetraS.toUpperCase()+sLetra+numMes;
	} 

	public String insertarUser(Integer numberUser, String nombreCompleto, String paterno, String contrasenia, Integer idPersona, Statement statement) throws SQLException{
		String hash = passwordEncoder.encode(contrasenia);
		String[] obtieneNombre = nombreCompleto.split(" ");
        String nombre = obtieneNombre[0];
        char[] apellido= paterno.toCharArray();
        char apM = apellido[0];
        String inicialApellido = String.valueOf(apM);
        String formatearCeros = String.format("%03d", numberUser);
		String user = nombre+inicialApellido+formatearCeros;
		 statement.executeUpdate(queryUsuario(idPersona, hash, user),
					Statement.RETURN_GENERATED_KEYS);
		return user;
	}

	
	
	private String queryUsuario(Integer idPersona, String contrasenia, String user) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_USUARIOS");
		q.agregarParametroValues("ID_PERSONA", idPersona.toString());
		q.agregarParametroValues("ID_OFICINA", "3");
		q.agregarParametroValues("ID_ROL", "150");
		q.agregarParametroValues("IND_ACTIVO", "1");
		q.agregarParametroValues("CVE_CONTRASENIA", "'"+contrasenia+"'");
		q.agregarParametroValues("CVE_USUARIO", "'"+user.toUpperCase()+"'");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_DATE()");
		q.agregarParametroValues("IND_CONTRATANTE", "1");
		return q.obtenerQueryInsertar();
	}

	public Response<Object> enviarCorreo(String user, String correo, String nombre, String paterno, String materno,
			String contrasenia) throws IOException {
		try {
			log.info("envioCorreo {} "+urlEnvioCorreo);
			String nombreUser = nombre.toUpperCase()+" "+paterno.toUpperCase()+" "+materno.toUpperCase(); 
			String credenciales = "<b>Nombre completo del Usuario:</b> "+nombreUser+"<br> <b>Clave de usuario: </b>"+user.toUpperCase() +"<br> <b>Contraseña: </b>"+contrasenia;
		
			CorreoRequest correoR = new CorreoRequest(user.toUpperCase(), credenciales, correo, AppConstantes.USR_CONTRASENIA);
			//Hacemos el consumo para enviar el codigo por correo
			return providerRestTemplate.consumirServicio(correoR, urlEnvioCorreo);
		} catch (Exception e) {
			log.error(ERROR_RECUPERAR_INFORMACION);
			return new Response<>(true, HttpStatus.OK.value(), ERROR_RECUPERAR_INFORMACION);
		}
		
	}


}

package es.tfg.controlador.security;

import com.sun.research.ws.wadl.Method;
import es.tfg.modelo.PersonasDAO;
import java.io.IOException;

import java.util.Base64;
import java.util.StringTokenizer;

public class AuthenticationService {

    private PersonasDAO personaDAO;

    public boolean authenticate(String authCredentials) {

        if (null == authCredentials) {
            return false;
        }
        // header value format will be "Basic encodedstring" for Basic
        // authentication. Example "Basic YWRtaW46YWRtaW4="
        final String encodedUserPassword = authCredentials.replaceFirst("Basic"
                + " ", "");
        String usernameAndPassword = null;
        try {
            //Method method = methodInvoked.getMethod();
            byte[] decodedBytes = Base64.getDecoder().decode(
                    encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        personaDAO = new PersonasDAO();
        return personaDAO.compruebaPersona(username, password);

    }

    public boolean authoritate(String authCredentials, String peticion, String url) {

        if (null == authCredentials) {
            return false;
        }
        // header value format will be "Basic encodedstring" for Basic
        // authentication. Example "Basic YWRtaW46YWRtaW4="
        final String encodedUserPassword = authCredentials.replaceFirst("Basic"
                + " ", "");
        String usernameAndPassword = null;
        try {
            //Method method = methodInvoked.getMethod();
            byte[] decodedBytes = Base64.getDecoder().decode(
                    encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        personaDAO = new PersonasDAO();
        boolean isAdmin = personaDAO.esAdmin(username);
        if (isAdmin) {
            return true;
        } else {
            //reglas de autorizacion
//            String[] peticionURL = url.split("/");
//            String s = peticionURL[0];
//            String t = peticionURL[1];
//            String r = peticionURL[2];
            return false;
        }
    }
}

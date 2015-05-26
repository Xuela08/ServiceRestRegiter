package es.tfg.controlador.security;

import es.tfg.modelo.Aporte;
import es.tfg.modelo.DAO.AportesDAO;
import es.tfg.modelo.DAO.PersonasDAO;
import es.tfg.modelo.DAO.TokenDAO;

public class AuthenticationService {

    private PersonasDAO personaDAO;
    private AportesDAO aporteDAO;
    private TokenDAO tokenDAO;
    public static String SERVICE_PERSONAS = "personas";
    public static String SERVICE_NOTICIAS = "noticias";

    public String authenticate(String authCredentials) {
        String usuarioToken = null;
        tokenDAO = new TokenDAO();
        if (null == authCredentials) {
            return usuarioToken;
        }
        usuarioToken = tokenDAO.verificaYusuario(authCredentials);

        return usuarioToken;

    }

    public boolean authoritate(String usuToken, String peticion, String url) {

        if (null == usuToken) {
            return false;
        }

        personaDAO = new PersonasDAO();
        aporteDAO = new AportesDAO();
        boolean isAdmin = personaDAO.esAdmin(usuToken);
        if (isAdmin) {
            return true;
        } else { //No tener en cuenta el caso de ser administrador
            String[] peticionURL = url.split("/");
            if (SERVICE_PERSONAS.equals(peticionURL[1])) { //Reglas de autorizacion del servicio personas
                
                //Get /personas
                if (peticion.equals("GET") && url.equals("/"+SERVICE_PERSONAS)) {
                    return true;
                } else if ((peticion.equals("PUT") || peticion.equals("DELETE") || peticion.equals("GET")) && usuToken.equals(peticionURL[2])) {
                    return true; //PUT-DELETE-GET /personas/{username} <-debe coincidir url con usuario
                } else {
                    return false;// RESTO DE OPCIONES NO PERMITIDAS
                }

            }else if(SERVICE_NOTICIAS.equals(peticionURL[1])){
                
                //Solo tiene restriccion el PUT y DELETE
                //pues debe ser el usuario
                //es decir si son esos métodos y no coincide el usuario, devolvemos false
                if ((peticion.equals("PUT") || peticion.equals("DELETE"))) {
                    long id = Long.parseLong(peticionURL[2]) ;
                    Aporte aporte = aporteDAO.obtenAporte(id);
                    if(aporte!=null){
                        if(usuToken.equals(aporte.getPropietario())) return true;
                    }
                    return false;
                }
                
                return true;
                
            }

            return false;
            //reglas de autorizacion
            //---------------------Como estaba solo ocn personas---------------------------
            /*
             String raizUrl = "/personas";
            
             boolean b1 = url.equals(raizUrl+"/"+usuToken);
             //Get /personas
             if(peticion.equals("GET") && url.equals(raizUrl)){
             return true;
             }else if((peticion.equals("PUT") || peticion.equals("DELETE") || peticion.equals("GET") )&& url.equals(raizUrl+"/"+usuToken) ){
             return true; //PUT-DELETE-GET /personas/{username} <-debe coincidir url con usuario
             }else{
             return false;// RESTO DE OPCIONES NO PERMITIDAS
             }*/
//            String[] peticionURL = url.split("/");
//            String s = peticionURL[0];
//            String t = peticionURL[1];
//            String r = peticionURL[2];
        }
    }
}

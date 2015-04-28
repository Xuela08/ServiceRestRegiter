package es.tfg.controlador.services;

import es.tfg.modelo.PersonasDAO;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import es.tfg.modelo.Persona;

@Path("personas")
public class PersonaResource {

    private PersonasDAO personaDAO;

    public PersonaResource() {
        this.personaDAO = new PersonasDAO();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Persona getAll() {
        Persona p1 = new Persona("mjcd0005", "Manuel", "Castro", "mjcd0005@gmail.com");
        
        return p1;
    }
}

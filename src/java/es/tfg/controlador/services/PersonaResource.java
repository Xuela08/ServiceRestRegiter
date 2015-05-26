package es.tfg.controlador.services;

import es.tfg.modelo.DAO.PersonasDAO;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import es.tfg.modelo.Persona;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

@Path("personas")
public class PersonaResource {

    private PersonasDAO personaDAO;

    public PersonaResource() {
        this.personaDAO = new PersonasDAO();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Persona persona, @Context UriInfo uriInfo) {
        Persona newPersona = this.personaDAO.guardaPersona(persona);

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI newUri = uriBuilder.path(String.valueOf(newPersona.getUsuario())).build();

        return Response.created(newUri).entity(newPersona).build();
    }
    
    @PUT
    @Path("{usuario}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Persona update(Persona persona, @PathParam("usuario") String usu) {
        String modificado = persona.getUsuario();
        if(usu.equals(persona.getUsuario())){
            this.personaDAO.actualizaPersona(persona);
            return this.personaDAO.obtenPersona(persona.getUsuario());
        }else{
            return null;
        }
    }
    
    @DELETE
    @Path("{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("usuario") String usu) { 
        this.personaDAO.eliminaPersona(this.personaDAO.obtenPersona(usu));

        return Response.noContent().build();
    }
    
    @GET
    @Path("{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona getPersona(@PathParam("usuario") String usu) {

        return this.personaDAO.obtenPersona(usu);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Persona> getAll() {
        List<Persona> list = this.personaDAO.obtenListaPersonas();

        return list;
    }

    //post autenticate
}
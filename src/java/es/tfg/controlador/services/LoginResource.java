package es.tfg.controlador.services;

import es.tfg.modelo.PersonasDAO;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import es.tfg.modelo.Persona;
import es.tfg.modelo.Token;
import es.tfg.modelo.TokenDAO;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

@Path("acceso")
public class LoginResource {

    private TokenDAO tokenDAO;

    public LoginResource() {
        this.tokenDAO = new TokenDAO();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context UriInfo uriInfo, @FormParam("username") String username, @FormParam("password") String password) {
        Token newToken = this.tokenDAO.login(username, password);

        if (newToken != null) {
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            URI newUri = uriBuilder.path(String.valueOf(newToken.getToken())).build();

            return Response.created(newUri).entity(newToken).build();
        }
        
        return Response.status(401).build();
    }

    @DELETE
    @Path("logout/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@PathParam("token") String token) {
        boolean accion = this.tokenDAO.logout(token);
        if (accion) {
            return Response.noContent().build();
        }
        return Response.status(400).build();

    }
}

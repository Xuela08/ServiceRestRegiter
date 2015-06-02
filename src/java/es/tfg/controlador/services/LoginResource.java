package es.tfg.controlador.services;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import es.tfg.modelo.Token;
import es.tfg.modelo.DAO.TokenDAO;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.StringTokenizer;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;

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
    public Response login(@Context UriInfo uriInfo, @HeaderParam("Authorization") String authCredentials) {

        if (authCredentials != null) {
            final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
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
            Token newToken = this.tokenDAO.login(username, password);
            if (newToken != null) {
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                URI newUri = uriBuilder.path(String.valueOf(newToken.getToken())).build();

                return Response.created(newUri).entity(newToken).build();
            }
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

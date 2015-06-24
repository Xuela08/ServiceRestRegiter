package es.tfg.controlador.services;

import es.tfg.modelo.Aporte;
import es.tfg.modelo.DAO.AportesDAO;
import es.tfg.modelo.DAO.PersonasDAO;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("noticias")
public class NoticiaResource {

    private AportesDAO aportesDAO;
    public static String TIPO = "Noticia";

    public NoticiaResource() {
        this.aportesDAO = new AportesDAO();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Aporte aporte, @Context UriInfo uriInfo) {
        aporte.setTipo(TIPO);
        long idAporte = this.aportesDAO.nuevoAporte(aporte);

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI newUri = uriBuilder.path(String.valueOf(idAporte)).build();

        return Response.created(newUri).entity(aportesDAO.obtenAporte(idAporte)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Aporte update(Aporte aporte, @PathParam("id") long id) {
        Aporte aporteId = this.aportesDAO.obtenAporte(id);
        if (TIPO.equals(aporteId.getTipo())) {
            aporte.setId(id);
            aporte.setTipo(TIPO);
            aporte.setF_creacion(aporteId.getF_creacion());
            this.aportesDAO.actualizaAporte(aporte);
            return this.aportesDAO.obtenAporte(id);
        } else {
            return null;
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") long id) {
        Aporte remove = this.aportesDAO.obtenAporte(id);
        if (remove != null && TIPO.equals(remove.getTipo())) {
            this.aportesDAO.eliminaAporte(remove);
        }

        return Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Aporte> getAll(@QueryParam("inicio") int inicio, @QueryParam("tamPagina") int tamPagina) {
        List<Aporte> list = null;
        if (inicio == 0 && tamPagina == 0) {
            list = this.aportesDAO.obtenListaAporte(TIPO);
        } else {
            inicio = inicio - 1;
            inicio = inicio * tamPagina;
            list = this.aportesDAO.obtenListaAporte(TIPO, inicio, tamPagina);
        }

        return list;
    }

    @GET
    @Path("tam")
    @Produces(MediaType.APPLICATION_JSON)
    public long getTam() {
        
        return this.aportesDAO.getTam(TIPO);
        
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Aporte getId(@PathParam("id") long id) {
        Aporte aporte = this.aportesDAO.obtenAporte(id);
        if (aporte != null && TIPO.equals(aporte.getTipo())) {
            return aporte;
        }

        return null;
    }
    
    @GET
    @Path("propietario")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Aporte> getAllUser(@QueryParam("usuario") String usuario) {
        List<Aporte> list = (List<Aporte>)this.aportesDAO.obtenAportesUsuario(TIPO, usuario);

        return list;
    }
    
}

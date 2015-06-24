package es.tfg.controlador.services;

import es.tfg.modelo.Aporte;
import es.tfg.modelo.Apunte;
import es.tfg.modelo.DAO.ApuntesDAO;
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

@Path("apuntes")
public class ApunteResource {
    
    public static String TIPO = "Apunte";
    private ApuntesDAO apuntesDAO;

    public ApunteResource() {
        apuntesDAO = new ApuntesDAO();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Apunte apunte, @Context UriInfo uriInfo) {
        apunte.getDatosGenerales().setTipo(TIPO);
        long idApunte = this.apuntesDAO.nuevoApunte(apunte);
        
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI newUri = uriBuilder.path(String.valueOf(idApunte)).build();

        return Response.created(newUri).entity(apuntesDAO.obtenApunte(idApunte)).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Apunte update(Apunte apunte, @PathParam("id") long id) {
        apunte.setId(id);
        apuntesDAO.actualizaApunte(apunte);
        
        return apuntesDAO.obtenApunte(id);
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") long id) {
        Apunte remove = this.apuntesDAO.obtenApunte(id);
        if(remove != null){
            this.apuntesDAO.eliminaApunte(remove);
        }
        
        return Response.noContent().build();
    }
    
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Apunte> getAll() {
//        List<Apunte> list = this.apuntesDAO.obtenApunte();
//
//        return list;
//    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Apunte getId(@PathParam("id") long id) {
        Apunte apunte = this.apuntesDAO.obtenApunte(id);
        if(apunte != null){
            return apunte;
        }

        return null;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Apunte> getList(@QueryParam("carrera") String carrera, @QueryParam("curso") int curso, @QueryParam("asignatura") String asignatura) {
        List<Apunte> list;
        if(carrera == null){
            list = this.apuntesDAO.obtenApunte();
        }else{
            list = this.apuntesDAO.obtenApunteFiltrado(carrera, curso, asignatura);
        }
        
        return list;
    }
    
    
    @GET
    @Path("propiedades/carreras")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getPropiedadCarreras() {
        return this.apuntesDAO.getCarreras();
    }
    
    @GET
    @Path("propiedades/{carrera}/cursos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Integer> getPropiedadCursos(@PathParam("carrera") String carrera) {
        return this.apuntesDAO.getCursos(carrera);
    }
    
    @GET
    @Path("propiedades/{carrera}/{curso}/asignaturas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getPropiedadAsignaturas(@PathParam("carrera") String carrera, @PathParam("curso") int curso) {
        return this.apuntesDAO.getAsignaturas(carrera, curso);
    }
    
    @GET
    @Path("propietario")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Apunte> getAllUser(@QueryParam("usuario") String usuario) {
        List<Apunte> list = (List<Apunte>)this.apuntesDAO.obtenApuntesUsuario(TIPO, usuario);

        return list;
    }
    
}

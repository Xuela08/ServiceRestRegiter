package es.tfg.modelo.DAO;

import static es.tfg.controlador.services.ApunteResource.TIPO;
import es.tfg.modelo.Aporte;
import es.tfg.modelo.Apunte;
import es.tfg.modelo.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ApuntesDAO {

    private AportesDAO aportesDAO;
    private Session sesion;
    private Transaction tx;

    public long nuevoApunte(Apunte apunte) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            Date actual = new Date();
            apunte.getDatosGenerales().setF_creacion(actual);
            id = (long) sesion.save(apunte);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public void actualizaApunte(Apunte apunte) throws HibernateException {
        try {
            iniciaOperacion();
            Apunte apuntesModificar = (Apunte) sesion.get(Apunte.class, apunte.getId());
            apuntesModificar.getDatosGenerales().setTitulo(apunte.getDatosGenerales().getTitulo());
            apuntesModificar.getDatosGenerales().setDescripcion(apunte.getDatosGenerales().getDescripcion());
            apuntesModificar.getDatosGenerales().setBaseIMG(apunte.getDatosGenerales().getBaseIMG());
            apuntesModificar.setCarrera(apunte.getCarrera());
            apuntesModificar.setCurso(apunte.getCurso());
            apuntesModificar.setAsignatura(apunte.getAsignatura());
            apuntesModificar.setUrlDescarga(apunte.getUrlDescarga());

            sesion.update(apuntesModificar);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public void eliminaApunte(Apunte apunte) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(apunte);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public List<Apunte> obtenApunte() throws HibernateException {
        List<Apunte> apuntes = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("FROM Apunte");
            apuntes = query.list();
            long idgenerales;
            Aporte aporte;
            aportesDAO = new AportesDAO();
            for (int i = 0; i < apuntes.size(); i++) {
                aporte = null;
                idgenerales = apuntes.get(i).getDatosGenerales().getId();
                aporte = aportesDAO.obtenAporte(idgenerales);
                apuntes.get(i).setDatosGenerales(aporte);
            }
        } finally {
            sesion.close();
        }
        return apuntes;
    }

    public List<Apunte> obtenApunteFiltrado(String carrera, int curso, String asignatura) throws HibernateException {
        List<Apunte> apuntes = null;

        try {
            iniciaOperacion();
            Query query;
            if (curso == 0 && asignatura == null) {
                query = sesion.createQuery("FROM Apunte A WHERE A.carrera = :carrera_");
                query.setParameter("carrera_", carrera);
            }else if(curso == 0){
                query = sesion.createQuery("FROM Apunte A WHERE A.carrera = :carrera_ AND A.asignatura = :asignatura_");
                query.setParameter("carrera_", carrera);
                query.setParameter("asignatura_", asignatura);
            }else if(asignatura == null){
                query = sesion.createQuery("FROM Apunte A WHERE A.carrera = :carrera_ AND A.curso = :curso_");
                query.setParameter("carrera_", carrera);
                query.setParameter("curso_", curso);
            }else{
                query = sesion.createQuery("FROM Apunte A WHERE A.carrera = :carrera_ AND A.curso = :curso_ AND A.asignatura = :asignatura_");
                query.setParameter("carrera_", carrera);
                query.setParameter("curso_", curso);
                query.setParameter("asignatura_", asignatura);
            }
            apuntes = query.list();
            long idgenerales;
            Aporte aporte;
            aportesDAO = new AportesDAO();
            for (int i = 0; i < apuntes.size(); i++) {
                aporte = null;
                idgenerales = apuntes.get(i).getDatosGenerales().getId();
                aporte = aportesDAO.obtenAporte(idgenerales);
                apuntes.get(i).setDatosGenerales(aporte);
            }
        } finally {
            sesion.close();
        }
        return apuntes;
    }

    public Apunte obtenApunte(long idApunte) throws HibernateException {
        Apunte apunte = null;
        aportesDAO = new AportesDAO();
        try {
            iniciaOperacion();
            apunte = (Apunte) sesion.get(Apunte.class, idApunte);
            long id = apunte.getDatosGenerales().getId();
            Aporte aporte = aportesDAO.obtenAporte(id);
            apunte.setDatosGenerales(aporte);
        } finally {
            sesion.close();
        }
        return apunte;
    }
    
    public List<String> getCarreras() throws HibernateException {
        List<String> respuesta = null;
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("SELECT DISTINCT A.carrera FROM Apunte A");
            respuesta = query.list();
        } finally {
            sesion.close();
        }
        return respuesta;
    }
    
    public List<Integer> getCursos(String carrera) throws HibernateException {
        List<Integer> respuesta = null;
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("SELECT DISTINCT A.curso FROM Apunte A WHERE A.carrera = :carrera_ ORDER BY A.curso");
            query.setParameter("carrera_", carrera);
            respuesta = query.list();
        } finally {
            sesion.close();
        }
        return respuesta;
    }
    
    public List<String> getAsignaturas(String carrera, int curso) throws HibernateException {
        List<String> respuesta = null;
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("SELECT DISTINCT A.asignatura FROM Apunte A WHERE A.carrera = :carrera_ AND A.curso = :curso_");
            query.setParameter("carrera_", carrera);
            query.setParameter("curso_", curso);
            respuesta = query.list();
        } finally {
            sesion.close();
        }
        return respuesta;
    }
    
    public List<Apunte> obtenApuntesUsuario(String tipo, String usuario) throws HibernateException {
        List<Aporte> listaAportes = null;
        List<Apunte> listaApuntes = new ArrayList<>();
        List<Apunte> listaAux = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("FROM Aporte A WHERE A.tipo = :tipo_id AND A.propietario = :usuario");
            query.setParameter("tipo_id",tipo);
            query.setParameter("usuario",usuario);
            listaAportes = query.list();
            
            for(int i = 0; i < listaAportes.size(); i++){
                 query = sesion.createQuery("FROM Apunte A WHERE A.datosGenerales = :datosGenerales");
                 query.setParameter("datosGenerales", listaAportes.get(i));
                 listaAux = query.list();
                 listaApuntes.add(listaAux.get(0));
            }
            
        } finally {
            sesion.close();
        }

        return listaApuntes;
    } 

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos de apuntes", he);
    }
}

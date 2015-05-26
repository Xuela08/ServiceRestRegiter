package es.tfg.modelo.DAO;

import static es.tfg.controlador.services.ApunteResource.TIPO;
import es.tfg.modelo.Aporte;
import es.tfg.modelo.Apunte;
import es.tfg.modelo.HibernateUtil;
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
            apuntesModificar.getDatosGenerales().setUrlImagen(apunte.getDatosGenerales().getUrlImagen());
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
            //apuntes = sesion.createQuery("from Apunte").list();
            Query query = sesion.createQuery("FROM Apunte");
            apuntes = query.list();
            long idgenerales;
            Aporte aporte;
            aportesDAO = new AportesDAO();
            for(int i=0; i<apuntes.size() ; i++){
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
    
    
    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos de apuntes", he);
    }
}

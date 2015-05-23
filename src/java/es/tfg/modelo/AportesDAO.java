package es.tfg.modelo;

import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AportesDAO {

    private Session sesion;
    private Transaction tx;

    public long nuevoAporte(Aporte aporte) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            Date actual = new Date();
            aporte.setF_creacion(actual);
            id = (Long) sesion.save(aporte);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public void actualizaAporte(Aporte aporte) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.update(aporte);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public void eliminaAporte(Aporte aporte) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(aporte);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public Aporte obtenAporte(long idAporte) throws HibernateException {
        Aporte aporte = null;

        try {
            iniciaOperacion();
            aporte = (Aporte) sesion.get(Aporte.class, idAporte);
        } finally {
            sesion.close();
        }
        return aporte;
    }

    public List<Aporte> obtenListaAporte() throws HibernateException {
        List<Aporte> listaAporte = null;

        try {
            iniciaOperacion();
            listaAporte = sesion.createQuery("from Aporte").list();
        } finally {
            sesion.close();
        }

        return listaAporte;
    }

    public List<Aporte> obtenListaAporte(String tipo) throws HibernateException {
        List<Aporte> listaAporte = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("FROM Aporte A WHERE A.tipo = :tipo_id");
            query.setParameter("tipo_id",tipo);
            listaAporte = query.list();
        } finally {
            sesion.close();
        }

        return listaAporte;
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos de nuevo aporte", he);
    }
}

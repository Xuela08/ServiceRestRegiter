package es.tfg.modelo;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PersonasDAO {
    private Session sesion; 
    private Transaction tx;  
    public TokenDAO tokenDAO;
    
    public Persona guardaPersona(Persona persona) throws HibernateException 
    { 
        String usu = "";
        try 
        { 
            iniciaOperacion(); 
            usu = (String) sesion.save(persona); 
            tx.commit();
            persona = (Persona) sesion.get(Persona.class, usu); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        }  

        return persona; 
    }  

    public void actualizaPersona(Persona persona) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(persona); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  

    public void eliminaPersona(Persona persona) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion();
            sesion.delete(persona); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close();
            //Eliminar para invalidar token
            tokenDAO = new TokenDAO();
            tokenDAO.invalidaToken(persona.getUsuario());
        } 
    }  

    public Persona obtenPersona(String usuario) throws HibernateException 
    { 
        Persona persona = null;  
        try 
        { 
            iniciaOperacion(); 
            persona = (Persona) sesion.get(Persona.class, usuario); 
        } finally 
        { 
            sesion.close(); 
        }  

        return persona; 
    }  

    public List<Persona> obtenListaPersonas() throws HibernateException 
    { 
        List<Persona> listaPersonas = null;  

        try 
        { 
            iniciaOperacion(); 
            listaPersonas = sesion.createQuery("from Persona").list(); 
            for(int i=0; i< listaPersonas.size();i++){
                listaPersonas.get(i).setPassword(null);
            }
        } finally 
        { 
            sesion.close(); 
        }  

        return listaPersonas; 
    }
    
    public boolean compruebaPersona(String usuario, String pass) throws HibernateException
    {
        Persona persona = null;
        try 
        { 
            iniciaOperacion(); 
            persona = (Persona) sesion.get(Persona.class, usuario);
            if(persona == null)
                return false;
            else
                return persona.comparePassword(pass);
        } finally 
        { 
            sesion.close(); 
        } 
        
    }
    
    public boolean esAdmin(String usuario) throws HibernateException
    {
        Persona persona = null;
        try 
        { 
            iniciaOperacion(); 
            persona = (Persona) sesion.get(Persona.class, usuario);
            return persona.isAdministrador();
        } finally 
        { 
            sesion.close(); 
        } 
        
    }

    private void iniciaOperacion() throws HibernateException 
    { 
        sesion = HibernateUtil.getSessionFactory().openSession(); 
        tx = sesion.beginTransaction(); 
    }   

    private void manejaExcepcion(HibernateException he) throws HibernateException 
    { 
        tx.rollback(); 
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he); 
    }
}

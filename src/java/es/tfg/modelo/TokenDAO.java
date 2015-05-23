package es.tfg.modelo;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TokenDAO {

    private Session sesion;
    private Transaction tx;
    private PersonasDAO personaDAO;

    public Token login(String usuario, String password) throws HibernateException {

        Token token = null;

        try {
            this.personaDAO = new PersonasDAO();
            boolean autenticacion = this.personaDAO.compruebaPersona(usuario, password);

            iniciaOperacion();
            if (autenticacion) {
                Query query = sesion.createSQLQuery("SELECT * FROM token t WHERE t. idusuario = :idelusuario");
                query.setParameter("idelusuario", usuario);

                List<Object[]> tokens = query.list();
                if(tokens.size()>0){
                    Token tEliminar = null;
                    for(int i = 0; i<tokens.size();i++){
                        String sToken = (String) tokens.get(i)[0];
                        Date create = (Date) tokens.get(i)[1];
                        String idUsuario = (String) tokens.get(i)[2];
                        tEliminar = new Token(sToken, create, idUsuario);
                        sesion.delete(tEliminar);
                    }
                }
                Date actual = new Date();
                long mins = 10/*min*/ * 60 * 1000;
                mins = mins + actual.getTime();
                Date tAct = new Date(mins);
                String randToken = UUID.randomUUID().toString();
                token = new Token(randToken, tAct, usuario);
                sesion.save(token);
                tx.commit();
            }

        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
        return token;
    }

    public boolean logout(String tokenEliminar) throws HibernateException {
        Token logout = null;
        try 
        { 
            iniciaOperacion();
            logout = (Token) sesion.get(Token.class, tokenEliminar); 
            
            sesion.delete(logout); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        {
            sesion.close(); 
            return logout != null;
        } 
    }

    public String verificaYusuario(String token) throws HibernateException {
        Token t = null;
        try 
        { 
            iniciaOperacion();
            t = (Token) sesion.get(Token.class, token); 
            //verificaríamos la caducidad
            if(t != null){
            long timeCaducity = t.getCaducidad().getTime();
            Date actual = new Date();
            long timeActual = actual.getTime();
            
            long dif = timeCaducity - timeActual;
            if(dif>=0){
                //actualizaria la caducidad
                long mins = 10/*min*/ * 60 * 1000;
                mins = mins + actual.getTime();
                Date tAct = new Date(mins);
                t.setCaducidad(tAct);
                sesion.update(t); 
                tx.commit();
            }else{
                //borra token
                sesion.delete(t);
                tx.commit();
                t=null;
            }
            }
             
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        {
            sesion.close(); 
            if(t == null){
                return null;
            }
            return t.getIdUsuario();
        }
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos al logearte", he);
    }
}

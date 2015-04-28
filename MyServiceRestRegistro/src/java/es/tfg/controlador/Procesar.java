package es.tfg.controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.tfg.modelo.Persona;
import es.tfg.modelo.PersonasDAO;

public class Procesar extends HttpServlet{
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    PersonasDAO personaDAO = new PersonasDAO();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("txtUsuario");
        String nombre = request.getParameter("txtNombre");
        String apellido = request.getParameter("txtApellido");
        String email = request.getParameter("txtEmail");
        
        if(usuario.equals("") || nombre.equals("")|| apellido.equals("")|| email.equals("")){
            request.getRequestDispatcher("errorCampos.jsp").forward(request, response);
        }else{
            String usu = "";
            Persona p1 =  new Persona(usuario, nombre, apellido, email);
            usu = personaDAO.guardaPersona(p1);
            request.getSession().setAttribute("persona1", personaDAO.obtenPersona(p1.getUsuario()));
            //request.getSession().setAttribute("persona1", p1);
            request.getRequestDispatcher("exito.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

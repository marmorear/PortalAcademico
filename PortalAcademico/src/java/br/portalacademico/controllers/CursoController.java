package br.portalacademico.controllers;

import br.portalacademico.dao.CursoDao;
import br.portalacademico.model.Curso;
import br.portalacademico.util.AcaoDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CursoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            AcaoDao act = AcaoDao.valueOf(request.getParameter("acao"));
            CursoDao cursoDAO = new CursoDao();
            int idCurso = -1;
            String nomeCurso = "";
            String tipoCurso = "";
            HttpSession session = request.getSession();

            switch (act) {
                case READ:
                    Map<Curso, Integer> relatorio = cursoDAO.getTodosCursosCountAlunos();
                    session.setAttribute("listaDeCursos", relatorio);
                    response.sendRedirect("./relatorios/cursos.jsp");
                    break;
                case DELETE:
                    idCurso = Integer.parseInt(request.getParameter("idCurso"));

                    if (cursoDAO.deleteCurso(idCurso)) {
                        response.sendRedirect("./relatorios/loader.jsp?pagina=curso");
                    } else {
                        //precisamos pensar...
                    }
                    break;
                case CREATE:
                    nomeCurso = request.getParameter("nomeCurso");
                    tipoCurso = request.getParameter("tipoCurso");
                    if (cursoDAO.cadastraCurso(new Curso(0, nomeCurso, tipoCurso))) {
                        response.sendRedirect("./relatorios/loader.jsp?pagina=curso");
                    }
                    break;
                case EDIT:
                    nomeCurso = request.getParameter("nomeCurso");
                    tipoCurso = request.getParameter("tipoCurso");
                    idCurso = Integer.parseInt(request.getParameter("idCurso"));

                    if (cursoDAO.atualizaCurso(idCurso, nomeCurso, tipoCurso)) {
                        response.sendRedirect("./relatorios/loader.jsp?pagina=curso");
                    }

                    //Outra forma de chamar a atualização
                    //Curso curso = new Curso(idCurso, nomeCurso, tipoCurso);
                    //if(cursoDAO.atualizaCurso(curso)) {
                    //  response.sendRedirect("./relatorios/loader.jsp?pagina=curso");
                    //}
                    break;
                default:
                    break;
            }

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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CursoController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CursoController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

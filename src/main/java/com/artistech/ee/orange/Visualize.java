/*
 * Copyright 2017 ArtisTech, Inc.
 */
package com.artistech.ee.orange;

import com.artistech.ee.beans.DataManager;
import com.artistech.ee.beans.Data;
import com.artistech.utils.ExternalProcess;
import com.artistech.utils.StreamGobbler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author matta
 */
public class Visualize extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String viz_path = getInitParameter("path");

        Part pipeline_id_part = request.getPart("pipeline_id");
        String pipeline_id = IOUtils.toString(pipeline_id_part.getInputStream(), "UTF-8");
        final Data data = (Data) DataManager.getData(pipeline_id);
        final String file_list = data.getTestList();

        File test_file = new File(file_list);
        if (!test_file.exists()) {
            try (java.io.BufferedWriter writer = new BufferedWriter(new FileWriter(test_file))) {
                for (String f : data.getInputFiles()) {
                    writer.write(f + System.lineSeparator());
                }
            }
        }

        PipedInputStream in = new PipedInputStream();
        final PipedOutputStream out = new PipedOutputStream(in);
        StreamGobbler sg = new StreamGobbler(in);
        sg.start();

        final OutputStreamWriter bos = new OutputStreamWriter(out);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                File source = new File(data.getInput());
                File viz_dir = new File(data.getVizOut());
                if(!viz_dir.exists()) {
                    viz_dir.mkdirs();
                }

                /**
                 * JOINT ERE VIZ!
                 */
                File dest = new File(data.getJieOut());
                if (dest.exists()) {
                    try {
                        try {
                            FileUtils.copyDirectory(source, dest);
                        } catch (IOException e) {
                            Logger.getLogger(Visualize.class.getName()).log(Level.SEVERE, null, e);
                        }
                        ProcessBuilder pb = new ProcessBuilder("python3", "ere_visualizer.py", file_list, data.getJieOut(), data.getVizOut());
                        for (String cmd : pb.command()) {
                            Logger.getLogger(Visualize.class.getName()).log(Level.WARNING, cmd);
                        }

                        pb.directory(new File(viz_path));
                        pb.redirectErrorStream(true);
                        Process proc = pb.start();
                        StreamGobbler sg = new StreamGobbler(proc.getInputStream());
                        sg.start();

                        try {
                            proc.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(JIE.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        bos.write("JIE VIZ" + System.lineSeparator());
                        bos.write(sg.getUpdateText() + System.lineSeparator());
                        bos.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(Visualize.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
        ExternalProcess ex_proc = new ExternalProcess(sg, t);
        data.setProc(ex_proc);

        // displays done.jsp page after upload finished
        getServletContext().getRequestDispatcher("/watchProcess.jsp").forward(
                request, response);
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

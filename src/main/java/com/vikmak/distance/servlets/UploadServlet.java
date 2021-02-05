package com.vikmak.distance.servlets;

import com.vikmak.distance.dao.CityDAO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UploadServlet extends HttpServlet {
    public UploadServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = "";
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        File uploadDir = new File(CityDAO.getPath());
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        item.write(new File(CityDAO.getPath() + "NewCities.xml"));
                    }
                }
                //File uploaded successfully
                message = "ok";
            } catch (Exception ex) {
                message = "error";
            }
        } else {
            //File not found
            message = "none";
        }
        response.sendRedirect("/rest/cities/addNewCities?param=" + message);
    }


}
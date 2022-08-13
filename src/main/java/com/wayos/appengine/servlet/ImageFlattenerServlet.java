package com.wayos.appengine.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wayos.util.ImageGenerator;

/**
 * Servlet implementation class ImageFlattenerServlet
 */
@WebServlet("/flatten")
public class ImageFlattenerServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        
        ImageGenerator.Parameters parameters = new ImageGenerator.Parameters(request);
        ImageGenerator imageGenerator = new ImageGenerator(null);
        
        if (type!=null && type.equals("png")) {
            response.setContentType("image/png");
            OutputStream os = response.getOutputStream();
            ImageIO.write(imageGenerator.flatten(parameters), "PNG", os);  // write image as PNG (more)
        	return;
        }
                
    	response.getWriter().print(String.format("#%02X%02X%02X", 
    			parameters.color.getRed(),
    			parameters.color.getGreen(),
    			parameters.color.getBlue())
    			);
	}

}

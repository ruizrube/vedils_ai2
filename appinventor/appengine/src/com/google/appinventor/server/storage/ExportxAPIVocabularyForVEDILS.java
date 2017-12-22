package com.google.appinventor.server.storage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appinventor.server.OdeRemoteServiceServlet;

public class ExportxAPIVocabularyForVEDILS<T> extends OdeRemoteServiceServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(ExportxAPIVocabularyForVEDILS.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		String[] components = req.getRequestURI().split("/");
		LOG.info("requestURI ExportxAPIVedilsConcepts = " + req.getRequestURI());
		
		String conceptType = components[components.length-2];
		String conceptName = components[components.length-1];
		
		out.println("<html><head><title>xAPI vocabulary for VEDILS</title>\n");
		out.println("</head>\n<body>\n");
		out.println("<h1>xAPI vocabulary for VEDILS</h1>\n");
		out.println("<h2>" + conceptType + "</h2>\n");
		out.println("<p>" + conceptName + "</p>\n");
		out.println("</body></html>");
	}
}

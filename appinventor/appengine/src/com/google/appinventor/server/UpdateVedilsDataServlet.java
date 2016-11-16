package com.google.appinventor.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appinventor.server.storage.StorageIo;
import com.google.appinventor.server.storage.StorageIoInstanceHolder;

/**
 * Servlet to update information for ExportVedilsData.
 * 
 * @author SPI-FM at UCA
 */
public class UpdateVedilsDataServlet extends OdeServlet {
	
	private static final long serialVersionUID = 1L;
	private final transient StorageIo storageIo = StorageIoInstanceHolder.INSTANCE;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String debugMethod = req.getParameter("debugMethod");
		String projectId = req.getParameter("projectId");
		
		//storageIo.updateNumberOfTotalDebugs(Long.parseLong(projectId));
		if(debugMethod.equals("emulator")) {
			storageIo.updateNumberOfTotalEmulatorDebugs(Long.parseLong(projectId));
		} else if(debugMethod.equals("usb")) {
			storageIo.updateNumberOfTotalUSBDebugs(Long.parseLong(projectId));
		} else if(debugMethod.equals("companion")) {
			storageIo.updateNumberOfTotalCompanionDebugs(Long.parseLong(projectId));
		}
		
		String output = "<html><head><title>UpdateVedilsDataServlet</title></head>\n<body>\n<h1>Thanks!</h1>\n<p>The updated projet is: "+Long.parseLong(projectId)+" using the "+debugMethod+" debugMethod.</p>\n</body>\n</html>\n";
		PrintWriter out = new PrintWriter(resp.getWriter());
		out.println(output);
	}
}

package com.google.appinventor.server.storage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appinventor.server.OdeRemoteServiceServlet;
import com.google.appinventor.server.storage.StoredData.TAGExcluir;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * Servlet to export VEDILS data for further analysis.
 *
 *
 */
public class ExportVedilsData<T> extends OdeRemoteServiceServlet {

		private static final long serialVersionUID = 1L;
	private Gson gson;
	private ObjectifyStorageIo storage;

	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = new PrintWriter(resp.getWriter());

		gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy(String.class)).serializeNulls().create();
		storage = new ObjectifyStorageIo();

	
		try {
			String action = req.getParameter("action");
			String entity = req.getParameter("entity");
			String file = req.getParameter("file");
			String userId = req.getParameter("userId");
			String projectId = req.getParameter("projectId");

			if (action.equals("getFile")) {
				out.println(new String(storage.downloadRawFile(userId, Long.parseLong(projectId), file), "UTF-8"));
			} else if (action.equals("getProjectsByUserId")) {
				out.println(gson.toJson((storage.getProjects(userId))));
			} else if (action.equals("getFilesByUserIdAndProjectId")) {
				out.println(gson.toJson((storage.getProjectSourceFiles(userId, Long.parseLong(projectId)))));
			} else if (action.equals("getBuildsByUserIdAndProjectId")) {
				out.println(gson.toJson((storage.getProjectOutputFiles(userId, Long.parseLong(projectId)))));
			} else if (action.equals("listEntities")) {
				out.println(gson.toJson(procesar(
						(Class<T>) Class.forName("com.google.appinventor.server.storage.StoredData$" + entity))));
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ArrayList<T> procesar(Class<T> clase) {
		Objectify datastore = ObjectifyService.begin();

		ArrayList<T> listaDeObjetos = new ArrayList<T>();

		for (T obj : datastore.query(clase)) {
			listaDeObjetos.add(obj);
		}
		return listaDeObjetos;
	}
	
	public class MyExclusionStrategy implements ExclusionStrategy {
		private final Class<?> typeToSkip;

		public MyExclusionStrategy(Class<?> typeToSkip) {
			this.typeToSkip = typeToSkip;
		}

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(TAGExcluir.class) != null;
		}
	}


}

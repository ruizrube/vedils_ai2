package vedils.flink.client;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;

public class FlinkClientTest {
	
	public FlinkClientTest() {
		TJWSEmbeddedJaxrsServer server = new TJWSEmbeddedJaxrsServer(); 
		server.setPort(8080);
		//server.getDeployment().getResources().add(myResourceInstance); 
		// alternative server.getDeployment().getProviderClasses().add("com.my.my.resource.class.name"); 
	}
	
}

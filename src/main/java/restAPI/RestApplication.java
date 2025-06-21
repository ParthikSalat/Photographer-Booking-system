package restAPI;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")  // This is the base URL path for your API
public class RestApplication extends ResourceConfig {

    public RestApplication() {
        // Scan your REST API package
        packages("restAPI");

        // Enable multipart support for file upload
        register(MultiPartFeature.class);
    }
}

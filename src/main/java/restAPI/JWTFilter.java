package restAPI;

import com.mycompany.photographer.resources.JwtUtil;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class JWTFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authHeader.substring("Bearer".length()).trim();

        try {
            // ✅ Corrected: Call the validateToken method from JwtUtil
            Claims claims = JwtUtil.validateToken(token);

            int userId = (Integer) claims.get("id"); // 'id' matches your generateToken method
            String username = claims.getSubject();    // 'sub' = username
            String role = claims.get("role", String.class);

            // ✅ You can access this info in your REST resources
            requestContext.setProperty("userId", userId);
            requestContext.setProperty("username", username);
            
            requestContext.setProperty("role", role);

        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}

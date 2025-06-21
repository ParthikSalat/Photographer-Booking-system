package restAPI;

import EJB.UserBeans;
import Entity.Users;
import com.mycompany.photographer.resources.JwtUtil;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    @EJB
    private UserBeans userBeans;

    @POST
    public Response login(Users loginData) {
        Users user = userBeans.validateUser(loginData.getEmail(), loginData.getPasswordHash());

        if (user != null) {
        String token = JwtUtil.generateToken(user.getUserID(),user.getFullName(), user.getEmail(), "user");
        return Response.ok("{\"token\":\"" + token + "\"}").build();
    
   

        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Json.createObjectBuilder()
                    .add("error", "Invalid credentials")
                    .build())
                .build();
        }
    }
}

package EJB;

import Entity.Users;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    private List<Users> userList;

    @PostConstruct
    public void init() {
        fetchUsers();
    }

    public void fetchUsers() {
        try {
            URL url = new URL("http://localhost:8080/Photographer/api/users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner sc = new Scanner(conn.getInputStream());
                StringBuilder json = new StringBuilder();
                while (sc.hasNext()) {
                    json.append(sc.nextLine());
                }
                sc.close();

                ObjectMapper mapper = new ObjectMapper();
                userList = mapper.readValue(json.toString(), new TypeReference<List<Users>>() {});
                System.out.println("Fetched " + userList.size() + " users.");
            } else {
                System.err.println("Error fetching users. Response code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Users> getUserList() {
        if (userList == null || userList.isEmpty()) {
            fetchUsers(); // Ensure list is populated
        }
        return userList;
    }

    public int getUserCount() {
        return getUserList().size(); // Always matches list
    }
}

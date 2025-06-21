package EJB;

import Entity.Photographers;
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

@Named("photographerController")
@SessionScoped
public class PhotographerController implements Serializable {

    private List<Photographers> photographerList;

    @PostConstruct
    public void fetchPhotographers() {
        try {
            URL url = new URL("http://localhost:8080/Photographer/api/photographers");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder json = new StringBuilder();
                while (scanner.hasNext()) {
                    json.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                photographerList = mapper.readValue(json.toString(), new TypeReference<List<Photographers>>() {});
                System.out.println("Fetched " + photographerList.size() + " photographers.");
            } else {
                System.err.println("Error fetching photographers. Response code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Photographers> getPhotographerList() {
        return photographerList;
    }

    public int getPhotographerCount() {
        return photographerList != null ? photographerList.size() : 0;
    }
}

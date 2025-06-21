
import Entity.Gallery;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class demo implements Serializable {

    private String photographerIDs;
    private List<Gallery> galleryImages;

    public String getPhotographerIDs() {
        return photographerIDs;
    }

    public void setPhotographerIDs(String photographerIDs) {
        this.photographerIDs = photographerIDs;
    }

    public List<Gallery> getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(List<Gallery> galleryImages) {
        this.galleryImages = galleryImages;
    }

    public String loadPhotographerImagesFromString() {
        if (photographerIDs == null || photographerIDs.trim().isEmpty()) {
            galleryImages = new ArrayList<>();
            return null;
        }

        try {
            String[] idArray = photographerIDs.split(",");
            List<Integer> ids = Arrays.stream(idArray)
                                      .map(String::trim)
                                      .filter(s -> !s.isEmpty())
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());

            // Call REST API
            String apiUrl = "http://localhost:8080/Photographer/api/gallery/photographers";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Convert ID list to JSON
            String json = new ObjectMapper().writeValueAsString(ids);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            InputStream in = conn.getInputStream();
            galleryImages = new ObjectMapper().readValue(in, new TypeReference<List<Gallery>>() {});
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

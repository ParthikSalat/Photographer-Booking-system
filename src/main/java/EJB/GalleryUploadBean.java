package EJB;

import Entity.Gallery;
import Entity.Photographers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Named("galleryUploadBean") // Ensure unique bean name
@RequestScoped
public class GalleryUploadBean {

    private Part file;
    private int photographerId;

    @Inject
    private GalleryBeans galleryService;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public int getPhotographerId() {
        return photographerId;
    }

    public void setPhotographerId(int photographerId) {
        this.photographerId = photographerId;
    }

    public String upload() {
        try {
            // Ensure uploads folder exists
            String uploadFolder = "C:\\Users\\preet\\OneDrive\\Documents\\Final\\Photographer (5)\\Photographer (2)\\Photographer\\src\\main\\webapp\\images"; // ensure this exists
            String fileName = UUID.randomUUID() + "_" + Paths.get(file.getSubmittedFileName()).getFileName().toString();
            Path filePath = Paths.get(uploadFolder, fileName);

            // Save file
            try (InputStream input = file.getInputStream()) {
                Files.copy(input, filePath);
            }

            // Relative path used for display (e.g., src="images/uuid_filename.jpg")
            String relativePath = "images/" + fileName;

            // DB Insertion
            Photographers photographer = galleryService.getPhotographerById(photographerId);
            if (photographer == null) {
                return "error.xhtml?faces-redirect=true&message=Photographer+not+found";
            }

            Gallery g = new Gallery();
            g.setImageURL(relativePath);
            g.setPhotographerID(photographer);
            galleryService.createGalleryImage(g, photographer);

            return "galleryList.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            return "error.xhtml?faces-redirect=true&message=" + e.getMessage();
        }
    }
}

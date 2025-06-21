package EJB;

import Entity.Gallery;
import Entity.Photographers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.*;
import java.io.Serializable;
import java.util.List;

@Named("galleryController")
@SessionScoped
public class GalleryController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private GalleryBeans galleryService;

    private List<Gallery> galleryList;
    private Gallery newGallery;
    private Gallery selectedGallery;

    private int selectedPhotographerID;
    private Part uploadedFile;

    private final String uploadFolder = "C:\\Users\\preet\\OneDrive\\Pictures\\Photographer"; // Change if needed

    @PostConstruct
    public void init() {
        galleryList = galleryService.getAllGalleryImages();
        newGallery = new Gallery();
        selectedGallery = new Gallery();
    }

    public List<Gallery> getGalleryList() {
        galleryList = galleryService.getAllGalleryImages();
        return galleryList;
    }

    public String prepareCreate() {
        newGallery = new Gallery();
        return "http://localhost:8080/Photographer/Gallery/Create.xhtml";
    }

    public String createGallery() {
        try {
            if (uploadedFile == null || uploadedFile.getSubmittedFileName().isEmpty()) {
                throw new IllegalArgumentException("No file selected for upload.");
            }

            // Save file to disk
            String fileName = Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString();
            String fullPath = uploadFolder + File.separator + fileName;
            InputStream input = uploadedFile.getInputStream();
            Files.copy(input, new File(fullPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Set image URL relative to web context
            newGallery.setImageURL("C:\\Users\\preet\\OneDrive\\Pictures\\Photographer" + fileName);

            // Associate photographer
            Photographers photographer = new Photographers();
            photographer.setPhotographerID(selectedPhotographerID);
            newGallery.setPhotographerID(photographer);

            // Set any additional required fields here if your Gallery entity requires them
            // Example:
            // newGallery.setTitle("Default Title");
            // newGallery.setCreatedAt(LocalDateTime.now());

            // Save to database
            galleryService.createGalleryImage(newGallery, photographer);

            return "home.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            while (cause != null) {
                System.err.println("Cause: " + cause.getMessage());
                cause = cause.getCause();
            }
            return null;
        }
    }

    public String prepareUpdate(Gallery gallery) {
        selectedGallery = gallery;
        selectedPhotographerID = gallery.getPhotographerID().getPhotographerID();
        return "galleryEdit.xhtml?faces-redirect=true";
    }

    public String updateGallery() {
        Photographers photographer = new Photographers();
        photographer.setPhotographerID(selectedPhotographerID);
        selectedGallery.setPhotographerID(photographer);
        galleryService.updateGalleryImage(selectedGallery);
        return "galleryList.xhtml?faces-redirect=true";
    }

    public void deleteGallery(Gallery gallery) {
        galleryService.deleteGalleryImage(gallery.getImageID());
    }

    // Getters and Setters

    public Gallery getNewGallery() {
        return newGallery;
    }

    public void setNewGallery(Gallery newGallery) {
        this.newGallery = newGallery;
    }

    public Gallery getSelectedGallery() {
        return selectedGallery;
    }

    public void setSelectedGallery(Gallery selectedGallery) {
        this.selectedGallery = selectedGallery;
    }

    public int getSelectedPhotographerID() {
        return selectedPhotographerID;
    }

    public void setSelectedPhotographerID(int selectedPhotographerID) {
        this.selectedPhotographerID = selectedPhotographerID;
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}

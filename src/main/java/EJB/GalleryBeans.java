package EJB;

import Entity.Gallery;
import Entity.Photographers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Stateless
public class GalleryBeans {

    @PersistenceContext(unitName = "extphotopul")
    private EntityManager em;

    // Relative path under the deployed server context (e.g., "images/")
    private static final String UPLOAD_FOLDER = "images/";

    // Absolute server path to the web application will be set by a JSF managed bean
    private String realPath = "";

    public void setRealPath(String path) {
        this.realPath = path;
    }

    // For file uploads (if needed):
    public void createGalleryImageWithFile(Part uploadedFile, int photographerID) throws IOException {
        if (uploadedFile == null || uploadedFile.getSize() == 0) {
            throw new IllegalArgumentException("Uploaded file is missing");
        }

        String originalFileName = Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String uniqueName = UUID.randomUUID().toString() + extension;

        // Save file inside the realPath + UPLOAD_FOLDER folder
        File uploadDir = new File(realPath + UPLOAD_FOLDER);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File file = new File(uploadDir, uniqueName);
        try (InputStream input = uploadedFile.getInputStream();
             OutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
        }

        // Save to DB
        Photographers photographer = getPhotographerById(photographerID);
        if (photographer == null) {
            throw new IllegalArgumentException("Photographer not found");
        }
        Gallery gallery = new Gallery();
        gallery.setImageURL("images/" + uniqueName);  // Store the relative path
        gallery.setPhotographerID(photographer);
        em.persist(gallery);
    }

    // The method used by the REST API when image URL is already provided.
    public void createGalleryImage(Gallery newGallery, Photographers photographer) {
        if (photographer == null) {
            throw new IllegalArgumentException("Photographer is null");
        }
        newGallery.setPhotographerID(photographer);
        if (newGallery.getImageURL() == null || newGallery.getImageURL().trim().isEmpty()) {
            throw new IllegalArgumentException("Image URL is missing");
        }
        em.persist(newGallery);
    }

    public Photographers getPhotographerById(int id) {
        return em.find(Photographers.class, id);
    }

    public List<Gallery> getAllGalleryImages() {
        return em.createNamedQuery("Gallery.findAll", Gallery.class).getResultList();
    }

    public Gallery getGalleryImageById(int id) {
        return em.find(Gallery.class, id);
    }

    public void updateGalleryImage(Gallery updatedGallery) {
        em.merge(updatedGallery);
    }

    public boolean deleteGalleryImage(int id) {
        Gallery gallery = em.find(Gallery.class, id);
        if (gallery != null) {
            em.remove(gallery);
            return true;
        }
        return false;
    }

    public List<Gallery> getGalleryImagesByPhotographer(Photographers photographer) {
        return em.createQuery("SELECT g FROM Gallery g WHERE g.photographerID = :photographer", Gallery.class)
                 .setParameter("photographer", photographer)
                 .getResultList();
    }

    public Photographers findPhotographerById(Integer photographerID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void addGalleryImage(Gallery g) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Gallery> getGalleryByPhotographerID(int photographerID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

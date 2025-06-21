    package EJB;

    import javax.enterprise.context.RequestScoped;
    import javax.inject.Named;
    import javax.ws.rs.client.Client;
    import javax.ws.rs.client.ClientBuilder;
    import javax.ws.rs.core.GenericType;
    import javax.ws.rs.core.MediaType;
    import java.io.Serializable;
    import java.util.*;
    import java.util.stream.Collectors;
    import javax.annotation.PostConstruct;

    @Named("galleryListBean")
    @RequestScoped
    public class GalleryListBean implements Serializable {

        // DTO class
        public static class GalleryDTO implements Serializable {
            private int imageID;
            private String imageURL;
            private int photographerID;
             private String photographerName;

            public String getImagePath() {
                return imageURL;
            }

            public void setImagePath(String imagePath) {
                this.imageURL = imagePath;
            }

            public int getImageID() {
                return imageID;
            }

            public void setImageID(int imageID) {
                this.imageID = imageID;
            }

            public String getImageURL() {
                return imageURL;
            }

            public void setImageURL(String imageURL) {
                this.imageURL = imageURL;
            }

            public int getPhotographerID() {
                return photographerID;
            }

            public void setPhotographerID(int photographerID) {
                this.photographerID = photographerID;
            }
              public String getPhotographerName() {
            return photographerName;
        }

        public void setPhotographerName(String photographerName) {
            this.photographerName = photographerName;
        }
        }

        @PostConstruct
        public void init() {
            loadAllImages();
        }

        // ✅ String to receive comma-separated IDs
        private String photographerId;

        private List<GalleryDTO> galleryImages;

        public String getPhotographerId() {
            return photographerId;
        }

        public void setPhotographerId(String photographerId) {
            this.photographerId = photographerId;
        }

        public List<GalleryDTO> getGalleryImages() {
            return galleryImages;
        }

        public void setGalleryImages(List<GalleryDTO> galleryImages) {
            this.galleryImages = galleryImages;
        }

        // ✅ Called from JSF to load images based on comma-separated IDs
 public void loadPhotographerImagesByIDs() {
    if (photographerId == null || photographerId.trim().isEmpty()) {
        return;
    }

    List<Integer> photographerIds;
    try {
        photographerIds = Arrays.stream(photographerId.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    } catch (Exception e) {
        e.printStackTrace();
        return;
    }

    List<GalleryDTO> allImages = new ArrayList<>();
    Client client = ClientBuilder.newClient();

    try {
        for (Integer id : photographerIds) {
            // ✅ Fetch gallery images for each photographer
            List<GalleryDTO> rawList = client
                    .target("http://localhost:8080/Photographer/api/gallery/with-photographer/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<GalleryDTO>>() {});

            // ✅ Fetch photographer name for each ID
            String name = client
                    .target("http://localhost:8080/Photographer/api/photographers/" + id + "/name")
                    .request(MediaType.TEXT_PLAIN)
                    .get(String.class);

            // ✅ Add imagePath and name
            for (GalleryDTO img : rawList) {
                img.setImagePath("http://localhost:8080/Photographer/" + img.getImageURL());
                img.setPhotographerName(name);
            }

            allImages.addAll(rawList);
        }

        this.galleryImages = allImages;

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        client.close();
    }
}
      

public void loadAllImages() {
    try {
        Client client = ClientBuilder.newClient();
        List<GalleryDTO> rawList = client
            .target("http://localhost:8080/Photographer/api/gallery")
            .request(MediaType.APPLICATION_JSON)
            .get(new GenericType<List<GalleryDTO>>() {});
        
        // For each image, set image path and fetch name
        for (GalleryDTO img : rawList) {
            img.setImagePath("http://localhost:8080/Photographer/" + img.getImageURL());

            try {
                String name = client
                    .target("http://localhost:8080/Photographer/api/photographers/" + img.getPhotographerID() + "/name")
                    .request(MediaType.TEXT_PLAIN)
                    .get(String.class);

                img.setPhotographerName(name);
            } catch (Exception e) {
                System.out.println("Failed to fetch name for ID: " + img.getPhotographerID());
                e.printStackTrace();
                img.setPhotographerName("Unknown");
            }

            // Optional: Log for debug
            System.out.println("Image ID: " + img.getImageID());
            System.out.println("Photographer ID: " + img.getPhotographerID());
            System.out.println("Photographer Name: " + img.getPhotographerName());
            System.out.println("---------------------------------");
        }

        client.close();
        this.galleryImages = rawList;

    } catch (Exception e) {
        e.printStackTrace();
    }
}



public void loadPhotographerImages() {
        try {
            System.out.println("Loading images for photographer ID: " + photographerId);
            Client client = ClientBuilder.newClient();
            List<GalleryDTO> rawList = client
                .target("http://localhost:8080/Photographer/api/gallery/photographer/" + photographerId)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<GalleryDTO>>() {});
            client.close();

            // Fix full path to make images load in browser
            for (GalleryDTO img : rawList) {
                img.setImagePath("http://localhost:8080/Photographer/" + img.getImageURL());
            }

            this.galleryImages = rawList;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

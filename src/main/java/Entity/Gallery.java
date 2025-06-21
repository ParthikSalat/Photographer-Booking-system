/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author preet
 */
@Entity
@Table(name = "gallery")
@NamedQueries({
    @NamedQuery(name = "Gallery.findAll", query = "SELECT g FROM Gallery g"),
    @NamedQuery(name = "Gallery.findByImageID", query = "SELECT g FROM Gallery g WHERE g.imageID = :imageID"),
    @NamedQuery(name = "Gallery.findByImageURL", query = "SELECT g FROM Gallery g WHERE g.imageURL = :imageURL")})
public class Gallery implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ImageID")
    private Integer imageID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ImageURL")
    private String imageURL;
    @JoinColumn(name = "PhotographerID", referencedColumnName = "PhotographerID")
    @ManyToOne
    private Photographers photographerID;

    public Gallery() {
    }

    public Gallery(Integer imageID) {
        this.imageID = imageID;
    }

    public Gallery(Integer imageID, String imageURL) {
        this.imageID = imageID;
        this.imageURL = imageURL;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Photographers getPhotographerID() {
        return photographerID;
    }

    public void setPhotographerID(Photographers photographerID) {
        this.photographerID = photographerID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imageID != null ? imageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gallery)) {
            return false;
        }
        Gallery other = (Gallery) object;
        if ((this.imageID == null && other.imageID != null) || (this.imageID != null && !this.imageID.equals(other.imageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Gallery[ imageID=" + imageID + " ]";
    }
    
}

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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author parthik
 */
@Entity
@Table(name = "reviews")
@NamedQueries({
    @NamedQuery(name = "Reviews.findAll", query = "SELECT r FROM Reviews r"),
    @NamedQuery(name = "Reviews.findByReviewID", query = "SELECT r FROM Reviews r WHERE r.reviewID = :reviewID"),
    @NamedQuery(name = "Reviews.findByRating", query = "SELECT r FROM Reviews r WHERE r.rating = :rating")})
public class Reviews implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ReviewID")
    private Integer reviewID;
    @Column(name = "Rating")
    private Integer rating;
    @Lob
    @Size(max = 65535)
    @Column(name = "Comment")
    private String comment;
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    @ManyToOne
    private Users userID;
    @JoinColumn(name = "PhotographerID", referencedColumnName = "PhotographerID")
    @ManyToOne
    private Photographers photographerID;

    public Reviews() {
    }

    public Reviews(Integer reviewID) {
        this.reviewID = reviewID;
    }

    public Integer getReviewID() {
        return reviewID;
    }

    public void setReviewID(Integer reviewID) {
        this.reviewID = reviewID;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users userID) {
        this.userID = userID;
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
        hash += (reviewID != null ? reviewID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reviews)) {
            return false;
        }
        Reviews other = (Reviews) object;
        if ((this.reviewID == null && other.reviewID != null) || (this.reviewID != null && !this.reviewID.equals(other.reviewID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Reviews[ reviewID=" + reviewID + " ]";
    }
    
}

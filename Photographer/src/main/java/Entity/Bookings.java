/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author parthik
 */
@Entity
@Table(name = "bookings")
@NamedQueries({
    @NamedQuery(name = "Bookings.findAll", query = "SELECT b FROM Bookings b"),
    @NamedQuery(name = "Bookings.findByBookingID", query = "SELECT b FROM Bookings b WHERE b.bookingID = :bookingID"),
    @NamedQuery(name = "Bookings.findByBookingDate", query = "SELECT b FROM Bookings b WHERE b.bookingDate = :bookingDate"),
    @NamedQuery(name = "Bookings.findByStatus", query = "SELECT b FROM Bookings b WHERE b.status = :status"),
    @NamedQuery(name = "Bookings.findByPaymentStatus", query = "SELECT b FROM Bookings b WHERE b.paymentStatus = :paymentStatus")})
public class Bookings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BookingID")
    private Integer bookingID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BookingDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;
    @Size(max = 9)
    @Column(name = "Status")
    private String status;
    @Size(max = 8)
    @Column(name = "PaymentStatus")
    private String paymentStatus;
    @OneToMany(mappedBy = "bookingID")
    private Collection<Payments> paymentsCollection;
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    @ManyToOne
    private Users userID;
    @JoinColumn(name = "PhotographerID", referencedColumnName = "PhotographerID")
    @ManyToOne
    private Photographers photographerID;

    public Bookings() {
    }

    public Bookings(Integer bookingID) {
        this.bookingID = bookingID;
    }

    public Bookings(Integer bookingID, Date bookingDate) {
        this.bookingID = bookingID;
        this.bookingDate = bookingDate;
    }

    public Integer getBookingID() {
        return bookingID;
    }

    public void setBookingID(Integer bookingID) {
        this.bookingID = bookingID;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Collection<Payments> getPaymentsCollection() {
        return paymentsCollection;
    }

    public void setPaymentsCollection(Collection<Payments> paymentsCollection) {
        this.paymentsCollection = paymentsCollection;
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
        hash += (bookingID != null ? bookingID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bookings)) {
            return false;
        }
        Bookings other = (Bookings) object;
        if ((this.bookingID == null && other.bookingID != null) || (this.bookingID != null && !this.bookingID.equals(other.bookingID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Bookings[ bookingID=" + bookingID + " ]";
    }
    
}

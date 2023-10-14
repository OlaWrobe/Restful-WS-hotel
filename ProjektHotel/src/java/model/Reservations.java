package model;

import java.util.Date;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reservations {
    private int id;
    private int roomNumber;
    private int amountOfGuests;
    private Date checkInDate = new Date();
    private Date checkOutDate = new Date();
    Link roomLink;
    
    public Reservations(){
    }
    
    public Reservations(int id, int roomNumber, int amountOfGuests, Date checkInDate, Date checkOutDate){
    this.id = id;
    this.roomNumber = roomNumber;
    this.amountOfGuests = amountOfGuests;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    }
    
    public int getId(){
        return id;
    }
    public int getRoomNumber(){
        return roomNumber;
    }
    public int getAmountOfGuests(){
        return amountOfGuests;
    }
    public Date getCheckInDate(){
        return checkInDate;
    }
    public Date getCheckOutDate(){
        return checkOutDate;
    }
    public Link getRoomLink(){
        return roomLink;
    }
    
    public void setId(int id){
        this.id = id;
    }
    public void setRoomNumber(int roomNumber){
        this.roomNumber = roomNumber;
    }
    public void setAmountOfGuests(int amountOfGuests){
        this.amountOfGuests = amountOfGuests;
    }
    public void setCheckInDate(Date checkInDate){
        this.checkInDate = checkInDate;
    }
    public void setCheckOutDate(Date checkOutDate){
        this.checkOutDate = checkOutDate;
    }
    public void setRoomLink(Link roomLink){
        this.roomLink = roomLink;
    }
    public void addLink(String path, String name) {
    Link commentsLink = Link.fromUri(path).rel(name).build();
    this.roomLink = commentsLink;
    }
}

package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Room {

    private long roomNumber;
    private int capacity;
    private int price;
    //String nazwa;
    //int liczbaLozek;

    public Room() {
    }

    public Room(long roomNumber, int capacity, int price) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.price = price;
    }

    public long getRoomNumber() {
        return roomNumber;
    }
    public int getCapacity() {
        return capacity;
    }
    public int getPrice() {
        return price;
    }
    /*public String getNazwa(){
        return nazwa;
    }*/
    /*public int getLiczbaLozek(){
        return liczbaLozek;
    }*/

    public void setRoomNumber(long roomNumber) {
        this.roomNumber = roomNumber;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    /*public void setNazwa(String nazwa){
        this.nazwa = nazwa;
    }*/
    /*public void setLiczbaLozek(int liczbaLozek){
        this.liczbaLozek = liczbaLozek;
    }*/
}

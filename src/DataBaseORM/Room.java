/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseORM;

/**
 *
 * @author Ivora
 */
public class Room {
    private String roomId,type;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSqMeter() {
        return sqMeter;
    }

    public void setSqMeter(int sqMeter) {
        this.sqMeter = sqMeter;
    }

    public Room(String roomId, String type, int bedroom, int bathroom, int price, int sqMeter) {
        this.roomId = roomId;
        this.type = type;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.price = price;
        this.sqMeter = sqMeter;
    }



    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBedroom() {
        return bedroom;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    public int getBathroom() {
        return bathroom;
    }

    public void setBathroom(int bathroom) {
        this.bathroom = bathroom;
    }

  
   
    private int bedroom,bathroom,price,sqMeter;        
    
            
            
            
}

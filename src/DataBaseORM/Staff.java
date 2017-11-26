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
public class Staff extends User{
    private String staffId,firstName,lastName,city,reportTo,position;

    public Staff(String staffId, String firstName, String lastName, String city, String reportTo, String position) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.reportTo = reportTo;
        this.position = position;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReportTo() {
        return reportTo;
    }

    public void setReportTo(String reportTo) {
        this.reportTo = reportTo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}

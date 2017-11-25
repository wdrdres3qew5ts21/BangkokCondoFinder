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
public class Condo {
    private String condoId,condoName,area;
    private String parking,swimming,fitness;

    public Condo(){
        
    }

    public Condo(String condoId, String condoName, String area, int parking, int swimming, int fitness) {
        this.condoId = condoId;
        this.condoName = condoName;
        this.area = area;
        this.parking =(parking==1)?"Yes":"No";
        this.swimming = (swimming==1)?"Yes":"No";
        this.fitness = (fitness==1)?"Yes":"No";
    }

    public String getCondoId() {
        return condoId;
    }

    public void setCondoId(String condoId) {
        this.condoId = condoId;
    }

    public String getCondoName() {
        return condoName;
    }

    public void setCondoName(String condoName) {
        this.condoName = condoName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getSwimming() {
        return swimming;
    }

    public void setSwimming(String swiming) {
        this.swimming = swiming;
    }

    public String getFitness() {
        return fitness;
    }

    public void setFitness(String fitness) {
        this.fitness = fitness;
    }
    
   
}
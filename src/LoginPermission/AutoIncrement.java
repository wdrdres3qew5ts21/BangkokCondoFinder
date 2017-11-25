/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoginPermission;

import java.text.DecimalFormat;

/**
 *
 * @author Ivora
 */
public class AutoIncrement {
    /**
     * ใช้ Generate key.ให้กับ primary key ห้อง, view อื่นๆ อัตโนมัติ
     * @param input 
     */
    public static String Generate(String input){
        String prefix=input.charAt(0)+"";
        input=input.substring(1, input.length());
        input=(Integer.parseInt(input)+1)+"";
        DecimalFormat df=new DecimalFormat("0000");       
        return prefix+ df.format(Integer.parseInt(input));
    }
    public static void main(String[] args) {
        System.out.println(Generate("s0009"));
        
    }
    
}

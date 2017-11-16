package Animation;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ivora
 */
public class Fade {

    /**
     * สำหรับทำให้ Transition Fade In เข้ามาอย่างสวยงามได้ Node, เวลาที่ต้องการ,
     * จำนวนรอบ
     * @param node
     * @param durationSecond
     * @param cycleCount 
     */
    public static void setFadeOut(Node node,int durationSecond,int cycleCount) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1),node);
        fade.setAutoReverse(true);
        fade.setCycleCount(cycleCount);
        fade.setFromValue(1.0);
        fade.setToValue(0);
        fade.play();
    }
    /**
     * สำหรับทำให้ Transition Fade In เข้ามาอย่างสวยงามได้ Node โดยจะทำ 1 รอบ 1วินาที
     * @param node 
     */
    public static void setFadeOut(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1),node);
        fade.setAutoReverse(true);
        fade.setCycleCount(1);
        fade.setFromValue(1.0);
        fade.setToValue(0);
        fade.play();
    }
    /**
     * Fade In โดยจะทำงานคือเข้ามา 1 วินาที 1 รอบ
     * @param node 
     */  
        public static void setFadeIn(Node node) {
            
        FadeTransition fade = new FadeTransition(Duration.seconds(1),node);
        fade.setAutoReverse(true);
        fade.setCycleCount(1);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
        /**
         * โดยจะทำการ FadeIn Animation Node ตามด้วย เวลาวินาที แล้วก็รอบที่ต้องให้วนทำงาน
         * @param node
         * @param durationSecond
         * @param cycleCount 
         */
        public static void setFadeIn(Node node,int durationSecond,int cycleCount) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1),node);
        fade.setAutoReverse(true);
        fade.setCycleCount(cycleCount);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
        
    
    
}

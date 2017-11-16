/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.home;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLHomeController implements Initializable {

    // Width and height of image in pixels
    private final double IMG_WIDTH = 600;
    private final double IMG_HEIGHT = 400;

    private final int NUM_OF_IMGS = 3;
    private final int SLIDE_FREQ = 4; // in secs
//    private StackPane root;
//    private Pane clipPane;
    @FXML
    private HBox imgContainer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        
        //image view
        ImageView imgGreen = new ImageView("http://gematsu.com/wp-content/uploads/2016/12/Blue-Reflection-Christmas-2016-Wallpaper.jpg");
        ImageView imgBlue = new ImageView("http://allgamesdelta.s-ul.eu/zzKR1Pio");
        ImageView imgRose = new ImageView("https://www.segalization.com/wp-content/uploads/2017/03/Valkyria-Revolution-and-Blue-Reflection-1080P-Wallpaper.jpg");
//        ImageView imgGreen = new ImageView("https://4...content-available-to-author-only...r.com/c6d92d641dd0f063af1aa80f8d1d36b8/tumblr_nk6mh1baYD1sqk7hko1_1280.jpg");
//        ImageView imgBlue = new ImageView("https://4...content-available-to-author-only...r.com/58f1e25e4cd1a1d990e17d1835c41f87/tumblr_niuao21Sa61sqk7hko1_1280.jpg");
//        ImageView imgRose = new ImageView("https://4...content-available-to-author-only...r.com/da455c51e4468e705a61f1800763c0e8/tumblr_niyf6pOg441sqk7hko1_1280.jpg");
    imgBlue.setPreserveRatio(true);
    
        imgContainer.getChildren().addAll(imgGreen, imgBlue, imgRose);
      
    }    
    
    private void startAnimation(final HBox hbox) {
        //error occured on (ActionEvent t) line
        //slide action
        EventHandler<ActionEvent> slideAction = (ActionEvent t) -> {
            TranslateTransition trans = new TranslateTransition(Duration.seconds(1.5), hbox);
            trans.setByX(-IMG_WIDTH);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.play();
        };
        //eventHandler
        EventHandler<ActionEvent> resetAction = (ActionEvent t) -> {
            TranslateTransition trans = new TranslateTransition(Duration.seconds(1), hbox);
            trans.setByX((NUM_OF_IMGS - 1) * IMG_WIDTH);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.play();
        };

        List<KeyFrame> keyFrames = new ArrayList<>();
        for (int i = 1; i <= NUM_OF_IMGS; i++) {
            if (i == NUM_OF_IMGS) {
                keyFrames.add(new KeyFrame(Duration.seconds(i * SLIDE_FREQ), resetAction));
            } else {
                keyFrames.add(new KeyFrame(Duration.seconds(i * SLIDE_FREQ), slideAction));
            }
        }
//add timeLine
        Timeline anim = new Timeline(keyFrames.toArray(new KeyFrame[NUM_OF_IMGS]));

        anim.setCycleCount(Timeline.INDEFINITE);
        anim.playFromStart();
    }

    
}

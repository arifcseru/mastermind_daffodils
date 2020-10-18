/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.quiz.presentationtier;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Arifur_Rahman
 */
public class ImageContainerController implements ActionListener, Initializable, ControlledScreen {
    Timer timer = new Timer(1000, (ActionListener) this);
    public static int counter = 0;
    ScreensController myController;
    @FXML
    private Label counterLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      timer.start();
      counterLabel.setText("Please Look at the picture");
      QuizScreenController.mediaVisited = true;
    }    

    @Override
    public void setScreenParent(ScreensController screensController) {
        myController = screensController;
    }   


    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        counter ++;
        
        if (counter >=5) {
            timer.stop();
            myController.setScreen(ScreensFramework.quizScreen);
        }
        System.out.println(counter);
        
    }
    
}

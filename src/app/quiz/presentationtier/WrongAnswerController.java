/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.quiz.presentationtier;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Arifur_Rahman
 */
public class WrongAnswerController implements ActionListener,Initializable, ControlledScreen {
    ScreensController myController;
    Timer timer = new Timer(1000, (ActionListener) this);
    public static int counter = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        counter = 0;
        timer.start();
    }    

    @Override
    public void setScreenParent(ScreensController screensController) {
        myController = screensController;
    }    

    @FXML
    private void mouseEventExit(MouseEvent event) {
        // myController.setScreen(ScreensFramework.quizScreen);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
          counter ++;
        
        if (counter >=3) {
            timer.stop();
            myController.setScreen(ScreensFramework.quizScreen);
        }
        System.out.println(counter);
    }

    
}

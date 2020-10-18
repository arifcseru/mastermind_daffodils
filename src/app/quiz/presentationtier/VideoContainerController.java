/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.quiz.presentationtier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Arifur_Rahman
 */
public class VideoContainerController implements ActionListener,Initializable, ControlledScreen {
     Timer timer = new Timer(1000, (ActionListener) this);
    public static int counter = 0;
    ScreensController myController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void setScreenParent(ScreensController screensController) {
        myController = screensController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        counter ++;
        
        if (counter >=5) {
            timer.stop();
            myController.setScreen(ScreensFramework.quizScreen);
        }
        System.out.println(counter);
    }
    
}

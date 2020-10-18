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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Arifur_Rahman
 */
public class CorrectAnswerController implements ActionListener, Initializable, ControlledScreen {

    Timer timer = new Timer(1000, (ActionListener) this);
    public static int counter = 0;
    ScreensController myController;

    /**
     * Initializes the controller class.
     */
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
    private void keyEventExit(KeyEvent event) {
        System.out.println("detected keyevent");
        if (event.getCode().isWhitespaceKey()) {
            //myController.setScreen(ScreensFramework.quizScreen);
        }
    }

    @FXML
    private void mouseEventExit(MouseEvent event) {
        //myController.setScreen(ScreensFramework.quizScreen);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        counter++;

        if (counter >= 3) {
            timer.stop();
            myController.setScreen(ScreensFramework.quizScreen);
        }
        System.out.println(counter);
    }

}

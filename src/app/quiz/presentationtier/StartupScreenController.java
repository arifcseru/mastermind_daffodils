/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.quiz.presentationtier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Arifur_Rahman
 */
public class StartupScreenController implements ActionListener, Initializable, ControlledScreen {

    Timer timer = new Timer(1000, (ActionListener) this);
    public static int counter = 0;

    ScreensController myController;
    @FXML
    private Label counterLabel;
    @FXML
    private ImageView startupScreenImageView;
    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    private Media media;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        counter = 0;
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        timer.start();
        commonPlayAction("mediaFiles/settings/openingwrong.wav");
        startupScreenImageView.setLayoutX(bounds.getMaxX() / 2 - startupScreenImageView.getFitWidth() / 2);
        startupScreenImageView.setLayoutY(bounds.getMaxY() / 2 - startupScreenImageView.getFitHeight() / 2);
    }

    public void commonPlayAction(String fileAddressStr) {
        String fileAddress = new File(fileAddressStr).getAbsolutePath();
        media = new Media(new File(fileAddress).toURI().toString());
        player = new MediaPlayer(media);
        player.play();
        mediaView = new MediaView(player);
        mediaView.setMediaPlayer(player);
    }

    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        counter++;

        if (counter >= 3) {
            timer.stop();
            myController.setScreen(ScreensFramework.homeScreen);
        }
        //System.out.println(counter);
    }

}

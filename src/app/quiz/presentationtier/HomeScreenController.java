/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package app.quiz.presentationtier;

import app.quiz.businesstier.QuestionTO;
import app.quiz.businesstier.QuizUtils;
import com.app.persistencetier.QuestionsService;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Angie
 */
public class HomeScreenController implements ActionListener, Initializable, ControlledScreen {

    Timer timer = new Timer(5000, (ActionListener) this);
    public static int counter = 0;
    Boolean fileEnabled = false;
    ScreensController myController;
    List<QuestionTO> questions = new ArrayList();
    File questionFile = null;
    Scanner input = new Scanner(System.in);
    String answer = "";
    @FXML
    private ImageView homeScreenImageView;
    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    private Media media;
    @FXML
    private MediaView homeScreenMediaView;
    @FXML
    private Button exitNowActionBtn;
    @FXML
    private Button loadQuestionBtn;
    @FXML
    private Button editQuestionBtn;
    @FXML
    private Button newQuestionBtn;
    @FXML
    private VBox homeScreenHBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        newQuestionBtn.setMaxSize(bounds.getWidth(), bounds.getHeight());
        loadQuestionBtn.setMaxSize(bounds.getWidth(), bounds.getHeight());
        editQuestionBtn.setMaxSize(bounds.getWidth(), bounds.getHeight());
        exitNowActionBtn.setMaxSize(bounds.getWidth(), bounds.getHeight());

        timer.start();
        commonPlayAction("mediaFiles/settings/homeScreen_continuous.wav");
        homeScreenImageView.setLayoutX((bounds.getMaxX() / 2 - homeScreenImageView.getFitWidth() / 2));
        homeScreenImageView.setLayoutY((bounds.getMaxY() / 2 - homeScreenImageView.getFitHeight() / 2));
    }

    public void commonPlayAction(String fileAddressStr) {
        String fileAddress = new File(fileAddressStr).getAbsolutePath();
        media = new Media(new File(fileAddress).toURI().toString());
        player = new MediaPlayer(media);
        player.play();
        mediaView.setMediaPlayer(player);
    }

    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    private void editQuestionAction(ActionEvent event) {
        editQuestionCommonAction();
    }

    private void editQuestionCommonAction() {
        boolean authUser = AlertBoxController.authenticateUser("edit");
        if (authUser) {
            timer.stop();
            Integer questionSetId = AlertBoxController.getSelectedQuestionSet("Load Question To Edit", "Edit");
            if (questionSetId != null) {
                AlertBoxController.questionManager("Edit Question", questionSetId);
            } else {
                timer.start();
            }

        }
    }

    @FXML
    private void newQuestionAction(ActionEvent event) {
        newQuestionCommonAction();
    }

    private void newQuestionCommonAction() {
        boolean authUser = AlertBoxController.authenticateUser("new");
        if (authUser) {
            timer.stop();
            boolean returnResult = AlertBoxController.questionManager("Set New Question", null);
            if (returnResult) {
                timer.start();
            }
        }
    }

    @FXML
    private void loadQuestionAction(ActionEvent event) {
        loadQuestionCommonAction();
    }

    public void loadQuestionCommonAction(File questionFile) {
        loadFileQuestionCommonAction(questionFile);
    }

    public void loadQuestionCommonAction() {
        boolean authUser = AlertBoxController.authenticateUser("load");
        if (authUser && (ScreensFramework.loggedInUser == "admin" || ScreensFramework.loggedInUser == "operator")) {
            timer.stop();
            Integer questionSetId = AlertBoxController.getSelectedQuestionSet("Load Question For Quiz", "Load");

            if (questionSetId != null) {
                ScreensFramework.questionSetId = questionSetId;

                ScreensFramework.questions = QuestionsService.getAllQuestions(ScreensFramework.questionSetId);
                myController.setScreen(ScreensFramework.quizScreen);
            } else {
                timer.start();
            }

        }

    }

    public void loadFileQuestionCommonAction(File questionFile) {
        try {
            if (questionFile.exists()) {
                ScreensFramework.questionFile = questionFile;
            } else {
                questionFile = ScreensFramework.questionFile;
                System.out.println("The Question doesn't exists! loading default question.");
            }
            ScreensFramework.questions = QuizAppUtils.getQuestions(questionFile);
            myController.setScreen(ScreensFramework.quizScreen);
        } catch (IOException ex) {
            Logger.getLogger(HomeScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void exitNowAction(ActionEvent event) {
        exitNowCommonAction();
    }

    private void exitNowCommonAction() {
        Boolean result = AlertBoxController.confirmExit("Alert Box", "Do you want to exit?");
        if (result) {
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        counter++;
        if (timer.isRunning()) {
            commonPlayAction("mediaFiles/settings/homeScreen_continuous.wav");
        }
        
    }

    @FXML
    private void keyEventAction(KeyEvent event) {
        //System.out.println(event.getCharacter());
        if (event.getCode().isLetterKey()) {
            //System.out.println("Letter");
        }
        if (event.getCode().isWhitespaceKey()) {
            // System.out.println("Whitespace");
        }
        if (event.getCharacter().equalsIgnoreCase("N")) {
            newQuestionCommonAction();
        } else if (event.getCharacter().equalsIgnoreCase("L")) {
            loadQuestionCommonAction();
        } else if (event.getCharacter().equalsIgnoreCase("E")) {
            editQuestionCommonAction();
        } else if (event.getCharacter().equalsIgnoreCase("X")) {
            exitNowCommonAction();
        }
    }
}

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

import com.app.persistencetier.QuestionsService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author Angie
 */
public class ResultScreenController implements Initializable, ControlledScreen {

    ScreensController myController;
    File questionFile = null;
    @FXML
    private Button loadRoundQuestionButton;
    @FXML
    private Label quizCompleteMessage;
    @FXML
    private Label resultLabel;
    @FXML
    private VBox resultScreenVBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quizCompleteMessage.setText("Quiz Successfully Completed ");
        resultLabel.setText("Correct Answered: " + ScreensFramework.correctAnswers + " Out of " + ScreensFramework.questions.size());
        //commonPlayAction("mediaFiles/settings/homeScreen_continuous.wav");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        resultScreenVBox.setLayoutX((bounds.getMaxX() / 2 - resultScreenVBox.getWidth()/ 2));
        resultScreenVBox.setLayoutY((bounds.getMaxY() / 2 - resultScreenVBox.getHeight() / 2));
    }

    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    private void goToScreen1(ActionEvent event) {
        ScreensFramework.currentQuestion = 0;
        myController.setScreen(ScreensFramework.homeScreen);
    }


    @FXML
    private void loadQuestionAction(ActionEvent event) {
        loadQuestionCommonAction();
    }

    public void loadQuestionCommonAction() {
        Integer questionSetId = AlertBoxController.getSelectedQuestionSet("Load Question For Quiz", "Load");
        if (questionSetId != null) {
            ScreensFramework.questionSetId = questionSetId;
            ScreensFramework.currentQuestion = 0;
            ScreensFramework.questions = QuestionsService.getAllQuestions(questionSetId);
            myController.setScreen(ScreensFramework.quizScreen);
        }

    }

    public void loadQuestionCommonAction(File questionFile) {
        //timer.stop();
        try {
            //questionFile = QuizUtils.browseMultimediaFile();

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
}

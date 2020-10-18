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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Angie
 */
public class ScreensFramework extends Application {

    public static Integer currentQuestion = 0;
    public static List<QuestionTO> questions = new ArrayList();
    public static File questionFile = new File("Questions/demo.csv");

    public static String startupScreen = "StartupScreen";
    public static String startupScreenFile = "StartupScreen.fxml";

    public static String homeScreen = "HomeScreen";
    public static String homeScreenFile = "HomeScreen.fxml";
    public static String quizScreen = "QuizScreen";
    public static String quizScreenFile = "QuizScreen.fxml";
    public static String newQuestionsScreen = "NewQuestionsScreen";
    public static String newQuestionsScreenFile = "NewQuestionsScreen.fxml";

    public static String correctAnswer = "CorrectAnswer";
    public static String correctAnswerFile = "CorrectAnswer.fxml";
    public static String wrongAnswer = "WrongAnswer";
    public static String wrongAnswerFile = "WrongAnswer.fxml";

    public static String imageContainer = "ImageContainer";
    public static String imageContainerFile = "ImageContainer.fxml";
    public static String audioContainer = "AudioContainer";
    public static String audioContainerFile = "AudioContainer.fxml";
    public static String videoContainer = "VideoContainer";
    public static String videoContainerFile = "VideoContainer.fxml";

    public static String resultScreen = "ResultScreen";
    public static String resultScreenFile = "ResultScreen.fxml";
    public static Scene scene;
    static Integer questionSetId;
    static int correctAnswers = 0;
    static String loggedInUser;
    static String quizRoundName="Mastermind Quiz";

    @Override
    public void start(Stage primaryStage) {

        ScreensController mainContainer = new ScreensController();
        loadAllScreens(mainContainer);
        
        mainContainer.setScreen(ScreensFramework.startupScreen);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        // Scene scene = new Scene(root);
        // primaryStage.initStyle(StageStyle.UNDECORATED);
        scene = new Scene(root, Color.BLACK);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // primaryStage.setX(bounds.getMinX());
        // primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);

        primaryStage.show();
        /*Media pick = new Media("data/quiz_start.mp3");
        MediaPlayer player = new MediaPlayer(pick);
        player.play();
        
        MediaView mediaView = new MediaView(player);
        ((Group)scene.getRoot()).getChildren().add(mediaView);*/
 /* scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals("A")) {
                }
                System.out.println("OK Done "+ ke.getCode());
            }
        });*/
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void loadHomeScreen(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.homeScreen, ScreensFramework.homeScreenFile);
    }

    public static void loadQuizScreen(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.quizScreen, ScreensFramework.quizScreenFile);
    }

    public static void loadImageContainerScreen(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.imageContainer, ScreensFramework.imageContainerFile);
    }

    public static void loadVideoContainerScreen(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.videoContainer, ScreensFramework.videoContainerFile);
    }

    public static void loadAudioContainerScreen(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.audioContainer, ScreensFramework.audioContainerFile);
    }

    public static void loadCorrectAnswer(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.correctAnswer, ScreensFramework.correctAnswerFile);
    }

    public static void loadWrongAnswer(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.wrongAnswer, ScreensFramework.wrongAnswerFile);
    }
     public static void loadResultScreen(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.resultScreen, ScreensFramework.resultScreenFile);
    } 
     public static void loadStartupScreen(ScreensController mainContainer) {
        mainContainer.loadScreen(ScreensFramework.startupScreen, ScreensFramework.startupScreenFile);
    }
    private static void loadAllScreens(ScreensController mainContainer) {

        mainContainer.loadScreen(ScreensFramework.startupScreen, ScreensFramework.startupScreenFile);
        //mainContainer.loadScreen(ScreensFramework.newQuestionsScreen, ScreensFramework.newQuestionsScreenFile);

       // mainContainer.loadScreen(ScreensFramework.resultScreen, ScreensFramework.resultScreenFile);
    }
}

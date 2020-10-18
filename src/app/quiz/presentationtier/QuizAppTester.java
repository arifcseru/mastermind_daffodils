/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.quiz.presentationtier;

import app.quiz.businesstier.QuestionTO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Redeemer
 */
public class QuizAppTester {

    public static void main(String args[]) {
        List<QuestionTO> questions = new ArrayList();
        File questionFile = new File("Questions/class1.csv");
        questions = null;

        Scanner input = new Scanner(System.in);
        String answer = "";

        while (!answer.equalsIgnoreCase("0")) {
            System.out.println("1. New\n2. Load\n3. Edit\n0. Exit");
            answer = input.next();
            if (answer.equalsIgnoreCase("1")) {
                try {
                    QuizAppUtils.setNewQuestion();
                    //iniatializeQuizApp();
                } catch (IOException ex) {
                    Logger.getLogger(QuizAppTester.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (answer.equalsIgnoreCase("2")) {
                try {
                    questionFile = QuizAppUtils.browseQuestionFile();
                    questions = QuizAppUtils.getQuestions(questionFile);
                    QuizAppUtils.startQuiz(questions);
                } catch (IOException ex) {
                    Logger.getLogger(QuizAppTester.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (answer.equalsIgnoreCase("3")) {
                editOldQuestion();
            } else if (answer.equalsIgnoreCase("0")) {
                System.out.println("Thanks for using this Application");
            }
        }

    }

    private static void editOldQuestion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

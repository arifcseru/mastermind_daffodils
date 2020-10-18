package com.app.persistencetier;

import app.quiz.businesstier.QuestionSetNameTO;
import app.quiz.businesstier.QuestionTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class QuestionsService {

    public static Integer createQuestion(QuestionTO questionTO) {
        Integer questionId = null;
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "INSERT INTO questions (questionSetId,qsSequenceId,questionDetails,optionA,optionB,optionC,optionD,correctOption,fileLocation,roundId) VALUES('"
                    + questionTO.getQuestionSetId()
                    + "','"
                    + questionTO.getQsSequenceId()
                    + "','"
                    + questionTO.getQuestionDetail()
                    + "','"
                    + questionTO.getOptionA()
                    + "','"
                    + questionTO.getOptionB()
                    + "','"
                    + questionTO.getOptionC()
                    + "','"
                    + questionTO.getOptionD()
                    + "','"
                    + questionTO.getCorrectOption()
                    + "','"
                    + questionTO.getFileLocation()
                    + "','"
                    + questionTO.getRoundId()
                    + "')";
            statement.executeUpdate(sql);
            statement.close();
            statement = connection.createStatement();
            questionId = statement.getGeneratedKeys().getInt(1);
            //sql = "INSERT INTO roundQuestions (questionId,roundId) VALUES('"+questionId+"','"+roundId+"')";
            // statement.executeUpdate(sql);
            // System.out.println(questionId);
            //System.out.println();
            //statement.close();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        //System.out.println("dksjaf");
        return questionId;
    }

    public static List<Integer> createQuestions(List<QuestionTO> questions, QuestionSetNameTO questionSetNameTO) {
        List<Integer> questionIds = new ArrayList<Integer>();
        Integer questionSetId = new QuestionSetNamesService().createQuestionSet(questionSetNameTO);
        for (QuestionTO questionTO : questions) {
            //questionTO.setRoundId(roundId);
            questionTO.setQuestionSetId(questionSetId);
            Integer questionId = createQuestion(questionTO);
            questionIds.add(questionId);
        }
        return questionIds;
    }

    public static QuestionTO getQuestion(Integer questionId) {
        return null;
    }

    public static List<QuestionTO> getAllQuestions(Integer questionSetId) {
        Connection connection = null;
        Statement statement = null;
        QuestionTO questionTO = null;
        List<QuestionTO> questions = new ArrayList<QuestionTO>();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "SELECT * FROM questions WHERE questionSetId IN "
                    + "(SELECT questionSetId "
                    + "FROM QuestionSetName "
                    + "WHERE questionSetId = "
                    + questionSetId + ") ORDER BY qsSequenceId ASC ";
            //c.prepareStatement(sql, roundId);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                questionTO = new QuestionTO();
                questionTO.setQuestionId(Integer.parseInt(rs.getString("questionId")));
                questionTO.setQsSequenceId(Integer.parseInt(rs.getString("qsSequenceId")));
                questionTO.setQuestionDetail(rs.getString("questionDetails"));
                questionTO.setOptionA(rs.getString("optionA"));
                questionTO.setOptionB(rs.getString("optionB"));
                questionTO.setOptionC(rs.getString("optionC"));
                questionTO.setOptionD(rs.getString("optionD"));
                questionTO.setCorrectOption(rs.getString("correctOption"));
                questionTO.setFileLocation(rs.getString("fileLocation"));
                questions.add(questionTO);
            }
            statement.close();
            connection.close();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return (ArrayList<QuestionTO>) questions;
    }

    public static Boolean updateQuestion(Integer qsSequenceId, Integer questionSetId, QuestionTO questionTO) {
        Boolean updateResult = false;
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "UPDATE questions SET "
                    + " qsSequenceId=" + questionTO.getQsSequenceId()
                    + ", questionDetail=" + questionTO.getQuestionDetail()
                    + ", optionA=" + questionTO.getOptionA()
                    + ", optionB=" + questionTO.getOptionB()
                    + ", optionC=" + questionTO.getOptionC()
                    + ", optionD=" + questionTO.getOptionD()
                    + ", correctOption=" + questionTO.getCorrectOption()
                    + ", fileLocation=" + questionTO.getFileLocation()
                    + " WHERE questionSetId = " + questionSetId + " AND qsSequenceId = " + qsSequenceId;
            //c.prepareStatement(sql, roundId);
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
            updateResult = true;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return updateResult;
    }

    public static Boolean updateQuestions(List<QuestionTO> questions, Integer questionSetId) {
        Boolean updateResult = false;
        for (QuestionTO questionTO : questions) {
            updateResult = updateQuestion(questionTO.getQsSequenceId(), questionSetId, questionTO);
        }
        return updateResult;
    }

    public static Boolean saveQuestions(List<QuestionTO> questions, Integer questionSetId) {
        Boolean updateResult = false;
        for (QuestionTO questionTO : questions) {
            if (isAvailableQuestion(questionSetId, questionTO.getQsSequenceId())) {
                questionTO.setQuestionSetId(questionSetId);
                updateResult = updateQuestion(questionTO.getQsSequenceId(), questionSetId, questionTO);
            }else{
                questionTO.setQuestionSetId(questionSetId);
                createQuestion(questionTO);
            }
            
        }
        return updateResult;
    }
    public static Boolean createOrStoreQuestions(List<QuestionTO> questions, Integer questionSetId) {
        Boolean updateResult = false;
        for (QuestionTO questionTO : questions) {
            if (isAvailableQuestion(questionSetId, questionTO.getQsSequenceId())) {
                questionTO.setQuestionSetId(questionSetId);
                updateResult = updateQuestion(questionTO.getQsSequenceId(), questionSetId, questionTO);
            }else{
                questionTO.setQuestionSetId(questionSetId);
                createQuestion(questionTO);
            }
            
        }
        return updateResult;
    }

    public static Boolean isAvailableQuestion(Integer questionSetId, Integer qsSquenceId) {
        Boolean available = false;
        List<QuestionTO> questions = getQuestions(questionSetId, qsSquenceId);
        if (questions.size()>=1) {
            available = true;
        }
        return available;
    }
    public static List<QuestionTO> getQuestions(Integer questionSetId, Integer qsSequenceId){
        Connection connection = null;
        Statement statement = null;
        QuestionTO questionTO = null;
        List<QuestionTO> questions = new ArrayList<QuestionTO>();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "SELECT * FROM questions WHERE questionSetId = '" + questionSetId + "' AND qsSequenceId = '" + qsSequenceId + "' ORDER BY qsSequenceId ASC ";
            //c.prepareStatement(sql, roundId);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                questionTO = new QuestionTO();
                questionTO.setQuestionId(Integer.parseInt(rs.getString("questionId")));
                questionTO.setQsSequenceId(Integer.parseInt(rs.getString("qsSequenceId")));
                questionTO.setQuestionDetail(rs.getString("questionDetails"));
                questionTO.setOptionA(rs.getString("optionA"));
                questionTO.setOptionB(rs.getString("optionB"));
                questionTO.setOptionC(rs.getString("optionC"));
                questionTO.setOptionD(rs.getString("optionD"));
                questionTO.setCorrectOption(rs.getString("correctOption"));
                questionTO.setFileLocation(rs.getString("fileLocation"));
                questions.add(questionTO);
            }
            statement.close();
            connection.close();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return questions;
    } 
    public static Boolean deleteQuestion(Integer questionId) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "DELETE FROM questions WHERE questionId = '"+questionId+"'";
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        return true;
    }
    public static Boolean deleteSetQuestion(Integer questionSetId) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "DELETE FROM questions WHERE questionSetId = '"+questionSetId+"'";
            statement.execute(sql);
            statement.close();
            connection.close();
            QuestionSetNamesService.deleteQuestionSetDetails(questionSetId);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        return true;
    }

    public static Boolean deleteQuestions(List<QuestionTO> questions) {
        List<Integer> questionIds = new ArrayList<Integer>();
        Boolean deleteResult = false;
        for (QuestionTO question : questions) {
            deleteResult = deleteQuestion(question.getQuestionId());
        }
        return deleteResult;
    }
    

    public static void main(String[] args) {
        Boolean result =  deleteSetQuestion(9);
        System.out.println(result);

    }
}

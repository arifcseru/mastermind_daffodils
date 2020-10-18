package app.quiz.businesstier;

import javafx.beans.property.SimpleStringProperty;

public class QuestionTO {

    private Integer questionId;
    private Integer questionSetId;
    private Integer qsSequenceId;
    private String questionDetail;
    private String questionType;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;

    private String fileLocation;
    private Integer roundId;
    private String questionSetName;

    public QuestionTO() {
        this.qsSequenceId = 1;
        this.questionDetail = "";
        this.optionA = "";
        this.optionB = "";
        this.optionC = "";
        this.optionD = "";
        this.correctOption = "";
        this.fileLocation = "";
    }

    public QuestionTO(Integer qsSequenceId, String questionDetail, String optionA, String optionB, String optionC ,String optionD) {
        this.qsSequenceId = qsSequenceId;
        this.questionDetail = questionDetail;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(Integer questionSetId) {
        this.questionSetId = questionSetId;
    }

    public Integer getQsSequenceId() {
        return qsSequenceId;
    }

    public void setQsSequenceId(Integer qsSequenceId) {
        this.qsSequenceId = qsSequenceId;
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Integer getRoundId() {
        return roundId;
    }

    public void setRoundId(Integer roundId) {
        this.roundId = roundId;
    }

    public String getQuestionSetName() {
        return questionSetName;
    }

    public void setQuestionSetName(String questionSetName) {
        this.questionSetName = questionSetName;
    }

}

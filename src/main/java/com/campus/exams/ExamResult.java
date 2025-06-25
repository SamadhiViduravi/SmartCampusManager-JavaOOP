package com.campus.exams;

import com.campus.utils.Identifiable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ExamResult class representing student exam results
 */
public class ExamResult implements Identifiable {
    private String resultId;
    private String studentId;
    private String examId;
    private int marksObtained;
    private int maxMarks;
    private double percentage;
    private String grade;
    private String letterGrade;
    private double gradePoints;
    private boolean isPassed;
    private String remarks;
    private LocalDateTime submissionTime;
    private LocalDateTime evaluationTime;
    private String evaluatedBy;
    private boolean isAbsent;
    private boolean isMalpractice;
    private String malpracticeDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public ExamResult(String studentId, String examId, int marksObtained, int maxMarks, String grade) {
        this.resultId = generateResultId(studentId, examId);
        this.studentId = studentId;
        this.examId = examId;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
        this.grade = grade;
        this.isAbsent = false;
        this.isMalpractice = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        calculateDerivedValues();
    }
    
    private String generateResultId(String studentId, String examId) {
        return "R" + studentId + "_" + examId + "_" + System.currentTimeMillis() % 10000;
    }
    
    private void calculateDerivedValues() {
        // Calculate percentage
        this.percentage = maxMarks > 0 ? (double) marksObtained / maxMarks * 100 : 0.0;
        
        // Determine pass/fail (assuming 40% is passing)
        this.isPassed = percentage >= 40.0 && !isAbsent && !isMalpractice;
        
        // Calculate letter grade and grade points
        calculateLetterGradeAndPoints();
        
        // Set remarks
        setRemarksBasedOnPerformance();
    }
    
    private void calculateLetterGradeAndPoints() {
        if (isAbsent) {
            this.letterGrade = "AB";
            this.gradePoints = 0.0;
        } else if (isMalpractice) {
            this.letterGrade = "MP";
            this.gradePoints = 0.0;
        } else if (percentage >= 90) {
            this.letterGrade = "A+";
            this.gradePoints = 4.0;
        } else if (percentage >= 85) {
            this.letterGrade = "A";
            this.gradePoints = 3.7;
        } else if (percentage >= 80) {
            this.letterGrade = "A-";
            this.gradePoints = 3.3;
        } else if (percentage >= 75) {
            this.letterGrade = "B+";
            this.gradePoints = 3.0;
        } else if (percentage >= 70) {
            this.letterGrade = "B";
            this.gradePoints = 2.7;
        } else if (percentage >= 65) {
            this.letterGrade = "B-";
            this.gradePoints = 2.3;
        } else if (percentage >= 60) {
            this.letterGrade = "C+";
            this.gradePoints = 2.0;
        } else if (percentage >= 55) {
            this.letterGrade = "C";
            this.gradePoints = 1.7;
        } else if (percentage >= 50) {
            this.letterGrade = "C-";
            this.gradePoints = 1.3;
        } else if (percentage >= 40) {
            this.letterGrade = "D";
            this.gradePoints = 1.0;
        } else {
            this.letterGrade = "F";
            this.gradePoints = 0.0;
        }
    }
    
    private void setRemarksBasedOnPerformance() {
        if (isAbsent) {
            this.remarks = "Absent";
        } else if (isMalpractice) {
            this.remarks = "Malpractice detected";
        } else if (percentage >= 90) {
            this.remarks = "Outstanding performance";
        } else if (percentage >= 80) {
            this.remarks = "Excellent performance";
        } else if (percentage >= 70) {
            this.remarks = "Good performance";
        } else if (percentage >= 60) {
            this.remarks = "Satisfactory performance";
        } else if (percentage >= 40) {
            this.remarks = "Pass";
        } else {
            this.remarks = "Fail - Needs improvement";
        }
    }
    
    public void updateMarks(int newMarks, String newGrade) {
        this.marksObtained = newMarks;
        this.grade = newGrade;
        this.updatedAt = LocalDateTime.now();
        calculateDerivedValues();
    }
    
    public void markAbsent() {
        this.isAbsent = true;
        this.marksObtained = 0;
        this.updatedAt = LocalDateTime.now();
        calculateDerivedValues();
    }
    
    public void markMalpractice(String details) {
        this.isMalpractice = true;
        this.malpracticeDetails = details;
        this.marksObtained = 0;
        this.updatedAt = LocalDateTime.now();
        calculateDerivedValues();
    }
    
    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setEvaluationDetails(String evaluatedBy, LocalDateTime evaluationTime) {
        this.evaluatedBy = evaluatedBy;
        this.evaluationTime = evaluationTime;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addRemarks(String additionalRemarks) {
        this.remarks = (remarks != null ? remarks + "; " : "") + additionalRemarks;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isExcellent() {
        return percentage >= 85.0;
    }
    
    public boolean isGood() {
        return percentage >= 70.0 && percentage < 85.0;
    }
    
    public boolean isSatisfactory() {
        return percentage >= 60.0 && percentage < 70.0;
    }
    
    public boolean needsImprovement() {
        return percentage < 60.0 && !isAbsent && !isMalpractice;
    }
    
    public void displayResultInfo() {
        System.out.println("=== EXAM RESULT ===");
        System.out.println("Result ID: " + resultId);
        System.out.println("Student ID: " + studentId);
        System.out.println("Exam ID: " + examId);
        System.out.println("Marks Obtained: " + marksObtained + "/" + maxMarks);
        System.out.println("Percentage: " + String.format("%.2f", percentage) + "%");
        System.out.println("Grade: " + grade);
        System.out.println("Letter Grade: " + letterGrade);
        System.out.println("Grade Points: " + gradePoints);
        System.out.println("Result: " + (isPassed ? "PASS" : "FAIL"));
        System.out.println("Absent: " + (isAbsent ? "Yes" : "No"));
        System.out.println("Malpractice: " + (isMalpractice ? "Yes" : "No"));
        if (isMalpractice && malpracticeDetails != null) {
            System.out.println("Malpractice Details: " + malpracticeDetails);
        }
        System.out.println("Remarks: " + remarks);
        System.out.println("Submission Time: " + (submissionTime != null ? submissionTime : "Not recorded"));
        System.out.println("Evaluation Time: " + (evaluationTime != null ? evaluationTime : "Not evaluated"));
        System.out.println("Evaluated By: " + (evaluatedBy != null ? evaluatedBy : "Not specified"));
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
    }
    
    // Getters and Setters
    @Override
    public String getId() { return resultId; }
    
    public String getResultId() { return resultId; }
    public String getStudentId() { return studentId; }
    public String getExamId() { return examId; }
    public int getMarksObtained() { return marksObtained; }
    public int getMaxMarks() { return maxMarks; }
    public double getPercentage() { return percentage; }
    public String getGrade() { return grade; }
    public String getLetterGrade() { return letterGrade; }
    public double getGradePoints() { return gradePoints; }
    public boolean isPassed() { return isPassed; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { 
        this.remarks = remarks;
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getSubmissionTime() { return submissionTime; }
    public LocalDateTime getEvaluationTime() { return evaluationTime; }
    public String getEvaluatedBy() { return evaluatedBy; }
    public boolean isAbsent() { return isAbsent; }
    public boolean isMalpractice() { return isMalpractice; }
    public String getMalpracticeDetails() { return malpracticeDetails; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamResult that = (ExamResult) o;
        return Objects.equals(resultId, that.resultId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(resultId);
    }
    
    @Override
    public String toString() {
        return "ExamResult{" +
                "resultId='" + resultId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", examId='" + examId + '\'' +
                ", marksObtained=" + marksObtained +
                ", maxMarks=" + maxMarks +
                ", percentage=" + String.format("%.2f", percentage) +
                ", letterGrade='" + letterGrade + '\'' +
                ", isPassed=" + isPassed +
                '}';
    }
}
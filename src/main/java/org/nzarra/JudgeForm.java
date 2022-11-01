package org.nzarra;

import java.util.List;

public class JudgeForm {

    private Integer sectionId;

    private List<Score> scores;

    public JudgeForm() {}

    public JudgeForm(Integer sectionId, List<Score> scores) {
        this.sectionId = sectionId;
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Section ID: " + sectionId + ", Scores: " + scores;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}

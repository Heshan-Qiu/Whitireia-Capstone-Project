package org.nzarra;

import java.util.List;

public class MarshallForm {

    private Integer sectionId;

    private List<Competitor> competitors;

    public MarshallForm() {}

    public MarshallForm(Integer sectionId, List<Competitor> competitors) {
        this.sectionId = sectionId;
        this.competitors = competitors;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }

    @Override
    public String toString() {
        return "Section ID: " + sectionId + " Competitors: " + competitors;
    }
}

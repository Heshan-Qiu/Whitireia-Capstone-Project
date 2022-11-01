package org.nzarra;

import javax.persistence.*;

@Entity
public class Score {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer variety;

    private Integer timing;

    private Integer harmony;

    private Integer total;

    private Integer placing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id")
    private User judge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competitor_id")
    private Competitor competitor;

    protected Score() {}

    public Score(Competitor competitor) {
        this.competitor = competitor;
    }

    public Score(Section section, User judge, Competitor competitor, Integer variety, Integer timing, Integer harmony,
                 Integer total, Integer placing) {
        this.section = section;
        this.judge = judge;
        this.competitor = competitor;
        this.variety = variety;
        this.timing = timing;
        this.harmony = harmony;
        this.total = total;
        this.placing = placing;
    }

    @Override
    public String toString() {
        return "Section Name: " + (section != null ? section.getName() : "") +
                ", Judge: " + (judge != null ? judge.getUsername() : "") +
                ", Competitors: " + (competitor != null ? competitor.getNames() : "") +
                ", Variety: " + variety + ", Timing: " + timing + ", Harmony: " + harmony + ", Total: " + total +
                ", Placing: " + placing;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVariety() {
        return variety;
    }

    public void setVariety(Integer variety) {
        this.variety = variety;
    }

    public Integer getTiming() {
        return timing;
    }

    public void setTiming(Integer timing) {
        this.timing = timing;
    }

    public Integer getHarmony() {
        return harmony;
    }

    public void setHarmony(Integer harmony) {
        this.harmony = harmony;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPlacing() {
        return placing;
    }

    public void setPlacing(Integer placing) {
        this.placing = placing;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public User getJudge() {
        return judge;
    }

    public void setJudge(User judge) {
        this.judge = judge;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }
}

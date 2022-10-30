package org.nzarra;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
public class Section {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer index;

    private String name;

    private Time time;

    private String song;

    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    protected Section() {
    }

    public Section(Integer index, String name, Time time, String song, boolean active, Competition competition) {
        this.index = index;
        this.name = name;
        this.time = time;
        this.song = song;
        this.active = active;
        this.competition = competition;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }
}
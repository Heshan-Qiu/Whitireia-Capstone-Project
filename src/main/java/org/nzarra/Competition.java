package org.nzarra;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Competition {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private Date date;

    private String location;

    private Integer sections;

    @ElementCollection
    @CollectionTable(name = "competition_judges", joinColumns = @JoinColumn (name = "competition_id"))
    private List<String> judges;

    @ElementCollection
    @CollectionTable(name = "competition_scrutineers", joinColumns = @JoinColumn (name = "competition_id"))
    private List<String> scrutineers;

    private String marshall;

    private boolean active;

    protected Competition() {}

    public Competition(String name, Date date, String location, Integer sections, List<String> judges,
                       List<String> scrutineers, String marshall, boolean active) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.sections = sections;
        this.judges = judges;
        this.scrutineers = scrutineers;
        this.marshall = marshall;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSections() {
        return sections;
    }

    public void setSections(Integer sections) {
        this.sections = sections;
    }

    public List<String> getJudges() {
        return judges;
    }

    public void setJudges(List<String> judges) {
        this.judges = judges;
    }

    public List<String> getScrutineers() {
        return scrutineers;
    }

    public void setScrutineers(List<String> scrutineers) {
        this.scrutineers = scrutineers;
    }

    public String getMarshall() {
        return marshall;
    }

    public void setMarshall(String marshall) {
        this.marshall = marshall;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

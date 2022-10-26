package org.nzarra;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Competition {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Date date;

    private String location;

    private Integer sections;

    private boolean active;

    protected Competition() {}

    public Competition(String name, Date date, String location, Integer sections, boolean active) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.sections = sections;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

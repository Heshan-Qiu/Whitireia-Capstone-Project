package org.nzarra;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Competitor {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer lineup;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "competitor_name", joinColumns = @JoinColumn (name = "id"))
    private List<String> names;

    private String colours;

    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    protected Competitor() {}

    public Competitor(Integer id, Integer lineup, List<String> names, String colours, String number) {
        this.id = id;
        this.lineup = lineup;
        this.names = names;
        this.colours = colours;
        this.number = number;
    }

    public Competitor(Integer lineup, List<String> names, String colours, String number, Section section) {
        this.lineup = lineup;
        this.names = names;
        this.colours = colours;
        this.number = number;
        this.section = section;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Competitor competitor = (Competitor) obj;
        return Objects.equals(id, competitor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", lineup: " + lineup + ", names: " + names + ", colours: " + colours + ", number: " + number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLineup() {
        return lineup;
    }

    public void setLineup(Integer lineup) {
        this.lineup = lineup;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getColours() {
        return colours;
    }

    public void setColours(String colours) {
        this.colours = colours;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}

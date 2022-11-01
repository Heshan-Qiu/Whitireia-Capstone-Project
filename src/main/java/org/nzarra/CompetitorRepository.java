package org.nzarra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitorRepository extends JpaRepository<Competitor, Integer> {

    public List<Competitor> findAllBySection(Section section);

    public void deleteAllBySection(Section section);
}

package org.nzarra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    Page<Section> findAllByCompetition(Competition competition, Pageable pageable);

    List<Section> findAllByCompetitionAndActive(Competition competition, boolean active);
}

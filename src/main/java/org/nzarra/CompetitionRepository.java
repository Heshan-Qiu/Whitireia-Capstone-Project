package org.nzarra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {

    List<Competition> findAllByMarshallAndActive(String marshall, boolean active);

    Optional<Competition> findFirstByJudgesInAndActiveOrderByDateDesc(List<String> judges, boolean active);
}

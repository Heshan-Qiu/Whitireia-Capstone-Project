package org.nzarra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {

    List<Competition> findAllByMarshallAndActive(String marshall, boolean active);
}

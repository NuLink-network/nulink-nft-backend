package com.project.nulinknft.repository;

import com.project.nulinknft.entity.Referrer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferrerRepository extends JpaRepository<Referrer, Long> {
}

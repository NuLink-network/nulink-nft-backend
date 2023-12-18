package com.project.nulinknft.repository;

import com.project.nulinknft.entity.BlindBox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlindBoxRepository extends JpaRepository<BlindBox, Long> {

    Page<BlindBox> findAll(Specification<BlindBox> spec, Pageable pageable);

    long countBlindBoxByUserAndRecommenderNotNull(String user);

}

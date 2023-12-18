package com.project.nulinknft.repository;

import com.project.nulinknft.entity.ContractOffset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractOffsetRepository extends JpaRepository<ContractOffset, Long> {

    ContractOffset findByContractAddress(String contractAddress);
}

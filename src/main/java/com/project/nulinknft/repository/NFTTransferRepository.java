package com.project.nulinknft.repository;

import com.project.nulinknft.entity.NFTTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NFTTransferRepository  extends JpaRepository<NFTTransfer, Long> {
}

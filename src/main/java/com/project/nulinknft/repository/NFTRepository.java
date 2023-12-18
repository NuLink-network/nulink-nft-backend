package com.project.nulinknft.repository;

import com.project.nulinknft.entity.NFT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NFTRepository  extends JpaRepository<NFT, Long> {

    NFT findByTokenId(int tokenId);

    List<NFT> findAllByOwnerOrderByCreateTime(String owner);

    List<NFT> findAllByOwnerAndIsDecryptedOrderByCreateTime(String owner, boolean isDecrypted);

}

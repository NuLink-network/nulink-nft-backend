package com.project.nulinknft.repository;

import com.project.nulinknft.entity.Decrypting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecryptingRepository extends JpaRepository<Decrypting, Long> {

    Decrypting findByTokenId(int tokenId);

}

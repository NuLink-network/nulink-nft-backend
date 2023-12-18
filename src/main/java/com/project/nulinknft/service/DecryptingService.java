package com.project.nulinknft.service;

import com.project.nulinknft.dto.DecryptingDTO;
import com.project.nulinknft.entity.Decrypting;
import com.project.nulinknft.entity.NFT;
import com.project.nulinknft.exception.EntityExistException;
import com.project.nulinknft.repository.DecryptingRepository;
import com.project.nulinknft.repository.NFTRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DecryptingService {

    private final DecryptingRepository decryptingRepository;
    private final NFTRepository nftRepository;

    public DecryptingService(DecryptingRepository decryptingRepository, NFTRepository nftRepository) {
        this.decryptingRepository = decryptingRepository;
        this.nftRepository = nftRepository;
    }

    public Decrypting findByTokenId(int tokenId){
        return decryptingRepository.findByTokenId(tokenId);
    }

    @Transactional
    public void create(DecryptingDTO decrypting){
        Decrypting d = decryptingRepository.findByTokenId(decrypting.getTokenId());
        NFT nft = nftRepository.findByTokenId(decrypting.getTokenId());
        nft.setDecrypted(true);
        nft.setContent(decrypting.getContent());
        nftRepository.save(nft);
        if (null != d){
            throw new EntityExistException(Decrypting.class, "tokenId", String.valueOf(decrypting.getTokenId()));
        }
        d = new Decrypting();
        d.setTokenId(decrypting.getTokenId());
        d.setMetamaskUser(decrypting.getMetamaskUser());
        d.setPreUser(decrypting.getPreUser());
        decryptingRepository.save(d);
    }
}

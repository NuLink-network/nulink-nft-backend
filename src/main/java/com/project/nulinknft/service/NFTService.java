package com.project.nulinknft.service;

import com.project.nulinknft.entity.NFT;
import com.project.nulinknft.repository.NFTRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class NFTService {

    private final NFTRepository nftRepository;


    public NFTService(NFTRepository nftRepository) {
        this.nftRepository = nftRepository;
    }

    @Transactional
    public void create(NFT nft){
        nftRepository.save(nft);
    }

    public List<NFT> findByOwner(String owner){
        return nftRepository.findAllByOwnerOrderByCreateTime(owner);
    }

    public List<NFT> findByOwnerIsDecrypted(String owner, boolean Decrypted){
        return nftRepository.findAllByOwnerAndIsDecryptedOrderByCreateTime(owner, Decrypted);
    }

    public NFT findByTokenId(Integer tokenId){
        return nftRepository.findByTokenId(tokenId);
    }
}

package com.project.nulinknft.service;

import com.project.nulinknft.entity.NFT;
import com.project.nulinknft.entity.NFTTransfer;
import com.project.nulinknft.repository.NFTRepository;
import com.project.nulinknft.repository.NFTTransferRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class NFTTransferService {

    private final NFTTransferRepository nftTransferRepository;
    private final NFTRepository nftRepository;

    public NFTTransferService(NFTTransferRepository nftTransferRepository, NFTRepository nftRepository) {
        this.nftTransferRepository = nftTransferRepository;
        this.nftRepository = nftRepository;
    }

    @Transactional
    public void create(NFTTransfer nftTransfer){
        nftTransferRepository.save(nftTransfer);
        NFT nft = nftRepository.findByTokenId(nftTransfer.getTokenId());
        nft.setOwner(nftTransfer.getToAddress());
        nftRepository.save(nft);
    }
}

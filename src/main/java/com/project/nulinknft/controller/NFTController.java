package com.project.nulinknft.controller;

import com.project.nulinknft.dto.BaseResponse;
import com.project.nulinknft.entity.NFT;
import com.project.nulinknft.service.NFTService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "nft")
@RestController
@RequestMapping("nft")
public class NFTController {

    private final NFTService nftService;

    public NFTController(NFTService nftService) {
        this.nftService = nftService;
    }

    @GetMapping("findByOwner")
    public BaseResponse<List<NFT>> findByOwner(@RequestParam("owner") String owner){
        List<NFT> list = nftService.findByOwner(owner);
        return BaseResponse.success(list);
    }

    @GetMapping("findByOwner/encrypted")
    public BaseResponse<List<NFT>> findByOwnerEncrypted(@RequestParam("owner") String owner){
        List<NFT> list = nftService.findByOwnerIsDecrypted(owner, false);
        return BaseResponse.success(list);
    }

    @GetMapping("findByOwner/decrypted")
    public BaseResponse<List<NFT>> findByOwnerDecrypted(@RequestParam("owner") String owner){
        List<NFT> list = nftService.findByOwnerIsDecrypted(owner, true);
        return BaseResponse.success(list);
    }

    @GetMapping("findByTokenId/{tokenId}")
    public BaseResponse<NFT> findByTokenId(@PathVariable int tokenId){
        NFT nft = nftService.findByTokenId(tokenId);
        return BaseResponse.success(nft);
    }
}

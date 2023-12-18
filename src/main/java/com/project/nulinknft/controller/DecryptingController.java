package com.project.nulinknft.controller;

import com.project.nulinknft.dto.BaseResponse;
import com.project.nulinknft.dto.DecryptingDTO;
import com.project.nulinknft.entity.Decrypting;
import com.project.nulinknft.service.DecryptingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Decrypting")
@RestController
@RequestMapping("decrypting")
public class DecryptingController {

    private final DecryptingService decryptingService;

    public DecryptingController(DecryptingService decryptingService) {
        this.decryptingService = decryptingService;
    }

    @GetMapping("{tokenId}")
    @ApiOperation("find decrypting by tokenId")
    public BaseResponse<Decrypting> findByTokenId(@PathVariable int tokenId){
        return BaseResponse.success(decryptingService.findByTokenId(tokenId));
    }

    @PostMapping
    @ApiOperation("create decrypting")
    public BaseResponse create(@RequestBody DecryptingDTO decrypting){
        decryptingService.create(decrypting);
        return BaseResponse.success(null);
    }
}

package com.project.nulinknft.controller;

import com.project.nulinknft.dto.BaseResponse;
import com.project.nulinknft.dto.ReferDTO;
import com.project.nulinknft.service.BlindBoxService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "blindBox")
@RestController
@RequestMapping("blindBox")
public class BlindBoxController {

    private final BlindBoxService blindBoxService;

    public BlindBoxController(BlindBoxService blindBoxService) {
        this.blindBoxService = blindBoxService;
    }

    @GetMapping("referPage")
    public BaseResponse<Page<ReferDTO>> findReferPage(@RequestParam(value = "time", required = false) String time,
                                                      @RequestParam(value = "to", required = false) String to,
                                                      @RequestParam(value = "referralLevel", required = false) String referralLevel,
                                                      @RequestParam(value = "referralAddress") String referralAddress,
                                                      @RequestParam(value = "recommendedAddress", required = false) String recommendedAddress,
                                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size){
        return BaseResponse.success(blindBoxService.findReferDTOPage(time, to, referralLevel, referralAddress, recommendedAddress, page, size));
    }

    @GetMapping("referCount")
    public BaseResponse<Long> findReferCount(@RequestParam(value = "referralLevel", required = false) String referralLevel,
                             @RequestParam(value = "referralAddress") String referralAddress){
        return BaseResponse.success(blindBoxService.findReferralCount(null, null, referralLevel, referralAddress, null));
    }

    @GetMapping("myReferrersCount")
    public BaseResponse<Long> findMyReferrersCount(@RequestParam(value = "userAddress") String userAddress){
        return BaseResponse.success(blindBoxService.getMyReferrersCount(userAddress));
    }
}

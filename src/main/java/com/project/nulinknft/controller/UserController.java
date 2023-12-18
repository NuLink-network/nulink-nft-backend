package com.project.nulinknft.controller;

import com.project.nulinknft.dto.BaseResponse;
import com.project.nulinknft.dto.UserDTO;
import com.project.nulinknft.entity.User;
import com.project.nulinknft.exception.EntityNotFoundException;
import com.project.nulinknft.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "user")
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("findByAddress")
    public BaseResponse<UserDTO> findByAddress(@RequestParam("address") String address){
        User user = userService.findByAddress(address);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return BaseResponse.success(userDTO);
    }

    @PostMapping("update")
    public BaseResponse update(@Valid @RequestBody UserDTO userDTO){
        User user = userService.findById(userDTO.getId());
        if (user == null) {
            throw new EntityNotFoundException(User.class, "id", String.valueOf(userDTO.getId()));
        }
        BeanUtils.copyProperties(userDTO, user);
        userService.save(user);
        return BaseResponse.success(null);
    }

    @PostMapping("create")
    public BaseResponse create(@Valid @RequestBody UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userService.save(user);
        return BaseResponse.success(null);
    }
}

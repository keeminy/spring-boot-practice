package com.example.demo.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.codes.SuccessCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.entity.User;
import com.example.demo.example.jwt.AuthConstants;
import com.example.demo.example.jwt.TokenUtils;

@RestController
@RequestMapping("api/v1/test")
public class TestController {

    @PostMapping("/generateToken")
    public ResponseEntity<ApiResponse> selectCodeList(@RequestBody User user) {

        String resultToken = TokenUtils.generateJwtToken(user);

        ApiResponse ar = ApiResponse.builder().result(AuthConstants.TOKEN_TYPE + " " + resultToken)
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();

        return new ResponseEntity<>(ar, HttpStatus.OK);
    }
}

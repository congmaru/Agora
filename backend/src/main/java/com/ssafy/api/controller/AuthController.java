package com.ssafy.api.controller;

import com.ssafy.api.request.UserReissuePostReq;
import com.ssafy.api.response.UserAuthPostRes;
import com.ssafy.common.auth.RefreshToken;
import com.ssafy.common.util.RedisRepository;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.service.UserService;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepositorySupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import java.util.Optional;

/**
 * 인증 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "인증 API", tags = {"Auth."})
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private RedisRepository redisRepository;

	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes = "<strong>아이디와 패스워드</strong>를 통해 로그인 한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공", response = UserAuthPostRes.class),
        @ApiResponse(code = 401, message = "인증 실패", response = UserAuthPostRes.class),
        @ApiResponse(code = 404, message = "사용자 없음", response = UserAuthPostRes.class),
        @ApiResponse(code = 500, message = "서버 오류", response = UserAuthPostRes.class)
    })
	public ResponseEntity<UserAuthPostRes> login(@RequestBody @ApiParam(value="로그인 정보", required = true) UserLoginPostReq loginInfo) {
		String userId = loginInfo.getId();
		String password = loginInfo.getPassword();
		
		User user = userService.getUserByUserId(userId);
		// 로그인 시도하려는 회원이 존재하지 않는 경우
		if(user == null) {
			return ResponseEntity.status(404).body(new UserAuthPostRes().of(404, "존재하지 않는 회원입니다.", null));
		}
		// 로그인 요청한 유저로부터 입력된 패스워드 와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
		if(passwordEncoder.matches(password, user.getPassword())) {
			// 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
			return ResponseEntity.ok(new UserAuthPostRes().of(200, "Success", JwtTokenUtil.generateToken(userId)));
		}
		// 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
		return ResponseEntity.status(401).body(new UserAuthPostRes().of(401, "Invalid Password", null));
	}

	@PostMapping("/reissue")
	@ApiOperation(value = "로그인", notes = "<strong>아이디와 패스워드</strong>를 통해 로그인 한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = UserAuthPostRes.class),
			@ApiResponse(code = 401, message = "인증 실패", response = UserAuthPostRes.class),
			@ApiResponse(code = 404, message = "사용자 없음", response = UserAuthPostRes.class),
			@ApiResponse(code = 500, message = "서버 오류", response = UserAuthPostRes.class)
	})
	public ResponseEntity<?> reissue(@RequestBody UserReissuePostReq userReissuePostReq){
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		if(jwtTokenUtil.validateToken(userReissuePostReq.getRefreshToken())){
			return ResponseEntity.ok(UserAuthPostRes.of(200, "Refresh Token 정보가 유효하지 않습니다.",null));
		}
		Authentication authentication = jwtTokenUtil.getAuthentication(userReissuePostReq.getAccessToken());
		RefreshToken refreshToken = redisRepository.findById(authentication.getName()).get();
		if(!refreshToken.getRefreshToken().equals(userReissuePostReq.getRefreshToken())){
			return ResponseEntity.ok(UserAuthPostRes.of(200, "RefreshToken 정보가 잘못되었습니다..",null));
		}
		return ResponseEntity.ok(UserAuthPostRes.of(200, "Token 정보가 갱신되었습니다.", JwtTokenUtil.generateToken(authentication.getName())));

	}

}

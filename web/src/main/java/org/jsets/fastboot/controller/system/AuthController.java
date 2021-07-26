package org.jsets.fastboot.controller.system;

import org.jsets.fastboot.frame.controller.BaseController;
import org.jsets.fastboot.frame.model.DataResp;
import org.jsets.fastboot.model.entity.system.User;
import org.jsets.fastboot.model.vo.LoginVO;
import org.jsets.fastboot.security.SecurityUtils;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.auth.UsernamePasswordRequest;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/auths")
@Api(tags = "权限相关信息")
public class AuthController extends BaseController{

	/**
	 * 模拟登陆获取Token
	 */
	@PostMapping(value="/mock_login" ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("模拟用户登陆，获取Token")
	public DataResp<String> mockLogin(@RequestBody LoginVO entity) {
		UsernamePasswordRequest authRequest = new UsernamePasswordRequest();
		authRequest.setUsername(entity.getUsername());
		authRequest.setPassword(entity.getPassword());
		try {
			AuthenticationInfo info = SecurityUtils.login(authRequest);
			return this.dataResp(info.getToken().getAccessToken());
		} catch (UnauthorizedException e) {
			return this.dataRespFail(e.getMessage());
		}
	}
	
	/**
	 * 当前登录的用户
	 * @throws UnauthorizedException 
	 */
	@GetMapping("/current")
	@ApiOperation("获取登录用户信息")
	public DataResp<User> currentUser() throws UnauthorizedException {
		User user = SecurityUtils.getAccount();
		user.setPassword(null);
		return this.dataResp(user);
	}
	
}
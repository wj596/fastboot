package org.jsets.fastboot.controller.system;

import org.jsets.fastboot.frame.controller.BaseController;
import org.jsets.fastboot.frame.model.DataResp;
import org.jsets.fastboot.model.entity.system.User;
import org.jsets.fastboot.security.annotation.HasRole;
import org.jsets.fastboot.service.system.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
@Api(tags = "用户")
public class UserController extends BaseController{

    @Autowired
    private IUserService service;

    
	@ApiOperation("根据ID获取用户信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="主键", required=true ,paramType="path")
	})
	@GetMapping(value="/{id}" ,produces = MediaType.APPLICATION_JSON_VALUE)
	@HasRole("admin")
	public DataResp<User> get(@PathVariable("id") String id) {
		return this.dataResp(this.service.findById(id).get());
	}
}
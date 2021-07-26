package org.jsets.fastboot.model.entity.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jsets.fastboot.frame.model.BaseEntity;
import org.jsets.fastboot.model.enums.system.Sex;
import org.jsets.fastboot.model.enums.system.UserStatus;
import org.jsets.fastboot.security.model.IAccount;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(description = "用户")
public class User extends BaseEntity implements IAccount {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="账号")
	private String account; // 账号
	
	@ApiModelProperty(value="密码")
	private String password;// 密码
	
	@ApiModelProperty(value="姓名")
	private String name; // 姓名
	
	@ApiModelProperty(value="组织机构ID")
	private String orgId; // 组织机构ID
	
	@ApiModelProperty(value="用户状态")
	private UserStatus status;// 用户状态
	
	@ApiModelProperty(value="邮箱")
	private String email; // 邮箱
	
	@ApiModelProperty(value="手机号码")
	private String mobile;// 手机号码
	
	@ApiModelProperty(value="性别")
	private Sex sex;// 性别
	
	@ApiModelProperty(value="头像")
	private String avatar;// 头像

}

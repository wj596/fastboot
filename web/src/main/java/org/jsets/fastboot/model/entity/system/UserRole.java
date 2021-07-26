package org.jsets.fastboot.model.entity.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import org.jsets.fastboot.frame.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("sys_user_role")
@NoArgsConstructor
@ApiModel(description = "用户角色关联")
public class UserRole extends SuperEntity{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "用户主键不能为空")
	@ApiModelProperty(value = "用户主键")
	private Long userId;
	
	@NotNull(message = "角色主键不能为空")
	@ApiModelProperty(value = "角色主键")
	private Long roleId;

}
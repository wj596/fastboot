package org.jsets.fastboot.model.entity.system;

import javax.validation.constraints.NotNull;
import org.jsets.fastboot.frame.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("sys_role_resource")
@NoArgsConstructor
@ApiModel(description = "用户角色关联")
public class RoleResource extends SuperEntity{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "角色主键不能为空")
	@ApiModelProperty(value = "角色主键")
	private Long roleId;
	
	@NotNull(message = "资源主键不能为空")
	@ApiModelProperty(value = "资源主键")
	private Long resourceId;
}

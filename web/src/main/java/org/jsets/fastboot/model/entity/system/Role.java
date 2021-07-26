package org.jsets.fastboot.model.entity.system;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.jsets.fastboot.frame.model.BaseEntity;
import org.jsets.fastboot.model.enums.CommonStatus;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value = "Role", description = "用户")
public class Role extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "角色编码不能为空")
	@Size(max = 50, message = "角色编码长度不能超过50")
	@ApiModelProperty(value = "角色编码", required = true)
    private String code;
	
	@NotBlank(message = "角色名称不能为空")
	@Size(max = 50, message = "角色名称长度不能超过50")
	@ApiModelProperty(value = "角色名称", required = true)
    private String name;
	
	@NotBlank(message = "角色状态不能为空")
	@ApiModelProperty(value = "角色状态", required = true)
    private CommonStatus status;
	
	@ApiModelProperty(value = "序号", required = true)
    private Integer sequenceNumber;

}

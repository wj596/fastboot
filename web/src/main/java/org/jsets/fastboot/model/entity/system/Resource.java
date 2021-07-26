package org.jsets.fastboot.model.entity.system;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.jsets.fastboot.frame.model.BaseEntity;
import org.jsets.fastboot.model.enums.CommonStatus;
import org.jsets.fastboot.model.enums.system.ResourceType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
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
@TableName("sys_resource")
@ApiModel(value = "Resource", description = "资源")
public class Resource extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "资源编码不能为空")
	@Size(max = 50, message = "资源编码长度不能超过50")
	@ApiModelProperty(value = "资源编码", required = true)
	private String code; // 资源编码
	
	@NotBlank(message = "资源名称不能为空")
	@Size(max = 50, message = "资源名称长度不能超过50")
	@ApiModelProperty(value = "资源名称", required = true)
	private String title;// 资源名称
	
	@NotNull(message = "资源类型不能为空")
	@ApiModelProperty(value = "资源类型", required = true)
	private ResourceType type;
	
	@ApiModelProperty(value = "HTTP访问方法")
	private String httpMethod;
	
	@ApiModelProperty(value = "访问地址")
	private String path;// 访问地址
	
	@ApiModelProperty(value = "上级资源")
	private Long parentId;// 上级资源
	
	@ApiModelProperty(value = "图标")
	private String icon;// 图标
	
	@ApiModelProperty(value = "状态")
	private CommonStatus status;
	
	@ApiModelProperty(value = "序号")
	private Integer sequenceNumber;
	
	@ApiModelProperty(value = "资源描述")
	private String remark;

	@TableField(exist = false)
	@ApiModelProperty(value = "子节点")
	private List<Resource> children = Lists.newLinkedList();
	
}

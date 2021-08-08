package org.jsets.fastboot.generator.model;

import java.util.List;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class EntityInfo {
	
    private String name;
    private List<FieldInfo> fieldInfos = Lists.newLinkedList();
}

package org.jsets.fastboot.generator.model;

import java.io.Serializable;
import java.util.List;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class TableInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String name;
    private String desc;
    
}

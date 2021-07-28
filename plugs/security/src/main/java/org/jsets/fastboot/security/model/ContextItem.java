package org.jsets.fastboot.security.model;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class ContextItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String token;
	private String username;
	private String sessionId;
	
	public static ContextItem build() {
		return new ContextItem();
	}

}

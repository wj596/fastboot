package org.jsets.fastboot.common;
import org.jsets.fastboot.common.util.StringUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilsTest {

	@Test
	public void assertEmptyAndBlank() {
		assertTrue(StringUtils.isBlank("  "));
		assertFalse(StringUtils.isEmpty("  "));
	}

}

package org.jsets.fastboot.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.jsets.fastboot.generator.util.DatabaseMetaDataUtils;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.service.system.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataBaseUtilsTest {
	
	@Autowired
	private DataSource dataSource;

	//@Test
	public void getTableInfoList() {
		try {
			DatabaseMetaDataUtils.getTableInfoList(dataSource.getConnection());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getTableColumnInfoList() {
		try {
			DatabaseMetaDataUtils.getTableColumnInfoList(dataSource.getConnection(),"sys_resource");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}

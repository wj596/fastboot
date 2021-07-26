package org.jsets.fastboot.frame.handler;

import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.jsets.fastboot.security.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

public class BaseEntityMetaObjectHandler implements MetaObjectHandler {

    private static String CREATE_TIME = "createTime";
    private static String UPDATE_TIME = "updateTime";
    private static String CREATE_USER = "createUser";
    private static String UPDATE_USER = "updateUser";

	@Override
	public void insertFill(MetaObject metaObject) {
		Date createTime = new Date();
		if(metaObject.hasSetter(CREATE_TIME)) {
            setFieldValByName(CREATE_TIME, createTime, metaObject);
        }
		if(metaObject.hasSetter(UPDATE_TIME)) {
            setFieldValByName(UPDATE_TIME, createTime, metaObject);
        }
		
		if(SecurityUtils.hasSession()) {
			String user = SecurityUtils.getSession().get().getUsername();
			if(metaObject.hasSetter(CREATE_USER)) {
	            setFieldValByName(CREATE_USER, user, metaObject);
	        }
			if(metaObject.hasSetter(UPDATE_USER)) {
	            setFieldValByName(UPDATE_USER, user, metaObject);
	        }
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		if(metaObject.hasSetter(UPDATE_TIME)) {
            setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        }
		if(metaObject.hasSetter(UPDATE_USER) && SecurityUtils.hasSession()) {
            setFieldValByName(UPDATE_USER, SecurityUtils.getSession().get().getUsername(), metaObject);
        }
	}

}
package org.jsets.fastboot.multipleds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * 多样可切换数据源
 * 
 * @author wj596
 *
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
   
	@Override
    protected Object determineCurrentLookupKey() {
        return MultipleDataSourceContext.getDataSourceName();
    }
	
}
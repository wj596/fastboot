package org.jsets.fastboot.generator.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.generator.model.ColumnInfo;
import org.jsets.fastboot.generator.model.TableInfo;
import com.google.common.collect.Lists;

public class DatabaseMetaDataUtils {

	/**
	 * java类型转MySQL类型
	 * @param jtype
	 * @return
	 */
	public static String javaTypeToMysqlType(String jtype) {
		String result = "String";
		switch (jtype) {
		case "String":
			result = "varchar";
			break;
		case "Date":
			result = "datetime";
			break;
		case "Integer":
			result = "int";
			break;
		case "Long":
			result = "bigint";
			break;
		case "Float":
			result = "float";
			break;
		case "Double":
			result = "double";
			break;
		case "Boolean":
			result = "tinyint";
			break;
		case "BigDecimal":
			result = "decimal";
			break;
		case "byte[]":
			result = "blob";
			break;
		}
		return result;
	}
	
	public static String databaseTypeToJavaType(String columnType) {
		if ("varchar".equals(columnType) 
				|| "varchar2".equals(columnType)
				|| "nvarchar".equals(columnType)
				|| "char".equals(columnType) 
				|| "text".equals(columnType) 
				|| "mediumtext".equals(columnType)) {
			
			return "String";
			
		} else if ("tinyblob".equals(columnType) 
				|| "blob".equals(columnType) 
				|| "mediumblob".equals(columnType) 
				|| "longblob".equals(columnType)) {
			
			return "byte[]";
			
		} else if ("datetime".equals(columnType) 
				|| "date".equals(columnType) 
				|| "timestamp".equals(columnType) 
				|| "time".equals(columnType) 
				|| "year".equals(columnType)) {
			
			return "Date";
			
		} else if ("bit".equals(columnType) 
				|| "int".equals(columnType) 
				|| "tinyint".equals(columnType) 
				|| "smallint".equals(columnType)) {
			
			return "Integer";
      
		} else if ("int unsigned".equals(columnType) 
				|| "tinyint unsigned".equals(columnType)) {
			
			return "Integer";
			
		} else if ("bigint unsigned".equals(columnType) 
				|| "bigint".equals(columnType)) {
			
			return "Long";
			
		} else if ("float".equals(columnType) 
				|| "float unsigned".equals(columnType)) {
			
			return "Float";
			
		} else if ("double".equals(columnType) 
				|| "double unsigned".equals(columnType)) {
			
			return "Double";
			
		} else if ("decimal".equals(columnType) 
				|| "decimal unsigned".equals(columnType)) {
			
			return "BigDecimal";
		}
		
		return "String";
	}

	public static List<TableInfo> getTableInfoList(final Connection connection) {
		List<TableInfo> tableInfos = Lists.newLinkedList();
		try {
			DatabaseMetaData dmd = connection.getMetaData();
			ResultSet set = dmd.getTables(connection.getCatalog(), "%", "%", new String[] { "TABLE" });
			while (set.next()) {
				String tableName = set.getString("TABLE_NAME");
				String tableDesc = set.getString("REMARKS");
				TableInfo tableInfo = new TableInfo();
				tableInfo.setName(StringUtils.toLowerCase(tableName));
				tableInfo.setDesc(tableDesc);
				tableInfos.add(tableInfo);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return tableInfos;
	}

	public static List<ColumnInfo> getTableColumnInfoList(final Connection connection, final String tableName) {
		try {
			DatabaseMetaData dmd = connection.getMetaData();
			List<String> primaryKeys = Lists.newLinkedList();
			ResultSet pkSet = dmd.getPrimaryKeys(null, null, tableName);
			while (pkSet.next()) {
				primaryKeys.add(StringUtils.toLowerCase(pkSet.getString("COLUMN_NAME")));
			}

			List<ColumnInfo> columnInfos = Lists.newLinkedList();
			ResultSet columnSet = dmd.getColumns(connection.getCatalog(), "%", tableName, "%");
			while (columnSet.next()) {
				String columnName = columnSet.getString("COLUMN_NAME");
				String typeName = columnSet.getString("TYPE_NAME");
				String remarks = columnSet.getString("REMARKS");
				Integer columnSize = columnSet.getInt("COLUMN_SIZE");
				Integer decimalDigits = columnSet.getInt("DECIMAL_DIGITS");
				Integer nullable = columnSet.getInt("NULLABLE");
				ColumnInfo columnInfo = new ColumnInfo();
				columnInfo.setName(StringUtils.toLowerCase(columnName));
				columnInfo.setType(StringUtils.toLowerCase(typeName));
				columnInfo.setComment(remarks);
				columnInfo.setLength(columnSize);
				columnInfo.setRadix(decimalDigits);
				columnInfo.setNullable(nullable == 1);
				columnInfo.setPrimary(primaryKeys.contains(columnInfo.getName()));
				columnInfos.add(columnInfo);
			}
			return columnInfos;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}

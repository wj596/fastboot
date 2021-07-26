package org.jsets.fastboot.generator.util;

public class DataBaseUtils {

	
    public static List<TableInfo> getTableInfoList(final Connection connection) {
        List<TableMeta> tableMetas = Lists.newLinkedList();
        try (Connection connection = DbUtils.getConnection(ds)) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = "".equals(ds.getCatalog()) ? null : ds.getCatalog();
            ResultSet tableRet = metaData.getTables(catalog, "%", "%", new String[]{"TABLE"});
            while (tableRet.next()) {
                TableMeta tableMeta = new TableMeta();
                String tableName = tableRet.getString("TABLE_NAME");// 表明
                String tableDesc = tableRet.getString("REMARKS");// 表注释
                tableMeta.setId(UUID.randomUUID().toString());
                tableMeta.setModule(request.getModule());
                tableMeta.setTableName(tableName);
                tableMeta.setTableDesc(tableDesc);
                tableMeta.setBeanName(tableNameToBeanName(tableName, request.getTablePrefix()));
                String projectName = request.getProjectName();
                String moduleName = StringTools.isEmpty(request.getModule())?"":"."+request.getModule();
                String entityPackage = String.format("net.jqsoft.%s.entity%s", projectName, moduleName);
                String mapperPackage = String.format("net.jqsoft.%s.mapper%s", projectName, moduleName);
                String servicePackage = String.format("net.jqsoft.%s.service%s", projectName, moduleName);
                String serviceImplPackage = String.format("net.jqsoft.%s.service%s.impl", projectName, moduleName);
                String controllerPackage = String.format("net.jqsoft.%s.controller%s", projectName, moduleName);
                tableMeta.setEntityPackage(entityPackage);
                tableMeta.setMapperPackage(mapperPackage);
                tableMeta.setServicePackage(servicePackage);
                tableMeta.setServiceImplPackage(serviceImplPackage);
                tableMeta.setControllerPackage(controllerPackage);
                tableMetas.add(tableMeta);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取表元数据失败:" + e.getMessage());
        }

        return tableMetas;
    }
	
	
	
}

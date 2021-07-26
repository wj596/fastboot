package org.jsets.fastboot.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

/**
 * Json工具类
 *
 * @Author wangjie
 * @date 2021.07.05 23:27
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        // 忽略在json字符串中存在，但是在java对象中不存在对应属性的情况，防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许有注释
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        //只包含非空字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.FORMATER_DAY_TIME_SECOND));
    }

    /**
     * json转对象
     *
     * @param json Json字符串
     * @param requestType 预期类型
     * @return <T> T
     */
    public static <T> T parse(String json, Class<T> requestType) {
        if (StringUtils.isBlank(json)) {
            throw new IllegalArgumentException("json字符串不能为空");
        }

        if (Objects.isNull(requestType)) {
            throw new IllegalArgumentException("requestType不能为空");
        }
        
        try {
            return objectMapper.readValue(json, requestType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析json错误", e);
        }
    }

    public static <T> List<T> parseList(String json, Class<T> requestType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, requestType);

        try {
            List<T> list  = objectMapper.readValue(json, javaType);
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析json错误", e);
        }
    }

    /**
     * 转JSON字符串
     *
     * @Param object 对象
     * @return java.lang.String
     **/
    public static String toJsonString(Object object){
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("object对象不能为空");
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化json字符串错误", e);
        }
    }

    /**
     * 对象转缩进排版的json
     *
     * @param object 对象
     * @return String
     */
    public static String toIndentJsonString(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("object对象不能为空");
        }

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化json字符串错误", e);
        }
    }

}

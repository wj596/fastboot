package org.jsets.fastboot.frame.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.annotation.Transactional;
import org.jsets.fastboot.frame.service.IBaseService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Service基类
 * 
 * @param <T> 实体
 * 
 * <br>
 * IBaseService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ）
 * <br>
 * copy from @see com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
public class BaseService<T, M extends BaseMapper<T>> implements IBaseService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    protected M baseMapper;

    @Override
    public M getBaseMapper() {
		return this.baseMapper;
	}

    protected Class<T> entityClass = currentModelClass();

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    protected Class<M> mapperClass = currentMapperClass();

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
    	return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }
    
    @SuppressWarnings("unchecked")
	protected Class<M> currentMapperClass() {
    	return (Class<M>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    /**
     * @see ResolvableType
     * @since 3.4.3
     */
    protected ResolvableType getResolvableType() {
        return ResolvableType.forClass(ClassUtils.getUserClass(getClass()));
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    /**
     * 获取mapperStatementId
     *
     * @param sqlMethod 方法名
     * @return 命名id
     * @since 3.4.0
     */
    protected String getSqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.getSqlStatement(mapperClass, sqlMethod);
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(T entity) {
        if (null != entity) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
            Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
            return StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal)) ? save(entity) : updateById(entity);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        return SqlHelper.saveOrUpdateBatch(this.entityClass, this.mapperClass, IbatisLog.INSTANCE, entityList, batchSize, (sqlSession, entity) -> {
            Object idVal = ReflectionKit.getFieldValue(entity, keyProperty);
            return StringUtils.checkValNull(idVal)
                || CollectionUtils.isEmpty(sqlSession.selectList(getSqlStatement(SqlMethod.SELECT_BY_ID), entity));
        }, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(sqlStatement, param);
        });
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return baseMapper.selectOne(queryWrapper);
        }
        return SqlHelper.getObject(IbatisLog.INSTANCE, baseMapper.selectList(queryWrapper));
    }

    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return SqlHelper.getObject(IbatisLog.INSTANCE, baseMapper.selectMaps(queryWrapper));
    }

    @Override
    public <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return SqlHelper.getObject(IbatisLog.INSTANCE, listObjs(queryWrapper, mapper));
    }

    /**
     * 执行批量操作
     *
     * @param list      数据集合
     * @param batchSize 批量大小
     * @param consumer  执行方法
     * @param <E>       泛型
     * @return 操作结果
     * @since 3.3.1
     */
    protected <E> boolean executeBatch(Collection<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
        return SqlHelper.executeBatch(this.entityClass, IbatisLog.INSTANCE, list, batchSize, consumer);
    }

    /**
     * 执行批量操作（默认批次提交数量{@link IService#DEFAULT_BATCH_SIZE}）
     *
     * @param list     数据集合
     * @param consumer 执行方法
     * @param <E>      泛型
     * @return 操作结果
     * @since 3.3.1
     */
    protected <E> boolean executeBatch(Collection<E> list, BiConsumer<SqlSession, E> consumer) {
        return executeBatch(list, DEFAULT_BATCH_SIZE, consumer);
    }
    
    @Override
	public Optional<T> findById(Serializable id) {
		return Optional.ofNullable(this.baseMapper.selectById(id));
	}

	@Override
	public Optional<T> findOne(Wrapper<T> queryWrapper) {
		List<T> ls = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(ls)) {
            int size = ls.size();
            if (size > 1) {
            	LOGGER.warn(String.format("Warn: execute Method There are  %s results.", size));
            }
            return Optional.of(ls.get(0));
        }
        return Optional.empty();
	}

	public static class IbatisLog implements org.apache.ibatis.logging.Log {

    	private static IbatisLog INSTANCE = new IbatisLog();

    	private IbatisLog() {
    	}

    	public static IbatisLog getInstance(){ 
    		return INSTANCE; 
    	}

    	@Override
    	public boolean isDebugEnabled() {
    		return false;
    	}

    	@Override
    	public boolean isTraceEnabled() {
    		return false;
    	}

    	@Override
    	public void error(String s, Throwable e) {
    		LOGGER.error(s, e);
    	}

    	@Override
    	public void error(String s) {
    		LOGGER.error(s);
    	}

    	@Override
    	public void debug(String s) {
    		LOGGER.debug(s);
    	}

    	@Override
    	public void trace(String s) {
    		LOGGER.info(s);
    	}

    	@Override
    	public void warn(String s) {
    		LOGGER.warn(s);
    	}
    }

	@Override
	public LambdaQueryWrapper<T> getLambdaWrapper() {
		return new LambdaQueryWrapper<T>();
	}

	@Override
	public UpdateWrapper<T> getUpdateWrapper() {
		return new UpdateWrapper<T>();
	}

	@Override
	public QueryWrapper<T> getQueryWrapper() {
		return new QueryWrapper<T>();
	}
	
}

package com.xiaochu.chapter1.helper;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.xiaochu.chapter1.util.PropertyUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by DongChao on 2016/6/26.
 */
public final class DataBaseHelper {
    /**
     * 将Connection作为本地线程变量，这样Connection就不会出现线程安全问题。
    */
    public static final ThreadLocal<Connection> CONNECTION_HOLDER ;
    public static final ComboPooledDataSource DATA_SOURCE ;
    public static final Logger LOGGER = LoggerFactory.getLogger(DataBaseHelper.class);
    public static final QueryRunner QUERY_RUNNER ;
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();

        Properties conf = PropertyUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

      /*
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can't load jdbc driver!", e);
        }*/

        DATA_SOURCE = new ComboPooledDataSource();
        try {
            DATA_SOURCE.setDriverClass(DRIVER);
            DATA_SOURCE.setJdbcUrl(URL);
            DATA_SOURCE.setUser(USERNAME);
            DATA_SOURCE.setPassword(PASSWORD);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据库连接
     *
     * @return 一个数据库连接
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        try {
            connection = DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            LOGGER.error("get connection failure!", e);
        }
        return connection;
    }
    /**
     * 查询实体列表
     *
     * @param entityClass 实体class
     * @param sql 查询sql
     * @param params 可变参数
     * @param <T> 泛型
     * @return list集合
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = new ArrayList<T>();
        Connection connection = getConnection();
        try {
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass));
        } catch (SQLException sqle) {
            LOGGER.error("query " + entityClass.getName() + "List failure!", sqle);
        }
        return entityList;
    }

    /**
     * @param entityClass 要查询的实体的class
     * @param sql 查询sql
     * @param params 参数
     * @param <T> 泛型
     * @return 还回一个T类型的实体对象
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity = null;
        Connection connection = getConnection();
        try {
            entity = QUERY_RUNNER.query(connection, sql, new BeanHandler<T>(entityClass),params);
        } catch (SQLException sqle) {
            LOGGER.error("query " + entityClass.getName() + " is failure!", sqle);
        }
        return entity;
    }

    /**
     * 多表查询时将数据封装为Map的集合
     * @param sql 查询sql
     * @param params 可变个数参数
     * @return 一个list的Map集合
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Connection connection = null;
        try {
            connection = getConnection();
            result = QUERY_RUNNER.query(connection, sql, new MapListHandler(), params);
        } catch (Exception e) {
            LOGGER.error("query map list failure!", e);
        }
        return result;
    }

    /**
     * 更新数据,可用于更新，插入，删除操作
     * @param sql sql语句
     * @param params 参数
     * @return 还回更新成功的记录数
     */
    public static int executeUpdate(String sql, Object... params){
        int rows = 0 ;
        Connection connection ;
        try {
            connection = getConnection();
            rows = QUERY_RUNNER.update(connection,sql,params);
        }catch (Exception e){
            LOGGER.error("update is failure!",e);
        }
        return rows ;
    }

    /**
     * 插入实体
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        String sql = "INSERT INTO "+getTableName(entityClass);
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(",");
            values.append("?,");
        }
        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(columns.lastIndexOf(","),columns.length(),")");
        sql += columns+" VALUES " + values ;

        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql,params)==1;
    }

    /**
     * 通过id更新实体
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        String sql = "UPDATE "+getTableName(entityClass)+" SET ";
        StringBuffer columns = new StringBuffer() ;
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append("=?,");
        }
        sql += columns.replace(columns.lastIndexOf(","),columns.length(),")")+" WHERE id=?";
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(paramList);
        paramList.add(id);
        Object[] params = paramList.toArray();
        return  executeUpdate(sql,params)==1;
    }

    /**
     * 通过id删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = "delete from "+getTableName(entityClass) + " WHERE id = ?" ;
        return executeUpdate(sql,id)==1;
    }

    /**
     *  通过Class获取表的名称，表名为该类名的小写
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName() ;
    }
    public static void executeSqlFile(String filePath){
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String sql ;
        try {
            while ((sql=reader.readLine())!=null){
                DataBaseHelper.executeUpdate(sql);
            }
        } catch (IOException e) {
            LOGGER.error("execute sql file failure!",e);
            throw new RuntimeException(e);
        }
    }

}

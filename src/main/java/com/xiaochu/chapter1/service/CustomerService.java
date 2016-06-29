package com.xiaochu.chapter1.service;

import com.xiaochu.chapter1.helper.DataBaseHelper;
import com.xiaochu.chapter1.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DongChao on 2016/6/24.
 */
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    /**
     * 基于纯JDBC的数据操作
     * 获取客户列表
     * @param keyWord
     * @return
     */
    @Deprecated
    public List<Customer> getCustomerList_jdbc(String keyWord){
        Connection connection = null ;
        List<Customer> customerList = new ArrayList<Customer>();
        try {
         String sql = "select * from customer ";
         connection = DataBaseHelper.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet resultSet = stmt.executeQuery();
         while (resultSet.next()){
             Customer customer = new Customer();
             customer.setId(resultSet.getLong("id"));
             customer.setName(resultSet.getString("name"));
             customer.setContact(resultSet.getString("contact"));
             customer.setTelephone(resultSet.getString("telephone"));
             customer.setEmail(resultSet.getString("email"));
             customer.setRemark(resultSet.getString("remark"));
             customerList.add(customer);
         }
        }catch (Exception e){
            LOGGER.error("execute sql failure!",e);
        }
        return customerList ;
    }

    /**
     * 基于apache dbutils的数据库操作
     * @param keyWord
     * @return
     */
    public List<Customer> getCustomerList(String keyWord){
        String sql = "select * from customer ";
        return  DataBaseHelper.queryEntityList(Customer.class,sql,keyWord) ;
    }

    /**
     * 根据用户id获取客户信息
     * @param id
     * @return
     */
    public Customer getCustomer(long id){
        String sql = "select * from customer where id = ?" ;
        return DataBaseHelper.queryEntity(Customer.class,sql,id);
    }

    /**
     * 根据给定的信息创建一个新的客户
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String,Object> fieldMap){
        return DataBaseHelper.insertEntity(Customer.class,fieldMap);
    }

    /**
     * 更新客户信息
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(long id ,Map<String,Object> fieldMap){
        return DataBaseHelper.updateEntity(Customer.class,id,fieldMap) ;
    }

    /**
     * 删除客户
     * @param id
     * @return
     */
    public boolean deleteCustomer(long id){
        return DataBaseHelper.deleteEntity(Customer.class,id);
    }
}

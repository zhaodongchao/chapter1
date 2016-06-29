package com.xiaochu.chapter1.test;

import com.xiaochu.chapter1.helper.DataBaseHelper;
import com.xiaochu.chapter1.model.Customer;
import com.xiaochu.chapter1.service.CustomerService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * CustomerServiceTest单元测试
 * Created by DongChao on 2016/6/24.
 */
public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest() {
        this.customerService = new CustomerService();
    }
    @Before
    public void init() throws IOException {
     //初始化数据库
     DataBaseHelper.executeSqlFile("init.sql");
    }

    @Test
    public void getCustomerList(){
        List<Customer> list = customerService.getCustomerList("");
        Assert.assertEquals(3,list.size());
    }

    @Test
    public void getCustomer(){
    }

    @Test
    public void createCustomer(){
    }

    @Test
    public void updateCustomer(){
    }

    @Test
    public void deleteCustomer(){
    }
}

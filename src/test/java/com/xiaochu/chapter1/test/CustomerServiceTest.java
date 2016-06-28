package com.xiaochu.chapter1.test;

import com.xiaochu.chapter1.service.CustomerService;
import org.junit.Before;
import org.junit.Test;

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
    public void init(){
     //初始化数据库
    }
    @Test
    public void getCustomerList(String keyWord){
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

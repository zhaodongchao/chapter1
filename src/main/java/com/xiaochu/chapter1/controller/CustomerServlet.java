package com.xiaochu.chapter1.controller;

import com.xiaochu.chapter1.model.Customer;
import com.xiaochu.chapter1.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by DongChao on 2016/6/24.
 */
@WebServlet("/index.jsp")
public class CustomerServlet extends HttpServlet{
    private CustomerService customerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> customerList= customerService.getCustomerList("");
        req.setAttribute("customerList",customerList);
        req.getRequestDispatcher("/WEB-INF/view/customer.jsp").forward(req,resp);
    }

    @Override
    public void init() throws ServletException {
        customerService = new CustomerService();
    }


}

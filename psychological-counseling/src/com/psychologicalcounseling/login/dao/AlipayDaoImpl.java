package com.psychologicalcounseling.login.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.psychologicalcounseling.entity.User;
import com.psychologicalcounseling.util.BaseDao;

@Repository
public class AlipayDaoImpl extends BaseDao{
     public void insertCourseOrderByPrecreate(int courseId,int userId,String orderId4Alipay ,float courseorderPrice) {
    	 String sql="insert into courseorder(courseId,userId,orderId4Alipay,courseorderBuyTime,courseorderPrice) values(?,?,?,?,?)";
    	 int result=insert(sql,courseId,userId,orderId4Alipay,new Date(),courseorderPrice);
    	 if(result==0) {
    		 System.out.println("订单插入失败");
    	 }else {
    		 System.out.println("订单插入成功");
    	 }
     }
     public void insertUser(String alipayUserId) {
    	 String sql="insert into user(userRegistTime,alipayUserId) values(?,?)";
    	 System.out.println("*********");
    	 int result=insert(sql,new Date(),alipayUserId);
    	 if(result==0) {
    		 System.out.println("用户插入失败");
    	 }else {
    		 System.out.println("用户插入成功");
    	 }
     }
     public List isNewUser4Alipay(String alipayUserId) {
    	 String hql=" from User where alipayUserId=?";
         List<User> list=find(hql,alipayUserId);
         
         return list;
     }
     public int findUserId(String alipayUserId) {
    	 String hql=" from User where alipayUserId=?";
    	 List<User> list=find(hql,alipayUserId);
    	 return list.get(0).getUserId();
     }
     
}

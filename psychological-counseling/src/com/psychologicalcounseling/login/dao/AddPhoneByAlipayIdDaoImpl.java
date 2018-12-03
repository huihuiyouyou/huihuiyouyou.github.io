package com.psychologicalcounseling.login.dao;

import org.springframework.stereotype.Repository;

import com.psychologicalcounseling.util.BaseDao;

@Repository
public class AddPhoneByAlipayIdDaoImpl extends BaseDao{
	 public void updatePhone(String userPhone,String alipayUserId) {
		 
		 //这里用的sql语句，用BaseDao里面的insert方法，insert方法同时兼具update的功能。
		 String sql="update user set userPhone=? where alipayUserId=?";
         int result=insert(sql,userPhone,alipayUserId);
         
         if(result==1) {
        	 System.out.println("第三方用户手机号更新成功");
         }else {
        	 System.out.println("第三方用户手机号更新失败");

         }
     }

}

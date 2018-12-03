package com.psychologicalcounseling.login.controller;

import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.aliyuncs.exceptions.ClientException;
import com.google.gson.Gson;
import com.psychologicalcounseling.login.dao.IsNewPhoneDaoImpl;
import com.psychologicalcounseling.login.service.AddPhoneByAlipayIdServiceImpl;
import com.psychologicalcounseling.login.service.RegistServiceImpl;
import com.psychologicalcounseling.login.service.VerifyPwdServiceImpl;
import com.psychologicalcounseling.util.AliyunMessage;
/**
 * 
 *@desc:此控制器控制登录注册的所有功能
 *@author 刘田会
 *@date:2018年11月22日下午9:19:36
 */

@Controller
@RequestMapping("/login")
public class LoginController {
	@Resource
	private IsNewPhoneDaoImpl isps;
	@Resource
	private RegistServiceImpl rsl;
	@Resource
	private VerifyPwdServiceImpl vpsi;
	@Resource
	private AddPhoneByAlipayIdServiceImpl apbaisi;
/**
 * 
 *@desc:根据手机号码判断是否为新用户
 *@param phoneNum
 *@return false-新用户 true-老用户
 *@return:String
 *@trhows
 */

	@RequestMapping("/isNewPhone")
	@ResponseBody
	public String isNewphone(@RequestParam(value="phoneNum",required=false) String phoneNum) {
		
		System.out.println(phoneNum+"**********");
		if(isps.isNewPhoneDaoImpl(phoneNum)) {
			return "{\"result\":\"true\"}";
		}else {
			return "{\"result\":\"false\"}";
		}
	}
/**
 * 
 *@desc:调用阿里云接口发送信息
 *@param phoneNumber 
 *@param session 
 *@return:void 
 *@trhows
 */
	@RequestMapping("/getMessage")
	@ResponseBody
	public String checkphoneNumber(@RequestParam(value="phoneNum",required=false) String phoneNumber,HttpSession session
			) {
		AliyunMessage smsdemo=new AliyunMessage();
		System.out.println("************");
		String code=String.valueOf(new Random().nextInt(899999)+100000);
		JSONObject json=new JSONObject();
		json.put("code", code);
		json.put("createTime", System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());
		//判断超时
		session.setMaxInactiveInterval(3600);
		session.setAttribute("verifyCode", json);
	    try {
			smsdemo.getResult(phoneNumber,code);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return "{\"result\":\"meaningless\"}";
	}
/**
 * 
 *@desc:判断验证码是否正确
 *@param code
 *@param session
 *@return
 *@return:String
 *@trhows
 */
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/verifyCode")
	@ResponseBody
    public String verifyCode(@RequestParam(value="code",required=false) String code,HttpSession session) {
		
		JSONObject json=(JSONObject) session.getAttribute("verifyCode");
		Gson gson=new Gson();
		if(json!=null && !"".equals(json)) {
			if(System.currentTimeMillis()-(long)((JSONObject) session.getAttribute("verifyCode")).get("createTime")>5*60*1000) {
				json.remove("verifyCode");
				return "{\"result\":\"outOfTime\"}";
			}
			if(json.getString("code").equals(code)) {
				return "{\"result\":\"same\"}";
			}else {
				return "{\"result\":\"different\"}";
			}
		}else {
			if(code==null) {
				return "";
			}else {
				return "{\"result\":\"pleaseGetMessage\"}";
			}
		}	
	}
/**
 * 
 *@desc:用户注册/快速登录    如果新用户查数据库，老用户直接跳到首页
 *@param phoneNum  
 *@return
 *@return:String
 *@trhows
 */
	@RequestMapping("/regist")
    public String regist(@RequestParam(value="phoneNum",required=false) String phoneNum) {
		//如果是新用户
		JSONObject json=new JSONObject(this.isNewphone(phoneNum));
		System.out.println(json.getString("result"));
		if(json.getString("result").equals("false")) {
			//result为影响的条数
		   int result=rsl.regist(phoneNum);
		}
		return "index";
	}
/**    ----------------------------------分界线-------------------------------------------下面是登录
 * 
 *@desc: 在用账号密码方式登录时，检查密码是否正确
 *@param phoneNum 根据手机号，判断密码
 *@return
 *@return:String  same--密码正确   |  different--密码错误
 *@trhows
 */
	@RequestMapping("/verifyPwd")
	@ResponseBody
    public String verifyPwd(@RequestParam(value="phoneNum",required=false) String phoneNum,@RequestParam(value="pwd",required=false) String pwd) {
	
	if(phoneNum==null||phoneNum.equals("")) {
		return "{\"result\":\"pleaseGetPhone\"}";

	}else {
			if(vpsi.verifyPwd(phoneNum, pwd)) {
				return "{\"result\":\"same\"}";
			}else {
				return "{\"result\":\"different\"}";
			}
		}
	
	}
	/**
	 * 
	 *@desc:第三方登录时，用来完善用户的手机号。
	 *@param phoneNum 要完善的手机号
	 *@param AlipayUserId 用户的阿里云的Id，根据这个Id，来更新手机号
	 *@return:void
	 *@trhows
	 */
	@RequestMapping("/addPhone")
	@ResponseBody
    public void addPhone(@RequestParam(value="phoneNum",required=false) String phoneNum,
    					@RequestParam(value="alipayUserId",required=false) String alipayUserId) {
		apbaisi.addPhone(phoneNum, "912758505");
	}

}

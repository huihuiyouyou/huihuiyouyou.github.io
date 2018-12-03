<%@page import="com.alipay.api.internal.util.AlipaySignature"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.psychologicalcounseling.util.*"%>
<%@ page import="com.psychologicalcounseling.login.dao.*"%>
<%@ page import="com.psychologicalcounseling.login.service.*"%>
<%@ page import="com.alipay.api.*"%>
<%@ page import="com.alipay.api.response.*"%>
<%@ page import=" javax.annotation.Resource"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="org.json.JSONObject"%>
<%! @Resource private AlipayServiceImpl asi; %>
<%
    
    
	//获取支付宝POST过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
		params.put(name, valueStr);
	}
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
		
// 		//商户Id
// 		String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8");
		
// 		//课程Id
// 		String courseId = new String(request.getParameter("courseId").getBytes("ISO-8859-1"),"UTF-8");
		
		//用户Id
		String json = new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8");
		JSONObject pa=new JSONObject(json);
        String courseId=(String)pa.get("courseId");
        String userId=(String)pa.get("userId");
        
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		//计算得出通知验证结果
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, "RSA2");
		
		if(verify_result){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
			if(trade_status.equals("TRADE_FINISHED")){
				
			} else if (trade_status.equals("TRADE_SUCCESS")){
				AlipayServiceImpl asi=new AlipayServiceImpl();
				AlipayTradeQueryResponse alipayTradeQueryResponse=asi.AlipayTradeQuery(out_trade_no);
				//进行正确性判断。
				if(alipayTradeQueryResponse.getTotalAmount().equals(total_amount)){
					
					asi.insertCourseOrderByPrecreate(Integer.parseInt(courseId),Integer.parseInt(userId),out_trade_no, Float.parseFloat(total_amount));  					
				}
					
				
			}
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			out.clear();
			out.println("success");	//请不要修改或删除

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			out.println("fail");
		}
%>

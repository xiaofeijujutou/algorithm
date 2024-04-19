package com.xiaofei.algorithm.test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlipayBizContent {

	/**
	 * 任务：
	 * 1、main中实现控制台打印，输出的内容是json，并且符合https://opendocs.alipay.com/open/028r8t?scene=common
	 * 该网址指定的biz_content参数要求
	 * 2、genAliPayBiz是给其他程序调用的，排除genAliPayBiz中的所有bug
	 * 3、进行单元测试，保证各种参数传入情况都能正确的发送符合支付宝接口规范的消息
	 */

	private static DecimalFormat alipayAmountDF = new DecimalFormat("#.00");

	private static String genAliPayBiz(String orderNo, String subject,
									   BigDecimal amount) {
		String total_amount = alipayAmountDF.format(amount.doubleValue());

		String rst = "{\"out_trade_no\":\"" + orderNo + "\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\","
				+ "\"total_amount\":" + total_amount + "," + "\"subject\":\""
				+ subject + "\"}";
		return rst;
	}

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.print("输入订单号:");//商家自定义
		String orderNo = scanner.next();
		System.out.print("输入商品:");//不能存在特殊符号
		String subject = scanner.next();
		System.out.print("输入金额:");//两位小数[0.01,100000000]
		String amount = scanner.next();
		String regex = "[/=&]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(subject);
//		if (orderNo.length()){
//			throw
//		}
		if (matcher.find()) {
			throw new RuntimeException("商品存在 /,=,&等特殊符号");
		}
		BigDecimal value = null;
		try {
			value = new BigDecimal(amount);
			BigDecimal lowerBound = new BigDecimal("0.01");
			BigDecimal upperBound = new BigDecimal("100000000");
			if (value.compareTo(lowerBound) >= 0 && value.compareTo(upperBound) <= 0) {
				//在范围里面
			} else {
				throw new RuntimeException("不在指定区间内");
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("金额格式化错误");
		}
		System.out.println(genAliPayBiz(orderNo, subject, value));
	}
}
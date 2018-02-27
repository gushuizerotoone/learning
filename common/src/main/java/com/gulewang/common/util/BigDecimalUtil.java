package com.gulewang.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

  /**
   * 把金额从元到分 2.312 -> 231 2.3 -> 230 20 -> 2000 2 -> 200
   * 
   * @param bd
   * @return
   */
  public static String yuanToCent(BigDecimal bd) {
    bd = bd.setScale(2, RoundingMode.DOWN);
    bd = bd.movePointRight(2);
    return bd.toPlainString();
  }

  /**
   * 把金额从分到元 231 -> 2.31 230 -> 2.30 2000 -> 20.00 200 -> 2.00
   * 
   * @param amt
   * @return
   */
  public static BigDecimal centToYuan(String amt) {
    BigDecimal bd = new BigDecimal(amt);
    bd = bd.setScale(0, RoundingMode.DOWN);
    bd = bd.movePointLeft(2);
    return bd;
  }


  /**
   * 把金额设为了两位小数
   * 
   * 2.3 -> 2.30 20 -> 20.00 2.0 -> 2.00 20. -> 20.00 -2.5 -> -2.50
   * 
   * @param bd
   * @return
   */
  public static String getTwoBitCent(BigDecimal bd) {
    bd = bd.setScale(2, RoundingMode.FLOOR);
    return bd.toPlainString();
  }

  /**
   * 把金额设为了两位小数
   * 
   * 2.3 -> 2.30 20 -> 20.00 2.0 -> 2.00 20. -> 20.00 -2.5 -> -2.50
   * 
   * @param bd
   * @return
   */
  public static Double getTwoBitCentDouble(BigDecimal bd) {
    bd = bd.setScale(2);
    return bd.doubleValue();
  }

  /**
   * 把金额设为了两位小数
   * 
   * 2.3 -> 2.30 20 -> 20.00 2.0 -> 2.00 20. -> 20.00 -2.5 -> -2.50
   * 
   * @param amountStr
   * @return
   */
  public static Double getTwoBitCentDouble(String amountStr) {
    BigDecimal bd = strToTwoBitBigDecimal(amountStr);
    return getTwoBitCentDouble(bd);
  }

  /**
   * 把字符串的金额转为bigDecimal 0 -> 0.00 20.1 -> 20.10 20.12 -> 20.12 20.123 -> IllegalArgumentException
   * 
   * @param amountStr
   * @return
   */
  public static BigDecimal strToTwoBitBigDecimal(String amountStr) {
    BigDecimal bd = new BigDecimal(amountStr);
    return bd.setScale(2, RoundingMode.HALF_EVEN);
  }

  /**
   * 获取两位小数的BigDecimal 1.2299999 -> 1.23 1.23 -> 1.23 1.2 -> 1.20 1 -> 1.00
   * 
   * @param amount
   * @return
   */
  public static BigDecimal doubleToTwoBitBigDecimal(double amount) {
    BigDecimal ret = new BigDecimal(amount);
    return ret.setScale(2, RoundingMode.HALF_EVEN);
  }

  /**
   * 获取6位小数的BigDecimal
   *
   * @param amount
   * @return
   */
  public static BigDecimal roundingToSixDecimal(BigDecimal amount) {
    return amount.setScale(6, BigDecimal.ROUND_HALF_EVEN);
  }

  /**
   * 按照给定的基数进位，
   * ceilByRadix(101, 10) = 120
   * ceilByRadix(101, 100) = 200
   */
  public static BigDecimal ceilByRadix(BigDecimal num, BigDecimal radix){
    if(num.compareTo(BigDecimal.ZERO) == 0){
      return radix;
    }else{
      return num.divide(radix, 0, RoundingMode.CEILING).multiply(radix);
    }
  }

  /**
   * 获取2位小数的BigDecimal
   *
   * @param amount
   * @return
   */
  public static BigDecimal roundingToTwoDecimal(BigDecimal amount) {
    return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }

  /**
   * 获取2位小数的BigDecimal, 舍弃多余小数位
   *
   * @param amount
   * @return
   */
  public static BigDecimal downToTwoDecimal(BigDecimal amount) {
    return amount == null ? BigDecimal.ZERO : amount.setScale(2, BigDecimal.ROUND_DOWN);
  }

  /**
   * 获取两个BigDecimal之间大的那个
   * 
   * @param b1
   * @param b2
   * @return
   */
  public static BigDecimal max(BigDecimal b1, BigDecimal b2) {
    return b1.max(b2);
  }

  /**
   * 获取两个BigDecimal之间小的那个
   * 
   * @param b1
   * @param b2
   * @return
   */
  public static BigDecimal min(BigDecimal b1, BigDecimal b2) {
    return b1.min(b2);
  }

  /**
   * 1.3 -> 000000000013
   * 
   * @param bd
   * @return
   */
  public static String leftPadCentWithZero(BigDecimal bd) {
    return StringUtils.leftPad(BigDecimalUtil.yuanToCent(bd), 12, "0");
  }

  public static void main(String[] args) {
//    System.out.println(doubleToTwoBitBigDecimal(0.01999));
    BigDecimal b = new BigDecimal("1.53256780974");
    System.out.println(b.setScale(6, RoundingMode.HALF_EVEN));
  }

}

package com.gulewang.backend.service;

import com.gulewang.stock.api.bean.StockBo;
import com.gulewang.stock.api.bean.StockDailyTradeInfoBo;

import java.util.List;

/**
 * Created by Thomas on 17/4/9.
 */
public interface CompositeUserStockService {

  boolean addInterestStock(String userId, List<String> codes);

  boolean removeInterestStock(String userId, List<String> codes);
}

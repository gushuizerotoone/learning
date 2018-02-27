package com.gulewang.backend.service;

import com.gulewang.stock.api.bean.StockBo;
import com.gulewang.stock.api.bean.StockDailyTradeInfoBo;

import java.util.List;

/**
 * Created by Thomas on 2017/4/15.
 */
public interface CompositeUserStockQueryService {

  List<StockDailyTradeInfoBo> getInterestStockDailyTradeInfo(String userId);

  /** 模糊搜索 */
  List<StockBo> queryStock(String code);
}

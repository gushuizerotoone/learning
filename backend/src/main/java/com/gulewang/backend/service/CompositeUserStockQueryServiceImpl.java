package com.gulewang.backend.service;

import com.gulewang.backend.cache.StockCache;
import com.gulewang.stock.api.bean.StockBo;
import com.gulewang.stock.api.bean.StockDailyTradeInfoBo;
import com.gulewang.stock.api.bean.UserInterestStocksBo;
import com.gulewang.stock.service.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thomas on 2017/4/15.
 */
@Service
public class CompositeUserStockQueryServiceImpl implements CompositeUserStockQueryService {

  @Autowired
  UserStockService userStockService;

  @Autowired
  StockCache stockCache;

  @Override
  public List<StockDailyTradeInfoBo> getInterestStockDailyTradeInfo(String userId) {
    UserInterestStocksBo userInterestStocksBo = userStockService.getUserInterestStocks(userId);
    return stockCache.getUserStockDailyTradeInfoList(userInterestStocksBo.getCodes());
  }

  @Override
  public List<StockBo> queryStock(String code) {
    return stockCache.searchStockAndZhiShu(code);
  }
}

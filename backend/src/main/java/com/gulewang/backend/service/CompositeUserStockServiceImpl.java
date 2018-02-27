package com.gulewang.backend.service;

import com.gulewang.backend.cache.StockCache;
import com.gulewang.common.exception.AppException;
import com.gulewang.common.exception.AppExceptionCode;
import com.gulewang.stock.api.bean.StockBo;
import com.gulewang.stock.service.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thomas on 17/4/9.
 */
@Service
public class CompositeUserStockServiceImpl implements CompositeUserStockService {
  @Autowired
  UserStockService userStockService;

  @Autowired
  StockCache stockCache;

  @Override
  public boolean addInterestStock(String userId, List<String> codes) {
    if (codes == null) {
      return true;
    }
    if (codes.parallelStream().filter(code -> !stockCache.isValidStockOrZhiShu(code)).findAny().isPresent()) {
      throw new AppException(AppExceptionCode.STOCK_CODE_NOT_VALID);
    }
    return userStockService.addInterestStock(userId, codes);
  }

  @Override
  public boolean removeInterestStock(String userId, List<String> codes) {
    if (codes == null) {
      return true;
    }
    if (codes.parallelStream().filter(code -> !stockCache.isValidStockOrZhiShu(code)).findAny().isPresent()) {
      throw new AppException(AppExceptionCode.STOCK_CODE_NOT_VALID);
    }
    return userStockService.removeInterestStock(userId, codes);
  }

}

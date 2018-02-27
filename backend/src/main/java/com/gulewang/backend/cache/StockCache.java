package com.gulewang.backend.cache;

import com.google.common.collect.Lists;
import com.gulewang.common.util.CodeUtil;
import com.gulewang.stock.api.bean.StockBo;
import com.gulewang.stock.api.bean.StockDailyTradeInfoBo;
import com.gulewang.stock.service.StockService;
import com.gulewang.stock.service.StockTradeInfoFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 17/4/8.
 */
@Component
public class StockCache {

  private static final Logger logger = LoggerFactory.getLogger(StockCache.class);

  @Autowired
  StockService stockService;

  @Autowired
  StockTradeInfoFetcher stockTradeInfoFetcher;

  private Map<String, StockDailyTradeInfoBo> stockAndZhiShuDailyTradeInfoBoMap;
  private Map<String, StockBo> stockSet;
  private Map<String, StockBo> zhiShuSet;

  @Scheduled(fixedDelay = 2000, initialDelay = 2000)
  private void loadStockAndZhiShuDailyTradeInfo() {
    logger.debug("Begin to loadStockAndZhiShuDailyTradeInfo...");
    if (stockSet == null) {
      loadStocks();
    }
    List<StockDailyTradeInfoBo> stockAndZhiShuDailyTradeInfoBoList = stockTradeInfoFetcher.fetchTradeInfo(Lists.newArrayList(stockSet.keySet()));
    List<StockDailyTradeInfoBo> zhiShuDailyTradeInfoBoList = stockTradeInfoFetcher.fetchZhiShuTradeInfo(Lists.newArrayList(zhiShuSet.keySet()));

    stockAndZhiShuDailyTradeInfoBoList.addAll(zhiShuDailyTradeInfoBoList); // add stock and zhiShu together
    stockAndZhiShuDailyTradeInfoBoMap = stockAndZhiShuDailyTradeInfoBoList.parallelStream().collect(Collectors.toMap(StockDailyTradeInfoBo::getCode, Function.identity()));

    logger.debug("Finish loadStockAndZhiShuDailyTradeInfo...");
  }

  private void loadStocks() {
    stockSet = stockService.getAllStocksToMap();
    zhiShuSet = stockService.getAllZhiShuToMap();
  }

  /**
   * code可能是300xx, SZ300...
   * 模糊查询
   * @param code
   * @return
   */
  public List<StockBo> searchStockAndZhiShu(String code) {
    final String queryCode = CodeUtil.getQueryCode(code);

    List<StockBo> stockBoList = Lists.newArrayList();
    List<StockBo> stockPart = stockSet.entrySet().parallelStream()
            .filter(entry -> entry.getKey().startsWith(queryCode))
            .map(entry -> entry.getValue())
            .sorted(Comparator.comparing(StockBo::getCode))
            .collect(Collectors.toList());

    List<StockBo> zhiShuPart = zhiShuSet.entrySet().parallelStream()
            .filter(entry -> entry.getKey().startsWith(queryCode))
            .map(entry -> entry.getValue())
            .sorted(Comparator.comparing(StockBo::getCode))
            .collect(Collectors.toList());

    stockBoList.addAll(stockPart);
    stockBoList.addAll(zhiShuPart);

    int toIndex = stockBoList.size() > 10 ? 10 : stockBoList.size(); // 先只取10条
    return stockBoList.subList(0, toIndex);
  }

  public boolean isValidStockOrZhiShu(String code) {
    return stockSet.containsKey(code) || zhiShuSet.containsKey(code);
  }

  public List<StockDailyTradeInfoBo> getUserStockDailyTradeInfoList(List<String> codes) {
    if (CollectionUtils.isEmpty(codes)) {
      return Collections.emptyList();
    }

    if (stockAndZhiShuDailyTradeInfoBoMap == null) {
      loadStockAndZhiShuDailyTradeInfo();
    }

    return codes.stream().map(code -> stockAndZhiShuDailyTradeInfoBoMap.get(code)).collect(Collectors.toList());
  }

}

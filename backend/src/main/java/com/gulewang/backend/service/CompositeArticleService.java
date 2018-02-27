package com.gulewang.backend.service;

import com.gulewang.article.bean.ArticleBo;
import com.gulewang.article.bean.CreateArticleReqBo;
import com.gulewang.article.bean.CreateCommentReqBo;
import com.gulewang.backend.bean.AggregateArticleBo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Thomas on 2017/4/15.
 */
public interface CompositeArticleService {

  List<AggregateArticleBo> findFriendArticles(String followerUserId, Pageable pageable);

  /**
   * 获取指定朋友用户的文章
   * @param userId 登录用户
   * @param userName 指定用户的文章
   * @param pageable
   * @return
   */
  List<AggregateArticleBo> findFriendProfileArticles(String userId, String userName, PageRequest pageable);

  /**
   * 获取指定陌生人的文章
   * @param authorUserName
   * @return
   */
  List<AggregateArticleBo> findStrangerArticles(String authorUserName);

  boolean praise(String praiseUserId, String articleId);

  String comment(String articleId, String commentUserId, CreateCommentReqBo createCommentReqBo);

  AggregateArticleBo findArticle(String userId, String articleId);
}

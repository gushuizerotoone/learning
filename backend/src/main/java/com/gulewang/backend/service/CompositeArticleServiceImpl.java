package com.gulewang.backend.service;

import com.google.common.collect.Lists;
import com.gulewang.article.bean.ArticleBo;
import com.gulewang.article.bean.CreateCommentReqBo;
import com.gulewang.article.service.ArticleService;
import com.gulewang.backend.bean.AggregateArticleBo;
import com.gulewang.backend.bean.CommentBo;
import com.gulewang.common.exception.AppException;
import com.gulewang.common.exception.AppExceptionCode;
import com.gulewang.common.util.AssertUtil;
import com.gulewang.user.bean.BasicUserBo;
import com.gulewang.user.bean.UserBo;
import com.gulewang.user.domain.PrivateSetting;
import com.gulewang.user.service.UserService;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 2017/4/15.
 */
@Service
public class CompositeArticleServiceImpl implements CompositeArticleService {

  private static final Logger logger = LoggerFactory.getLogger(CompositeArticleServiceImpl.class);

  @Autowired
  UserService userService;

  @Autowired
  ArticleService articleService;

  @Override
  public List<AggregateArticleBo> findFriendArticles(String followerUserId, Pageable pageable) {
    logger.info("Request to find all friend articles: {}", followerUserId);

    Map<String, BasicUserBo> basicUserBoMap = userService.getFriendsAndUserSelfToMap(followerUserId);
    List<ArticleBo> articleBoList = articleService.findAllAuthorArticles(Lists.newArrayList(basicUserBoMap.keySet()), pageable);

    List<AggregateArticleBo> articleBos = articleBoList.stream().map(articleBo -> convertToAggregateArticleBoOfFriend(articleBo, basicUserBoMap)).collect(Collectors.toList());

    logger.info("Response to find all friend articles: {}, size: {}", followerUserId, articleBos.size());
    return articleBos;
  }

  @Override
  public List<AggregateArticleBo> findFriendProfileArticles(String requestUserId, String authorUserName, PageRequest pageable) {
    logger.info("Request to find author articles: requestUserId: {}, authorUserName: {}, pageable: {}", requestUserId, authorUserName, pageable);

    UserBo authorUserBo = userService.getUserByUserName(authorUserName);
    AssertUtil.checkState(isFriendShip(requestUserId, authorUserBo.getId()), AppExceptionCode.PERMISSION_DENY);

    Map<String, BasicUserBo> basicUserBoMap = userService.getFriendsAndUserSelfToMap(requestUserId);
    List<ArticleBo> articleBoList = articleService.findArticles(authorUserBo.getId(), pageable);

    List<AggregateArticleBo> articleBos = articleBoList.stream().map(articleBo -> convertToAggregateArticleBoOfFriend(articleBo, basicUserBoMap)).collect(Collectors.toList());
    logger.info("Response to find all friend articles, requestUserId: {}, size: {}", requestUserId, articleBos.size());
    return articleBos;
  }

  @Override
  public List<AggregateArticleBo> findStrangerArticles(String authorUserName) {
    logger.info("Request to find author articles: authorUserName: {}", authorUserName);

    UserBo authorUserBo = userService.getUserByUserName(authorUserName);

    PrivateSetting authorPrivateSetting = authorUserBo.getPrivateSetting();

    PageRequest pageRequest = checkPrivateSetting(authorUserBo, authorPrivateSetting);
    if (pageRequest == null) {
      return Lists.newArrayList();
    }

    List<ArticleBo> articleBoList = articleService.findArticles(authorUserBo.getId(), pageRequest);

    List<AggregateArticleBo> articleBos = articleBoList.stream().map(articleBo -> convertToAggregateArticleBoOfStrange(articleBo, authorUserBo)).collect(Collectors.toList());
    logger.info("Response to find all friend articles, size: {}", articleBos.size());
    return articleBos;
  }

  /**
   * 检查requestUser查看authorUser文章的权限
   */
  private PageRequest checkPrivateSetting(UserBo authorUserBo, PrivateSetting authorPrivateSetting) {
    if (authorPrivateSetting.isTenArticlesOpen()) { // 陌生人，允许看十篇
      return new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "dateTime"));
    }

    return null; // 陌生人，不允许看了
  }

  @Override
  public boolean praise(String praiseUserId, String articleId) {
    logger.info("Request to praise article, userId:{}, articleId:{}", praiseUserId, articleId);
    checkIfFriendship(praiseUserId, articleId);

    return articleService.praise(praiseUserId, articleId);
  }

  @Override
  public String comment(String articleId, String commentUserId, CreateCommentReqBo createCommentReqBo) {
    logger.info("Request to comment article, articleId: {}, commentUserId: {}", articleId, commentUserId);
    checkIfFriendship(commentUserId, articleId);

    return articleService.comment(articleId, commentUserId, createCommentReqBo.getContent());
  }

  @Override
  public AggregateArticleBo findArticle(String userId, String articleId) {
    logger.info("Request to find article, articleId: {}, userId: {}", articleId, userId);
    checkIfFriendship(userId, articleId);

    ArticleBo articleBo = articleService.findArticle(articleId);
    Map<String, BasicUserBo> basicUserBoMap = userService.getFriendsAndUserSelfToMap(userId);

    AggregateArticleBo aggregateArticleBo = convertToAggregateArticleBoOfFriend(articleBo, basicUserBoMap);
    logger.info("Response of find article, articleId: {}", articleId);
    return aggregateArticleBo;
  }

  /**
   * 判断userId和articleId的author是不是好友关系
   */
  private void checkIfFriendship(String userId, String articleId) {
    ArticleBo articleBo = articleService.findArticle(articleId);

    if (!isFriendShip(userId, articleBo.getAuthorUserId())) {
      logger.info("No friendship between these two users: {}, {}", userId, articleBo.getAuthorUserId());
      throw new AppException(AppExceptionCode.ILLEGAL_ARGUMENT);
    }
  }

  /**
   * 判断是否是朋友关系
   * 如果是自个本身，相当于好友关系
   */
  private boolean isFriendShip(String userIdA, String userIdB) {
    if (userIdA.equals(userIdB)) { // 自个本身，相当于好友关系
      return true;
    }
    UserBo userA = userService.getUser(userIdA);
    AssertUtil.checkNotNull(userA, AppExceptionCode.USER_NOT_FOUND);

    if (userA.getFriendUserIds().parallelStream().filter(oneUserId -> oneUserId.equals(userIdB)).findAny().isPresent()) {
      return true;
    }
    return false;
  }

  /**
   * 陌生人之间转换为aggregateArticleBo
   * 陌生人是不能看赞和评论的
   * @param articleBo
   * @return
   */
  private AggregateArticleBo convertToAggregateArticleBoOfStrange(ArticleBo articleBo, UserBo authorUserBo) {
    AggregateArticleBo aggregateArticleBo = new AggregateArticleBo();
    try {
      PropertyUtils.copyProperties(aggregateArticleBo, articleBo);
    } catch (Exception e) {
      e.printStackTrace();
    }

    aggregateArticleBo.setProfileImage(authorUserBo.getProfileImage());
    aggregateArticleBo.setAuthorNickName(authorUserBo.getNickName());
    aggregateArticleBo.setAuthorUserName(authorUserBo.getUserName());

    return aggregateArticleBo;
  }

  /**
   * 朋友之间转换为aggregateArticleBo
   * @param articleBo
   * @return
   */
  private AggregateArticleBo convertToAggregateArticleBoOfFriend(ArticleBo articleBo, Map<String, BasicUserBo> basicUserBoMap) {
    AggregateArticleBo aggregateArticleBo = new AggregateArticleBo();
    try {
      PropertyUtils.copyProperties(aggregateArticleBo, articleBo);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 赞的转换
    List<BasicUserBo> praiseUserList = articleBo.getPraiseList().stream()
            .filter(praiseUserId -> basicUserBoMap.containsKey(praiseUserId)) // 朋友关系才可以看
            .map(praiseUserId -> basicUserBoMap.get(praiseUserId))
            .collect(Collectors.toList());

    // 评论的转换
    List<CommentBo> commentBoList = articleBo.getCommentList().stream()
            .filter(comment -> basicUserBoMap.containsKey(comment.getCommentUserId())) // 朋友关系才可以看
            .map(comment -> {
              CommentBo commentBo = new CommentBo();
              try {
                PropertyUtils.copyProperties(commentBo, comment);
              } catch (Exception e) {
                e.printStackTrace();
              }
              commentBo.setCommentUserProfileImage(basicUserBoMap.get(comment.getCommentUserId()).getProfileImage());
              commentBo.setCommentUserNickName(basicUserBoMap.get(comment.getCommentUserId()).getNickName());
              return commentBo;
            })
            .collect(Collectors.toList());

    BasicUserBo authorUserBo = basicUserBoMap.get(articleBo.getAuthorUserId());
    aggregateArticleBo.setProfileImage(authorUserBo.getProfileImage());
    aggregateArticleBo.setAuthorNickName(authorUserBo.getNickName());
    aggregateArticleBo.setAuthorUserName(authorUserBo.getUserName());

    aggregateArticleBo.setPraiseCount(articleBo.getPraiseList().size());
    aggregateArticleBo.setPraiseUserList(praiseUserList);
    aggregateArticleBo.setCommentList(commentBoList);
    return aggregateArticleBo;
  }

}

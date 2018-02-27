package com.gulewang.backend.bean;

import com.gulewang.user.bean.BasicUserBo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 2017/6/28.
 */
public class AggregateArticleBo {
  private String id;
  private String authorUserId;
  private String authorNickName;
  private String authorUserName;
  private String profileImage;
  private String title;
  private String content;
  private Long dateTime;
  private List<BasicUserBo> praiseUserList = new ArrayList<>(); // 赞的用户
  private Integer praiseCount; // 总赞的个数
  private Integer aiValue; // ai影响力
  private List<CommentBo> commentList; // 评论

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAuthorUserId() {
    return authorUserId;
  }

  public void setAuthorUserId(String authorUserId) {
    this.authorUserId = authorUserId;
  }

  public String getAuthorNickName() {
    return authorNickName;
  }

  public void setAuthorNickName(String authorNickName) {
    this.authorNickName = authorNickName;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getDateTime() {
    return dateTime;
  }

  public void setDateTime(Long dateTime) {
    this.dateTime = dateTime;
  }

  public List<BasicUserBo> getPraiseUserList() {
    return praiseUserList;
  }

  public void setPraiseUserList(List<BasicUserBo> praiseUserList) {
    this.praiseUserList = praiseUserList;
  }

  public Integer getAiValue() {
    return aiValue;
  }

  public void setAiValue(Integer aiValue) {
    this.aiValue = aiValue;
  }

  public List<CommentBo> getCommentList() {
    return commentList;
  }

  public void setCommentList(List<CommentBo> commentList) {
    this.commentList = commentList;
  }

  public Integer getPraiseCount() {
    return praiseCount;
  }

  public void setPraiseCount(Integer praiseCount) {
    this.praiseCount = praiseCount;
  }

  public String getAuthorUserName() {
    return authorUserName;
  }

  public void setAuthorUserName(String authorUserName) {
    this.authorUserName = authorUserName;
  }
}

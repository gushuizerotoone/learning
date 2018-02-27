package com.gulewang.backend.bean;

/**
 * Created by Thomas on 2017/6/28.
 */
public class CommentBo {
  private String id;
  private String commentUserId;
  private String commentUserNickName;
  private String commentUserProfileImage;
  private String content;
  private Long dateTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCommentUserId() {
    return commentUserId;
  }

  public void setCommentUserId(String commentUserId) {
    this.commentUserId = commentUserId;
  }

  public String getCommentUserProfileImage() {
    return commentUserProfileImage;
  }

  public void setCommentUserProfileImage(String commentUserProfileImage) {
    this.commentUserProfileImage = commentUserProfileImage;
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

  public String getCommentUserNickName() {
    return commentUserNickName;
  }

  public void setCommentUserNickName(String commentUserNickName) {
    this.commentUserNickName = commentUserNickName;
  }
}

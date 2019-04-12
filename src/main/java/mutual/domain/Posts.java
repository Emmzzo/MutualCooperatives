/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutual.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Emma
 */
@Entity
@Table(name = "Posts")
@NamedQuery(name = "Posts.findAll", query = "SELECT r FROM Posts r order by postId desc")
public class Posts extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "postId")
	private int postId;
	@Column(name = "postDate")
	private Date postDate;
	@Column(name = "status")
	private String status;

	@Column(name = "description")
	private String description;
	@ManyToOne
	@JoinColumn(name = "member")
	private Users member;
	@ManyToOne
	@JoinColumn(name = "mutualcoop")
	private MutualCooperative mutualcoop;
	@Transient
	private String action;
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Users getMember() {
		return member;
	}
	public void setMember(Users member) {
		this.member = member;
	}
	public MutualCooperative getMutualcoop() {
		return mutualcoop;
	}
	public void setMutualcoop(MutualCooperative mutualcoop) {
		this.mutualcoop = mutualcoop;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
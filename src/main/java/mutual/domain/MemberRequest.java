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
@Table(name = "MemberRequest")
@NamedQuery(name = "MemberRequest.findAll", query = "SELECT r FROM MemberRequest r order by requestId desc")
public class MemberRequest extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "requestId")
	private int requestId;

	@Column(name = "requestDate")
	private Date requestDate;
	@Column(name = "requestStatus")
	private String requestStatus;
	@ManyToOne
	@JoinColumn(name = "member")
	private Users member;
	@ManyToOne
	@JoinColumn(name = "mutualcoop")
	private MutualCooperative mutualcoop;
	@Transient
	private String action;
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
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

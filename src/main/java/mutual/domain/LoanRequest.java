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
@Table(name = "LoanRequest")
@NamedQuery(name = "LoanRequest.findAll", query = "SELECT r FROM LoanRequest r order by requestId desc")
public class LoanRequest extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "requestId")
	private int requestId;

	@Column(name = "amount")
	private Double amount;
	@Column(name = "requestDate")
	private Date requestDate;
	@Column(name = "approvedDate")
	private Date approvedDate;
	@Column(name = "status")
	private String status;
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

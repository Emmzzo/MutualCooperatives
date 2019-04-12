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
@Table(name ="InterestCharges")
@NamedQuery(name = "InterestCharges.findAll", query = "SELECT r FROM InterestCharges r order by chargeId desc")
public class InterestCharges extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "chargeId")
	private int chargeId;
	@Column(name = "amount")
	private double amount;
	@Column(name = "givenDate")
	private Date givenDate;
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
	public int getChargeId() {
		return chargeId;
	}
	public void setChargeId(int chargeId) {
		this.chargeId = chargeId;
	}
	public Date getGivenDate() {
		return givenDate;
	}
	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public MutualCooperative getMutualcoop() {
		return mutualcoop;
	}
	public void setMutualcoop(MutualCooperative mutualcoop) {
		this.mutualcoop = mutualcoop;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}

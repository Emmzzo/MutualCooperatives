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
@Table(name = "MutualCoopMembers")
@NamedQuery(name = "MutualCoopMembers.findAll", query = "SELECT r FROM MutualCoopMembers r order by mutualMemberId desc")
public class MutualCoopMembers extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "mutualMemberId")
	private int mutualMemberId;
	@Column(name = "memberSize")
	private int memberSize;
	@ManyToOne
	@JoinColumn(name = "member")
	private Users member;
	@ManyToOne
	@JoinColumn(name = "mutualcoop")
	private MutualCooperative mutualcoop;
	@Transient
	private String action;
	public int getMutualMemberId() {
		return mutualMemberId;
	}
	public void setMutualMemberId(int mutualMemberId) {
		this.mutualMemberId = mutualMemberId;
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
	public int getMemberSize() {
		return memberSize;
	}
	public void setMemberSize(int memberSize) {
		this.memberSize = memberSize;
	}
	
}

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
@Table(name = "MutualCoopPolicy")
@NamedQuery(name = "MutualCoopPolicy.findAll", query = "SELECT r FROM MutualCoopPolicy r order by policyId desc")
public class MutualCoopPolicy extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "policyId")
	private int policyId;

	@Column(name = "fineCharges")
	private Double fineCharges;
	@Column(name = "interesCharges")
	private Double interesCharges;
	@Column(name = "policyDescription")
	private String policyDescription;
	@ManyToOne
	@JoinColumn(name = "mutualcoop")
	private MutualCooperative mutualcoop;
	@Transient
	private String action;
	public int getPolicyId() {
		return policyId;
	}
	public void setPolicyId(int policyId) {
		this.policyId = policyId;
	}
	public Double getFineCharges() {
		return fineCharges;
	}
	public void setFineCharges(Double fineCharges) {
		this.fineCharges = fineCharges;
	}
	public Double getInteresCharges() {
		return interesCharges;
	}
	public void setInteresCharges(Double interesCharges) {
		this.interesCharges = interesCharges;
	}
	public String getPolicyDescription() {
		return policyDescription;
	}
	public void setPolicyDescription(String policyDescription) {
		this.policyDescription = policyDescription;
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

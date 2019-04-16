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
@Table(name = "MutualCooperative")
@NamedQuery(name = "MutualCooperative.findAll", query = "SELECT r FROM MutualCooperative r order by mutualCoopId desc")
public class MutualCooperative extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "mutualCoopId")
	private int mutualCoopId;

	@Column(name = "mutualCoopName")
	private String mutualCoopName;
	@Column(name = "mutualCoopType")
	private String mutualCoopType;
	@Column(name = "logo")
	private String logo;
	@Column(name = "status")
	private String status;
	@Column(name = "address")
	private String  address;
	@Transient
	private String action;
	@Transient
	private int countmembers;
	public int getMutualCoopId() {
		return mutualCoopId;
	}
	public void setMutualCoopId(int mutualCoopId) {
		this.mutualCoopId = mutualCoopId;
	}
	public String getMutualCoopName() {
		return mutualCoopName;
	}
	public void setMutualCoopName(String mutualCoopName) {
		this.mutualCoopName = mutualCoopName;
	}
	public String getMutualCoopType() {
		return mutualCoopType;
	}
	public void setMutualCoopType(String mutualCoopType) {
		this.mutualCoopType = mutualCoopType;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getCountmembers() {
		return countmembers;
	}
	public void setCountmembers(int countmembers) {
		this.countmembers = countmembers;
	}	
	
	
}

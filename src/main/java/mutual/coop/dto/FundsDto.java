package mutual.coop.dto;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import mutual.domain.MutualCooperative;
import mutual.domain.Users;

public class FundsDto {
	private static final long serialVersionUID = 1L;
	
	private int fundId;
	
	private Double amount;

	private Double balance;
	
	private Date givenDate;
	private String overloanrequest,fundavail,contDate;
	private String status,recordedBy,formatAmount,formatBalance,generiStatus;
	private Users usermember;
	
	private MutualCooperative mutualcoop;

	private boolean editable,editchanges;

	public int getFundId() {
		return fundId;
	}

	public void setFundId(int fundId) {
		this.fundId = fundId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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

	public Users getUsermember() {
		return usermember;
	}

	public void setUsermember(Users usermember) {
		this.usermember = usermember;
	}

	public MutualCooperative getMutualcoop() {
		return mutualcoop;
	}

	public void setMutualcoop(MutualCooperative mutualcoop) {
		this.mutualcoop = mutualcoop;
	}

	public String getContDate() {
		return contDate;
	}

	public void setContDate(String contDate) {
		this.contDate = contDate;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getRecordedBy() {
		return recordedBy;
	}

	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
	}

	public String getFormatAmount() {
		return formatAmount;
	}

	public void setFormatAmount(String formatAmount) {
		this.formatAmount = formatAmount;
	}

	public String getFormatBalance() {
		return formatBalance;
	}

	public void setFormatBalance(String formatBalance) {
		this.formatBalance = formatBalance;
	}

	public boolean isEditchanges() {
		return editchanges;
	}

	public void setEditchanges(boolean editchanges) {
		this.editchanges = editchanges;
	}

	public String getGeneriStatus() {
		return generiStatus;
	}

	public void setGeneriStatus(String generiStatus) {
		this.generiStatus = generiStatus;
	}

	public String getOverloanrequest() {
		return overloanrequest;
	}

	public void setOverloanrequest(String overloanrequest) {
		this.overloanrequest = overloanrequest;
	}

	public String getFundavail() {
		return fundavail;
	}

	public void setFundavail(String fundavail) {
		this.fundavail = fundavail;
	}

	
}

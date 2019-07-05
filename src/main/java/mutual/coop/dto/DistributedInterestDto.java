package mutual.coop.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import mutual.domain.MutualCooperative;
import mutual.domain.Users;

public class DistributedInterestDto implements Serializable {
private static final long serialVersionUID = 1L;
	
	private MutualCooperative mutualcoop;
	private Users usermember;
	private boolean editable,editchanges,loanrequest,accept,approval,paidStatus;
	private int incomeId,countRecord,interestId;
	private String sourceOfIncome,receiveDate;
	private Double incomeAmount;
	private Double recordedAmount;
	private String generiStatus,status,fullname,phone,formatAmount;
	private Date givenDate;
	public MutualCooperative getMutualcoop() {
		return mutualcoop;
	}
	public void setMutualcoop(MutualCooperative mutualcoop) {
		this.mutualcoop = mutualcoop;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public boolean isEditchanges() {
		return editchanges;
	}
	public void setEditchanges(boolean editchanges) {
		this.editchanges = editchanges;
	}
	public boolean isLoanrequest() {
		return loanrequest;
	}
	public void setLoanrequest(boolean loanrequest) {
		this.loanrequest = loanrequest;
	}
	public boolean isAccept() {
		return accept;
	}
	public void setAccept(boolean accept) {
		this.accept = accept;
	}
	public boolean isApproval() {
		return approval;
	}
	public void setApproval(boolean approval) {
		this.approval = approval;
	}
	public boolean isPaidStatus() {
		return paidStatus;
	}
	public void setPaidStatus(boolean paidStatus) {
		this.paidStatus = paidStatus;
	}
	public int getIncomeId() {
		return incomeId;
	}
	public void setIncomeId(int incomeId) {
		this.incomeId = incomeId;
	}
	public String getSourceOfIncome() {
		return sourceOfIncome;
	}
	public void setSourceOfIncome(String sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}
	public Double getIncomeAmount() {
		return incomeAmount;
	}
	public void setIncomeAmount(Double incomeAmount) {
		this.incomeAmount = incomeAmount;
	}
	public Double getRecordedAmount() {
		return recordedAmount;
	}
	public void setRecordedAmount(Double recordedAmount) {
		this.recordedAmount = recordedAmount;
	}
	public String getGeneriStatus() {
		return generiStatus;
	}
	public void setGeneriStatus(String generiStatus) {
		this.generiStatus = generiStatus;
	}
	public int getCountRecord() {
		return countRecord;
	}
	public void setCountRecord(int countRecord) {
		this.countRecord = countRecord;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getInterestId() {
		return interestId;
	}
	public void setInterestId(int interestId) {
		this.interestId = interestId;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFormatAmount() {
		return formatAmount;
	}
	public void setFormatAmount(String formatAmount) {
		this.formatAmount = formatAmount;
	}
	public Date getGivenDate() {
		return givenDate;
	}
	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
	}
	public Users getUsermember() {
		return usermember;
	}
	public void setUsermember(Users usermember) {
		this.usermember = usermember;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}	
	
}

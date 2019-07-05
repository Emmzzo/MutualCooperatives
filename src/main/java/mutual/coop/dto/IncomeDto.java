package mutual.coop.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import mutual.domain.MutualCooperative;
import mutual.domain.Users;

public class IncomeDto implements Serializable {
private static final long serialVersionUID = 1L;
	
	private MutualCooperative mutualcoop;
	private boolean editable,editchanges,loanrequest,accept,approval,paidStatus;
	private int incomeId,countRecord;
	private String sourceOfIncome;
	private Double incomeAmount;
	private Double recordedAmount;
	private String generiStatus,status;
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
	
	
}

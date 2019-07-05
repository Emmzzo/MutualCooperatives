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

public class LoanDto {
	private static final long serialVersionUID = 1L;
	
	private int requestId,dayremaining;
	private Double amount;

	private Double balance;
	private Date approvedDate,requestDate,returnDate;
	
	private String status,recordedBy,formatAmount,formatBalance,generiStatus;
	private Users usermember;
	
	private MutualCooperative mutualcoop;

	private boolean editable,editchanges,loanrequest,accept,approval,paidStatus;

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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getGeneriStatus() {
		return generiStatus;
	}

	public void setGeneriStatus(String generiStatus) {
		this.generiStatus = generiStatus;
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

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public boolean isPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(boolean paidStatus) {
		this.paidStatus = paidStatus;
	}

	public int getDayremaining() {
		return dayremaining;
	}

	public void setDayremaining(int dayremaining) {
		this.dayremaining = dayremaining;
	}
}

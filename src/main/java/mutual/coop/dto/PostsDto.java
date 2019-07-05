package mutual.coop.dto;

import java.io.Serializable;
import java.util.Date;

import mutual.domain.MutualCooperative;
import mutual.domain.Users;

public class PostsDto implements Serializable {
private static final long serialVersionUID = 1L;
	
	private int postId,countInfo;
	private Date approvedDate,createdDate;
	private String status,recordedBy,description,generiStatus;
	private Users usermember;
	
	private MutualCooperative mutualcoop;
	private boolean editable,editchanges,loanrequest,accept,approval,paidStatus;
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public boolean isPaidStatus() {
		return paidStatus;
	}
	public void setPaidStatus(boolean paidStatus) {
		this.paidStatus = paidStatus;
	}
	public int getCountInfo() {
		return countInfo;
	}
	public void setCountInfo(int countInfo) {
		this.countInfo = countInfo;
	}

}

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

public class MemberRequestDto {
	private int requestId;
	private Date requestDate;
	private String requestStatus;
	private Users member;
	private MutualCooperative mutualcoop;
	private String action;
	private boolean editable;
	private boolean showcontact,rejectrequest;
	private boolean hidecontact,pendingstatus;
	private int countinfo;
	
	public boolean isRejectrequest() {
		return rejectrequest;
	}
	public void setRejectrequest(boolean rejectrequest) {
		this.rejectrequest = rejectrequest;
	}
	public boolean isPendingstatus() {
		return pendingstatus;
	}
	public void setPendingstatus(boolean pendingstatus) {
		this.pendingstatus = pendingstatus;
	}
	public int getCountinfo() {
		return countinfo;
	}
	public void setCountinfo(int countinfo) {
		this.countinfo = countinfo;
	}
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
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
	public boolean isShowcontact() {
		return showcontact;
	}
	public void setShowcontact(boolean showcontact) {
		this.showcontact = showcontact;
	}
	public boolean isHidecontact() {
		return hidecontact;
	}
	public void setHidecontact(boolean hidecontact) {
		this.hidecontact = hidecontact;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	
}

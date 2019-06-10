package mutual.coop.dto;
import java.sql.Timestamp;

import mutual.domain.MutualCooperative;
import mutual.domain.Users;

public class MutualCoopMemberDto {
	private int mutualMemberId;
	private int memberSize;
	private Users member;
	private MutualCooperative mutualcoop;
	private String action,email;
	private boolean showcontact,hidecontact;
	private int countinfo;
	private String fineCharges;
	private String interesCharges,fineondelayedcontribution,mincontribution,periodicinvestterm;
	private String policyDescription;
	private boolean renderBoard,pendingstatus,approvedreq,blockedstatus,rejectedstatus,editable;
	private Users members;
	private Timestamp crtdDtTime;
	public String getFineCharges() {
		return fineCharges;
	}
	public void setFineCharges(String fineCharges) {
		this.fineCharges = fineCharges;
	}
	public String getInteresCharges() {
		return interesCharges;
	}
	public void setInteresCharges(String interesCharges) {
		this.interesCharges = interesCharges;
	}
	public String getPolicyDescription() {
		return policyDescription;
	}
	public void setPolicyDescription(String policyDescription) {
		this.policyDescription = policyDescription;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public int getCountinfo() {
		return countinfo;
	}
	public void setCountinfo(int countinfo) {
		this.countinfo = countinfo;
	}
	public int getMutualMemberId() {
		return mutualMemberId;
	}
	public void setMutualMemberId(int mutualMemberId) {
		this.mutualMemberId = mutualMemberId;
	}
	public int getMemberSize() {
		return memberSize;
	}
	public void setMemberSize(int memberSize) {
		this.memberSize = memberSize;
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
	public boolean isRenderBoard() {
		return renderBoard;
	}
	public void setRenderBoard(boolean renderBoard) {
		this.renderBoard = renderBoard;
	}
	public boolean isPendingstatus() {
		return pendingstatus;
	}
	public void setPendingstatus(boolean pendingstatus) {
		this.pendingstatus = pendingstatus;
	}
	public boolean isApprovedreq() {
		return approvedreq;
	}
	public void setApprovedreq(boolean approvedreq) {
		this.approvedreq = approvedreq;
	}
	public boolean isBlockedstatus() {
		return blockedstatus;
	}
	public void setBlockedstatus(boolean blockedstatus) {
		this.blockedstatus = blockedstatus;
	}
	public boolean isRejectedstatus() {
		return rejectedstatus;
	}
	public void setRejectedstatus(boolean rejectedstatus) {
		this.rejectedstatus = rejectedstatus;
	}
	public Users getMembers() {
		return members;
	}
	public void setMembers(Users members) {
		this.members = members;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Timestamp getCrtdDtTime() {
		return crtdDtTime;
	}
	public void setCrtdDtTime(Timestamp crtdDtTime) {
		this.crtdDtTime = crtdDtTime;
	}
	public String getFineondelayedcontribution() {
		return fineondelayedcontribution;
	}
	public void setFineondelayedcontribution(String fineondelayedcontribution) {
		this.fineondelayedcontribution = fineondelayedcontribution;
	}
	public String getMincontribution() {
		return mincontribution;
	}
	public void setMincontribution(String mincontribution) {
		this.mincontribution = mincontribution;
	}
	public String getPeriodicinvestterm() {
		return periodicinvestterm;
	}
	public void setPeriodicinvestterm(String periodicinvestterm) {
		this.periodicinvestterm = periodicinvestterm;
	}
	
	
}

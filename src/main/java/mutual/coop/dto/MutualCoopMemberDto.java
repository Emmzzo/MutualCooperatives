package mutual.coop.dto;
import mutual.domain.MutualCooperative;
import mutual.domain.Users;

public class MutualCoopMemberDto {
	private int mutualMemberId;
	private int memberSize;
	private Users member;
	private MutualCooperative mutualcoop;
	private String action;
	private boolean showcontact;
	private boolean hidecontact;
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
	
	
}

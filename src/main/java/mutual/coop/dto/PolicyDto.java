package mutual.coop.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import mutual.domain.MutualCooperative;
public class PolicyDto implements Serializable {

	private int policyId;
	private String fineCharges,termofcontribution;
	private String interesCharges,fineondelayedcontribution,mincontribution;
	private boolean editable;
	private String policyDescription;
	private MutualCooperative mutualcoop;
	private Date recordedDate;
	private boolean approved,rejected;
	private String recordedBy;
	private String action;
	private int countinfo;
	
	public int getPolicyId() {
		return policyId;
	}
	
	public void setPolicyId(int policyId) {
		this.policyId = policyId;
	}
	
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
	public MutualCooperative getMutualcoop() {
		return mutualcoop;
	}
	public void setMutualcoop(MutualCooperative mutualcoop) {
		this.mutualcoop = mutualcoop;
	}
	public Date getRecordedDate() {
		return recordedDate;
	}
	public void setRecordedDate(Date recordedDate) {
		this.recordedDate = recordedDate;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public boolean isRejected() {
		return rejected;
	}
	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
	public int getCountinfo() {
		return countinfo;
	}
	public void setCountinfo(int countinfo) {
		this.countinfo = countinfo;
	}

	public String getTermofcontribution() {
		return termofcontribution;
	}

	public void setTermofcontribution(String termofcontribution) {
		this.termofcontribution = termofcontribution;
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
}


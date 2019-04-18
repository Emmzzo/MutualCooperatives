package mutual.coop.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import mutual.domain.UserCategory;
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;

	/* this is for userName */

	private String viewId;

	private String viewName;

	private String fullname;
	private String address;
	private String gender;
	private String image;

	private String loginStatus;

	private String status;
	private Date createdDate;
	private UserCategory userCategory;
	private boolean editable;
	private String action;
	private String institution;
	private String phone;
	private String email;
	private String genericStatus;
	private Timestamp upDtTime;
	private String updatedBy;
	private boolean notify;
	private boolean renderBoard,pendingstatus,approvedreq,blockedstatus,rejectedstatus;
	 private int countinfo;
	public int getCountinfo() {
		return countinfo;
	}
	public void setCountinfo(int countinfo) {
		this.countinfo = countinfo;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public UserCategory getUserCategory() {
		return userCategory;
	}
	public void setUserCategory(UserCategory userCategory) {
		this.userCategory = userCategory;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGenericStatus() {
		return genericStatus;
	}
	public void setGenericStatus(String genericStatus) {
		this.genericStatus = genericStatus;
	}
	public Timestamp getUpDtTime() {
		return upDtTime;
	}
	public void setUpDtTime(Timestamp upDtTime) {
		this.upDtTime = upDtTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public boolean isNotify() {
		return notify;
	}
	public void setNotify(boolean notify) {
		this.notify = notify;
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
	
	
}

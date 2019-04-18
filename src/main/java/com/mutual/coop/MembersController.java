package com.mutual.coop;

import java.awt.image.renderable.RenderedImageFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.primefaces.model.UploadedFile;
import mutual.common.DbConstant;
import mutual.common.JSFBoundleProvider;
import mutual.common.JSFMessagers;
import mutual.common.SessionUtils;
import mutual.coop.dto.ContactDto;
import mutual.coop.dto.UserDto;
import mutual.dao.impl.ContactImpl;
import mutual.dao.impl.DistrictImpl;
import mutual.dao.impl.LoginImpl;
import mutual.dao.impl.MutualCoopMembersImpl;
import mutual.dao.impl.MutualCooperativeImpl;
import mutual.dao.impl.ProvinceImpl;
import mutual.dao.impl.UserCategoryImpl;
import mutual.dao.impl.UserImpl;
import mutual.domain.Contact;
import mutual.domain.District;
import mutual.domain.MutualCoopMembers;
import mutual.domain.MutualCooperative;
import mutual.domain.Province;
import mutual.domain.UserCategory;
import mutual.domain.Users;

@ManagedBean
@ViewScoped
public class MembersController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "MembersController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;

	private String imageName;
	private Users users;

	private UserCategory usercat;
	private Users usersSession;
	private int userIdNumber;
	private int provinceId;
	private int districtId;
	private int sectorId;
	private int cellId;
	private int villageId;
	private int categoryId;
	private String password;
	private String confirmPswd;
	private String useremail;
	private UserDto userDto;
	private List<Users> usersDetails = new ArrayList<Users>();
	private List<Users> useravail = new ArrayList<Users>();
	private List<Users> staffList = new ArrayList<Users>();
	private List<UserCategory> catDetails = new ArrayList<UserCategory>();
	private List<UserDto> userDtoDetails = new ArrayList<UserDto>();
	private List<UserDto> userDtosDetails = new ArrayList<UserDto>();
	List<ContactDto> contactDtoDetails = new ArrayList<ContactDto>();
	private List<UserDto> repDtosDetails = new ArrayList<UserDto>();
	private List<UserCategory> userCatDetails = new ArrayList<UserCategory>();
	private List<UserCategory> staffPosition = new ArrayList<UserCategory>();
	private MutualCooperative mutual = new MutualCooperative();
	private MutualCoopMembers mutualMembers = new MutualCoopMembers();
	/* class injection */
	JSFBoundleProvider provider = new JSFBoundleProvider();
	UserImpl usersImpl = new UserImpl();
	ProvinceImpl provImpl = new ProvinceImpl();
	DistrictImpl districtImpl = new DistrictImpl();
	UserCategoryImpl catImpl = new UserCategoryImpl();
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	LoginImpl loginImpl = new LoginImpl();
	MutualCooperativeImpl mutualImpl = new MutualCooperativeImpl();
	MutualCoopMembersImpl mutualMembersImpl = new MutualCoopMembersImpl();
	private List<MutualCoopMembers> mutualMembersList = new ArrayList<MutualCoopMembers>();
	private List<MutualCooperative> mutualCoopPendingRequest = new ArrayList<MutualCooperative>();
	private String choice;
	private Contact contact;
	private ContactDto contactDto;
	private boolean rendered;
	private boolean renderForeignCountry;
	private boolean rendersaveButton;
	private boolean renderprofile;
	private String option;
	private String selection;
	private Date dateofBirth;
	private int age;
	private int days;
	private int count;
	private int contactSize;
	private int repavail;
	private int userCatid;
	private Date to;
	private Date from;
	private boolean renderDataTable;
	private boolean nextButoon;
	private String redirect;
	private String range;
	private String searchKey;
	private boolean renderBoard;
	private boolean renderDatePanel;
	private boolean renderEditedTableByDate;
	private boolean renderEditedTableByBoard;
	private boolean renderBoardOption;
	private boolean renderHeading;
	private boolean renderRepTable;
	private boolean renderRepContactDash;
	private boolean availrepSize;
	private boolean renderContactForm;
	private boolean renderRepContactAvailTable;
	private boolean renderOtherContForm;
	ContactImpl contactImpl = new ContactImpl();

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");

		if (users == null) {
			users = new Users();
		}
		if (usercat == null) {
			usercat = new UserCategory();

		}
		if (userDto == null) {
			userDto = new UserDto();
		}
		if (contact == null) {
			contact = new Contact();
		}
		if (contactDto == null) {
			contactDto = new ContactDto();
		}

		try {
			// List Mutual Rep already Registered and those with registration req pending.
			usersDetails = usersImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "userCategory" },
					new Object[] { ACTIVE, catImpl.getUserCategoryById(MutualRepcat, "userCatid") }, "Users",
					"userId desc");
			showMutualRepAvail(usersDetails);
			userDtosDetails = mutualRepList(usersDetails);

			if (null != usersSession) {
				if (usersSession.getUserCategory().getUserCatid() == admincat) {
					repavail = mutualRepRequestSize();
					renderBoardOption = true;
				}
			} else {
				LOGGER.info("NO USER LOGGED IN!!");
			}

			useravail = usersImpl.getGenericListWithHQLParameter(
					new String[] { "genericStatus", "userCategory", "status" },
					new Object[] { ACTIVE, catImpl.getUserCategoryById(MutualRepcat, "userCatid"), DESACTIVE }, "Users",
					"userId desc");
			repDtosDetails = mutualRepListRequest(useravail);
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public int mutualRepRequestSize() throws Exception {
		usersDetails = usersImpl.getGenericListWithHQLParameter(
				new String[] { "genericStatus", "userCategory", "status" },
				new Object[] { ACTIVE, catImpl.getUserCategoryById(MutualRepcat, "userCatid"), DESACTIVE }, "Users",
				"userId desc");
		return (usersDetails.size());
	}

	public void showMutualRepAvail(List<Users> list) {
		if (list.size() > 0) {
			this.rendered = true;
			this.renderBoard = false;
		} else {
			this.renderBoard = true;
			this.rendered = false;
		}
	}

	public void showMutualRepForm() {
		this.rendered = false;
		this.renderBoard = true;
	}

	public void BackRepForm() {
		this.rendered = true;
		this.renderBoard = false;
	}

	public List<UserDto> mutualRepList(List<Users> list) {

		userDtosDetails = new ArrayList<UserDto>();
		int i = 1;
		for (Users users : list) {
			UserDto usedto = new UserDto();
			usedto.setEditable(false);
			usedto.setUserId(users.getUserId());
			usedto.setCountinfo(i);
			usedto.setFullname(users.getFullname());
			usedto.setEmail(users.getEmail());
			usedto.setPhone(users.getPhone());
			usedto.setAddress(users.getAddress());
			usedto.setGender(users.getGender());
			if (users.getStatus().equalsIgnoreCase(DESACTIVE)) {
				usedto.setPendingstatus(false);
			} else {
				usedto.setPendingstatus(true);
			}
			if (users.getStatus().equalsIgnoreCase(ACTIVE)) {
				usedto.setApprovedreq(false);
			} else {
				usedto.setApprovedreq(true);
			}
			if (users.getStatus().equalsIgnoreCase(Block)) {
				usedto.setBlockedstatus(false);
			} else {
				usedto.setBlockedstatus(true);
			}
			if (users.getStatus().equalsIgnoreCase(REJECTED)) {
				usedto.setRejectedstatus(false);
			} else {
				usedto.setRejectedstatus(true);
			}
			userDtosDetails.add(usedto);
			i++;
		}
		return (userDtosDetails);
	}

	public List<UserDto> mutualRepListRequest(List<Users> list) {

		repDtosDetails = new ArrayList<UserDto>();
		int i = 1;
		for (Users users : list) {
			UserDto usedto = new UserDto();
			usedto.setEditable(false);
			usedto.setUserId(users.getUserId());
			usedto.setCountinfo(i);
			usedto.setFullname(users.getFullname());
			usedto.setEmail(users.getEmail());
			usedto.setPhone(users.getPhone());
			usedto.setAddress(users.getAddress());
			usedto.setGender(users.getGender());
			if (users.getStatus().equalsIgnoreCase(DESACTIVE)) {
				usedto.setPendingstatus(false);
			} else {
				usedto.setPendingstatus(true);
			}
			repDtosDetails.add(usedto);
			i++;
		}
		return (repDtosDetails);
	}

	public String saveUserInfo() throws IOException, NoSuchAlgorithmException {
		try {
			try {
				Users user = new Users();
				user = usersImpl.getModelWithMyHQL(new String[] { "viewId" }, new Object[] { users.getViewId() },
						"from Users");
				usercat = catImpl.getUserCategoryById(guestcat, "userCatid");
				if (null != user) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.viewId"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: User Name already  recorded in the system! ");
					return null;
				}
				if (null == usercat) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.cat"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: category name not found in the system! ");
					return null;
				}

				if (null != users.getEmail())
					user = usersImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { users.getEmail() },
							"from Users");
				if (null != user) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				user = usersImpl.getModelWithMyHQL(new String[] { "phone" }, new Object[] { users.getPhone() },
						"from Users");
				if (null != user) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.phone.number"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: phone number already  recorded in the system! ");
					return null;
				}

			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}

			users.setImage("us.png");
			users.setCreatedBy("Guest");
			users.setCrtdDtTime(timestamp);
			users.setGenericStatus(ACTIVE);
			users.setUpdatedBy("Guest");
			users.setCrtdDtTime(timestamp);
			users.setViewName(loginImpl.criptPassword(users.getViewName()));
			users.setStatus(ACTIVE);
			users.setLoginStatus(OFFLINE);
			users.setUserCategory(usercat);
			usersImpl.saveUsers(users);

			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.user"));
			LOGGER.info(CLASSNAME + ":::User Details is saved");
			clearUserFuileds();
			return "";

		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::User Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	public String handleNotification() {
		if (null != usersSession) {
			if (usersSession.getUserCategory().getUserCatid() == admincat) {
				return "/page1.xhtml?face-redirect=true";
			}else if(usersSession.getUserCategory().getUserCatid() == MutualRepcat) {
			return "/menu/MemberRequestManagement.xhtml?faces-redirect=true";
			}		
	}
		return null;
	}

	public String saveMutualRepAndCoopInfo() throws IOException, NoSuchAlgorithmException {
		try {
			try {
				Users user = new Users();

				usercat = catImpl.getUserCategoryById(MutualRepcat, "userCatid");
				if (null == usercat) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.cat"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: category name not found in the system! ");
					return null;
				}

				if (null != users.getEmail())
					user = usersImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { users.getEmail() },
							"from Users");
				if (null != user) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				user = usersImpl.getModelWithMyHQL(new String[] { "phone" }, new Object[] { users.getPhone() },
						"from Users");
				if (null != user) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.phone.number"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: phone number already  recorded in the system! ");
					return null;
				}

			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
			// SAVING MUTUAL REP INFO
			users.setImage("us.png");
			users.setCreatedBy(usersSession.getViewId());
			users.setCrtdDtTime(timestamp);
			users.setGenericStatus(ACTIVE);
			users.setCrtdDtTime(timestamp);
			users.setStatus(ACTIVE);
			users.setLoginStatus(OFFLINE);
			users.setUserCategory(usercat);
			Random rand = new Random();
			int rand_int1 = rand.nextInt(1000);
			SendSupportEmail email = new SendSupportEmail();
			String pswdgiven = Distributed_Pswd + "" + rand_int1;
			boolean valid = email.sendMailMutualCoopRep(ACCEPTED, users.getFullname(), pswdgiven,
					usersSession.getFullname(), users.getEmail());
			String crpted_pswd = Distributed_Pswd + "" + rand_int1;
			LOGGER.info("PASSWORD TOBE INCRYPTED::" + crpted_pswd);
			users.setViewId(users.getEmail());
			users.setViewName(loginImpl.criptPassword(crpted_pswd));
			usersImpl.saveUsers(users);
			// SAVING MUTUAL COOP INFO
			mutual.setCreatedBy(usersSession.getViewId());
			mutual.setLogo("us.png");
			mutual.setCrtdDtTime(timestamp);
			mutual.setGenericStatus(ACTIVE);
			mutual.setCrtdDtTime(timestamp);
			mutual.setStatus(ACCEPTED);
			mutualImpl.saveMutualCooperative(mutual);
			// SAVING BOTH MUTUAL REP AND MUTUAL COOP IN MUTUALCOOPMEMBERS TABLE
			mutualMembers.setCreatedBy(usersSession.getViewId());
			mutualMembers.setCrtdDtTime(timestamp);
			// status to be updated when request approved
			mutualMembers.setGenericStatus(ACTIVE);
			mutualMembers.setCrtdDtTime(timestamp);
			mutualMembers.setUsermember(users);
			mutualMembers.setMutualcoop(mutual);
			mutualMembers.setMemberSize(incrementCount);
			mutualMembersImpl.saveMutualCoopMembers(mutualMembers);

			if (valid) {
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.mutualcooprep"));
				LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.sendemailfail.form.mutualcooprepinfo"));
				LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
			}
			clearUserFuileds();
			clearMutualCoopMemberFuileds();
			clearMutualCoopFuileds();
			return "";

		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	public String saveMutualRepPendingRequest() {
		try {
			try {
				Users user = new Users();

				usercat = catImpl.getUserCategoryById(MutualRepcat, "userCatid");
				if (null == usercat) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.cat"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: category name not found in the system! ");
					return null;
				}

				if (null != users.getEmail())
					user = usersImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { users.getEmail() },
							"from Users");
				if (null != user) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				user = usersImpl.getModelWithMyHQL(new String[] { "phone" }, new Object[] { users.getPhone() },
						"from Users");
				if (null != user) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.phone.number"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: phone number already  recorded in the system! ");
					return null;
				}

			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
			// SAVING MUTUAL REP INFO
			users.setImage("us.png");
			users.setCreatedBy("Mutual representative");
			users.setCrtdDtTime(timestamp);
			users.setGenericStatus(ACTIVE);
			users.setCrtdDtTime(timestamp);
			// status to be updated when request approved
			users.setStatus(DESACTIVE);
			users.setLoginStatus(OFFLINE);
			users.setUserCategory(usercat);
			usersImpl.saveUsers(users);
			// SAVING MUTUAL COOP INFO
			mutual.setCreatedBy("Mutual representative");
			mutual.setLogo("us.png");
			mutual.setCrtdDtTime(timestamp);
			mutual.setGenericStatus(ACTIVE);
			mutual.setCrtdDtTime(timestamp);
			// status to be updated when request approved
			mutual.setStatus(PENDING);
			mutualImpl.saveMutualCooperative(mutual);
			// SAVING BOTH MUTUAL REP AND MUTUAL COOP IN MUTUALCOOPMEMBERS TABLE
			mutualMembers.setCreatedBy("Mutual representative");
			mutualMembers.setCrtdDtTime(timestamp);
			// status to be updated when request approved
			mutualMembers.setGenericStatus(ACTIVE);
			mutualMembers.setCrtdDtTime(timestamp);
			mutualMembers.setUsermember(users);
			mutualMembers.setMutualcoop(mutual);
			mutualMembers.setMemberSize(incrementCount);
			mutualMembersImpl.saveMutualCoopMembers(mutualMembers);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.mutualcooprepinfo"));
			LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
			clearUserFuileds();
			clearMutualCoopMemberFuileds();
			clearMutualCoopFuileds();
			return "";

		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	public void renderContactTable() {
		this.rendered = true;
		this.renderRepContactDash = false;
	}

	public void showRepresent() {

		this.renderRepTable = true;
		this.renderHeading = true;
		this.renderRepContactDash = false;
	}

	public void clearUserFuileds() {
		users = new Users();
		usersDetails = null;
	}

	public void clearMutualCoopFuileds() {
		mutual = new MutualCooperative();
	}

	public void clearMutualCoopMemberFuileds() {
		mutualMembers = new MutualCoopMembers();
	}

	public String changeOption() {
		if (option.equals(Yes_Option)) {
			rendered = true;
			renderprofile = false;
			/* renderForeignCountry=true; */
			rendersaveButton = true;
			return (option);
		} else {
			rendered = false;
			renderprofile = false;
			rendersaveButton = true;
			return (option);
		}
	}

	public String getMyFormattedDate() {
		HttpSession session = SessionUtils.getSession();
		Users usersSes = new Users();
		// usersSes = (Users) session.getAttribute("userSession");
		// LOGGER.info("USERNAME::::" + usersSes.getDateOfBirth());
		//
		return null;
	}

	public void profilePage(UserDto user) {
		if (redirect.equals(Next_Option)) {
			if (null != user) {
				int userId = user.getUserId();
				HttpSession sessionuser = SessionUtils.getSession();
				sessionuser.setAttribute("userProfile", userId);
				nextButoon = true;
				renderRepContactDash = false;
			}
		} else {
			renderprofile = false;
			nextButoon = false;
		}

	}

	public String nextPage() {
		return "/menu/EditProfile.xhtml?faces-redirect=true";
	}

	public String getContextPath() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return request.getContextPath();
	}

	public void optionCombine() {
		rendered = false;
		renderForeignCountry = false;
		rendersaveButton = false;
		renderprofile = false;
		nextButoon = false;
	}

	public String cancel(UserDto user) {
		user.setEditable(false);
		return null;

	}

	public void manageOption() {

		if (selection.equals(Date_opt)) {
			this.renderDatePanel = true;
			this.renderBoardOption = false;
			this.renderEditedTableByBoard = false;
			LOGGER.info("Option founded:::::::::::" + selection);
		} else {
			this.renderDatePanel = false;
			this.renderBoardOption = true;
			this.renderEditedTableByDate = false;
		}

	}

	public String editAction(UserDto user) {
		user.setEditable(true);
		return null;
	}

	public boolean confirmPswd() {
		boolean valid = false;
		if (password.equalsIgnoreCase(confirmPswd)) {
			valid = true;
			return (valid);
		}
		return (valid);
	}

	public String backPage() {
		if (usersSession.getUserCategory().getUsercategoryName().equals(INSTITUTE_REP)) {
			return "/menu/ViewUsersDetails.xhtml?faces-redirect=true";
		} else {
			return "/menu/ListOfUsers.xhtml?faces-redirect=true";
		}
	}

	public String updateStatus(UserDto user) {
		LOGGER.info("update  saveAction method");
		// get all existing value but set "editable" to false
		try {
			Users us = new Users();
			us = new Users();
			Contact ct = new Contact();
			if (user != null)
				us = usersImpl.gettUserById(user.getUserId(), "userId");
			if (us != null)
				LOGGER.info("here update sart for " + us + " useriD " + us.getStatus());

			if (user.getStatus().equals(ACTIVE)) {
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				us.setStatus(DESACTIVE);
				user.setNotify(false);
			} else {
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				us.setStatus(ACTIVE);
				user.setNotify(false);
			}

			if (null != useremail) {
				ct = contactImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { useremail },
						"from Contact");
				if (null != ct) {
					usersImpl.UpdateUsers(us);
					useravail = usersImpl.getListWithHQL("from Users", 0, endrecord);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.userupdate"));
					LOGGER.info(CLASSNAME + "::Email sent successuful!!");
					this.useremail = null;
					LOGGER.info("EMAIL TO NOTIFY::::::::::::::::::::::::::::::::::::::" + useremail);
					boolean valid = notifyRepresentativeChange(useremail);
					if (valid) {
						LOGGER.info("returing values controller" + valid);
						setValid(true);
						JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.email.notifications"));
						LOGGER.info(CLASSNAME + ":::Contact Details is saved");
					} else {
						JSFMessagers.resetMessages();
						setValid(false);
						JSFMessagers.addErrorMessage(getProvider().getValue("com.notifyError.representative.user"));
						LOGGER.info(CLASSNAME + "::Fail to send Email!!");
						this.useremail = null;
					}
				} else {
					this.useremail = null;
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.notfound.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email not recorded in the system! ");
					return null;
				}
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("error.selected.invalid.email"));
			}
			return null;
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.updateuserError"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public Boolean notifyRepresentativeChange(String email) throws Exception {
		boolean valid = false;
		try {
			SendSupportEmail support = new SendSupportEmail();
			Contact ct = new Contact();

			if (null != email) {
				ct = contactImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { email }, "from Contact");
				if (null != ct) {
					// boolean verifyemail = support.sendMailTestVersion(ct.getUser().getFname(),
					// ct.getUser().getLname(),
					// ct.getEmail());
					// if (verifyemail) {
					// valid = true;
					// }
				} else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.notfound.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email not recorded in the system! ");
					return null;
				}

			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("error.selected.invalid.email"));
			}
		} catch (HibernateException e) {
			LOGGER.info(CLASSNAME + ":::Contact Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
			LOGGER.info(CLASSNAME + "" + e.getMessage());
			e.printStackTrace();
		}
		return (valid);
	}

	public String updateRepStatus(UserDto user) {
		LOGGER.info("update  saveAction method");
		// get all existing value but set "editable" to false
		try {
			Users us = new Users();
			us = new Users();
			if (user != null)
				us = usersImpl.gettUserById(user.getUserId(), "userId");
			if (us != null)
				LOGGER.info("here update sart for " + us + " useriD " + us.getStatus());

			if (user.getStatus().equals(ACTIVE)) {
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				us.setStatus(DESACTIVE);

			} else {
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				us.setStatus(ACTIVE);
			}
			Contact ct = new Contact();

			if (null != useremail) {
				ct = contactImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { useremail },
						"from Contact");
				if (null != ct) {
					usersImpl.UpdateUsers(us);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.userupdate"));
					LOGGER.info("EMAIL TO NOTIFY::::::::::::::::::::::::::::::::::::::" + useremail);
					boolean valid = notifyRepresentativeChange(useremail);
					if (valid) {
						JSFMessagers.resetMessages();
						setValid(true);
						JSFMessagers
								.addErrorMessage(getProvider().getValue("com.server.side.email.notification.status"));
						LOGGER.info(CLASSNAME + "::Email sent successuful!!");
						this.useremail = null;

					} else {
						JSFMessagers.resetMessages();
						setValid(false);
						JSFMessagers.addErrorMessage(getProvider().getValue("com.notifyError.representative.user"));
						LOGGER.info(CLASSNAME + "::Fail to send Email!!");
						this.useremail = null;
					}
				} else {
					this.useremail = null;
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.notfound.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email not recorded in the system! ");
					return null;
				}

			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("error.selected.invalid.email"));
			}
			return "";
			/* return "/menu/ViewUsersList.xhtml?faces-redirect=true"; */

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.updateuserError"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public void updateTable() throws Exception {
		try {

			if (choice.equals(country_rw)
					&& usersSession.getUserCategory().getUsercategoryName().equals(INSTITUTE_REP)) {
				rendered = true;
				renderForeignCountry = true;
				renderBoard = true;
				nextButoon = false;
				LOGGER.info(" REP LIST SIze:::::::" + userCatDetails.size());
			} else if (usersSession.getUserCategory().getUsercategoryName().equals(INSTITUTE_REP)) {
				rendered = false;
				renderForeignCountry = true;
				nextButoon = false;
				renderBoard = true;
				LOGGER.info(" REP LIST SIze:::::::" + userCatDetails.size());
			}

			// CATEGORY ON ADMIN PANEL
			if (choice.equals(country_rw) && usersSession.getUserCategory().getUsercategoryName().equals(SUPER_ADMIN)) {
				rendered = true;
				renderForeignCountry = true;
				renderBoard = false;
				nextButoon = true;
				LOGGER.info("ADMIN LIST SIze:::::::" + catDetails.size());
			} else if (usersSession.getUserCategory().getUsercategoryName().equals(SUPER_ADMIN)) {
				rendered = false;
				renderForeignCountry = true;
				nextButoon = true;
				renderBoard = false;
				LOGGER.info(" ADMIN LIST SIze:::::::" + catDetails.size());
			}
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}

	public void showDataTable() {

		if ((null != to) && (null != from)) {

			renderDataTable = true;
		}
	}

	public void addcontacts() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		users = (Users) session.getAttribute("userinfo");
	}

	public void showDatePanel() {

		if (selection.equals(Date_opt)) {
			renderDatePanel = true;
		}
	}

	public String saveUserNewContact() {

		try {

			try {

				Contact ct = new Contact();
				// LOGGER.info("USER DETAILS:::::::::::" + contact.getEmail() + ":::::::::" +
				// users.getFname() + "::::"
				// + users.getLname());
				if (null != contact.getEmail())
					ct = contactImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { contact.getEmail() },
							"from Contact");
				if (null != ct) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				ct = contactImpl.getModelWithMyHQL(new String[] { "phone" }, new Object[] { contact.getPhone() },
						"from Contact");
				if (null != ct) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.phone.number"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: phone number already  recorded in the system! ");
					return null;
				}

			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
			// :::Method to get user's info through session:::::::::://
			addcontacts();
			// :::End of Method :::::::::://

			/* FormSampleController sample = new FormSampleController(); */
			SendSupportEmail support = new SendSupportEmail();
			if (null != users) {
				contact.setCreatedBy(usersSession.getViewId());
				contact.setCrtdDtTime(timestamp);
				contact.setGenericStatus(ACTIVE);
				contact.setUpDtTime(timestamp);
				contact.setUpdatedBy(usersSession.getViewId());
				// LOGGER.info(users.getUserId() + "" + users.getFname() + ":::------->>>>>>User
				// searched founded");
				contact.setUser(usersImpl.gettUserById(users.getUserId(), "userId"));

				contact.setUpdatedBy(usersSession.getViewId());
				// :::saving contact action:::::::::::://
				contactImpl.saveContact(contact);
				// :::::End of saving:::::::::::::// JSFMessagers.resetMessages();
				// JSFMessagers.resetMessages();
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.contact"));
				// LOGGER.info(CLASSNAME + ":::Contact Details is saved" + contact.getEmail() +
				// ":::::::"
				// + users.getFname() + ":::" + users.getLname());
				// boolean verifyemail = support.sendMailTestVersion(users.getFname(),
				// users.getLname(),
				// contact.getEmail());
				// ::: end sending email action:::::::::::://
				// if (verifyemail) {
				// LOGGER.info("returing values controller" + verifyemail);
				// setValid(true);
				// JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.email.notifications"));
				// LOGGER.info(CLASSNAME + ":::Contact Details is saved");
				// } else {
				// JSFMessagers.resetMessages();
				// setValid(false);
				// JSFMessagers.addErrorMessage(getProvider().getValue("com.notifyError.representative.user"));
				// }
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
			}
			clearContactFuileds();
			return null;
		} catch (HibernateException e) {
			LOGGER.info(CLASSNAME + ":::Contact Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
			LOGGER.info(CLASSNAME + "" + e.getMessage());
			e.printStackTrace();
			return "";
		}

	}

	private void clearContactFuileds() {
		this.useremail = null;
		contact = new Contact();
		userIdNumber = 0;
		// usersDetails=null;
	}

	public String cancelContact(ContactDto contact) {
		contact.setEditable(false);
		// usersImpl.UpdateUsers(user);
		return null;

	}

	public void showRepresentContAvail() {
		this.renderRepContactDash = false;
		this.renderRepContactAvailTable = true;
	}

	public String changePage() {

		if (usersSession.getUserCategory().getUsercategoryName().equals(INSTITUTE_REP)) {
			return "/menu/ListOfUsers.xhtml?faces-redirect=true";
		}
		return null;
	}

	public void addOthercontacts() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		contactDto = (ContactDto) session.getAttribute("continfo");
	}

	public void otherUserContact(ContactDto cont) {
		HttpSession sessionuser = SessionUtils.getSession();

		if (null != cont) {
			/*
			 * renderContactForm = true;
			 * 
			 * rendered = false; renderForeignCountry = false;
			 */

			// Session creation to get user info from dataTable row
			sessionuser.setAttribute("continfo", cont);
			addOthercontacts();
			this.renderOtherContForm = true;
			this.renderRepContactAvailTable = false;

		}

	}

	@SuppressWarnings("unchecked")
	public String saveAction(UserDto user) throws Exception {
		LOGGER.info("update  saveAction method");
		if (user != null) {
			Users us = new Users();
			us = new Users();
			us = usersImpl.gettUserById(user.getUserId(), "userId");
			LOGGER.info("here update sart for " + us + " useriD " + us.getUserId());
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + us + " useriD " + us.getUserId());

			if (null != us) {
				user.setEditable(false);
				us.setFullname(user.getFullname());
				us.setAddress(user.getAddress());
				us.setPhone(user.getPhone());
				us.setEmail(user.getEmail());
				us.setGender(user.getGender());
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				usersImpl.UpdateUsers(us);
				userDtosDetails = new ArrayList<UserDto>();
				usersDetails = usersImpl.getGenericListWithHQLParameter(
						new String[] { "genericStatus", "userCategory" },
						new Object[] { ACTIVE, catImpl.getUserCategoryById(MutualRepcat, "userCatid") }, "Users",
						"userId desc");
				userDtosDetails = mutualRepList(usersDetails);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("info.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroinfo.changed.message"));
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public String unblockAction(UserDto user) throws Exception {
		LOGGER.info("update  saveAction method");
		if (user != null) {
			Users us = new Users();
			us = new Users();
			us = usersImpl.gettUserById(user.getUserId(), "userId");
			LOGGER.info("here update sart for " + us + " useriD " + us.getUserId());
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + us + " useriD " + us.getUserId());

			if (null != us) {
				user.setEditable(false);
				us.setStatus(ACTIVE);
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				usersImpl.UpdateUsers(us);
				userDtosDetails = new ArrayList<UserDto>();
				usersDetails = usersImpl.getGenericListWithHQLParameter(
						new String[] { "genericStatus", "userCategory" },
						new Object[] { ACTIVE, catImpl.getUserCategoryById(MutualRepcat, "userCatid") }, "Users",
						"userId desc");
				userDtosDetails = mutualRepList(usersDetails);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("activate.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("activate.changed.message.error"));
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public String approveAction(UserDto user) throws NoSuchAlgorithmException {
		try {
			LOGGER.info("update  saveAction method");
			if (user != null) {
				Users us = new Users();
				us = new Users();
				us = usersImpl.gettUserById(user.getUserId(), "userId");
				LOGGER.info("here update sart for " + us + " useriD " + us.getUserId());
				LOGGER.info("++++++++++++++++++++++++++here update sart for " + us + " useriD " + us.getUserId());

				if (null != us) {
					user.setEditable(false);
					us.setStatus(ACTIVE);
					us.setUpdatedBy(usersSession.getViewId());
					us.setUpDtTime(timestamp);
					Random rand = new Random();
					int rand_int1 = rand.nextInt(1000);
					SendSupportEmail email = new SendSupportEmail();
					String pswdgiven = Distributed_Pswd + "" + rand_int1;
					boolean valid = email.sendMailMutualCoopRep(ACCEPTED, user.getFullname(), pswdgiven,
							usersSession.getFullname(), user.getEmail());
					String crpted_pswd = Distributed_Pswd + "" + rand_int1;
					LOGGER.info("PASSWORD TOBE INCRYPTED::" + crpted_pswd);
					us.setViewId(user.getEmail());
					us.setViewName(loginImpl.criptPassword(crpted_pswd));
					usersImpl.UpdateUsers(us);
					repDtosDetails = new ArrayList<UserDto>();
					useravail = usersImpl.getGenericListWithHQLParameter(
							new String[] { "genericStatus", "userCategory", "status" },
							new Object[] { ACTIVE, catImpl.getUserCategoryById(MutualRepcat, "userCatid"), DESACTIVE },
							"Users", "userId desc");
					repDtosDetails = mutualRepListRequest(useravail);
					if (usersSession.getUserCategory().getUserCatid() == admincat) {
						repavail = mutualRepRequestSize();
					}
					if (valid) {
						JSFMessagers.resetMessages();
						setValid(true);
						JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.mutualcooprep"));
						LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
					} else {
						JSFMessagers.resetMessages();
						setValid(false);
						JSFMessagers
								.addErrorMessage(getProvider().getValue("com.sendemailfail.form.mutualcooprepinfo"));
						LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
					}
				}
			}
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public String rejectAction(UserDto user) throws Exception {
		LOGGER.info("update  saveAction method");
		if (user != null) {
			Users us = new Users();
			us = new Users();
			us = usersImpl.gettUserById(user.getUserId(), "userId");
			LOGGER.info("here update sart for " + us + " useriD " + us.getUserId());
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + us + " useriD " + us.getUserId());

			if (null != us) {
				user.setEditable(false);
				us.setStatus(REJECTED);
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				usersImpl.UpdateUsers(us);
				repDtosDetails = new ArrayList<UserDto>();
				useravail = usersImpl.getGenericListWithHQLParameter(
						new String[] { "genericStatus", "userCategory", "status" },
						new Object[] { ACTIVE, catImpl.getUserCategoryById(MutualRepcat, "userCatid"), DESACTIVE },
						"Users", "userId desc");
				repDtosDetails = mutualRepListRequest(useravail);
				if (usersSession.getUserCategory().getUserCatid() == admincat) {
					repavail = mutualRepRequestSize();
				}
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("reject.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroreject.changed.message"));
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public String blockAction(UserDto user) throws Exception {
		LOGGER.info("update  saveAction method");
		if (user != null) {
			Users us = new Users();
			us = new Users();
			us = usersImpl.gettUserById(user.getUserId(), "userId");
			LOGGER.info("here update sart for " + us + " useriD " + us.getUserId());
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + us + " useriD " + us.getUserId());

			if (null != us) {
				user.setEditable(false);
				us.setStatus(Block);
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				usersImpl.UpdateUsers(us);
				userDtosDetails = new ArrayList<UserDto>();
				usersDetails = usersImpl.getGenericListWithHQLParameter(
						new String[] { "genericStatus", "userCategory" },
						new Object[] { ACTIVE, catImpl.getUserCategoryById(MutualRepcat, "userCatid") }, "Users",
						"userId desc");
				userDtosDetails = mutualRepList(usersDetails);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("block.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroblock.changed.message"));
		}

		return null;
	}

	public void editUserContact(Users user) {
		HttpSession sessionuser = SessionUtils.getSession();

		if (null != user)
			renderContactForm = true;
		rendered = false;
		// Session creation to get user info from dataTable row
		sessionuser.setAttribute("userinfo", user);
		// LOGGER.info("Info Founded are userid:>>>>>>>>>>>>>>>>>>>>>>fname:" +
		// user.getFname());
		addcontacts();
	}

	public String editContactAction(ContactDto contact) {

		contact.setEditable(true);
		// usersImpl.UpdateUsers(user);
		return null;
	}

	public String saveContact() throws Exception {
		try {

			try {

				Contact ct = new Contact();
				if (null != contact.getEmail())
					ct = contactImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { contact.getEmail() },
							"from Contact");
				if (null != ct) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				ct = contactImpl.getModelWithMyHQL(new String[] { "phone" }, new Object[] { contact.getPhone() },
						"from Contact");
				if (null != ct) {

					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.phone.number"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: phone number already  recorded in the system! ");
					return null;
				}

			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
			// :::Method to get user's info through session:::::::::://
			addOthercontacts();
			// :::End of Method :::::::::://

			/* FormSampleController sample = new FormSampleController(); */
			SendSupportEmail support = new SendSupportEmail();

			if (null != contactDto) {
				users = contactDto.getUser();

				contact.setCreatedBy(usersSession.getViewId());
				contact.setCrtdDtTime(timestamp);
				contact.setGenericStatus(ACTIVE);
				contact.setUpDtTime(timestamp);
				contact.setCreatedBy(usersSession.getViewId());
				contact.setCrtdDtTime(timestamp);
				contact.setGenericStatus(ACTIVE);
				contact.setUpDtTime(timestamp);
				// LOGGER.info(users.getUserId() + "" + users.getFname() + ":::------->>>>>>User
				// searched founded");
				contact.setUser(usersImpl.gettUserById(users.getUserId(), "userId"));
				// :::sending email action:::::::::::://
				contact.setUpdatedBy(usersSession.getViewId());
				// :::saving contact action:::::::::::://
				contactImpl.saveContact(contact);
				// :::::End of saving::::::::::::://
				JSFMessagers.resetMessages();
				setValid(true); //
				JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.contact"));
				// LOGGER.info(CLASSNAME + ":::Other Contact:::::" + users.getFname() + "::::" +
				// users.getLname() + "::"
				// + contact.getEmail());

				// boolean verifyemail = support.sendMailTestVersion(users.getFname(),
				// users.getLname(),
				// contact.getEmail());
				// // ::: end sending email action:::::::::::://
				// if (verifyemail) {
				// LOGGER.info("returing values controller" + verifyemail);
				// setValid(true);
				// JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.email.notifications"));
				// LOGGER.info(CLASSNAME + ":::Contact Details is saved");
				// } else {
				// JSFMessagers.resetMessages();
				// setValid(false);
				// JSFMessagers.addErrorMessage(getProvider().getValue("com.notifyError.representative.user"));
				// }
			} else {

				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
			}
			clearContactFuileds();
			return null;

		} catch (HibernateException e) {
			LOGGER.info(CLASSNAME + ":::Contact Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
			LOGGER.info(CLASSNAME + "" + e.getMessage());
			e.printStackTrace();
			return "";
		}

	}

	public String getCLASSNAME() {
		return CLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		CLASSNAME = cLASSNAME;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Users getUsersSession() {
		return usersSession;
	}

	public void setUsersSession(Users usersSession) {
		this.usersSession = usersSession;
	}

	public int getUserIdNumber() {
		return userIdNumber;
	}

	public void setUserIdNumber(int userIdNumber) {
		this.userIdNumber = userIdNumber;
	}

	public List<Users> getUsersDetails() {
		return usersDetails;
	}

	public void setUsersDetails(List<Users> usersDetails) {
		this.usersDetails = usersDetails;
	}

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}

	public UserImpl getUsersImpl() {
		return usersImpl;
	}

	public void setUsersImpl(UserImpl usersImpl) {
		this.usersImpl = usersImpl;
	}

	public ProvinceImpl getProvImpl() {
		return provImpl;
	}

	public void setProvImpl(ProvinceImpl provImpl) {
		this.provImpl = provImpl;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public DistrictImpl getDistrictImpl() {
		return districtImpl;
	}

	public void setDistrictImpl(DistrictImpl districtImpl) {
		this.districtImpl = districtImpl;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public int getSectorId() {
		return sectorId;
	}

	public void setSectorId(int sectorId) {
		this.sectorId = sectorId;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public int getVillageId() {
		return villageId;
	}

	public void setVillageId(int villageId) {
		this.villageId = villageId;
	}

	public UserCategory getUsercat() {
		return usercat;
	}

	public void setUsercat(UserCategory usercat) {
		this.usercat = usercat;
	}

	public List<UserCategory> getCatDetails() {
		return catDetails;
	}

	public void setCatDetails(List<UserCategory> catDetails) {
		this.catDetails = catDetails;
	}

	public UserCategoryImpl getCatImpl() {
		return catImpl;
	}

	public void setCatImpl(UserCategoryImpl catImpl) {
		this.catImpl = catImpl;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPswd() {
		return confirmPswd;
	}

	public void setConfirmPswd(String confirmPswd) {
		this.confirmPswd = confirmPswd;
	}

	public List<UserDto> getUserDtoDetails() {
		return userDtoDetails;
	}

	public void setUserDtoDetails(List<UserDto> userDtoDetails) {
		this.userDtoDetails = userDtoDetails;
	}

	public List<UserDto> getUserDtosDetails() {
		return userDtosDetails;
	}

	public void setUserDtosDetails(List<UserDto> userDtosDetails) {
		this.userDtosDetails = userDtosDetails;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public LoginImpl getLoginImpl() {
		return loginImpl;
	}

	public void setLoginImpl(LoginImpl loginImpl) {
		this.loginImpl = loginImpl;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public boolean isRenderForeignCountry() {
		return renderForeignCountry;
	}

	public void setRenderForeignCountry(boolean renderForeignCountry) {
		this.renderForeignCountry = renderForeignCountry;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(Date dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public boolean isRenderDataTable() {
		return renderDataTable;
	}

	public void setRenderDataTable(boolean renderDataTable) {
		this.renderDataTable = renderDataTable;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public boolean isRendersaveButton() {
		return rendersaveButton;
	}

	public void setRendersaveButton(boolean rendersaveButton) {
		this.rendersaveButton = rendersaveButton;
	}

	public boolean isRenderprofile() {
		return renderprofile;
	}

	public void setRenderprofile(boolean renderprofile) {
		this.renderprofile = renderprofile;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public boolean isNextButoon() {
		return nextButoon;
	}

	public void setNextButoon(boolean nextButoon) {
		this.nextButoon = nextButoon;
	}

	public boolean isRenderBoard() {
		return renderBoard;
	}

	public void setRenderBoard(boolean renderBoard) {
		this.renderBoard = renderBoard;
	}

	public List<UserDto> getRepDtosDetails() {
		return repDtosDetails;
	}

	public void setRepDtosDetails(List<UserDto> repDtosDetails) {
		this.repDtosDetails = repDtosDetails;
	}

	public boolean isRenderEditedTableByDate() {
		return renderEditedTableByDate;
	}

	public void setRenderEditedTableByDate(boolean renderEditedTableByDate) {
		this.renderEditedTableByDate = renderEditedTableByDate;
	}

	public boolean isRenderEditedTableByBoard() {
		return renderEditedTableByBoard;
	}

	public void setRenderEditedTableByBoard(boolean renderEditedTableByBoard) {
		this.renderEditedTableByBoard = renderEditedTableByBoard;
	}

	public boolean isRenderDatePanel() {
		return renderDatePanel;
	}

	public void setRenderDatePanel(boolean renderDatePanel) {
		this.renderDatePanel = renderDatePanel;
	}

	public boolean isRenderBoardOption() {
		return renderBoardOption;
	}

	public void setRenderBoardOption(boolean renderBoardOption) {
		this.renderBoardOption = renderBoardOption;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public boolean isRenderHeading() {
		return renderHeading;
	}

	public void setRenderHeading(boolean renderHeading) {
		this.renderHeading = renderHeading;
	}

	public boolean isRenderRepTable() {
		return renderRepTable;
	}

	public void setRenderRepTable(boolean renderRepTable) {
		this.renderRepTable = renderRepTable;
	}

	public boolean isRenderRepContactDash() {
		return renderRepContactDash;
	}

	public void setRenderRepContactDash(boolean renderRepContactDash) {
		this.renderRepContactDash = renderRepContactDash;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isAvailrepSize() {
		return availrepSize;
	}

	public void setAvailrepSize(boolean availrepSize) {
		this.availrepSize = availrepSize;
	}

	public int getContactSize() {
		return contactSize;
	}

	public void setContactSize(int contactSize) {
		this.contactSize = contactSize;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public boolean isRenderContactForm() {
		return renderContactForm;
	}

	public void setRenderContactForm(boolean renderContactForm) {
		this.renderContactForm = renderContactForm;
	}

	public ContactImpl getContactImpl() {
		return contactImpl;
	}

	public void setContactImpl(ContactImpl contactImpl) {
		this.contactImpl = contactImpl;
	}

	public List<ContactDto> getContactDtoDetails() {
		return contactDtoDetails;
	}

	public void setContactDtoDetails(List<ContactDto> contactDtoDetails) {
		this.contactDtoDetails = contactDtoDetails;
	}

	public int getRepavail() {
		return repavail;
	}

	public void setRepavail(int repavail) {
		this.repavail = repavail;
	}

	public boolean isRenderRepContactAvailTable() {
		return renderRepContactAvailTable;
	}

	public void setRenderRepContactAvailTable(boolean renderRepContactAvailTable) {
		this.renderRepContactAvailTable = renderRepContactAvailTable;
	}

	public ContactDto getContactDto() {
		return contactDto;
	}

	public void setContactDto(ContactDto contactDto) {
		this.contactDto = contactDto;
	}

	public boolean isRenderOtherContForm() {
		return renderOtherContForm;
	}

	public void setRenderOtherContForm(boolean renderOtherContForm) {
		this.renderOtherContForm = renderOtherContForm;
	}

	public List<UserCategory> getUserCatDetails() {
		return userCatDetails;
	}

	public void setUserCatDetails(List<UserCategory> userCatDetails) {
		this.userCatDetails = userCatDetails;
	}

	public List<Users> getUseravail() {
		return useravail;
	}

	public void setUseravail(List<Users> useravail) {
		this.useravail = useravail;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public int getUserCatid() {
		return userCatid;
	}

	public void setUserCatid(int userCatid) {
		this.userCatid = userCatid;
	}

	public List<UserCategory> getStaffPosition() {
		return staffPosition;
	}

	public void setStaffPosition(List<UserCategory> staffPosition) {
		this.staffPosition = staffPosition;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public List<Users> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<Users> staffList) {
		this.staffList = staffList;
	}

	public MutualCooperative getMutual() {
		return mutual;
	}

	public void setMutual(MutualCooperative mutual) {
		this.mutual = mutual;
	}

	public MutualCooperativeImpl getMutualImpl() {
		return mutualImpl;
	}

	public void setMutualImpl(MutualCooperativeImpl mutualImpl) {
		this.mutualImpl = mutualImpl;
	}

	public List<MutualCooperative> getMutualCoopPendingRequest() {
		return mutualCoopPendingRequest;
	}

	public void setMutualCoopPendingRequest(List<MutualCooperative> mutualCoopPendingRequest) {
		this.mutualCoopPendingRequest = mutualCoopPendingRequest;
	}

	public MutualCoopMembers getMutualMembers() {
		return mutualMembers;
	}

	public void setMutualMembers(MutualCoopMembers mutualMembers) {
		this.mutualMembers = mutualMembers;
	}

	public MutualCoopMembersImpl getMutualMembersImpl() {
		return mutualMembersImpl;
	}

	public void setMutualMembersImpl(MutualCoopMembersImpl mutualMembersImpl) {
		this.mutualMembersImpl = mutualMembersImpl;
	}

	public List<MutualCoopMembers> getMutualMembersList() {
		return mutualMembersList;
	}

	public void setMutualMembersList(List<MutualCoopMembers> mutualMembersList) {
		this.mutualMembersList = mutualMembersList;
	}

}

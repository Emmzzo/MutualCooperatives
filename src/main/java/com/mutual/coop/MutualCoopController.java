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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import mutual.common.Formating;
import mutual.common.JSFBoundleProvider;
import mutual.common.JSFMessagers;
import mutual.common.SessionUtils;
import mutual.coop.dto.ContactDto;
import mutual.coop.dto.FundsDto;
import mutual.coop.dto.LoanDto;
import mutual.coop.dto.MemberRequestDto;
import mutual.coop.dto.MutualCoopMemberDto;
import mutual.coop.dto.PolicyDto;
import mutual.coop.dto.UserDto;
import mutual.dao.impl.ContactImpl;
import mutual.dao.impl.DistrictImpl;
import mutual.dao.impl.FinesImpl;
import mutual.dao.impl.FundsImpl;
import mutual.dao.impl.InterestChargesImpl;
import mutual.dao.impl.LoanRequestImpl;
import mutual.dao.impl.LoginImpl;
import mutual.dao.impl.MemberRequestImpl;
import mutual.dao.impl.MutualCoopMembersImpl;
import mutual.dao.impl.MutualCoopPolicyImpl;
import mutual.dao.impl.MutualCooperativeImpl;
import mutual.dao.impl.ProvinceImpl;
import mutual.dao.impl.UserCategoryImpl;
import mutual.dao.impl.UserImpl;
import mutual.domain.Contact;
import mutual.domain.District;
import mutual.domain.Fines;
import mutual.domain.Funds;
import mutual.domain.InterestCharges;
import mutual.domain.LoanRequest;
import mutual.domain.MemberRequest;
import mutual.domain.MutualCoopMembers;
import mutual.domain.MutualCoopPolicy;
import mutual.domain.MutualCooperative;
import mutual.domain.Province;
import mutual.domain.UserCategory;
import mutual.domain.Users;

@ManagedBean
@ViewScoped
public class MutualCoopController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "MutualCoopController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;

	private String imageName;
	private Users users;

	private UserCategory usercat;
	private Users usersSession;
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
	private List<MutualCoopMemberDto> mutualMembersListDto = new ArrayList<MutualCoopMemberDto>();
	private List<MutualCoopMemberDto> mutualCoopMembersListDto = new ArrayList<MutualCoopMemberDto>();
	private MutualCoopMemberDto coopMemberDto = new MutualCoopMemberDto();
	private List<MutualCooperative> mutualCoopPendingRequest = new ArrayList<MutualCooperative>();
	private MemberRequestImpl memberReqImpl = new MemberRequestImpl();
	private MutualCoopPolicy policy = new MutualCoopPolicy();
	private List<MutualCoopPolicy> mutualpolicy, listofAvailableCoop = new ArrayList<MutualCoopPolicy>();
	MutualCoopPolicyImpl policyImpl = new MutualCoopPolicyImpl();
	private List<LoanRequest> loanRequestList = new ArrayList<LoanRequest>();
	private MutualCoopPolicy newpolicy = new MutualCoopPolicy();
	private List<PolicyDto> policyDtoList = new ArrayList<PolicyDto>();
	private PolicyDto policyDto = new PolicyDto();
	private Funds memberFunds = new Funds();
	private Fines finees= new Fines();
	FinesImpl finesImpl= new FinesImpl();
	FundsImpl fundsImpl = new FundsImpl();
	private List<MemberRequestDto> requestDtoList = new ArrayList<MemberRequestDto>();
	private List<MemberRequest> requestList = new ArrayList<MemberRequest>();
	private List<FundsDto> fundDtoList = new ArrayList<FundsDto>();
	private List<Funds> fundsList = new ArrayList<Funds>();
	private List<LoanDto> loanDtoList = new ArrayList<LoanDto>();
	private LoanRequest request;
	private InterestCharges charges;
	private InterestChargesImpl chargeImpl = new InterestChargesImpl();
	private LoanRequestImpl requestImpl = new LoanRequestImpl();
	private String choice;
	private boolean rendered, renderBoard, rendersubmit, memberfunds, fundsform, renderDataTable, renderprofile,
			rendersaveButton, renderCount, renderForeignCountry, fundsDetails, renderdialog;
	private String option;
	private String selection;
	private String fines, interest, amount, mincontribution, fineoncontribution;
	private Date recordeddate;
	private int count;

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
		if (null == memberFunds) {
			memberFunds = new Funds();
		}
		if (null == coopMemberDto) {
			coopMemberDto = new MutualCoopMemberDto();
		}

		if (null == charges) {
			charges = new InterestCharges();
		}
		if(null==finees) {
			finees= new Fines();
		}
		try {

			if (usersSession.getUserCategory().getUserCatid() == MutualRepcat) {
				mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
						new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
						"from MutualCoopMembers");
				if (null == mutualMembers) {
					LOGGER.info("NOTHING FOUND");
				}

				policy = policyImpl.getModelWithMyHQL(new String[] { "genericStatus", "mutualcoop" },
						new Object[] { ACTIVE, mutualMembers.getMutualcoop() }, "from MutualCoopPolicy");
				if (null == policy) {
					LOGGER.info("NO POLICY FOUND");
					rendersaveButton = true;
					renderForeignCountry = true;
				} else {
					this.rendered = true;
					renderForeignCountry = false;
					renderDataTable = true;
				}

				mutualpolicy = policyImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "mutualcoop" },
						new Object[] { ACTIVE, mutualImpl.getMutualCooperativeById(
								mutualMembers.getMutualcoop().getMutualCoopId(), "mutualCoopId") },
						"MutualCoopPolicy", "policyId desc");

				policyDtoList = mutualPolicyList(mutualpolicy);
				rendersubmit = true;

				requestList = memberReqImpl.getGenericListWithHQLParameter(
						new String[] { "requestStatus", "mutualcoop" },
						new Object[] { RequestStatus, mutualMembers.getMutualcoop() }, "MemberRequest",
						"requestDate asc");

				count = requestList.size();
				if (count > 0) {
					renderCount = true;
				}
				requestDtoList = mutualCoopFollowers(requestList);

				mutualCoopMembersListDto = mutualCoopMembersDetails();
				this.memberfunds = true;

				loanDtoList = loanRequestDetails();

			} else {
				mutualCoopPendingRequest = mutualCoopList();
				if (mutualCoopPendingRequest.size() > 0) {
					this.renderForeignCountry = true;
				}
			}

			//
			// listofAvailableCoop=policyImpl.getGenericListWithHQLParameter(new String[] {
			// "genericStatus"},
			// new Object[] { ACTIVE, mutualImpl.getMutualCooperativeById(
			// mutualMembers.getMutualcoop().getMutualCoopId(), "mutualCoopId") },
			// "MutualCoopPolicy", "policyId desc");

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public List<LoanDto> loanRequestDetails() {

		try {
			if (usersSession.getUserCategory().getUserCatid() == MutualRepcat) {

				MutualCoopMembers members = new MutualCoopMembers();
				members = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
						new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
						"from MutualCoopMembers");
				if (null != members) {
					loanRequestList = requestImpl.getGenericListWithHQLParameter(
							new String[] { "genericStatus", "mutualcoop" },
							new Object[] { ACTIVE, members.getMutualcoop() }, "LoanRequest", "requestDate desc");

					loanDtoList = new ArrayList<LoanDto>();
					DecimalFormat fmt = new DecimalFormat("###,###.##");

					for (LoanRequest loan : loanRequestList) {
						LoanDto request = new LoanDto();
						request.setEditable(false);
						request.setRequestId(loan.getRequestId());
						request.setMutualcoop(loan.getMutualcoop());
						request.setUsermember(loan.getUsermember());
						/* request.setAmount(loan.getAmount()); */
						request.setFormatBalance(fmt.format(loan.getAmount()));
						request.setApprovedDate(loan.getApprovedDate());
						request.setRequestDate(loan.getRequestDate());
						request.setGeneriStatus(loan.getGenericStatus());
						request.setRecordedBy(loan.getUpdatedBy());
						request.setStatus(loan.getStatus());

						if (loan.getStatus().equalsIgnoreCase(LoanStatus)) {
							request.setEditchanges(false);
						} else {
							request.setEditchanges(true);
						}
						if (loan.getStatus().equalsIgnoreCase(REJECTED)) {
							request.setLoanrequest(false);
						} else {
							request.setLoanrequest(true);
						}
						if (loan.getStatus().equalsIgnoreCase(ACCEPTED)) {
							request.setAccept(false);
						} else {
							request.setAccept(true);
						}
						if (loan.getStatus().equalsIgnoreCase(LoanPayStatus)) {
							request.setApproval(false);
						} else {
							request.setApproval(true);
						}

						loanDtoList.add(request);
					}

				}
			}

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
		return (loanDtoList);
	}

	public List<PolicyDto> mutualPolicyList(List<MutualCoopPolicy> list) {

		policyDtoList = new ArrayList<PolicyDto>();
		int i = 1;
		for (MutualCoopPolicy p : list) {
			PolicyDto pdto = new PolicyDto();
			pdto.setEditable(false);
			pdto.setPolicyId(p.getPolicyId());
			pdto.setInteresCharges(String.valueOf(p.getInteresCharges()));
			pdto.setFineCharges(String.valueOf(p.getFineCharges()));
			pdto.setFineondelayedcontribution(String.valueOf(p.getFineondelay()));
			pdto.setMincontribution(String.valueOf(p.getMinContribution()));
			pdto.setTermofcontribution(String.valueOf(p.getPeriodicinvestterm()));
			pdto.setPolicyDescription(p.getPolicyDescription());
			pdto.setRecordedDate(p.getCrtdDtTime());
			pdto.setRecordedBy(p.getCreatedBy());
			pdto.setCountinfo(i);
			if (p.getGenericStatus().equalsIgnoreCase(ACTIVE)) {
				pdto.setApproved(false);
			} else {
				pdto.setApproved(true);
			}

			if (p.getGenericStatus().equalsIgnoreCase(DESACTIVE)) {
				pdto.setRejected(false);
			} else {
				pdto.setRejected(true);
			}

			policyDtoList.add(pdto);
			i++;
		}
		return (policyDtoList);
	}

	public void doAction(LoanDto loan) {
		if (null != loan) {
			LOGGER.info("::::LOAN INFO::: " + loan);
			HttpSession sessionmember = SessionUtils.getSession();
			sessionmember.setAttribute("loaninfo", loan);
			this.memberfunds = false;
			this.renderdialog = true;
		}
	}

	public void close() {

		this.memberfunds = true;
		this.renderdialog = false;
	}

	public List<MemberRequestDto> mutualCoopFollowers(List<MemberRequest> list) {

		requestDtoList = new ArrayList<MemberRequestDto>();
		int i = 1;
		for (MemberRequest req : list) {
			MemberRequestDto reqDto = new MemberRequestDto();
			reqDto.setRequestId(req.getRequestId());
			reqDto.setMember(req.getMember());
			reqDto.setRequestDate(req.getRequestDate());
			reqDto.setEditable(false);
			reqDto.setCountinfo(i);
			reqDto.setRequestStatus(req.getRequestStatus());
			reqDto.setMutualcoop(req.getMutualcoop());
			if (req.getRequestStatus().equalsIgnoreCase(RequestStatus)) {
				reqDto.setPendingstatus(false);
			} else {
				reqDto.setPendingstatus(true);
			}

			requestDtoList.add(reqDto);
			i++;
		}
		return (requestDtoList);
	}

	public String addPolicy() {
		HttpSession sessionpolicy = SessionUtils.getSession();
		if (null != mutualMembers) {
			sessionpolicy.setAttribute("mutual", mutualMembers.getMutualcoop());
			return "/menu/PolicyManagement.xhtml?faces-redirect=true";
		}

		return null;

	}

	public String editPolicy() throws Exception {
		HttpSession sessionpolicy = SessionUtils.getSession();
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		if (null == mutualMembers) {
			LOGGER.info("NOTHING FOUND");
		}
		policy = policyImpl.getModelWithMyHQL(new String[] { "genericStatus", "mutualcoop" },
				new Object[] { ACTIVE, mutualMembers.getMutualcoop() }, "from MutualCoopPolicy");
		if (null == policy) {
			LOGGER.info("NO POLICY FOUND");
			this.rendered = false;
			renderForeignCountry = true;
			renderDataTable = false;
		} else {
			LOGGER.info("POLICY FOUND");
			// rendersaveButton = true;
			renderForeignCountry = true;
			renderDataTable = false;
			renderBoard = true;
			rendersubmit = false;
			sessionpolicy.setAttribute("policy", policy);
		}
		return null;
	}

	public void newPolicy() {
		try {
			try {
				if (null != newpolicy.getPolicyDescription() && null != recordeddate && Double.parseDouble(fines) > 0
						&& Double.parseDouble(interest) > 0 && Double.parseDouble(fineoncontribution) > 0
						&& Double.parseDouble(mincontribution) > 0) {
					LOGGER.info(CLASSNAME + "INFORMATION VALID!");
				} else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error.validdata"));
				}

			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
			}
			HttpSession session = SessionUtils.getSession();
			policy = (MutualCoopPolicy) session.getAttribute("policy");
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			fmt.format(recordeddate);
			if (null != policy) {

				policy.setGenericStatus(DESACTIVE);
				policyImpl.UpdateMutualCoopPolicy(policy);

				newpolicy.setCreatedBy(usersSession.getFullname());
				newpolicy.setCrtdDtTime(new java.sql.Timestamp(recordeddate.getTime()));
				newpolicy.setGenericStatus(ACTIVE);
				newpolicy.setMutualcoop(policy.getMutualcoop());
				newpolicy.setInteresCharges(Double.parseDouble(interest));
				newpolicy.setFineCharges(Double.parseDouble(fines));
				newpolicy.setFineondelay(Double.parseDouble(fineoncontribution));
				newpolicy.setMinContribution(Double.parseDouble(mincontribution));
				policyImpl.saveMutualCoopPolicy(newpolicy);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.policy"));
				LOGGER.info(CLASSNAME + ":::User Details is saved");
				clearUserFuileds();
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			}
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::User Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public String savePolicy() {
		try {
			try {
				if (null != newpolicy.getPolicyDescription() && null != recordeddate && Double.parseDouble(fines) > 0
						&& Double.parseDouble(interest) > 0) {
					LOGGER.info(CLASSNAME + "INFORMATION VALID!");
				} else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error.validdata"));
				}

			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
			HttpSession session = SessionUtils.getSession();
			mutual = (MutualCooperative) session.getAttribute("mutual");
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			fmt.format(recordeddate);
			if (null != mutual) {
				newpolicy.setCreatedBy(usersSession.getFullname());
				newpolicy.setCrtdDtTime(new java.sql.Timestamp(recordeddate.getTime()));
				newpolicy.setGenericStatus(ACTIVE);
				newpolicy.setMutualcoop(mutual);
				newpolicy.setInteresCharges(Double.parseDouble(interest));
				newpolicy.setFineCharges(Double.parseDouble(fines));
				policyImpl.saveMutualCoopPolicy(newpolicy);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.policy"));
				LOGGER.info(CLASSNAME + ":::User Details is saved");
				clearUserFuileds();
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			}

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

	@SuppressWarnings("static-access")
	public void saveFunds() throws Exception {
		try {
			int days = 0;
			try {
				if (Double.parseDouble(amount) > 0) {
					LOGGER.info(CLASSNAME + "INFORMATION VALID!");
				} else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error.validdata"));
				}

			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
			}
			HttpSession session = SessionUtils.getSession();
			coopMemberDto = (MutualCoopMemberDto) session.getAttribute("memberfunds");

			if (null != coopMemberDto) {

				Funds funds = new Funds();
				funds = fundsImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember", "mutualcoop" },
						new Object[] { ACTIVE, coopMemberDto.getMember(), coopMemberDto.getMutualcoop() },
						"from Funds");
				if (null != funds) {
					funds.setGenericStatus(DESACTIVE);
					fundsImpl.UpdateFunds(funds);
					// EXISTING FUNDS
					LOGGER.info("EXISTING FUNDS::::" + funds.getBalance() + "MEMBER GIVEN::"
							+ funds.getUsermember().getFullname());

					// Check fund contibuted against standardidized fund in coop policy and check if
					// yesterday there was a contributed fund

					SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
					String dt = smf.format(new Date());
					String dt2 = smf.format(funds.getGivenDate());
					Date date1 = smf.parse(dt);
					Date date2 = smf.parse(dt2);
					Formating fmt = new Formating();
					LOGGER.info("TODAY DATE:::" + smf.format(date1));
					LOGGER.info("::::Yesterday DATE::::" + smf.format(date2));
					
					mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
							new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
							"from MutualCoopMembers");
					policy = policyImpl.getModelWithMyHQL(new String[] { "genericStatus", "mutualcoop" },
							new Object[] { ACTIVE, mutualMembers.getMutualcoop() }, "from MutualCoopPolicy");
					if (date1.compareTo(date2) > 0) {
						days = fmt.daysBetween(date2, date1);
						LOGGER.info("DAYS BETWEEN :::::"+days);
						if (days == 1 && Double.parseDouble(amount)>=policy.getMinContribution()) {
							memberFunds.setGenericStatus(ACTIVE);
							memberFunds.setCreatedBy(usersSession.getFullname());
							memberFunds.setCrtdDtTime(timestamp);
							memberFunds.setGivenDate(timestamp);
							memberFunds.setUsermember(coopMemberDto.getMember());
							memberFunds.setMutualcoop(coopMemberDto.getMutualcoop());
							memberFunds.setAmount(Double.parseDouble(amount));
							memberFunds.setBalance(funds.getBalance() + Double.parseDouble(amount));
							memberFunds.setStatus(FundStatus);
							fundsImpl.saveFunds(memberFunds);
							JSFMessagers.resetMessages();
							setValid(true);
							JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.funds"));
							LOGGER.info(CLASSNAME + ":::No fines Details is saved");
							clearFundsFuileds();
						}else {
							if(Double.parseDouble(amount)>=policy.getMinContribution()) {
								memberFunds.setGenericStatus(ACTIVE);
								memberFunds.setCreatedBy(usersSession.getFullname());
								memberFunds.setCrtdDtTime(timestamp);
								memberFunds.setGivenDate(timestamp);
								memberFunds.setUsermember(coopMemberDto.getMember());
								memberFunds.setMutualcoop(coopMemberDto.getMutualcoop());
								memberFunds.setAmount(Double.parseDouble(amount));
								memberFunds.setBalance(funds.getBalance() + Double.parseDouble(amount));
								memberFunds.setStatus(FundStatus);
								fundsImpl.saveFunds(memberFunds);
								finees.setGenericStatus(ACTIVE);
								finees.setCreatedBy(usersSession.getFullname());
								finees.setCrtdDtTime(timestamp);
								finees.setGivenDate(timestamp);
								finees.setUsermember(coopMemberDto.getMember());
								finees.setAmount(policy.getFineondelay()*(days-1));
								finees.setMutualcoop(coopMemberDto.getMutualcoop());
								finees.setStatus(PENDING);
								finesImpl.saveFines(finees);
								JSFMessagers.resetMessages();
								setValid(true);
								JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.fineesoncontribution"));
								LOGGER.info(CLASSNAME + ":::Fines Details is saved");	
							}
						}
					}

				} else {
					memberFunds.setGenericStatus(ACTIVE);
					memberFunds.setCreatedBy(usersSession.getFullname());
					memberFunds.setCrtdDtTime(timestamp);
					memberFunds.setGivenDate(timestamp);
					memberFunds.setUsermember(coopMemberDto.getMember());
					memberFunds.setMutualcoop(coopMemberDto.getMutualcoop());
					memberFunds.setAmount(Double.parseDouble(amount));
					memberFunds.setBalance(Double.parseDouble(amount));
					memberFunds.setStatus(FundStatus);
					fundsImpl.saveFunds(memberFunds);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.funds"));
					LOGGER.info(CLASSNAME + ":::New Funds Details is saved");
					clearFundsFuileds();
				}

			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			}
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::User Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void clearUserFuileds() {
		newpolicy = new MutualCoopPolicy();
		interest = null;
		fines = null;
		recordeddate = null;
	}

	public void clearFundsFuileds() {
		memberFunds = new Funds();
		amount = null;
	}

	public List<MutualCooperative> mutualCoopList() {

		mutualCoopPendingRequest = new ArrayList<MutualCooperative>();
		for (Object[] data : mutualMembersImpl.reportList(
				"select sum(mb.memberSize),mc.mutualCoopId,mc.mutualCoopName,mc.mutualCoopType,mc.address from MutualCoopMembers mb,MutualCooperative mc where mc.mutualCoopId=mb.mutualcoop group by mb.mutualcoop")) {
			MutualCooperative mutualcoop = new MutualCooperative();
			mutualcoop.setCountmembers(Integer.parseInt(data[0] + ""));
			mutualcoop.setMutualCoopId(Integer.parseInt(data[1] + ""));
			mutualcoop.setMutualCoopName(data[2] + "");
			mutualcoop.setMutualCoopType(data[3] + "");
			mutualcoop.setAddress(data[4] + "");
			// if(((Users)data[5]).getUserId()==MutualRepcat) {
			// mutualcoop.setAction(((Users)data[5]).getPhone());
			// }
			mutualCoopPendingRequest.add(mutualcoop);
		}
		return (mutualCoopPendingRequest);
	}

	public String editAction(PolicyDto policy) {
		policy.setEditable(true);
		return null;
	}

	public String cancel(PolicyDto policy) {
		policy.setEditable(false);
		return null;

	}

	public String editFundAction(FundsDto fund) {
		fund.setEditable(true);
		fund.setEditchanges(true);
		return null;
	}

	public String cancelFund(FundsDto fund) {
		fund.setEditable(false);
		fund.setEditchanges(false);
		return null;
	}

	@SuppressWarnings("unchecked")
	public String saveFundAction(FundsDto fund) throws Exception {
		LOGGER.info("update  saveAction method");
		if (fund != null) {
			Funds funds = new Funds();
			;
			funds = fundsImpl.getFundsById(fund.getFundId(), "fundId");
			LOGGER.info("here update sart for " + funds + " FiD " + funds.getFundId());
			if (null != funds) {
				// fund.setEditable(false);
				if (funds.getAmount() < Double.parseDouble(fund.getFormatAmount())) {

					funds.setBalance(
							funds.getBalance() + (Double.parseDouble(fund.getFormatAmount())) - funds.getAmount());

					LOGGER.info("amount increaseed");
				} else {
					funds.setBalance(
							funds.getBalance() - (funds.getAmount() - Double.parseDouble(fund.getFormatAmount())));
					LOGGER.info("db amount" + funds.getAmount());
					LOGGER.info(":::AMOUNT CHANGE:::" + Double.parseDouble(fund.getFormatAmount()));

					LOGGER.info(
							"amount decreased" + ((funds.getAmount()) - (Double.parseDouble(fund.getFormatAmount()))));
				}
				fund.setEditchanges(false);
				funds.setUpdatedBy(usersSession.getFullname());
				funds.setAmount(Double.parseDouble(fund.getFormatAmount()));
				funds.setUpDtTime(timestamp);
				fundsImpl.UpdateFunds(funds);
				fundsList = fundsImpl.getGenericListWithHQLParameter(new String[] { "status", "usermember" },
						new Object[] { FundStatus, usersImpl.gettUserById(fund.getUsermember().getUserId(), "userId") },
						"Funds", "balance asc");
				fundDtoList = new ArrayList<FundsDto>();
				fundDtoList = listFunds(fundsList);
				SendSupportEmail email = new SendSupportEmail();

				if (null != fund.getUsermember().getEmail()) {
					boolean valid = email.sendMailMutualMember("account", fund.getUsermember().getFullname(),
							fund.getFormatAmount(), fund.getGivenDate(), usersSession.getFullname(),
							fund.getUsermember().getEmail());
					if (valid) {
						JSFMessagers.resetMessages();
						setValid(true);
						JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.mutualmember"));
						LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
					} else {
						JSFMessagers.resetMessages();
						setValid(false);
						JSFMessagers
								.addErrorMessage(getProvider().getValue("com.sendemailfail.form.mutualcooprepinfo"));
						LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
					}
				} else {
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("info.fundschanged.message"));
				}
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroinfo.fundschanged.message"));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public String saveAction(PolicyDto policy) throws Exception {
		LOGGER.info("update  saveAction method");
		if (policy != null) {
			MutualCoopPolicy pol = new MutualCoopPolicy();
			pol = new MutualCoopPolicy();
			pol = policyImpl.getMutualCoopPolicyById(policy.getPolicyId(), "policyId");
			LOGGER.info("here update sart for " + pol + " PiD " + pol.getPolicyId());
			if (null != pol) {

				policy.setEditable(false);
				pol.setPolicyDescription(policy.getPolicyDescription());
				pol.setInteresCharges(Double.parseDouble(policy.getInteresCharges()));
				pol.setFineCharges(Double.parseDouble(policy.getFineCharges()));
				pol.setCrtdDtTime(new java.sql.Timestamp(policy.getRecordedDate().getTime()));
				pol.setPeriodicinvestterm(policy.getTermofcontribution());
				pol.setFineondelay(Double.parseDouble(policy.getFineondelayedcontribution()));
				pol.setMinContribution(Double.parseDouble(policy.getMincontribution()));
				pol.setCreatedBy(policy.getRecordedBy());
				pol.setUpdatedBy(usersSession.getFullname());
				pol.setUpDtTime(timestamp);
				policyImpl.UpdateMutualCoopPolicy(pol);

				mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
						new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
						"from MutualCoopMembers");
				if (null == mutualMembers) {
					LOGGER.info("NOTHING FOUND");
				}
				mutualpolicy = policyImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "mutualcoop" },
						new Object[] { ACTIVE, mutualImpl.getMutualCooperativeById(
								mutualMembers.getMutualcoop().getMutualCoopId(), "mutualCoopId") },
						"MutualCoopPolicy", "policyId desc");

				policyDtoList = mutualPolicyList(mutualpolicy);
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

	public String getCLASSNAME() {
		return CLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		CLASSNAME = cLASSNAME;
	}

	@SuppressWarnings("unchecked")
	public List<MutualCoopMemberDto> mutualCoopMembersDetails() {

		try {
			if (usersSession.getUserCategory().getUserCatid() == MutualRepcat) {

				MutualCoopMembers members = new MutualCoopMembers();
				members = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
						new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
						"from MutualCoopMembers");
				if (null != members) {
					List<MutualCoopMembers> cooplist = new ArrayList<MutualCoopMembers>();
					cooplist = mutualMembersImpl.getGenericListWithHQLParameter(
							new String[] { "genericStatus", "mutualcoop" },
							new Object[] { ACTIVE, members.getMutualcoop() }, "MutualCoopMembers",
							"mutualMemberId desc");
					int i = 1;
					for (MutualCoopMembers cop : cooplist) {
						MutualCoopMemberDto details = new MutualCoopMemberDto();
						details.setEditable(false);
						details.setMutualMemberId(cop.getMutualMemberId());
						details.setMutualMemberId(cop.getMutualMemberId());
						details.setMember(cop.getUsermember());
						details.setMutualcoop(cop.getMutualcoop());
						details.setCountinfo(i);
						if (cop.getUsermember().getUserCategory().getUserCatid() == MutualRepcat) {
							details.setShowcontact(false);
							details.setHidecontact(true);
						} else {
							details.setShowcontact(true);
							details.setHidecontact(false);
						}
						if (cop.getUsermember().getStatus().equalsIgnoreCase(ACTIVE)) {
							details.setApprovedreq(false);
						} else {
							details.setApprovedreq(true);
						}
						if (cop.getUsermember().getStatus().equalsIgnoreCase(Block)) {
							details.setBlockedstatus(false);
						} else {
							details.setBlockedstatus(true);
						}
						mutualCoopMembersListDto.add(details);
						i++;
					}
				}
			}

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
		return (mutualCoopMembersListDto);
	}

	public String blockAction(MutualCoopMemberDto member) {
		LOGGER.info("update  saveAction method");
		if (member != null) {
			Users us = new Users();
			us = new Users();
			us = usersImpl.gettUserById(member.getMember().getUserId(), "userId");
			LOGGER.info("here update sart for " + us + " useriD " + us.getUserId());
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + us + " useriD " + us.getUserId());
			if (null != us) {
				us.setStatus(Block);
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				usersImpl.UpdateUsers(us);
				mutualCoopMembersListDto = new ArrayList<MutualCoopMemberDto>();
				mutualCoopMembersListDto = mutualCoopMembersDetails();
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

	public String rejectAction(LoanDto loan) {
		LOGGER.info("update  saveAction method");
		if (loan != null) {
			LoanRequest req = new LoanRequest();
			req = requestImpl.getLoanRequestById(loan.getRequestId(), "requestId");
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + req + " useriD " + req.getRequestId());
			if (null != req) {
				req.setStatus(REJECTED);
				req.setUpdatedBy(usersSession.getFullname());
				req.setUpDtTime(timestamp);
				req.setApprovedDate(timestamp);
				requestImpl.UpdateLoanRequest(req);
				loanDtoList = new ArrayList<LoanDto>();
				loanDtoList = loanRequestDetails();
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("reject.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroblock.changed.message"));
		}

		return null;
	}

	public String paymentAction() throws Exception {
		LOGGER.info("update  saveAction method");
		HttpSession session = SessionUtils.getSession();
		LoanDto loan = new LoanDto();
		loan = (LoanDto) session.getAttribute("loaninfo");
		if (loan != null) {
			LoanRequest req = new LoanRequest();
			req = requestImpl.getLoanRequestById(loan.getRequestId(), "requestId");
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + req + " useriD " + req.getRequestId());
			if (null != req) {
				req.setStatus(LoanPayStatus);
				req.setUpdatedBy(usersSession.getFullname());
				req.setUpDtTime(timestamp);
				req.setApprovedDate(timestamp);
				requestImpl.UpdateLoanRequest(req);
				loanDtoList = new ArrayList<LoanDto>();
				loanDtoList = loanRequestDetails();
				Funds funds = new Funds();
				funds = fundsImpl.getModelWithMyHQL(
						new String[] { "genericStatus", "usermember", "mutualcoop", "status" },
						new Object[] { ACTIVE, req.getUsermember(), req.getMutualcoop(), FundStatus }, "from Funds");
				LOGGER.info("::::MEMBER BALANCE::::" + funds.getBalance() + "Requested amount ::" + req.getAmount()
						+ "REMAINING FEES:" + (funds.getBalance() - req.getAmount()) + "");
				funds.setBalance(funds.getBalance() + req.getAmount());
				funds.setUpdatedBy(usersSession.getFullname());
				funds.setUpDtTime(timestamp);
				fundsImpl.UpdateFunds(funds);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("approvepayment.changed.message"));
				this.memberfunds = true;
				this.renderdialog = false;
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroblock.changed.message"));
		}

		return null;
	}

	public String appAction(LoanDto loan) throws Exception {
		LOGGER.info("update  saveAction method");
		double currentFund = currentMemberFunds(loan);
		LOGGER.info("::::::MEMBERCURRENTFUNDAMOUNT:::" + currentFund);
		mutualMembers = new MutualCoopMembers();
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		MutualCoopPolicy pol = new MutualCoopPolicy();

		pol = policyImpl.getModelWithMyHQL(new String[] { "genericStatus", "mutualcoop" },
				new Object[] { ACTIVE, mutualMembers.getMutualcoop() }, "from MutualCoopPolicy");
		if (loan != null) {
			LoanRequest req = new LoanRequest();
			req = requestImpl.getLoanRequestById(loan.getRequestId(), "requestId");
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + req + " useriD " + req.getRequestId());
			if (null != req && null != pol && currentFund > req.getAmount()) {
				req.setStatus(ACCEPTED);
				req.setUpdatedBy(usersSession.getFullname());
				req.setUpDtTime(timestamp);
				req.setApprovedDate(timestamp);
				requestImpl.UpdateLoanRequest(req);

				loanDtoList = new ArrayList<LoanDto>();
				loanDtoList = loanRequestDetails();

				charges.setCreatedBy(usersSession.getFullname());
				charges.setCrtdDtTime(timestamp);
				charges.setGenericStatus(ACTIVE);
				charges.setStatus(PENDING);
				charges.setAmount((req.getAmount() * pol.getInteresCharges()) / 100);
				charges.setUsermember(req.getUsermember());
				charges.setMutualcoop(req.getMutualcoop());
				charges.setLoanRequest(req);
				chargeImpl.saveInterestCharges(charges);
				Funds funds = new Funds();
				funds = fundsImpl.getModelWithMyHQL(
						new String[] { "genericStatus", "usermember", "mutualcoop", "status" },
						new Object[] { ACTIVE, req.getUsermember(), req.getMutualcoop(), FundStatus }, "from Funds");
				LOGGER.info("::::MEMBER BALANCE::::" + funds.getBalance() + "Requested amount ::" + req.getAmount()
						+ "REMAINING FEES:" + (funds.getBalance() - req.getAmount()) + "");
				funds.setBalance(funds.getBalance() - req.getAmount());
				funds.setUpdatedBy(usersSession.getFullname());
				funds.setUpDtTime(timestamp);
				fundsImpl.UpdateFunds(funds);
				clearCharges();
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("approve.changed.message"));
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("insufficientamount.changed.message.member"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
		}

		return null;
	}

	public double overallLoanAccepted() throws Exception {
		double overloan = 0.0;
		if (usersSession.getUserCategory().getUserCatid() == MutualRepcat) {
			mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
					new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
					"from MutualCoopMembers");
			if (null != mutualMembers.getMutualcoop()) {
				for (Object[] data : requestImpl
						.reportList("select sum(l.amount),l.mutualcoop from LoanRequest l where l.mutualcoop ="
								+ mutualMembers.getMutualcoop().getMutualCoopId() + " and l.status='" + ACCEPTED
								+ "'")) {
					if (null == data[0]) {
						overloan = overloan + overloan;
					} else {
						overloan = Double.parseDouble(data[0] + "");
					}

				}

			}
		}
		return overloan;
	}

	public Double currentCoopFunds() throws Exception {
		double overloan = overallLoanAccepted();
		double fundoverloan = 0.0;
		if (usersSession.getUserCategory().getUserCatid() == MutualRepcat) {
			mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
					new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
					"from MutualCoopMembers");
			if (null != mutualMembers.getMutualcoop()) {

				for (Object[] data : fundsImpl
						.reportList("select sum(f.balance),f.mutualcoop from Funds f where mutualcoop ="
								+ mutualMembers.getMutualcoop().getMutualCoopId() + " and f.genericStatus='" + ACTIVE
								+ "' and f.status='" + FundStatus + "'")) {

					if (null != data[0] && Double.parseDouble(data[0] + "") > overloan) {
						fundoverloan = Double.parseDouble(data[0] + "") - overloan;
					}
				}
			}
		}
		return fundoverloan;
	}

	public Double currentMemberFunds(LoanDto loan) throws Exception {
		// double overloan=overallLoanAccepted();
		double fundoverloan = 0.0;
		if (usersSession.getUserCategory().getUserCatid() == MutualRepcat) {
			mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
					new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
					"from MutualCoopMembers");
			if (null != mutualMembers.getMutualcoop()) {

				for (Object[] data : fundsImpl
						.reportList("select sum(f.balance),f.mutualcoop from Funds f where mutualcoop ="
								+ mutualMembers.getMutualcoop().getMutualCoopId() + " and f.genericStatus='" + ACTIVE
								+ "' and f.status='" + FundStatus + "' and usermember="
								+ loan.getUsermember().getUserId() + "")) {

					fundoverloan = Double.parseDouble(data[0] + "");
				}
			}
		}
		return fundoverloan;
	}

	public void clearCharges() {
		charges = new InterestCharges();
	}

	public String addFundAction(MutualCoopMemberDto member) {
		HttpSession sessionmember = SessionUtils.getSession();
		if (null != member) {
			LOGGER.info("::MEMBER DETAILS::" + member.getMember().getFullname());
			this.memberfunds = false;
			this.fundsform = true;
			coopMemberDto = member;
			sessionmember.setAttribute("memberfunds", coopMemberDto);

		}
		return null;
	}

	public String unblockAction(MutualCoopMemberDto member) {
		LOGGER.info("update  saveAction method");
		if (member != null) {
			Users us = new Users();
			us = new Users();
			us = usersImpl.gettUserById(member.getMember().getUserId(), "userId");
			LOGGER.info("here update sart for " + us + " useriD " + us.getUserId());
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + us + " useriD " + us.getUserId());
			if (null != us) {
				us.setStatus(ACTIVE);
				us.setUpdatedBy(usersSession.getViewId());
				us.setUpDtTime(timestamp);
				usersImpl.UpdateUsers(us);
				mutualCoopMembersListDto = new ArrayList<MutualCoopMemberDto>();
				mutualCoopMembersListDto = mutualCoopMembersDetails();
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
	public void coopDetails(MutualCooperative coop) {

		try {
			if (null != coop) {
				List<MutualCoopMembers> cooplist = new ArrayList<MutualCoopMembers>();
				cooplist = mutualMembersImpl.getGenericListWithHQLParameter(
						new String[] { "genericStatus", "mutualcoop" }, new Object[] { ACTIVE, coop },
						"MutualCoopMembers", "mutualMemberId desc");

				for (MutualCoopMembers cop : cooplist) {
					MutualCoopMemberDto details = new MutualCoopMemberDto();
					details.setMutualMemberId(cop.getMutualMemberId());
					details.setMember(cop.getUsermember());
					details.setMutualcoop(cop.getMutualcoop());
					if (cop.getUsermember().getUserCategory().getUserCatid() == MutualRepcat) {
						details.setShowcontact(false);
						details.setHidecontact(true);
					} else {
						details.setShowcontact(true);
						details.setHidecontact(false);
					}
					mutualMembersListDto.add(details);
				}
			}
			renderForeignCountry = false;
			rendered = true;

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public String rejectAction(MemberRequestDto req) throws Exception {
		try {

			LOGGER.info("update  saveAction method" + req.getRequestId());

			if (req != null) {
				MemberRequest us = new MemberRequest();
				us = new MemberRequest();
				us = memberReqImpl.getMemberRequestById(req.getRequestId(), "requestId");

				LOGGER.info("here update sart for " + us + " useriD " + us.getRequestId());

				if (null != us) {
					req.setEditable(false);
					us.setRequestStatus(REJECTED);
					us.setUpdatedBy(usersSession.getFullname());
					us.setUpDtTime(timestamp);
					memberReqImpl.UpdateMemberRequest(us);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("reject.changed.message"));
					// UPDATING REQUEST VIEW
					requestDtoList = new ArrayList<MemberRequestDto>();
					mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
							new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
							"from MutualCoopMembers");
					requestList = memberReqImpl.getGenericListWithHQLParameter(
							new String[] { "requestStatus", "mutualcoop" },
							new Object[] { RequestStatus, mutualMembers.getMutualcoop() }, "MemberRequest",
							"requestDate asc");
					requestDtoList = mutualCoopFollowers(requestList);
					count = requestList.size();
					if (count > 0) {
						renderCount = true;
					}

				} else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("erroreject.changed.message"));
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
	public void showMemberFunds(MutualCoopMemberDto member) throws Exception {

		if (null != member) {
			fundsList = fundsImpl.getGenericListWithHQLParameter(
					new String[] { "status", "usermember", "mutualcoop" }, new Object[] { FundStatus,
							usersImpl.gettUserById(member.getMember().getUserId(), "userId"), member.getMutualcoop() },
					"Funds", "balance asc");
			fundDtoList = new ArrayList<FundsDto>();
			fundDtoList = listFunds(fundsList);
			this.memberfunds = false;
			this.fundsDetails = true;
		}
	}

	public List<FundsDto> listFunds(List<Funds> list) {
		fundDtoList = new ArrayList<FundsDto>();
		DecimalFormat fmt = new DecimalFormat("###,###.##");
		for (Funds funds : list) {
			FundsDto fundDto = new FundsDto();
			if (funds.getGenericStatus().equalsIgnoreCase(ACTIVE)) {
				fundDto.setEditable(false);
			} else {
				fundDto.setEditable(true);
			}
			fundDto.setEditchanges(false);
			fundDto.setFundId(funds.getFundId());
			// fundDto.setAmount(funds.getAmount());
			fundDto.setFormatAmount(fmt.format(funds.getAmount()));
			// fundDto.setBalance(funds.getBalance());
			fundDto.setFormatBalance(fmt.format(funds.getBalance()));
			fundDto.setGivenDate(funds.getGivenDate());
			fundDto.setRecordedBy(funds.getCreatedBy());
			fundDto.setStatus(funds.getStatus());
			fundDto.setUsermember(funds.getUsermember());
			fundDto.setMutualcoop(funds.getMutualcoop());
			fundDto.setGeneriStatus(funds.getGenericStatus());
			fundDtoList.add(fundDto);
		}

		return (fundDtoList);
	}

	@SuppressWarnings("unchecked")
	public String approveAction(MemberRequestDto req) {
		try {
			boolean valid;
			LOGGER.info("update  saveAction method" + req.getRequestId());

			if (req != null) {
				MemberRequest us = new MemberRequest();
				us = new MemberRequest();
				us = memberReqImpl.getMemberRequestById(req.getRequestId(), "requestId");

				LOGGER.info("here update sart for " + us + " useriD " + us.getRequestId());

				if (null != us) {
					req.setEditable(false);
					us.setRequestStatus(ACCEPTED);
					us.setUpdatedBy(usersSession.getFullname());
					us.setUpDtTime(timestamp);

					SendSupportEmail email = new SendSupportEmail();
					if (null != req.getMember().getEmail()) {
						valid = email.sendMailGuestReq(ACCEPTED, req.getMember().getFullname(),
								usersSession.getFullname(), req.getMember().getEmail());
						if (valid) {
							JSFMessagers.resetMessages();
							setValid(true);
							JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.mutualcooprep"));
							LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
						} else {
							JSFMessagers.resetMessages();
							setValid(false);
							JSFMessagers.addErrorMessage(
									getProvider().getValue("com.sendemailfail.form.mutualcooprepinfo"));
							LOGGER.info(CLASSNAME + ":::mutual cooperative and rep Details is saved");
						}
					}

					memberReqImpl.UpdateMemberRequest(us);

					Users user = new Users();
					user = usersImpl.gettUserById(req.getMember().getUserId(), "userId");
					LOGGER.info("GUEST INFO" + user.getFullname());
					if (null != user) {
						UserCategory cat = new UserCategory();
						cat = catImpl.getUserCategoryById(membercat, "userCatid");
						user.setUserCategory(cat);
						usersImpl.UpdateUsers(user);
						// SAVING BOTH MUTUAL REP AND MUTUAL COOP IN MUTUALCOOPMEMBERS TABLE
						MutualCoopMembers newMember = new MutualCoopMembers();
						newMember.setCreatedBy(usersSession.getFullname());
						newMember.setCrtdDtTime(timestamp);
						// status to be updated when request approved
						newMember.setGenericStatus(ACTIVE);
						newMember.setCrtdDtTime(timestamp);
						newMember.setUsermember(user);
						newMember.setMutualcoop(req.getMutualcoop());
						newMember.setMemberSize(incrementCount);
						mutualMembersImpl.saveMutualCoopMembers(newMember);
					}

					// UPDATING REQUEST VIEW
					requestDtoList = new ArrayList<MemberRequestDto>();
					mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
							new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
							"from MutualCoopMembers");
					requestList = memberReqImpl.getGenericListWithHQLParameter(
							new String[] { "requestStatus", "mutualcoop" },
							new Object[] { RequestStatus, mutualMembers.getMutualcoop() }, "MemberRequest",
							"requestDate asc");
					requestDtoList = mutualCoopFollowers(requestList);
					count = requestList.size();
					if (count > 0) {
						renderCount = true;
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

	public void backMember() {
		this.fundsDetails = false;
		this.memberfunds = true;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public UserCategory getUsercat() {
		return usercat;
	}

	public void setUsercat(UserCategory usercat) {
		this.usercat = usercat;
	}

	public Users getUsersSession() {
		return usersSession;
	}

	public void setUsersSession(Users usersSession) {
		this.usersSession = usersSession;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public List<Users> getUsersDetails() {
		return usersDetails;
	}

	public void setUsersDetails(List<Users> usersDetails) {
		this.usersDetails = usersDetails;
	}

	public List<Users> getUseravail() {
		return useravail;
	}

	public void setUseravail(List<Users> useravail) {
		this.useravail = useravail;
	}

	public List<Users> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<Users> staffList) {
		this.staffList = staffList;
	}

	public List<UserCategory> getCatDetails() {
		return catDetails;
	}

	public void setCatDetails(List<UserCategory> catDetails) {
		this.catDetails = catDetails;
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

	public List<UserDto> getRepDtosDetails() {
		return repDtosDetails;
	}

	public void setRepDtosDetails(List<UserDto> repDtosDetails) {
		this.repDtosDetails = repDtosDetails;
	}

	public List<UserCategory> getUserCatDetails() {
		return userCatDetails;
	}

	public void setUserCatDetails(List<UserCategory> userCatDetails) {
		this.userCatDetails = userCatDetails;
	}

	public List<UserCategory> getStaffPosition() {
		return staffPosition;
	}

	public void setStaffPosition(List<UserCategory> staffPosition) {
		this.staffPosition = staffPosition;
	}

	public MutualCooperative getMutual() {
		return mutual;
	}

	public void setMutual(MutualCooperative mutual) {
		this.mutual = mutual;
	}

	public MutualCoopMembers getMutualMembers() {
		return mutualMembers;
	}

	public void setMutualMembers(MutualCoopMembers mutualMembers) {
		this.mutualMembers = mutualMembers;
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

	public DistrictImpl getDistrictImpl() {
		return districtImpl;
	}

	public void setDistrictImpl(DistrictImpl districtImpl) {
		this.districtImpl = districtImpl;
	}

	public UserCategoryImpl getCatImpl() {
		return catImpl;
	}

	public void setCatImpl(UserCategoryImpl catImpl) {
		this.catImpl = catImpl;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public LoginImpl getLoginImpl() {
		return loginImpl;
	}

	public void setLoginImpl(LoginImpl loginImpl) {
		this.loginImpl = loginImpl;
	}

	public MutualCooperativeImpl getMutualImpl() {
		return mutualImpl;
	}

	public void setMutualImpl(MutualCooperativeImpl mutualImpl) {
		this.mutualImpl = mutualImpl;
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

	public List<MutualCooperative> getMutualCoopPendingRequest() {
		return mutualCoopPendingRequest;
	}

	public void setMutualCoopPendingRequest(List<MutualCooperative> mutualCoopPendingRequest) {
		this.mutualCoopPendingRequest = mutualCoopPendingRequest;
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

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isRenderDataTable() {
		return renderDataTable;
	}

	public void setRenderDataTable(boolean renderDataTable) {
		this.renderDataTable = renderDataTable;
	}

	public boolean isRenderBoard() {
		return renderBoard;
	}

	public void setRenderBoard(boolean renderBoard) {
		this.renderBoard = renderBoard;
	}

	public List<MutualCoopMemberDto> getMutualMembersListDto() {
		return mutualMembersListDto;
	}

	public void setMutualMembersListDto(List<MutualCoopMemberDto> mutualMembersListDto) {
		this.mutualMembersListDto = mutualMembersListDto;
	}

	public MutualCoopPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(MutualCoopPolicy policy) {
		this.policy = policy;
	}

	public List<MutualCoopPolicy> getMutualpolicy() {
		return mutualpolicy;
	}

	public void setMutualpolicy(List<MutualCoopPolicy> mutualpolicy) {
		this.mutualpolicy = mutualpolicy;
	}

	public MutualCoopPolicyImpl getPolicyImpl() {
		return policyImpl;
	}

	public void setPolicyImpl(MutualCoopPolicyImpl policyImpl) {
		this.policyImpl = policyImpl;
	}

	public String getFines() {
		return fines;
	}

	public void setFines(String fines) {
		this.fines = fines;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public Date getRecordeddate() {
		return recordeddate;
	}

	public void setRecordeddate(Date recordeddate) {
		this.recordeddate = recordeddate;
	}

	public MutualCoopPolicy getNewpolicy() {
		return newpolicy;
	}

	public void setNewpolicy(MutualCoopPolicy newpolicy) {
		this.newpolicy = newpolicy;
	}

	public List<PolicyDto> getPolicyDtoList() {
		return policyDtoList;
	}

	public void setPolicyDtoList(List<PolicyDto> policyDtoList) {
		this.policyDtoList = policyDtoList;
	}

	public PolicyDto getPolicyDto() {
		return policyDto;
	}

	public void setPolicyDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}

	public boolean isRendersubmit() {
		return rendersubmit;
	}

	public void setRendersubmit(boolean rendersubmit) {
		this.rendersubmit = rendersubmit;
	}

	public List<MutualCoopPolicy> getListofAvailableCoop() {
		return listofAvailableCoop;
	}

	public void setListofAvailableCoop(List<MutualCoopPolicy> listofAvailableCoop) {
		this.listofAvailableCoop = listofAvailableCoop;
	}

	public List<MemberRequest> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<MemberRequest> requestList) {
		this.requestList = requestList;
	}

	public MemberRequestImpl getMemberReqImpl() {
		return memberReqImpl;
	}

	public void setMemberReqImpl(MemberRequestImpl memberReqImpl) {
		this.memberReqImpl = memberReqImpl;
	}

	public boolean isRenderCount() {
		return renderCount;
	}

	public void setRenderCount(boolean renderCount) {
		this.renderCount = renderCount;
	}

	public List<MemberRequestDto> getRequestDtoList() {
		return requestDtoList;
	}

	public void setRequestDtoList(List<MemberRequestDto> requestDtoList) {
		this.requestDtoList = requestDtoList;
	}

	public List<MutualCoopMemberDto> getMutualCoopMembersListDto() {
		return mutualCoopMembersListDto;
	}

	public void setMutualCoopMembersListDto(List<MutualCoopMemberDto> mutualCoopMembersListDto) {
		this.mutualCoopMembersListDto = mutualCoopMembersListDto;
	}

	public boolean isMemberfunds() {
		return memberfunds;
	}

	public void setMemberfunds(boolean memberfunds) {
		this.memberfunds = memberfunds;
	}

	public boolean isFundsform() {
		return fundsform;
	}

	public void setFundsform(boolean fundsform) {
		this.fundsform = fundsform;
	}

	public MutualCoopMemberDto getCoopMemberDto() {
		return coopMemberDto;
	}

	public void setCoopMemberDto(MutualCoopMemberDto coopMemberDto) {
		this.coopMemberDto = coopMemberDto;
	}

	public Funds getMemberFunds() {
		return memberFunds;
	}

	public void setMemberFunds(Funds memberFunds) {
		this.memberFunds = memberFunds;
	}

	public FundsImpl getFundsImpl() {
		return fundsImpl;
	}

	public void setFundsImpl(FundsImpl fundsImpl) {
		this.fundsImpl = fundsImpl;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public boolean isFundsDetails() {
		return fundsDetails;
	}

	public void setFundsDetails(boolean fundsDetails) {
		this.fundsDetails = fundsDetails;
	}

	public List<FundsDto> getFundDtoList() {
		return fundDtoList;
	}

	public void setFundDtoList(List<FundsDto> fundDtoList) {
		this.fundDtoList = fundDtoList;
	}

	public List<Funds> getFundsList() {
		return fundsList;
	}

	public void setFundsList(List<Funds> fundsList) {
		this.fundsList = fundsList;
	}

	public List<LoanDto> getLoanDtoList() {
		return loanDtoList;
	}

	public void setLoanDtoList(List<LoanDto> loanDtoList) {
		this.loanDtoList = loanDtoList;
	}

	public LoanRequest getRequest() {
		return request;
	}

	public void setRequest(LoanRequest request) {
		this.request = request;
	}

	public LoanRequestImpl getRequestImpl() {
		return requestImpl;
	}

	public void setRequestImpl(LoanRequestImpl requestImpl) {
		this.requestImpl = requestImpl;
	}

	public List<LoanRequest> getLoanRequestList() {
		return loanRequestList;
	}

	public void setLoanRequestList(List<LoanRequest> loanRequestList) {
		this.loanRequestList = loanRequestList;
	}

	public InterestCharges getCharges() {
		return charges;
	}

	public void setCharges(InterestCharges charges) {
		this.charges = charges;
	}

	public InterestChargesImpl getChargeImpl() {
		return chargeImpl;
	}

	public void setChargeImpl(InterestChargesImpl chargeImpl) {
		this.chargeImpl = chargeImpl;
	}

	public boolean isRenderdialog() {
		return renderdialog;
	}

	public void setRenderdialog(boolean renderdialog) {
		this.renderdialog = renderdialog;
	}

	public String getMincontribution() {
		return mincontribution;
	}

	public void setMincontribution(String mincontribution) {
		this.mincontribution = mincontribution;
	}

	public String getFineoncontribution() {
		return fineoncontribution;
	}

	public void setFineoncontribution(String fineoncontribution) {
		this.fineoncontribution = fineoncontribution;
	}

	public Fines getFinees() {
		return finees;
	}

	public void setFinees(Fines finees) {
		this.finees = finees;
	}

	public FinesImpl getFinesImpl() {
		return finesImpl;
	}

	public void setFinesImpl(FinesImpl finesImpl) {
		this.finesImpl = finesImpl;
	}

}

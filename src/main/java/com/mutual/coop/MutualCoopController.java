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
import mutual.coop.dto.MemberRequestDto;
import mutual.coop.dto.MutualCoopMemberDto;
import mutual.coop.dto.PolicyDto;
import mutual.coop.dto.UserDto;
import mutual.dao.impl.ContactImpl;
import mutual.dao.impl.DistrictImpl;
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

	private List<MutualCooperative> mutualCoopPendingRequest = new ArrayList<MutualCooperative>();
	private MemberRequestImpl memberReqImpl = new MemberRequestImpl();
	private MutualCoopPolicy policy = new MutualCoopPolicy();
	private List<MutualCoopPolicy> mutualpolicy,listofAvailableCoop = new ArrayList<MutualCoopPolicy>();
	MutualCoopPolicyImpl policyImpl = new MutualCoopPolicyImpl();
	private MutualCoopPolicy newpolicy = new MutualCoopPolicy();
	private List<PolicyDto> policyDtoList = new ArrayList<PolicyDto>();
	private PolicyDto policyDto= new PolicyDto();
	private List<MemberRequestDto> requestDtoList = new ArrayList<MemberRequestDto>();
	private List<MemberRequest>requestList= new ArrayList<MemberRequest>();
	private String choice;
	private boolean rendered;
	private boolean renderForeignCountry;
	private boolean rendersaveButton,renderCount;
	private boolean renderprofile;
	private boolean renderDataTable;
	private boolean renderBoard,rendersubmit;
	private String option;
	private String selection;
	private String fines, interest;
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

		try {

			
			if(usersSession.getUserCategory().getUserCatid()==MutualRepcat) {
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
				
				policyDtoList=mutualPolicyList(mutualpolicy);
				rendersubmit=true;
				
				requestList=memberReqImpl.getGenericListWithHQLParameter(new String[] { "requestStatus", "mutualcoop" },
						new Object[] { RequestStatus,mutualMembers.getMutualcoop() },
						"MemberRequest", "requestDate asc");
				
				count=requestList.size();
				if(count>0) {
					renderCount=true;
				}
				requestDtoList=mutualCoopFollowers(requestList);
				
			}else {
				mutualCoopPendingRequest = mutualCoopList();
				if (mutualCoopPendingRequest.size() > 0) {
					this.renderForeignCountry = true;
				}
			}
			
//			
//			listofAvailableCoop=policyImpl.getGenericListWithHQLParameter(new String[] { "genericStatus"},
//						new Object[] { ACTIVE, mutualImpl.getMutualCooperativeById(
//								mutualMembers.getMutualcoop().getMutualCoopId(), "mutualCoopId") },
//						"MutualCoopPolicy", "policyId desc");
			
			
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

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
	
	
	
	public List<MemberRequestDto> mutualCoopFollowers(List<MemberRequest> list) {

		requestDtoList = new ArrayList<MemberRequestDto>();
		int i = 1;
		for (MemberRequest req : list) {
			MemberRequestDto reqDto= new MemberRequestDto();
			reqDto.setRequestId(req.getRequestId());
			reqDto.setMember(req.getMember());
			reqDto.setRequestDate(req.getRequestDate());
			reqDto.setEditable(false);
			reqDto.setCountinfo(i);
			reqDto.setRequestStatus(req.getRequestStatus());
			reqDto.setMutualcoop(req.getMutualcoop());
			if(req.getRequestStatus().equalsIgnoreCase(RequestStatus)) {
				reqDto.setPendingstatus(false);	
			}else {
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
		if(null==policy) {
			LOGGER.info("NO POLICY FOUND");
			this.rendered = false;
			renderForeignCountry = true;
			renderDataTable = false;
		} else {
			LOGGER.info("POLICY FOUND");
//			rendersaveButton = true;
			renderForeignCountry = true;
			renderDataTable = false;
			renderBoard=true;
			rendersubmit=false;
			sessionpolicy.setAttribute("policy",policy);	
		}
		return null;
	}
	
	public void newPolicy() {
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
			}
			HttpSession session = SessionUtils.getSession();
			policy = (MutualCoopPolicy) session.getAttribute("policy");
			SimpleDateFormat fmt= new SimpleDateFormat("dd/MM/yyyy");
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
			SimpleDateFormat fmt= new SimpleDateFormat("dd/MM/yyyy");
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

	public void clearUserFuileds() {
		newpolicy= new MutualCoopPolicy();
		interest=null;
		fines=null;
		recordeddate=null;
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
	
	@SuppressWarnings("unchecked")
	public String saveAction(PolicyDto policy) throws Exception  {
		LOGGER.info("update  saveAction method");
		if (policy != null) {
			MutualCoopPolicy pol = new MutualCoopPolicy();
			pol = new MutualCoopPolicy();
			pol = policyImpl.getMutualCoopPolicyById(policy.getPolicyId(), "policyId");
			LOGGER.info("here update sart for " + pol + " PiD " + pol.getPolicyId());
			if (null!=pol) {
				
				policy.setEditable(false);
				pol.setPolicyDescription(policy.getPolicyDescription());
				pol.setInteresCharges(Double.parseDouble(policy.getInteresCharges()));
				pol.setFineCharges(Double.parseDouble(policy.getFineCharges()));
				pol.setCrtdDtTime(new java.sql.Timestamp(policy.getRecordedDate().getTime()));
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
				
				policyDtoList=mutualPolicyList(mutualpolicy);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("info.changed.message"));
				} 
		}else {
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
			
			LOGGER.info("update  saveAction method"+req.getRequestId());
			
			if (req != null) {
				MemberRequest us = new MemberRequest();
				us = new MemberRequest();
				us=memberReqImpl.getMemberRequestById(req.getRequestId(),"requestId");
				
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
					//UPDATING REQUEST VIEW
					requestDtoList = new ArrayList<MemberRequestDto>();
					mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
							new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
							"from MutualCoopMembers");
					requestList=memberReqImpl.getGenericListWithHQLParameter(new String[] { "requestStatus", "mutualcoop" },
							new Object[] { RequestStatus,mutualMembers.getMutualcoop() },
							"MemberRequest", "requestDate asc");
					requestDtoList=mutualCoopFollowers(requestList);
					count=requestList.size();
					if(count>0) {
						renderCount=true;
					}
					
				}else {
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
	public String approveAction(MemberRequestDto req)  {
		try {
			boolean valid ;
			LOGGER.info("update  saveAction method"+req.getRequestId());
			
			if (req != null) {
				MemberRequest us = new MemberRequest();
				us = new MemberRequest();
				us=memberReqImpl.getMemberRequestById(req.getRequestId(),"requestId");
				
				LOGGER.info("here update sart for " + us + " useriD " + us.getRequestId());
				

				if (null != us) {
					req.setEditable(false);
					us.setRequestStatus(ACCEPTED);
					us.setUpdatedBy(usersSession.getFullname());
					us.setUpDtTime(timestamp);
					
					SendSupportEmail email = new SendSupportEmail();
					if(null!=req.getMember().getEmail()) {
						valid= email.sendMailGuestReq(ACCEPTED,req.getMember().getFullname(), 
								usersSession.getFullname(), req.getMember().getEmail());
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
					
					memberReqImpl.UpdateMemberRequest(us);
						
					Users user= new Users();
					user=  usersImpl.gettUserById(req.getMember().getUserId(), "userId");
					LOGGER.info("GUEST INFO"+user.getFullname());
					if(null!=user) {
						UserCategory cat= new UserCategory();
						cat=catImpl.getUserCategoryById(membercat, "userCatid");
						user.setUserCategory(cat);
						usersImpl.UpdateUsers(user);
						// SAVING BOTH MUTUAL REP AND MUTUAL COOP IN MUTUALCOOPMEMBERS TABLE
						MutualCoopMembers newMember= new MutualCoopMembers();
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
					
					//UPDATING REQUEST VIEW
					requestDtoList = new ArrayList<MemberRequestDto>();
					mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
							new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
							"from MutualCoopMembers");
					requestList=memberReqImpl.getGenericListWithHQLParameter(new String[] { "requestStatus", "mutualcoop" },
							new Object[] { RequestStatus,mutualMembers.getMutualcoop() },
							"MemberRequest", "requestDate asc");
					requestDtoList=mutualCoopFollowers(requestList);
					count=requestList.size();
					if(count>0) {
						renderCount=true;
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
		
}

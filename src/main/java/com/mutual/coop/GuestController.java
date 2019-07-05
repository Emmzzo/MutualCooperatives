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

import com.google.gson.Gson;

import mutual.common.DbConstant;
import mutual.common.Formating;
import mutual.common.JSFBoundleProvider;
import mutual.common.JSFMessagers;
import mutual.common.SessionUtils;
import mutual.coop.dto.ContactDto;
import mutual.coop.dto.DistributedInterestDto;
import mutual.coop.dto.FinesDto;
import mutual.coop.dto.FundsDto;
import mutual.coop.dto.IncomeDto;
import mutual.coop.dto.InterestChargeDto;
import mutual.coop.dto.LoanDto;
import mutual.coop.dto.MutualCoopMemberDto;
import mutual.coop.dto.MutualMembersPolicyDto;
import mutual.coop.dto.PolicyDto;
import mutual.coop.dto.PostsDto;
import mutual.coop.dto.UserDto;
import mutual.dao.impl.ContactImpl;
import mutual.dao.impl.DistributedInterestImpl;
import mutual.dao.impl.DistrictImpl;
import mutual.dao.impl.FundsImpl;
import mutual.dao.impl.InterestChargesImpl;
import mutual.dao.impl.LoanRequestImpl;
import mutual.dao.impl.LoginImpl;
import mutual.dao.impl.MemberRequestImpl;
import mutual.dao.impl.MutualCoopMembersImpl;
import mutual.dao.impl.MutualCoopPolicyImpl;
import mutual.dao.impl.MutualCooperativeImpl;
import mutual.dao.impl.PostsImpl;
import mutual.dao.impl.ProvinceImpl;
import mutual.dao.impl.UserCategoryImpl;
import mutual.dao.impl.UserImpl;
import mutual.domain.Contact;
import mutual.domain.DistributedInterest;
import mutual.domain.District;
import mutual.domain.Funds;
import mutual.domain.InterestCharges;
import mutual.domain.LoanRequest;
import mutual.domain.MemberRequest;
import mutual.domain.MutualCoopMembers;
import mutual.domain.MutualCoopPolicy;
import mutual.domain.MutualCooperative;
import mutual.domain.Posts;
import mutual.domain.Province;
import mutual.domain.UserCategory;
import mutual.domain.Users;

@ManagedBean
@ViewScoped
public class GuestController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "GuestController :: ";
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
	private String overallFunds;
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
	private List<MutualCoopMemberDto> memberCoopList = new ArrayList<MutualCoopMemberDto>();
	private MutualCoopMemberDto mutualCoopMemberDto = new MutualCoopMemberDto();
	private List<MutualCooperative> mutualCoopPendingRequest = new ArrayList<MutualCooperative>();
	private List<MutualMembersPolicyDto> availablecoop = new ArrayList<MutualMembersPolicyDto>();
	private MutualCoopPolicy policy = new MutualCoopPolicy();
	private List<MutualCoopPolicy> mutualpolicy, listofAvailableCoop = new ArrayList<MutualCoopPolicy>();
	private List<LoanRequest> loanRequestList = new ArrayList<LoanRequest>();
	private List<LoanRequest> memberLoanRequest = new ArrayList<LoanRequest>();
	MutualCoopPolicyImpl policyImpl = new MutualCoopPolicyImpl();
	private MutualCoopPolicy newpolicy = new MutualCoopPolicy();
	private List<InterestChargeDto> chargeDtosList, interestDtosList,
			interestStatDto = new ArrayList<InterestChargeDto>();
	private InterestCharges charges;
	private InterestChargesImpl chargeImpl = new InterestChargesImpl();
	private List<PolicyDto> policyDtoList = new ArrayList<PolicyDto>();
	private List<LoanDto> loanDtoList, loanStatDto = new ArrayList<LoanDto>();
	private List<LoanDto> memberLoanDtoList = new ArrayList<LoanDto>();
	private List<FundsDto> fundDtoList, fundsStat = new ArrayList<FundsDto>();
	private List<FundsDto> overallFundDtoList = new ArrayList<FundsDto>();
	private List<Funds> fundsList = new ArrayList<Funds>();
	private List<Funds> overallFundsList = new ArrayList<Funds>();
	private PolicyDto policyDto = new PolicyDto();
	private MutualMembersPolicyDto mutualcoopinfo = new MutualMembersPolicyDto();
	private MemberRequest memberReq;
	private LoanRequest request;
	FundsImpl fundsImpl = new FundsImpl();
	private LoanRequestImpl requestImpl = new LoanRequestImpl();
	private MemberRequestImpl memberReqImpl = new MemberRequestImpl();
	private String choice, overAllLoan, overLoanGeneralStat, overAllInterest;
	private boolean rendered, daybefore, dayafter;;
	private boolean renderForeignCountry;
	private boolean rendersaveButton;
	private boolean renderprofile;
	private boolean renderDataTable;
	private boolean renderBoard, rendersubmit, renderNotify,renderGraph;
	private String option;
	private String selection;
	private String fines, interest, amount,overAllIncome,overAllFines,overAllReceivedIncome;
	private Date recordeddate, returnDate;
	private int count, days, countNotify;
	private Posts post = new Posts();
	private List<Posts> allposts = new ArrayList<Posts>();
	PostsImpl postImpl = new PostsImpl();
	private PostsDto postDtos = new PostsDto();
	private List<PostsDto> mutualpost = new ArrayList<PostsDto>();
	private List<IncomeDto> allIncomeDto,incomeStatDto = new ArrayList<IncomeDto>();
	private List<FinesDto> finesDtoList, finesStatistic = new ArrayList<FinesDto>();
	private DistributedInterest distInterest;
	private List<DistributedInterest> distIncomeList = new ArrayList<DistributedInterest>();
	DistributedInterestImpl distInterestImpl = new DistributedInterestImpl();
	private List<DistributedInterestDto> interestDto = new ArrayList<DistributedInterestDto>();
	@SuppressWarnings({ "unchecked", "static-access" })
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

		if (memberReq == null) {
			memberReq = new MemberRequest();
		}
		if (request == null) {
			request = new LoanRequest();
		}
		try {

			if (usersSession.getUserCategory().getUserCatid() == guestcat) {
				availablecoop = mutualCoopList();
				this.rendered = true;
				count = availablecoop.size();
			} else {
				memberCoopList = memberMutualCoopList();
				this.rendered = true;
				count = memberCoopList.size();
				mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
				if (null != mutualCoopMemberDto) {
					mutualMembersList = mutualMembersImpl.getGenericListWithHQLParameter(
							new String[] { "genericStatus", "mutualcoop" },
							new Object[] { ACTIVE, mutualCoopMemberDto.getMutualcoop() }, "MutualCoopMembers",
							"mutualMemberId asc");
					mutualMembersListDto = availableMembers(mutualMembersList);
					loanRequestList = requestImpl.getGenericListWithHQLParameter(
							new String[] { "genericStatus", "mutualcoop", "usermember" },
							new Object[] { ACTIVE, mutualCoopMemberDto.getMutualcoop(), usersSession }, "LoanRequest",
							"requestDate desc");
					if (loanRequestList.size() > 0) {
						renderDataTable = true;
						loanDtoList = loanRequested(loanRequestList);
					} else {
						rendersaveButton = true;
					}
					memberLoanRequest = requestImpl.getGenericListWithHQLParameter(
							new String[] { "genericStatus", "mutualcoop" },
							new Object[] { ACTIVE, mutualCoopMemberDto.getMutualcoop() }, "LoanRequest",
							"requestDate asc");

					memberLoanDtoList = memberLoanRequested(memberLoanRequest);

					fundsList = fundsImpl.getGenericListWithHQLParameter(
							new String[] { "status", "usermember", "mutualcoop" },
							new Object[] { FundStatus, usersSession, mutualCoopMemberDto.getMutualcoop() }, "Funds",
							"balance asc");

					if (fundsList.size() > 0) {
						renderForeignCountry = true;
						fundDtoList = listFunds(fundsList);
						// overallFundDtoList=overallCoopFunds();
					} else {
						renderForeignCountry = false;
						renderBoard = true;
					}
					LoanRequest request2 = new LoanRequest();
					request2 = requestImpl.getModelWithMyHQL(new String[] { "status", "usermember", "mutualcoop" },
							new Object[] { ACCEPTED, usersImpl.gettUserById(usersSession.getUserId(), "userId"),
									mutualCoopMemberDto.getMutualcoop() },
							"from LoanRequest");
					Formating fmt = new Formating();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date today = fmt.getCurrentDateFormtNOTime();
					if (null != request2) {
						simpleDateFormat.format(request2.getReturnDate());

						if (request2.getReturnDate().after(today)) {
							days = fmt.daysBetween(today, request2.getReturnDate());
							dayafter = true;
						} else {
							days = fmt.daysBetween(request2.getReturnDate(), today);
							daybefore = true;
						}
					}
					overallFundsStatistics();

					overallLoanStatistics();

					overallLoanGeneralStatistics();
					
					overallIncomeStatistics();
					
					overallInterestStatistics();
					
					overallFinesStatistics();
					showMemberTotInterestReceived();
					
					
					LOGGER.info("::::JSON VALUE::::"+this.overAllReceivedIncome);
					this.renderGraph=true;
					// LISTING M.COOP ADVERTISMENT
					allposts = postImpl.getGenericListWithHQLParameter(
							new String[] { "genericStatus", "mutualcoop", "status" },
							new Object[] { ACTIVE, mutualCoopMemberDto.getMutualcoop(), ACTIVE }, "Posts",
							"postDate asc");
					if (allposts.size() > 0) {
						renderNotify = true;
						countNotify = allposts.size();
					}
				}

			}

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}

	public void overallLoanStatistics() throws Exception {
		// Get the values from the session
		HttpSession session = SessionUtils.getSession();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		if (null != mutualCoopMemberDto) {
			loanStatDto = new ArrayList<LoanDto>();
			for (Object[] data : fundsImpl
					.reportList("select sum(l.amount),l.status from LoanRequest l where mutualcoop="
							+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId() + " and usermember="
							+ usersSession.getUserId() + " group by l.status")) {
				LoanDto loan = new LoanDto();
				loan.setAmount(Double.parseDouble(data[0] + ""));
				loan.setStatus(data[1] + "");
				loanStatDto.add(loan);
				LOGGER.info("Amount::" + data[0] + "::Status:" + data[1] + "");
			}

			this.overAllLoan = new Gson().toJson(loanStatDto);
		}
	}

	public void overallLoanGeneralStatistics() throws Exception {
		// Get the values from the session
		HttpSession session = SessionUtils.getSession();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		if (null != mutualCoopMemberDto) {
			loanStatDto = new ArrayList<LoanDto>();
			for (Object[] data : fundsImpl
					.reportList("select sum(l.amount),l.status from LoanRequest l where mutualcoop="
							+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId() + " group by l.status")) {
				LoanDto loan = new LoanDto();
				loan.setAmount(Double.parseDouble(data[0] + ""));
				loan.setStatus(data[1] + "");
				loanStatDto.add(loan);
				LOGGER.info("Amount::" + data[0] + "::Status:" + data[1] + "");
			}

			this.overLoanGeneralStat = new Gson().toJson(loanStatDto);
		}
	}

	public void overallInterestStatistics() throws Exception {
		// Get the values from the session
		HttpSession session = SessionUtils.getSession();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		interestStatDto = new ArrayList<InterestChargeDto>();
		for (Object[] data : fundsImpl
				.reportList("select sum(inc.amount),inc.status from InterestCharges inc where inc.mutualcoop="
						+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId()
						+ "  and inc.status is not null group by inc.status")) {
			InterestChargeDto interest = new InterestChargeDto();
			interest.setAmount(Double.parseDouble(data[0] + ""));
			interest.setStatus(data[1] + "");
			interestStatDto.add(interest);
			LOGGER.info("Amount::" + data[0] + "::Status:" + data[1] + "");
		}

		this.overAllInterest = new Gson().toJson(interestStatDto);

	}
	
	
	public void overallIncomeStatistics() throws Exception {
		// Get the values from the session
		HttpSession session = SessionUtils.getSession();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		incomeStatDto = new ArrayList<IncomeDto>();
		for (Object[] data : fundsImpl
				.reportList("select  sum(inc.incomeAmount),inc.status from OtherIncome inc where inc.mutualcoop="
						+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId()
						+ "  and inc.status is not null group by inc.status")) {
			IncomeDto inc = new IncomeDto();
			inc.setIncomeAmount(Double.parseDouble(data[0] + ""));
			if(data[1].equals(DESACTIVE)) {
				inc.setStatus("shared");
			}else {
			inc.setStatus(data[1] + "");
			}
			
			incomeStatDto.add(inc);
			LOGGER.info("Amount::" + data[0] + "::Status:" + data[1] + "");
		}

		this.overAllIncome = new Gson().toJson(incomeStatDto);

	}
	
	
	
	
	
	public void overallFinesStatistics() throws Exception {
		// Get the values from the session
		HttpSession session = SessionUtils.getSession();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		finesStatistic = new ArrayList<FinesDto>();
		for (Object[] data : fundsImpl.reportList("select sum(f.amount),f.status from Fines f where mutualcoop="
				+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId() + " group by f.status")) {
			FinesDto fines = new FinesDto();
			fines.setAmount(Double.parseDouble(data[0] + ""));
			fines.setStatus(data[1] + "");
			finesStatistic.add(fines);
			LOGGER.info("Amount::" + data[0] + "::Status:" + data[1] + "");
		}

		this.overAllFines = new Gson().toJson(finesStatistic);

	}
	
	public List<DistributedInterestDto> showMemberTotInterestReceived() throws Exception {
		int countinfo = 1;
		DecimalFormat fmt = new DecimalFormat("###,###.##");
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
		interestDto = new ArrayList<DistributedInterestDto>();
		HttpSession session = SessionUtils.getSession();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		for (Object[] data : chargeImpl.reportList(
				"select inc.interestId,us.fullname,us.phone,sum(inc.amount),inc.givenDate,inc.status,inc.usermember from DistributedInterest inc,Users us  where us.userId=inc.usermember and inc.genericStatus='"
						+ ACTIVE + "' and inc.mutualcoop=" + mutualCoopMemberDto.getMutualcoop().getMutualCoopId()
						+ " and inc.usermember="+usersSession.getUserId()+" group by DATE_FORMAT(givenDate,'%d/%m/%Y') order by inc.interestId")) {
			DistributedInterestDto interest = new DistributedInterestDto();
			interest.setCountRecord(countinfo);
			if (null != data[3]) {
				interest.setFullname(data[1] + "");
				interest.setPhone(data[2] + "");
				interest.setStatus(data[5] + "");
				interest.setFormatAmount(fmt.format(Double.parseDouble(data[3] + "")));
				interest.setIncomeAmount(Double.parseDouble(data[3]+""));
				interest.setReceiveDate(smf.format((Timestamp) data[4]));
				interest.setGivenDate((Timestamp)data[4]);
				interest.setUsermember((Users) data[6]);
				if (data[5].equals(PENDING)) {
					interest.setApproval(false);
				} else {
					interest.setEditable(true);
					interest.setApproval(true);
				}
			} else {
				interest.setStatus("Unavailable");
				interest.setApproval(true);
				interest.setFormatAmount("0.0");
			}
			interestDto.add(interest);
			countinfo++;
		}
		this.overAllReceivedIncome = new Gson().toJson(interestDto);
		
		return interestDto;
	}
	public List<FundsDto> overallCoopFunds() {
		HttpSession session = SessionUtils.getSession();
		overallFundDtoList = new ArrayList<FundsDto>();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		DecimalFormat fmt = new DecimalFormat("###,###.##");
		double overloan = overallLoanAccepted();
		if (null != mutualCoopMemberDto) {

			for (Object[] data : fundsImpl
					.reportList("select sum(f.amount),f.mutualcoop from Funds f where mutualcoop ="
							+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId() + " and f.status='" + FundStatus
							+ "'")) {

				FundsDto fundDto = new FundsDto();
				fundDto.setMutualcoop((MutualCooperative) data[1]);
				if (null != data[0]) {
					fundDto.setFormatBalance(fmt.format(data[0]) + "");
				} else {
					fundDto.setFormatBalance(fmt.format(decimalpoint));
				}

				if (Double.parseDouble(data[0] + "") > overloan) {
					fundDto.setFundavail(fmt.format(Double.parseDouble(data[0] + "") - overloan));
					fundDto.setOverloanrequest(fmt.format(overloan));
				}
				overallFundDtoList.add(fundDto);
			}

		}

		renderForeignCountry = false;
		rendersubmit = true;
		return overallFundDtoList;
	}

	public Double currentCoopFunds() {
		HttpSession session = SessionUtils.getSession();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		double overloan = overallLoanAccepted();
		double fundoverloan = 0.0;
		if (null != mutualCoopMemberDto) {

			for (Object[] data : fundsImpl
					.reportList("select sum(f.balance),f.mutualcoop from Funds f where mutualcoop ="
							+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId() + " and f.genericStatus='" + ACTIVE
							+ "' and f.status='" + FundStatus + "'")) {

				if (Double.parseDouble(data[0] + "") > overloan && null != data[0]) {
					fundoverloan = Double.parseDouble(data[0] + "") - overloan;
				}
			}

		}

		return fundoverloan;
	}

	
	public void showGraphStat() throws Exception {
		showMemberTotInterestReceived();
		this.renderGraph=false;
		this.rendersubmit=true;
		
	}
	
	
	
	public double overallLoanAccepted() {
		HttpSession session = SessionUtils.getSession();
		overallFundDtoList = new ArrayList<FundsDto>();
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		double overloan = 0.0;
		if (null != mutualCoopMemberDto) {
			for (Object[] data : requestImpl
					.reportList("select sum(l.amount),l.mutualcoop from LoanRequest l where l.mutualcoop ="
							+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId() + " and l.status='" + ACCEPTED
							+ "'")) {
				if (null != data[0]) {
					overloan = Double.parseDouble(data[0] + "");
				} else {
					overloan = overloan + overloan;
				}

			}
		}

		return overloan;
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

	public void backMember() {
		this.rendersaveButton = true;
		this.renderDataTable = false;
	}

	public List<LoanDto> memberLoanRequested(List<LoanRequest> list) {
		memberLoanDtoList = new ArrayList<LoanDto>();
		DecimalFormat fmt = new DecimalFormat("###,###.##");
		for (LoanRequest loan : list) {
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
			request.setReturnDate(loan.getReturnDate());
			if (loan.getStatus().equalsIgnoreCase(LoanStatus)) {
				request.setEditchanges(false);
			} else {
				request.setEditchanges(true);
			}
			if (loan.getStatus().equalsIgnoreCase(ACCEPTED)) {
				request.setAccept(false);
			} else {
				request.setAccept(true);
			}
			if (loan.getStatus().equalsIgnoreCase(REJECTED)) {
				request.setApproval(false);
			} else {
				request.setApproval(true);
			}
			if (loan.getStatus().equalsIgnoreCase(LoanPayStatus)) {
				request.setPaidStatus(false);
			} else {
				request.setPaidStatus(true);
			}

			memberLoanDtoList.add(request);
		}

		return memberLoanDtoList;
	}

	public void overallFundsStatistics() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
		fundsStat = new ArrayList<FundsDto>();
		for (Object[] data : fundsImpl
				.reportList("select sum(f.amount),DATE_FORMAT(f.givenDate,'%d/%m/%Y') from Funds f where status='"
						+ FundStatus + "' and f.mutualcoop=" + mutualCoopMemberDto.getMutualcoop().getMutualCoopId()
						+ " and f.givenDate is not null group by DATE_FORMAT(givenDate,'%d/%m/%Y')")) {
			FundsDto funds = new FundsDto();

			LOGGER.info("::amount::::" + data[0] + "::Date:" + data[1] + "");
			funds.setAmount(Double.parseDouble(data[0] + ""));
			funds.setContDate(data[1] + "");
			fundsStat.add(funds);
		}

		this.overallFunds = new Gson().toJson(fundsStat);

	}

	public List<LoanDto> loanRequested(List<LoanRequest> list) {
		loanDtoList = new ArrayList<LoanDto>();
		DecimalFormat fmt = new DecimalFormat("###,###.##");
		for (LoanRequest loan : list) {
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
			request.setReturnDate(loan.getReturnDate());
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
				request.setPaidStatus(false);
			} else {
				request.setPaidStatus(true);
			}

			if (null == loan.getApprovedDate() && null == loan.getUpdatedBy()) {
				request.setApproval(false);
			} else {
				request.setApproval(true);
			}
			loanDtoList.add(request);
		}

		return loanDtoList;
	}

	public List<MutualCoopMemberDto> availableMembers(List<MutualCoopMembers> list) {
		mutualMembersListDto = new ArrayList<MutualCoopMemberDto>();
		int i = 1;
		for (MutualCoopMembers usermember : list) {
			MutualCoopMemberDto mbrDto = new MutualCoopMemberDto();
			mbrDto.setCountinfo(i);
			mbrDto.setEditable(false);
			mbrDto.setMember(usermember.getUsermember());
			mutualMembersListDto.add(mbrDto);
			i++;
		}
		return mutualMembersListDto;
	}

	public void mutualDetails(MutualMembersPolicyDto coop) {
		if (coop.getMembers().getUserCategory().getUserCatid() == MutualRepcat) {
			mutualcoopinfo = coop;
			HttpSession sessiondetail = SessionUtils.getSession();
			sessiondetail.setAttribute("details", mutualcoopinfo);
			this.rendered = false;
			this.renderDataTable = true;

		}
	}

	public String mutualInfo(MutualCoopMemberDto coop) {
		if (null != coop) {
			// mutualCoopMemberDto=coop;
			HttpSession sessionuser = SessionUtils.getSession();
			sessionuser.setAttribute("coopdetails", coop);
			return "/MemberDashboard.xhtml?faces-redirect=true";
		} else {
			return null;
		}
	}

	@SuppressWarnings("static-access")
	public void saveLoanRequest() throws Exception {
		try {
			try {
				if (Double.parseDouble(amount) > 0 && null != returnDate) {
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
			mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
			if (null != mutualCoopMemberDto) {

				MutualCooperative coop = new MutualCooperative();
				Users user = new Users();
				coop = mutualImpl.getMutualCooperativeById(mutualCoopMemberDto.getMutualcoop().getMutualCoopId(),
						"mutualCoopId");
				user = usersImpl.gettUserById(usersSession.getUserId(), "userId");
				Formating fmt = new Formating();
				SimpleDateFormat fmtdate = new SimpleDateFormat("yyyy-MM-dd");
				Date today = fmt.getCurrentDateFormtNOTime();
				Date rdate = null;
				rdate = fmtdate.parse(fmtdate.format(returnDate));
				if (null != coop && null != user && rdate.after(today)) {

					request.setGenericStatus(ACTIVE);
					request.setCreatedBy(usersSession.getFullname());
					request.setCrtdDtTime(timestamp);
					request.setRequestDate(timestamp);
					request.setStatus(LoanStatus);
					request.setAmount(Double.parseDouble(amount));
					request.setMutualcoop(coop);
					request.setUsermember(user);
					request.setReturnDate(new java.sql.Date(returnDate.getTime()));
					requestImpl.saveLoanRequest(request);

					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.loan.request"));
					amount = new String();
				} else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.dateinternal.error"));
				}
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

	public List<MutualMembersPolicyDto> mutualCoopList() {

		availablecoop = new ArrayList<MutualMembersPolicyDto>();
		for (Object[] data : mutualMembersImpl.reportList(
				"select sum(mb.memberSize), p.mutualcoop,mb.usermember,p.policyDescription,p.interesCharges,p.fineCharges from MutualCooperative m,MutualCoopMembers mb,MutualCoopPolicy p,Users us where m.mutualCoopId=mb.mutualcoop and m.mutualCoopId=p.mutualcoop and us.userId=mb.usermember and p.genericStatus='"
						+ ACTIVE + "' group by p.mutualcoop")) {
			MutualMembersPolicyDto mutualcoop = new MutualMembersPolicyDto();
			mutualcoop.setCountinfo(Integer.parseInt(data[0] + ""));
			mutualcoop.setMutualcoop((MutualCooperative) data[1]);
			mutualcoop.setMembers((Users) data[2]);
			mutualcoop.setPolicyDescription(data[3] + "");
			mutualcoop.setInteresCharges(data[4] + "");
			mutualcoop.setFineCharges(data[5] + "");

			availablecoop.add(mutualcoop);
		}
		return (availablecoop);
	}

	public List<MutualCoopMemberDto> memberMutualCoopList() {

		memberCoopList = new ArrayList<MutualCoopMemberDto>();
		for (Object[] data : mutualMembersImpl.reportList(
				"select sum(mb.memberSize), mb.mutualcoop,mb.usermember,p.policyDescription,p.interesCharges,p.fineCharges,p.crtdDtTime,p.minContribution,p.periodicinvestterm,p.fineondelay from MutualCooperative m,MutualCoopMembers mb,Users us ,MutualCoopPolicy p\r\n"
						+ "where m.mutualCoopId=mb.mutualcoop and us.userId=mb.usermember  and mb.usermember = "
						+ usersSession.getUserId() + " and m.mutualCoopId=p.mutualcoop and p.genericStatus='" + ACTIVE
						+ "' group by mb.mutualcoop")) {
			MutualCoopMemberDto mutualcoop = new MutualCoopMemberDto();
			mutualcoop.setCountinfo(Integer.parseInt(data[0] + ""));
			mutualcoop.setMutualcoop((MutualCooperative) data[1]);
			// if(((Users) data[2]).getUserCategory().getUserCatid()==MutualRepcat) {
			// mutualcoop.setEmail(((Users) data[2]).getEmail());
			// }
			mutualcoop.setMembers((Users) data[2]);
			mutualcoop.setPolicyDescription(data[3] + "");
			mutualcoop.setInteresCharges(data[4] + "");
			mutualcoop.setFineCharges(data[5] + "");
			mutualcoop.setCrtdDtTime((Timestamp) data[6]);
			mutualcoop.setMincontribution(data[7] + "");
			mutualcoop.setPeriodicinvestterm(data[8] + "");
			mutualcoop.setFineondelayedcontribution(data[9] + "");
			memberCoopList.add(mutualcoop);
		}
		return (memberCoopList);
	}

	public void guestRequest() {
		HttpSession session = SessionUtils.getSession();
		mutualcoopinfo = new MutualMembersPolicyDto();
		mutualcoopinfo = (MutualMembersPolicyDto) session.getAttribute("details");

		if (null != mutualcoopinfo && null != usersSession) {
			memberReq.setGenericStatus(ACTIVE);
			memberReq.setRequestStatus(RequestStatus);
			memberReq.setMember(usersSession);
			memberReq.setMutualcoop(mutualcoopinfo.getMutualcoop());
			memberReq.setCreatedBy(usersSession.getFullname());
			memberReq.setCrtdDtTime(timestamp);
			memberReq.setRequestDate(timestamp);
			memberReqImpl.saveMemberRequest(memberReq);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.request"));
			LOGGER.info(CLASSNAME + ":::User Details is saved");
			clearUserFuileds();
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.fail.form.request"));
		}

	}

	public boolean isValid() {
		return isValid;
	}

	public void clearUserFuileds() {
		memberReq = new MemberRequest();
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

	public List<MutualCoopMemberDto> getMutualMembersListDto() {
		return mutualMembersListDto;
	}

	public void setMutualMembersListDto(List<MutualCoopMemberDto> mutualMembersListDto) {
		this.mutualMembersListDto = mutualMembersListDto;
	}

	public List<MutualCooperative> getMutualCoopPendingRequest() {
		return mutualCoopPendingRequest;
	}

	public void setMutualCoopPendingRequest(List<MutualCooperative> mutualCoopPendingRequest) {
		this.mutualCoopPendingRequest = mutualCoopPendingRequest;
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

	public List<MutualCoopPolicy> getListofAvailableCoop() {
		return listofAvailableCoop;
	}

	public void setListofAvailableCoop(List<MutualCoopPolicy> listofAvailableCoop) {
		this.listofAvailableCoop = listofAvailableCoop;
	}

	public MutualCoopPolicyImpl getPolicyImpl() {
		return policyImpl;
	}

	public void setPolicyImpl(MutualCoopPolicyImpl policyImpl) {
		this.policyImpl = policyImpl;
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

	public boolean isRendersubmit() {
		return rendersubmit;
	}

	public void setRendersubmit(boolean rendersubmit) {
		this.rendersubmit = rendersubmit;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<MutualMembersPolicyDto> getAvailablecoop() {
		return availablecoop;
	}

	public void setAvailablecoop(List<MutualMembersPolicyDto> availablecoop) {
		this.availablecoop = availablecoop;
	}

	public MutualMembersPolicyDto getMutualcoopinfo() {
		return mutualcoopinfo;
	}

	public void setMutualcoopinfo(MutualMembersPolicyDto mutualcoopinfo) {
		this.mutualcoopinfo = mutualcoopinfo;
	}

	public MemberRequest getMemberReq() {
		return memberReq;
	}

	public void setMemberReq(MemberRequest memberReq) {
		this.memberReq = memberReq;
	}

	public MemberRequestImpl getMemberReqImpl() {
		return memberReqImpl;
	}

	public void setMemberReqImpl(MemberRequestImpl memberReqImpl) {
		this.memberReqImpl = memberReqImpl;
	}

	public List<MutualCoopMemberDto> getMemberCoopList() {
		return memberCoopList;
	}

	public void setMemberCoopList(List<MutualCoopMemberDto> memberCoopList) {
		this.memberCoopList = memberCoopList;
	}

	public MutualCoopMemberDto getMutualCoopMemberDto() {
		return mutualCoopMemberDto;
	}

	public void setMutualCoopMemberDto(MutualCoopMemberDto mutualCoopMemberDto) {
		this.mutualCoopMemberDto = mutualCoopMemberDto;
	}

	public List<LoanRequest> getLoanRequestList() {
		return loanRequestList;
	}

	public void setLoanRequestList(List<LoanRequest> loanRequestList) {
		this.loanRequestList = loanRequestList;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public List<LoanDto> getLoanDtoList() {
		return loanDtoList;
	}

	public void setLoanDtoList(List<LoanDto> loanDtoList) {
		this.loanDtoList = loanDtoList;
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

	public FundsImpl getFundsImpl() {
		return fundsImpl;
	}

	public void setFundsImpl(FundsImpl fundsImpl) {
		this.fundsImpl = fundsImpl;
	}

	public List<FundsDto> getOverallFundDtoList() {
		return overallFundDtoList;
	}

	public void setOverallFundDtoList(List<FundsDto> overallFundDtoList) {
		this.overallFundDtoList = overallFundDtoList;
	}

	public List<Funds> getOverallFundsList() {
		return overallFundsList;
	}

	public void setOverallFundsList(List<Funds> overallFundsList) {
		this.overallFundsList = overallFundsList;
	}

	public List<LoanRequest> getMemberLoanRequest() {
		return memberLoanRequest;
	}

	public void setMemberLoanRequest(List<LoanRequest> memberLoanRequest) {
		this.memberLoanRequest = memberLoanRequest;
	}

	public List<LoanDto> getMemberLoanDtoList() {
		return memberLoanDtoList;
	}

	public void setMemberLoanDtoList(List<LoanDto> memberLoanDtoList) {
		this.memberLoanDtoList = memberLoanDtoList;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public boolean isDaybefore() {
		return daybefore;
	}

	public void setDaybefore(boolean daybefore) {
		this.daybefore = daybefore;
	}

	public boolean isDayafter() {
		return dayafter;
	}

	public void setDayafter(boolean dayafter) {
		this.dayafter = dayafter;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getOverallFunds() {
		return overallFunds;
	}

	public void setOverallFunds(String overallFunds) {
		this.overallFunds = overallFunds;
	}

	public List<FundsDto> getFundsStat() {
		return fundsStat;
	}

	public void setFundsStat(List<FundsDto> fundsStat) {
		this.fundsStat = fundsStat;
	}

	public String getCLASSNAME() {
		return CLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		CLASSNAME = cLASSNAME;
	}

	public Posts getPost() {
		return post;
	}

	public void setPost(Posts post) {
		this.post = post;
	}

	public List<Posts> getAllposts() {
		return allposts;
	}

	public void setAllposts(List<Posts> allposts) {
		this.allposts = allposts;
	}

	public PostsImpl getPostImpl() {
		return postImpl;
	}

	public void setPostImpl(PostsImpl postImpl) {
		this.postImpl = postImpl;
	}

	public PostsDto getPostDtos() {
		return postDtos;
	}

	public void setPostDtos(PostsDto postDtos) {
		this.postDtos = postDtos;
	}

	public List<PostsDto> getMutualpost() {
		return mutualpost;
	}

	public void setMutualpost(List<PostsDto> mutualpost) {
		this.mutualpost = mutualpost;
	}

	public boolean isRenderNotify() {
		return renderNotify;
	}

	public void setRenderNotify(boolean renderNotify) {
		this.renderNotify = renderNotify;
	}

	public int getCountNotify() {
		return countNotify;
	}

	public void setCountNotify(int countNotify) {
		this.countNotify = countNotify;
	}

	public List<LoanDto> getLoanStatDto() {
		return loanStatDto;
	}

	public void setLoanStatDto(List<LoanDto> loanStatDto) {
		this.loanStatDto = loanStatDto;
	}

	public String getChoic() {
		return choice;
	}

	public void setChoic(String choic) {
		this.choice = choic;
	}

	public String getOverAllLoan() {
		return overAllLoan;
	}

	public void setOverAllLoan(String overAllLoan) {
		this.overAllLoan = overAllLoan;
	}

	public String getOverLoanGeneralStat() {
		return overLoanGeneralStat;
	}

	public void setOverLoanGeneralStat(String overLoanGeneralStat) {
		this.overLoanGeneralStat = overLoanGeneralStat;
	}

	public List<InterestChargeDto> getChargeDtosList() {
		return chargeDtosList;
	}

	public void setChargeDtosList(List<InterestChargeDto> chargeDtosList) {
		this.chargeDtosList = chargeDtosList;
	}

	public List<InterestChargeDto> getInterestDtosList() {
		return interestDtosList;
	}

	public void setInterestDtosList(List<InterestChargeDto> interestDtosList) {
		this.interestDtosList = interestDtosList;
	}

	public List<InterestChargeDto> getInterestStatDto() {
		return interestStatDto;
	}

	public void setInterestStatDto(List<InterestChargeDto> interestStatDto) {
		this.interestStatDto = interestStatDto;
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

	public String getOverAllInterest() {
		return overAllInterest;
	}

	public void setOverAllInterest(String overAllInterest) {
		this.overAllInterest = overAllInterest;
	}

	public String getOverAllIncome() {
		return overAllIncome;
	}

	public void setOverAllIncome(String overAllIncome) {
		this.overAllIncome = overAllIncome;
	}

	public List<IncomeDto> getAllIncomeDto() {
		return allIncomeDto;
	}

	public void setAllIncomeDto(List<IncomeDto> allIncomeDto) {
		this.allIncomeDto = allIncomeDto;
	}

	public List<IncomeDto> getIncomeStatDto() {
		return incomeStatDto;
	}

	public void setIncomeStatDto(List<IncomeDto> incomeStatDto) {
		this.incomeStatDto = incomeStatDto;
	}

	public String getOverAllFines() {
		return overAllFines;
	}

	public void setOverAllFines(String overAllFines) {
		this.overAllFines = overAllFines;
	}

	public List<FinesDto> getFinesDtoList() {
		return finesDtoList;
	}

	public void setFinesDtoList(List<FinesDto> finesDtoList) {
		this.finesDtoList = finesDtoList;
	}

	public List<FinesDto> getFinesStatistic() {
		return finesStatistic;
	}

	public void setFinesStatistic(List<FinesDto> finesStatistic) {
		this.finesStatistic = finesStatistic;
	}

	public DistributedInterest getDistInterest() {
		return distInterest;
	}

	public void setDistInterest(DistributedInterest distInterest) {
		this.distInterest = distInterest;
	}

	public List<DistributedInterest> getDistIncomeList() {
		return distIncomeList;
	}

	public void setDistIncomeList(List<DistributedInterest> distIncomeList) {
		this.distIncomeList = distIncomeList;
	}

	public DistributedInterestImpl getDistInterestImpl() {
		return distInterestImpl;
	}

	public void setDistInterestImpl(DistributedInterestImpl distInterestImpl) {
		this.distInterestImpl = distInterestImpl;
	}

	public List<DistributedInterestDto> getInterestDto() {
		return interestDto;
	}

	public void setInterestDto(List<DistributedInterestDto> interestDto) {
		this.interestDto = interestDto;
	}

	public String getOverAllReceivedIncome() {
		return overAllReceivedIncome;
	}

	public void setOverAllReceivedIncome(String overAllReceivedIncome) {
		this.overAllReceivedIncome = overAllReceivedIncome;
	}

	public boolean isRenderGraph() {
		return renderGraph;
	}

	public void setRenderGraph(boolean renderGraph) {
		this.renderGraph = renderGraph;
	}	
}

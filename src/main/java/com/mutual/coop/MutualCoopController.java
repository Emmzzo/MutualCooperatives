package com.mutual.coop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;

import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import mutual.common.DbConstant;
import mutual.common.Formating;
import mutual.common.JSFBoundleProvider;
import mutual.common.JSFMessagers;
import mutual.common.SessionUtils;
import mutual.coop.dto.DistributedInterestDto;
import mutual.coop.dto.FinesDto;
import mutual.coop.dto.FundsDto;
import mutual.coop.dto.IncomeDto;
import mutual.coop.dto.InterestChargeDto;
import mutual.coop.dto.LoanDto;
import mutual.coop.dto.MemberRequestDto;
import mutual.coop.dto.MutualCoopMemberDto;
import mutual.coop.dto.PolicyDto;
import mutual.coop.dto.PostsDto;
import mutual.coop.dto.UserDto;
import mutual.dao.impl.DistributedInterestImpl;
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
import mutual.dao.impl.OtherIncomeImpl;
import mutual.dao.impl.PostsImpl;
import mutual.dao.impl.ProvinceImpl;
import mutual.dao.impl.UserCategoryImpl;
import mutual.dao.impl.UserImpl;
import mutual.domain.DistributedInterest;
import mutual.domain.Fines;
import mutual.domain.Funds;
import mutual.domain.InterestCharges;
import mutual.domain.LoanRequest;
import mutual.domain.MemberRequest;
import mutual.domain.MutualCoopMembers;
import mutual.domain.MutualCoopPolicy;
import mutual.domain.MutualCooperative;
import mutual.domain.OtherIncome;
import mutual.domain.Posts;
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
	private Fines finees = new Fines();
	FinesImpl finesImpl = new FinesImpl();
	FundsImpl fundsImpl = new FundsImpl();
	private List<FinesDto> finesDtoList, finesStatistic = new ArrayList<FinesDto>();
	private List<Fines> finesList = new ArrayList<Fines>();
	private List<MemberRequestDto> requestDtoList = new ArrayList<MemberRequestDto>();
	private List<MemberRequest> requestList = new ArrayList<MemberRequest>();
	private List<FundsDto> fundDtoList, fundsStat = new ArrayList<FundsDto>();
	private List<Funds> fundsList = new ArrayList<Funds>();
	private List<LoanDto> loanDtoList, loanStatDto = new ArrayList<LoanDto>();
	private LoanRequest request;
	private InterestCharges charges;
	private InterestChargesImpl chargeImpl = new InterestChargesImpl();
	private LoanRequestImpl requestImpl = new LoanRequestImpl();
	private InterestChargeDto chargeDto = new InterestChargeDto();
	private List<InterestChargeDto> chargeDtosList, interestDtosList,
			interestStatDto = new ArrayList<InterestChargeDto>();
	private List<InterestCharges> chargeList = new ArrayList<InterestCharges>();
	private Posts post = new Posts();
	private List<Posts> allposts, listOfPendPost = new ArrayList<Posts>();
	PostsImpl postImpl = new PostsImpl();
	private PostsDto postDtos = new PostsDto();
	private OtherIncome income;
	private OtherIncomeImpl incomeImpl = new OtherIncomeImpl();
	private List<OtherIncome> incomeList = new ArrayList<OtherIncome>();
	private MutualCoopMemberDto mutualCoopMemberDto = new MutualCoopMemberDto();
	private List<PostsDto> mutualpost = new ArrayList<PostsDto>();
	private List<IncomeDto> allIncomeDto,incomeStatDto = new ArrayList<IncomeDto>();
	private String choice;
	private boolean rendered, renderBoard, rendersubmit, memberfunds, fundsform, renderDataTable, renderprofile,
			rendersaveButton, renderCount, renderForeignCountry, fundsDetails, renderdialog, renderNotify,
			renderIncomeTable, renderIncomeForm,printPdf;
	private String option, dt1, dt2;
	private String selection;
	private String fines, interest, amount, mincontribution, fineoncontribution, incomeAmount, receiveAmount,
			sourceOfIncome, overAllFunds, overAllFines, overAllLoan, overAllInterest,overAllIncome;
	private Date recordeddate, date1, date2;
	private int count, days, countPendPost;
	private DistributedInterest distInterest;
	private List<DistributedInterest> distIncomeList = new ArrayList<DistributedInterest>();
	DistributedInterestImpl distInterestImpl = new DistributedInterestImpl();
	private List<DistributedInterestDto> interestDto = new ArrayList<DistributedInterestDto>();

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
		if (null == finees) {
			finees = new Fines();
		}
		if (null == post) {
			post = new Posts();
		}

		if (null == income) {
			income = new OtherIncome();
		}

		if (null == distInterest) {
			distInterest = new DistributedInterest();
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

				allposts = postImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Posts", "postId desc");
				mutualpost = mutualPostList(allposts);
				requestList = memberReqImpl.getGenericListWithHQLParameter(
						new String[] { "requestStatus", "mutualcoop" },
						new Object[] { RequestStatus, mutualMembers.getMutualcoop() }, "MemberRequest",
						"requestDate asc");

				incomeList = incomeImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "OtherIncome", "incomeId desc");

				allIncomeDto = mutualIncomeList(incomeList);

				renderIncomeTable = true;

				count = requestList.size();
				if (count > 0) {
					renderCount = true;
				}
				overallFinesStatistics();
				requestDtoList = mutualCoopFollowers(requestList);

				mutualCoopMembersListDto = mutualCoopMembersDetails();
				this.memberfunds = true;

				loanDtoList = loanRequestDetails();
				finesDtoList = finesDetails();
				chargeDtosList = interestChargesDetailsDetails();

				listOfPendPost = postImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "status" },
						new Object[] { ACTIVE, PENDING }, "Posts", "postId desc");
				if (listOfPendPost.size() > 0) {
					countPendPost = listOfPendPost.size();
					renderNotify = true;
				}
				overallInterestStatistics();
				overallIncomeStatistics();
				interestDtosList = new ArrayList<InterestChargeDto>();
				interestDtosList = showTotInterestReady();
				interestDto = new ArrayList<DistributedInterestDto>();
				interestDto = showMemberTotInterestReceived();
				overallFundsStatistics();
				overallLoanStatistics();
				
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

	public double showTotRegInvest() throws Exception {
		double sum = 0.0;
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		for (Object[] data : fundsImpl.reportList("select sum(f.amount),f.mutualcoop from Funds f where mutualcoop="
				+ mutualMembers.getMutualcoop().getMutualCoopId() + " and f.genericStatus='" + ACTIVE + "'")) {
			sum = sum + Double.parseDouble(data[0] + "");

		}
		LOGGER.info("TOTAL REGULAR INVEST FOR ALL MEMBERS::" + sum);
		return sum;
	}

	public List<InterestChargeDto> showTotInterestReady() throws Exception {
		int countinfo = 1;
		interestDtosList = new ArrayList<InterestChargeDto>();
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		for (Object[] data : chargeImpl.reportList(
				"select sum(inc.amount),inc.status,inc.genericStatus,inc.mutualcoop from InterestCharges inc where inc.status='"
						+ LoanPayStatus + "'and inc.mutualcoop=" + mutualMembers.getMutualcoop().getMutualCoopId()
						+ "")) {
			InterestChargeDto interest = new InterestChargeDto();
			interest.setCountInfo(countinfo);
			if (null != data[0]) {
				interest.setAmount(Double.parseDouble(data[0] + ""));
				interest.setApproval(false);
				interest.setStatus(data[1] + "");
			} else {
				interest.setAmount(Double.parseDouble("0.0"));
				interest.setStatus("Unavailable");
				interest.setApproval(true);
			}
			interestDtosList.add(interest);
			countinfo++;
		}
		return interestDtosList;
	}

	public void overallFundsStatistics() throws Exception {
		// Get the values from the session
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		fundDtoList = new ArrayList<FundsDto>();
		for (Object[] data : fundsImpl
				.reportList("select sum(f.amount),DATE_FORMAT(f.givenDate,'%d/%m/%Y') from Funds f where status='"
						+ FundStatus + "' and f.mutualcoop=" + mutualMembers.getMutualcoop().getMutualCoopId()
						+ " and f.givenDate is not null group by DATE_FORMAT(givenDate,'%d/%m/%Y')")) {
			FundsDto funds = new FundsDto();

			LOGGER.info("::amount::::" + data[0] + "::Date:" + data[1] + "");
			funds.setAmount(Double.parseDouble(data[0] + ""));
			funds.setContDate(data[1] + "");
			fundsStat.add(funds);
		}

		this.overAllFunds = new Gson().toJson(fundsStat);

	}

	public void overallFinesStatistics() throws Exception {
		// Get the values from the session
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		finesStatistic = new ArrayList<FinesDto>();
		for (Object[] data : fundsImpl.reportList("select sum(f.amount),f.status from Fines f where mutualcoop="
				+ mutualMembers.getMutualcoop().getMutualCoopId() + " group by f.status")) {
			FinesDto fines = new FinesDto();
			fines.setAmount(Double.parseDouble(data[0] + ""));
			fines.setStatus(data[1] + "");
			finesStatistic.add(fines);
			LOGGER.info("Amount::" + data[0] + "::Status:" + data[1] + "");
		}

		this.overAllFines = new Gson().toJson(finesStatistic);

	}

	public void overallLoanStatistics() throws Exception {
		// Get the values from the session
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		loanStatDto = new ArrayList<LoanDto>();
		for (Object[] data : fundsImpl.reportList("select sum(l.amount),l.status from LoanRequest l where mutualcoop="
				+ mutualMembers.getMutualcoop().getMutualCoopId() + " group by l.status")) {
			LoanDto loan = new LoanDto();
			loan.setAmount(Double.parseDouble(data[0] + ""));
			loan.setStatus(data[1] + "");
			loanStatDto.add(loan);
			LOGGER.info("Amount::" + data[0] + "::Status:" + data[1] + "");
		}

		this.overAllLoan = new Gson().toJson(loanStatDto);

	}

	public void overallInterestStatistics() throws Exception {
		// Get the values from the session
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		interestStatDto = new ArrayList<InterestChargeDto>();
		for (Object[] data : fundsImpl
				.reportList("select sum(inc.amount),inc.status from InterestCharges inc where inc.mutualcoop="
						+ mutualMembers.getMutualcoop().getMutualCoopId()
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
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		incomeStatDto = new ArrayList<IncomeDto>();
		for (Object[] data : fundsImpl
				.reportList("select  sum(inc.incomeAmount),inc.status from OtherIncome inc where inc.mutualcoop="
						+ mutualMembers.getMutualcoop().getMutualCoopId()
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
	

	public void approveInterestDistribution(DistributedInterestDto income) {
		if (null != income.getUsermember()) {
			/// UPDADING INCOME STATUS TO DESACTIVE

			for (Object[] data : chargeImpl.reportList(
					"select inc.interestId,inc.status,inc.usermember from DistributedInterest inc where inc.usermember="
							+ income.getUsermember().getUserId() + " and inc.status='" + PENDING + "'")) {
				InterestCharges charges = new InterestCharges();
				DistributedInterest interest = new DistributedInterest();
				interest = distInterestImpl.getDistributedInterestById(Integer.parseInt(data[0] + ""), "interestId");
				interest.setStatus(FundStatus);
				distInterestImpl.UpdateDistributedInterest(interest);

				LOGGER.info("UPDATE DONE SUCCESS !!!! FOR INTEREST ID" + charges.getChargeId());
			}
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.interestreceived.successinterest"));
			LOGGER.info(CLASSNAME + ":::ACTION COMPLETE SUCCESSFULL!!");
			LOGGER.info("--------------------------------------------");
		}
	}

	public List<DistributedInterestDto> showMemberTotInterestReceived() throws Exception {
		int countinfo = 1;
		DecimalFormat fmt = new DecimalFormat("###,###.##");
		interestDto = new ArrayList<DistributedInterestDto>();
		mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
				new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
				"from MutualCoopMembers");
		for (Object[] data : chargeImpl.reportList(
				"select inc.interestId,us.fullname,us.phone,sum(inc.amount),inc.givenDate,inc.status,inc.usermember from DistributedInterest inc,Users us  where us.userId=inc.usermember and inc.genericStatus='"
						+ ACTIVE + "' and inc.mutualcoop=" + mutualMembers.getMutualcoop().getMutualCoopId()
						+ " group by inc.usermember order by inc.interestId")) {
			DistributedInterestDto interest = new DistributedInterestDto();
			interest.setCountRecord(countinfo);
			if (null != data[3]) {
				interest.setFullname(data[1] + "");
				interest.setPhone(data[2] + "");
				interest.setStatus(data[5] + "");
				interest.setFormatAmount(fmt.format(Double.parseDouble(data[3] + "")));
				interest.setGivenDate((Timestamp) data[4]);
				interest.setUsermember((Users) data[6]);
				this.printPdf=true;
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
				this.printPdf=false;
			}
			interestDto.add(interest);
			countinfo++;
		}
		return interestDto;
	}

	// Function for interest collected from member for loan taken

	@SuppressWarnings("unchecked")
	public void interestDistribution(InterestChargeDto interest) throws Exception {
		if (null != interest.getAmount()) {
			double distamount = 0.0;
			double totIncome = showTotRegInvest();
			mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
					new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
					"from MutualCoopMembers");

			for (Object[] data : fundsImpl
					.reportList("select f.amount,f.usermember,f.mutualcoop from Funds f where f.mutualcoop="
							+ mutualMembers.getMutualcoop().getMutualCoopId() + " and f.genericStatus='" + ACTIVE
							+ "'")) {
				distamount = (Double.parseDouble(data[0] + "") * interest.getAmount()) / totIncome;
				LOGGER.info("USERMEMBER ::" + ((Users) data[1]).getFullname() + "OBTAINED EQUIV INCOME OF ::::"
						+ distamount);
				Users us = new Users();
				us = ((Users) data[1]);
				MutualCooperative coop = new MutualCooperative();
				coop = ((MutualCooperative) data[2]);
				saveDistributedIncome(us, coop, distamount);
				LOGGER.info("DISTRIBUTION OF INCOME DONE !! SAVING ACTION DONE");

			}

			/// UPDADING INCOME STATUS TO DESACTIVE

			for (Object[] data : chargeImpl.reportList(
					"select inc.chargeId,inc.status,inc.mutualcoop,inc.usermember from InterestCharges inc where inc.status='"
							+ LoanPayStatus + "' and inc.mutualcoop=" + mutualMembers.getMutualcoop().getMutualCoopId()
							+ "")) {
				InterestCharges charges = new InterestCharges();
				charges = chargeImpl.getInterestChargesById(Integer.parseInt(data[0] + ""), "chargeId");
				charges.setGenericStatus(INTERESTSHARED);
				charges.setStatus(INTERESTSHARED);
				chargeImpl.UpdateInterestCharges(charges);

				LOGGER.info("UPDATE DONE SUCCESS !!!! FOR INTEREST ID" + charges.getChargeId());
			}
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.interestDistributed.successinterest"));
			LOGGER.info(CLASSNAME + ":::ACTION COMPLETE SUCCESSFULL!!");
			LOGGER.info("--------------------------------------------");

		}

	}

	@SuppressWarnings("unchecked")
	public void incomeDistribution(IncomeDto amountrecord) throws Exception {
		if (null != amountrecord.getRecordedAmount()) {
			double distamount = 0.0;
			double totIncome = showTotRegInvest();
			mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
					new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
					"from MutualCoopMembers");

			for (Object[] data : fundsImpl
					.reportList("select f.amount,f.usermember,f.mutualcoop from Funds f where f.mutualcoop="
							+ mutualMembers.getMutualcoop().getMutualCoopId() + " and f.genericStatus='" + ACTIVE
							+ "'")) {
				distamount = (Double.parseDouble(data[0] + "") * amountrecord.getRecordedAmount()) / totIncome;
				LOGGER.info("USERMEMBER ::" + ((Users) data[1]).getFullname() + "OBTAINED EQUIV INCOME OF ::::"
						+ distamount);
				Users us = new Users();
				us = ((Users) data[1]);
				MutualCooperative coop = new MutualCooperative();
				coop = ((MutualCooperative) data[2]);
				saveDistributedIncome(us, coop, distamount);
				LOGGER.info("DISTRIBUTION OF INCOME DONE !! SAVING ACTION DONE");

			}

			/// UPDADING INCOME STATUS TO DESACTIVE
			OtherIncome income = new OtherIncome();
			income = incomeImpl.getOtherIncomeById(amountrecord.getIncomeId(), "incomeId");
			income.setStatus(DESACTIVE);
			income.setUpdatedBy(usersSession.getFullname());
			income.setUpDtTime(timestamp);
			incomeImpl.UpdateOtherIncome(income);
			incomeList = incomeImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "OtherIncome", "incomeId desc");

			allIncomeDto = mutualIncomeList(incomeList);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.incomeDistributed.successincome"));
			LOGGER.info(CLASSNAME + ":::ACTION COMPLETE SUCCESSFULL!!");
			LOGGER.info("--------------------------------------------");

		}

	}

	public String saveDistributedIncome(Users members, MutualCooperative coop, Double amountEarn) throws Exception {
		try {
			try {
				if (null != members && null != coop && null != amountEarn) {
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
			distInterest.setGenericStatus(ACTIVE);
			distInterest.setStatus(PENDING);
			distInterest.setCreatedBy(usersSession.getFullname());
			distInterest.setCrtdDtTime(timestamp);
			distInterest.setAmount(amountEarn);
			distInterest.setGivenDate(timestamp);
			distInterest.setMutualcoop(coop);
			distInterest.setUsermember(members);
			distInterestImpl.saveInterest(distInterest);
			clearDistributedIncomeFuileds();

			return "";

		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::Income Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings({ "unchecked", "static-access" })
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

					Formating fms = new Formating();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date today = fms.getCurrentDateFormtNOTime();

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
						request.setReturnDate(loan.getReturnDate());
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
							simpleDateFormat.format(loan.getReturnDate());
							days = fms.daysBetween(today, loan.getReturnDate());
							request.setDayremaining(days);
							LOGGER.info(":::DAYS REMAINING:::::" + days);
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

	@SuppressWarnings("unchecked")
	public List<FinesDto> finesDetails() {

		try {
			if (usersSession.getUserCategory().getUserCatid() == MutualRepcat) {

				MutualCoopMembers members = new MutualCoopMembers();
				members = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
						new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
						"from MutualCoopMembers");
				if (null != members) {
					finesList = finesImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "mutualcoop" },
							new Object[] { ACTIVE, members.getMutualcoop() }, "Fines", "givenDate asc");

					finesDtoList = new ArrayList<FinesDto>();
					DecimalFormat fmt = new DecimalFormat("###,###.##");
					for (Fines fines : finesList) {
						FinesDto fine = new FinesDto();
						fine.setEditable(false);
						fine.setFineId(fines.getFineId());
						fine.setMutualcoop(fines.getMutualcoop());
						fine.setUsermember(fines.getUsermember());
						fine.setGeneriStatus(fines.getGenericStatus());
						fine.setFormatBalance(fmt.format(fines.getAmount()));
						fine.setGivenDate(fines.getGivenDate());
						fine.setRecordedBy(fines.getCreatedBy());
						fine.setStatus(fines.getStatus());
						if (fines.getStatus().equalsIgnoreCase(PENDING)) {
							fine.setApproval(false);
						} else {
							fine.setApproval(true);
						}
						if (fines.getStatus().equalsIgnoreCase(LoanPayStatus)) {
							fine.setAccept(false);
						} else {
							fine.setAccept(true);
						}
						if (fines.getStatus().equalsIgnoreCase(REJECTED)) {
							fine.setPaidStatus(false);
						} else {
							fine.setPaidStatus(true);
						}

						finesDtoList.add(fine);
					}
				}
			}

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
		return (finesDtoList);
	}

	@SuppressWarnings("unchecked")
	public List<InterestChargeDto> interestChargesDetailsDetails() {

		try {
			if (usersSession.getUserCategory().getUserCatid() == MutualRepcat) {

				MutualCoopMembers members = new MutualCoopMembers();
				members = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
						new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
						"from MutualCoopMembers");
				if (null != members) {
					chargeList = chargeImpl.getGenericListWithHQLParameter(
							new String[] { "genericStatus", "mutualcoop" },
							new Object[] { ACTIVE, members.getMutualcoop() }, "InterestCharges", "crtdDtTime asc");
					chargeDtosList = new ArrayList<InterestChargeDto>();

					DecimalFormat fmt = new DecimalFormat("###,###.##");
					for (InterestCharges charge : chargeList) {
						InterestChargeDto interest = new InterestChargeDto();
						interest.setEditable(false);
						interest.setChargeId(charge.getChargeId());
						interest.setMutualcoop(charge.getMutualcoop());
						interest.setUsermember(charge.getUsermember());
						interest.setGeneriStatus(charge.getGenericStatus());
						interest.setFormatBalance(fmt.format(charge.getAmount()));
						interest.setCreatedDate(charge.getCrtdDtTime());
						interest.setRecordedBy(charge.getCreatedBy());
						interest.setStatus(charge.getStatus());
						if (charge.getStatus().equalsIgnoreCase(PENDING)) {
							interest.setApproval(false);
						} else {
							interest.setApproval(true);
						}
						if (charge.getStatus().equalsIgnoreCase(LoanPayStatus)) {
							interest.setAccept(false);
						} else {
							interest.setAccept(true);
						}
						if (charge.getStatus().equalsIgnoreCase(REJECTED)) {
							interest.setPaidStatus(false);
						} else {
							interest.setPaidStatus(true);
						}

						chargeDtosList.add(interest);
					}
				}
			}

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
		return (chargeDtosList);
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

	public List<PostsDto> mutualPostList(List<Posts> list) {

		mutualpost = new ArrayList<PostsDto>();
		int i = 1;
		for (Posts p : list) {

			PostsDto pdto = new PostsDto();
			pdto.setPostId(p.getPostId());
			pdto.setDescription(p.getDescription());
			pdto.setGeneriStatus(p.getGenericStatus());
			pdto.setCreatedDate(p.getPostDate());
			pdto.setMutualcoop(p.getMutualcoop());
			pdto.setUsermember(p.getMember());
			pdto.setEditable(false);
			pdto.setCountInfo(i);
			pdto.setStatus(p.getStatus());
			pdto.setRecordedBy(p.getCreatedBy());
			if (p.getStatus().equalsIgnoreCase(ACTIVE)) {
				pdto.setAccept(false);
			} else {
				pdto.setAccept(true);
			}
			if (p.getStatus().equalsIgnoreCase(PENDING)) {
				pdto.setApproval(false);

			} else {

				pdto.setApproval(true);
			}
			if (p.getStatus().equalsIgnoreCase(Block)) {
				pdto.setPaidStatus(false);

			} else {

				pdto.setPaidStatus(true);
			}
			mutualpost.add(pdto);
			i++;
		}
		return (mutualpost);
	}

	public List<IncomeDto> mutualIncomeList(List<OtherIncome> list) {

		allIncomeDto = new ArrayList<IncomeDto>();
		int i = 1;
		for (OtherIncome inc : list) {
			IncomeDto incDto = new IncomeDto();
			incDto.setIncomeId(inc.getIncomeId());
			incDto.setSourceOfIncome(inc.getSourceOfIncome());
			incDto.setIncomeAmount(inc.getIncomeAmount());
			incDto.setRecordedAmount(inc.getRecordedAmount());
			incDto.setGeneriStatus(inc.getGenericStatus());
			incDto.setStatus(inc.getStatus());
			incDto.setCountRecord(i);

			if (inc.getStatus().equalsIgnoreCase(DESACTIVE)) {
				incDto.setAccept(false);
			} else {
				incDto.setAccept(true);
			}
			if (inc.getStatus().equalsIgnoreCase(PENDING)) {
				incDto.setApproval(false);

			} else {

				incDto.setApproval(true);
			}

			allIncomeDto.add(incDto);
			i++;
		}
		return (allIncomeDto);
	}

	public void showIncomeForm() {
		this.renderIncomeTable = false;
		this.renderIncomeForm = true;
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

	public void doInterestAction(InterestChargeDto charge) {
		if (null != charge) {
			LOGGER.info("::::LOAN INFO::: " + charge);
			HttpSession sessionmember = SessionUtils.getSession();
			sessionmember.setAttribute("interstinfo", charge);
			this.memberfunds = false;
			this.renderdialog = true;
		}
	}

	public void doFineAction(FinesDto fines) {
		if (null != fines) {
			LOGGER.info("::::Fines INFO::: " + fines);
			HttpSession sessionmember = SessionUtils.getSession();
			sessionmember.setAttribute("fineinfo", fines);
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

	public void newPost() {
		this.rendered = false;
		renderForeignCountry = true;
		renderDataTable = false;
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

	public String saveIncome() throws Exception {
		try {
			try {
				if (Double.parseDouble(incomeAmount) > 0 && Double.parseDouble(receiveAmount) > 0
						&& null != sourceOfIncome) {
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

			mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
					new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
					"from MutualCoopMembers");
			income.setGenericStatus(ACTIVE);
			income.setStatus(PENDING);
			income.setCreatedBy(usersSession.getFullname());
			income.setCrtdDtTime(timestamp);
			income.setMutualcoop(mutualMembers.getMutualcoop());
			income.setRecordedAmount(Double.parseDouble(receiveAmount));
			income.setIncomeAmount(Double.parseDouble(incomeAmount));
			income.setSourceOfIncome(sourceOfIncome);
			incomeImpl.saveOtherIncome(income);

			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.income.successincome"));
			LOGGER.info(CLASSNAME + ":::No fines Details is saved");
			clearIncomeFuileds();

			return "";

		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::Income Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings({ "unchecked", "unchecked" })
	public String savePost() throws Exception {
		try {
			try {
				mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
						new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
						"from MutualCoopMembers");
				if (null != post.getDescription() && null != mutualMembers) {
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

			post.setCreatedBy(usersSession.getFullname());
			post.setGenericStatus(ACTIVE);
			post.setStatus(PENDING);
			post.setPostDate(timestamp);
			post.setCrtdDtTime(timestamp);
			post.setMember(usersImpl.gettUserById(usersSession.getUserId(), "userId"));
			post.setMutualcoop(mutualMembers.getMutualcoop());
			postImpl.savePosts(post);
			listOfPendPost = postImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "status" },
					new Object[] { ACTIVE, PENDING }, "Posts", "postId desc");
			countPendPost = listOfPendPost.size();
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.post"));
			LOGGER.info(CLASSNAME + ":::post Details is saved");
			clearPostFuileds();
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::Post Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public String saveMembersPost() throws Exception {
		try {
			try {

				HttpSession session = SessionUtils.getSession();
				mutualCoopMemberDto = (MutualCoopMemberDto) session.getAttribute("coopdetails");
				if (null != post.getDescription() && null != mutualCoopMemberDto) {
					LOGGER.info(CLASSNAME + "INFORMATION VALID! COOP DETAILS"
							+ mutualCoopMemberDto.getMutualcoop().getMutualCoopId());
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

			post.setCreatedBy(usersSession.getFullname());
			post.setGenericStatus(ACTIVE);
			post.setStatus(PENDING);
			post.setPostDate(timestamp);
			post.setCrtdDtTime(timestamp);
			post.setMember(usersImpl.gettUserById(usersSession.getUserId(), "userId"));
			post.setMutualcoop(mutualCoopMemberDto.getMutualcoop());
			postImpl.savePosts(post);
			listOfPendPost = postImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "status" },
					new Object[] { ACTIVE, PENDING }, "Posts", "postId desc");
			countPendPost = listOfPendPost.size();
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.post"));
			LOGGER.info(CLASSNAME + ":::post Details is saved");
			clearPostFuileds();
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::Post Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("static-access")
	public void saveMemberFunds() {
		try {
			int days = 0;
			Formating fmt = new Formating();
			Funds funds = new Funds();
			funds = fundsImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember", "mutualcoop" },
					new Object[] { ACTIVE, coopMemberDto.getMember(), coopMemberDto.getMutualcoop() }, "from Funds");
			try {
				policy = policyImpl.getModelWithMyHQL(new String[] { "genericStatus", "mutualcoop" },
						new Object[] { ACTIVE, mutualMembers.getMutualcoop() }, "from MutualCoopPolicy");
				if (Double.parseDouble(amount) == 0) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error.validdata"));
					this.renderBoard = false;
				}
				HttpSession session = SessionUtils.getSession();
				coopMemberDto = (MutualCoopMemberDto) session.getAttribute("memberfunds");

				if (null == coopMemberDto) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers
							.addErrorMessage(getProvider().getValue("com.server.side.internal.membernotfounderror"));
					this.renderBoard = false;
				}

				if (Double.parseDouble(amount) < policy.getMinContribution()) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(
							getProvider().getValue("com.server.side.internal.membercontributionerror"));
					this.renderBoard = true;

				}

				SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
				dt1 = smf.format(new Date());
				dt2 = smf.format(funds.getGivenDate());
				date1 = smf.parse(dt1);
				date2 = smf.parse(dt2);
				LOGGER.info("TODAY DATE:::" + smf.format(date1));
				LOGGER.info("::::Yesterday DATE::::" + smf.format(date2));
				if (date1.compareTo(date2) == 0) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers
							.addErrorMessage(getProvider().getValue("com.server.side.internal.membercontributederror"));
					this.renderBoard = false;
				}
			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
			}
			// GETTING MEMBERS INFO WHO PROVIDE CONTRIBUTION

			mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
					new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
					"from MutualCoopMembers");
			policy = policyImpl.getModelWithMyHQL(new String[] { "genericStatus", "mutualcoop" },
					new Object[] { ACTIVE, mutualMembers.getMutualcoop() }, "from MutualCoopPolicy");

			if (null == funds && Double.parseDouble(amount) >= policy.getMinContribution()) {
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
				this.renderBoard = false;
			} else if (null != funds && Double.parseDouble(amount) >= policy.getMinContribution()) {

				if (date1.compareTo(date2) > 0) {
					days = fmt.daysBetween(date2, date1);
					LOGGER.info("DAYS BETWEEN :::::" + days);
					if (days == 1) {
						/// updating member existing funds previous
						funds.setGenericStatus(DESACTIVE);
						fundsImpl.UpdateFunds(funds);
						LOGGER.info("::::PREVIOUS FUND UPDATED TO DESACTIVE STATUS:::");

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
						this.renderBoard = false;
					} else {
						/// updating member existing funds previous
						funds.setGenericStatus(DESACTIVE);
						fundsImpl.UpdateFunds(funds);
						LOGGER.info("::::PREVIOUS FUND UPDATED TO DESACTIVE STATUS:::");

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
						finees.setAmount(policy.getFineondelay() * (days - 1));
						finees.setMutualcoop(coopMemberDto.getMutualcoop());
						finees.setStatus(PENDING);
						finesImpl.saveFines(finees);
						clearFundsFuileds();
						JSFMessagers.resetMessages();
						setValid(true);
						JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.fineesoncontribution"));
						LOGGER.info(CLASSNAME + ":::Fines Details is saved");
						this.renderBoard = false;
					}
				}

			}

		} catch (Exception ex) {
			LOGGER.info(CLASSNAME + ":::User Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
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
						LOGGER.info("DAYS BETWEEN :::::" + days);
						if (days == 1 && Double.parseDouble(amount) >= policy.getMinContribution()) {
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
						} else {
							if (Double.parseDouble(amount) >= policy.getMinContribution()) {
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
								finees.setAmount(policy.getFineondelay() * (days - 1));
								finees.setMutualcoop(coopMemberDto.getMutualcoop());
								finees.setStatus(PENDING);
								finesImpl.saveFines(finees);
								JSFMessagers.resetMessages();
								setValid(true);
								JSFMessagers
										.addErrorMessage(getProvider().getValue("com.save.form.fineesoncontribution"));
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

	public void clearPostFuileds() {
		post = new Posts();
	}

	public void clearFundsFuileds() {
		memberFunds = new Funds();
		finees = new Fines();
		amount = null;
	}

	public void clearIncomeFuileds() {
		income = new OtherIncome();
		receiveAmount = new String();
		incomeAmount = new String();
		sourceOfIncome = new String();
	}

	public void clearDistributedIncomeFuileds() {
		distInterest = new DistributedInterest();
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

	public String editPostAction(PostsDto post) {
		post.setEditable(true);
		return null;
	}

	public String cancelPost(PostsDto post) {
		post.setEditable(false);
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

	@SuppressWarnings("unchecked")
	public String savePostAction(PostsDto post) throws Exception {
		LOGGER.info("update  saveAction method");
		if (policy != null) {
			Posts pol = new Posts();

			pol = postImpl.getPostsById(post.getPostId(), "postId");
			LOGGER.info("here update sart for " + pol + " PiD " + pol.getPostId());
			if (null != pol) {

				post.setEditable(false);
				pol.setDescription(post.getDescription());
				pol.setUpdatedBy(usersSession.getFullname());
				pol.setUpDtTime(timestamp);
				pol.setGenericStatus(post.getGeneriStatus());
				postImpl.UpdatePosts(pol);
				allposts = postImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Posts", "postId desc");
				mutualpost = mutualPostList(allposts);
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

	@SuppressWarnings("unchecked")
	public String blockPostAction(PostsDto post) throws Exception {
		LOGGER.info("update  saveAction method");
		if (post != null) {
			Posts p = new Posts();
			p = postImpl.getPostsById(post.getPostId(), "postId");
			LOGGER.info("here update sart for " + p + " postiD " + p.getPostId());
			if (null != p) {
				p.setStatus(Block);
				p.setUpdatedBy(usersSession.getViewId());
				p.setUpDtTime(timestamp);
				postImpl.UpdatePosts(p);

				allposts = postImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Posts", "postId desc");
				mutualpost = mutualPostList(allposts);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("blockpost.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroblock.changed.message"));
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public String unBlockPostAction(PostsDto post) throws Exception {
		LOGGER.info("update  saveAction method");
		if (post != null) {
			Posts p = new Posts();
			p = postImpl.getPostsById(post.getPostId(), "postId");
			LOGGER.info("here update sart for " + p + " postiD " + p.getPostId());
			if (null != p) {
				p.setStatus(DESACTIVE);
				p.setUpdatedBy(usersSession.getViewId());
				p.setUpDtTime(timestamp);
				postImpl.UpdatePosts(p);

				allposts = postImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Posts", "postId desc");
				mutualpost = mutualPostList(allposts);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("blockpost.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroblock.changed.message"));
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public String publishAction(PostsDto post) throws Exception {
		LOGGER.info("update  saveAction method");
		if (post != null) {
			Posts p = new Posts();
			p = postImpl.getPostsById(post.getPostId(), "postId");
			LOGGER.info("here update sart for " + p + " postiD " + p.getPostId());
			if (null != p) {
				p.setStatus(ACTIVE);
				p.setUpdatedBy(usersSession.getViewId());
				p.setUpDtTime(timestamp);
				postImpl.UpdatePosts(p);

				allposts = postImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Posts", "postId desc");
				mutualpost = mutualPostList(allposts);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("publishpost.changed.message"));
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

	public String rejectInterestAction(InterestChargeDto interest) {
		LOGGER.info("update  saveAction method");
		if (interest != null) {
			InterestCharges req = new InterestCharges();
			req = chargeImpl.getInterestChargesById(interest.getChargeId(), "chargeId");

			LOGGER.info("++++++++++++++++++++++++++here update sart for " + req + " useriD " + req.getChargeId());
			if (null != req) {

				req.setStatus(REJECTED);
				req.setUpdatedBy(usersSession.getFullname());
				req.setUpDtTime(timestamp);
				chargeImpl.UpdateInterestCharges(req);
				chargeDtosList = new ArrayList<InterestChargeDto>();
				chargeDtosList = interestChargesDetailsDetails();
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("interest.reject.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroblock.changed.message"));
		}

		return null;
	}

	public String rejectFineAction(FinesDto fines) {
		LOGGER.info("update  saveAction method");
		if (fines != null) {
			LoanRequest req = new LoanRequest();
			Fines fine = new Fines();
			fine = finesImpl.getFinesById(fines.getFineId(), "fineId");

			LOGGER.info("++++++++++++++++++++++++++here update sart for " + fine + " useriD " + fine.getFineId());
			if (null != fine) {

				fine.setStatus(REJECTED);
				fine.setUpdatedBy(usersSession.getFullname());
				fine.setUpDtTime(timestamp);
				finesImpl.UpdateFines(fine);
				finesDtoList = new ArrayList<FinesDto>();
				finesDtoList = finesDetails();
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("fine.reject.changed.message"));
			}
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("erroblock.changed.message"));
		}

		return null;
	}

	public String finesPaymentAction() throws Exception {
		LOGGER.info("update  saveAction method");
		HttpSession session = SessionUtils.getSession();
		FinesDto fine = new FinesDto();
		fine = (FinesDto) session.getAttribute("fineinfo");
		if (fine != null) {
			Fines req = new Fines();
			req = finesImpl.getFinesById(fine.getFineId(), "fineId");
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + req + " useriD " + req.getFineId());
			if (null != req) {
				req.setStatus(LoanPayStatus);
				req.setUpdatedBy(usersSession.getFullname());
				req.setUpDtTime(timestamp);
				finesImpl.UpdateFines(req);
				finesDtoList = new ArrayList<FinesDto>();
				finesDtoList = finesDetails();
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

	public String interestPaymentAction() throws Exception {
		LOGGER.info("update  saveAction method");
		HttpSession session = SessionUtils.getSession();
		InterestChargeDto interest = new InterestChargeDto();
		interest = (InterestChargeDto) session.getAttribute("interstinfo");
		if (interest != null) {
			InterestCharges req = new InterestCharges();
			req = chargeImpl.getInterestChargesById(interest.getChargeId(), "chargeId");
			LOGGER.info("++++++++++++++++++++++++++here update sart for " + req + " useriD " + req.getChargeId());
			if (null != req) {
				req.setStatus(LoanPayStatus);
				req.setUpdatedBy(usersSession.getFullname());
				req.setUpDtTime(timestamp);
				chargeImpl.UpdateInterestCharges(req);
				chargeDtosList = new ArrayList<InterestChargeDto>();
				chargeDtosList = interestChargesDetailsDetails();
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

	
	
	// CREATING FOOTER AND HEADER FOR PAGES

		class MyFooter extends PdfPageEventHelper {

			Font ffont1 = new Font(Font.FontFamily.UNDEFINED, 12, Font.ITALIC);

			Font ffont2 = new Font(Font.FontFamily.COURIER, 16, Font.ITALIC);

			public void onEndPage(PdfWriter writer, Document document) {

				try {
					PdfContentByte cb = writer.getDirectContent();
					Phrase header = new Phrase("");
					document.add(new Paragraph("\n"));
					Phrase footer = new Phrase("@Copyright MUTUAL COOPERATIVE...!\n", ffont2);
					ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header,
							(document.right() - document.left()) / 2 + document.leftMargin(), document.top() + 10, 0);
					ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
							(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);

				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		// Codes for creating the table and its contents
	
	public void membersInterestRecievedPdf() throws IOException, DocumentException {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		String xdate = dt.format(date);
		FacesContext context = FacesContext.getCurrentInstance();
		Document document = new Document();
		Rectangle rect = new Rectangle(20, 20, 800, 600);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		MyFooter event = new MyFooter();
		writer.setPageEvent(event);
		writer.setBoxSize("art", rect);
		document.setPageSize(rect);
		if (!document.isOpen()) {
			document.open();
		}
		Image img1 = Image.getInstance("D:\\Projects\\Nshimiye\\Project\\src\\main\\webapp\\resources\\image\\MCMSLOG2.jpg");
		document.add(img1);
		document.add(new Paragraph("\n"));

		Font font0 = new Font(Font.FontFamily.COURIER, 14, Font.ITALIC);
		PdfPTable table = new PdfPTable(6);

		table.setWidthPercentage(107);
		Paragraph header1 = new Paragraph("MEMBER INTEREST REPORT MADE ON  " + xdate + "",font0);
		// header.setAlignment(Element.ALIGN_RIGHT);
		header1.setAlignment(Element.ALIGN_CENTER);
		// header.add(header1);
		document.add(header1);
		document.add(new Paragraph("                                        "));
		// String myBoardName=t.getBoardName();

		PdfPCell pc = new PdfPCell(new Paragraph("MUTUAL COOPERATIVE MEMBERS INTEREST REPORT",font0));
		pc.setColspan(6);
		pc.setBackgroundColor(BaseColor.CYAN);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(pc);

		PdfPCell pc1 = new PdfPCell(new Paragraph("No", font0));
		pc1.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc1);

		PdfPCell pc2 = new PdfPCell(new Paragraph("Member names", font0));
		pc2.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc2);

		PdfPCell pc3 = new PdfPCell(new Paragraph("Phone number", font0));
		pc3.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc3);
		
		PdfPCell pc4 = new PdfPCell(new Paragraph("Total interest", font0));
		pc4.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc4);
		
		PdfPCell pc5= new PdfPCell(new Paragraph("Given date", font0));
		pc5.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc5);
		
		PdfPCell pc6 = new PdfPCell(new Paragraph("Interest status", font0));
		pc6.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc6);
		table.setHeaderRows(1);
		int index = 1;
		try {
			DecimalFormat fmt = new DecimalFormat("###,###.##");
			mutualMembers = mutualMembersImpl.getModelWithMyHQL(new String[] { "genericStatus", "usermember" },
					new Object[] { ACTIVE, usersImpl.gettUserById(usersSession.getUserId(), "userId") },
					"from MutualCoopMembers");
			for (Object[] data : chargeImpl.reportList(
					"select inc.interestId,us.fullname,us.phone,sum(inc.amount),inc.givenDate,inc.status,inc.usermember from DistributedInterest inc,Users us  where us.userId=inc.usermember and inc.genericStatus='"
							+ ACTIVE + "' and inc.mutualcoop=" + mutualMembers.getMutualcoop().getMutualCoopId()
							+ " group by inc.usermember order by inc.interestId")) {
				if (null != data[3]) {
					table.addCell(index + "");
					table.addCell(data[1] + "");
					table.addCell(data[2] + "");
					table.addCell(fmt.format(Double.parseDouble(data[3] + "")));
					table.addCell(dt.format((Timestamp)data[4]));
					table.addCell(data[5] + "");
				
				} else {
				}
				index++;
			}
			document.add(table);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph(new Chunk("Printed By:  " + usersSession.getFullname()+ "",
					FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.ITALIC, BaseColor.ORANGE))));
			document.close();

			writePDFToResponse(context.getExternalContext(), baos, "MCREP_REPORT");

			context.responseComplete();

		} catch (Exception e) {

			LOGGER.info(e.getMessage() + "Internal server error");
			e.printStackTrace();
		}
	}
	
	
	public void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName) {
		try {
			externalContext.responseReset();
			externalContext.setResponseContentType("application/pdf");
			externalContext.setResponseHeader("Expires", "0");
			externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			externalContext.setResponseHeader("Pragma", "public");
			externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
			externalContext.setResponseContentLength(baos.size());
			OutputStream out = externalContext.getResponseOutputStream();
			baos.writeTo(out);
			externalContext.responseFlushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public List<FinesDto> getFinesDtoList() {
		return finesDtoList;
	}

	public void setFinesDtoList(List<FinesDto> finesDtoList) {
		this.finesDtoList = finesDtoList;
	}

	public List<Fines> getFinesList() {
		return finesList;
	}

	public void setFinesList(List<Fines> finesList) {
		this.finesList = finesList;
	}

	public InterestChargeDto getChargeDto() {
		return chargeDto;
	}

	public void setChargeDto(InterestChargeDto chargeDto) {
		this.chargeDto = chargeDto;
	}

	public List<InterestChargeDto> getChargeDtosList() {
		return chargeDtosList;
	}

	public void setChargeDtosList(List<InterestChargeDto> chargeDtosList) {
		this.chargeDtosList = chargeDtosList;
	}

	public List<InterestCharges> getChargeList() {
		return chargeList;
	}

	public void setChargeList(List<InterestCharges> chargeList) {
		this.chargeList = chargeList;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
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

	public MutualCoopMemberDto getMutualCoopMemberDto() {
		return mutualCoopMemberDto;
	}

	public void setMutualCoopMemberDto(MutualCoopMemberDto mutualCoopMemberDto) {
		this.mutualCoopMemberDto = mutualCoopMemberDto;
	}

	public List<Posts> getListOfPendPost() {
		return listOfPendPost;
	}

	public void setListOfPendPost(List<Posts> listOfPendPost) {
		this.listOfPendPost = listOfPendPost;
	}

	public int getCountPendPost() {
		return countPendPost;
	}

	public void setCountPendPost(int countPendPost) {
		this.countPendPost = countPendPost;
	}

	public boolean isRenderNotify() {
		return renderNotify;
	}

	public void setRenderNotify(boolean renderNotify) {
		this.renderNotify = renderNotify;
	}

	public String getDt1() {
		return dt1;
	}

	public void setDt1(String dt1) {
		this.dt1 = dt1;
	}

	public String getDt2() {
		return dt2;
	}

	public void setDt2(String dt2) {
		this.dt2 = dt2;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public OtherIncome getIncome() {
		return income;
	}

	public void setIncome(OtherIncome income) {
		this.income = income;
	}

	public OtherIncomeImpl getIncomeImpl() {
		return incomeImpl;
	}

	public void setIncomeImpl(OtherIncomeImpl incomeImpl) {
		this.incomeImpl = incomeImpl;
	}

	public List<OtherIncome> getIncomeList() {
		return incomeList;
	}

	public void setIncomeList(List<OtherIncome> incomeList) {
		this.incomeList = incomeList;
	}

	public String getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(String incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public String getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(String receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public String getSourceOfIncome() {
		return sourceOfIncome;
	}

	public void setSourceOfIncome(String sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}

	public boolean isRenderIncomeTable() {
		return renderIncomeTable;
	}

	public void setRenderIncomeTable(boolean renderIncomeTable) {
		this.renderIncomeTable = renderIncomeTable;
	}

	public boolean isRenderIncomeForm() {
		return renderIncomeForm;
	}

	public void setRenderIncomeForm(boolean renderIncomeForm) {
		this.renderIncomeForm = renderIncomeForm;
	}

	public List<IncomeDto> getAllIncomeDto() {
		return allIncomeDto;
	}

	public void setAllIncomeDto(List<IncomeDto> allIncomeDto) {
		this.allIncomeDto = allIncomeDto;
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

	public List<InterestChargeDto> getInterestDtosList() {
		return interestDtosList;
	}

	public void setInterestDtosList(List<InterestChargeDto> interestDtosList) {
		this.interestDtosList = interestDtosList;
	}

	public List<DistributedInterestDto> getInterestDto() {
		return interestDto;
	}

	public void setInterestDto(List<DistributedInterestDto> interestDto) {
		this.interestDto = interestDto;
	}

	public String getOverAllFunds() {
		return overAllFunds;
	}

	public void setOverAllFunds(String overAllFunds) {
		this.overAllFunds = overAllFunds;
	}

	public List<FundsDto> getFundsStat() {
		return fundsStat;
	}

	public void setFundsStat(List<FundsDto> fundsStat) {
		this.fundsStat = fundsStat;
	}

	public List<FinesDto> getFinesStatistic() {
		return finesStatistic;
	}

	public void setFinesStatistic(List<FinesDto> finesStatistic) {
		this.finesStatistic = finesStatistic;
	}

	public String getOverAllFines() {
		return overAllFines;
	}

	public void setOverAllFines(String overAllFines) {
		this.overAllFines = overAllFines;
	}

	public List<LoanDto> getLoanStatDto() {
		return loanStatDto;
	}

	public void setLoanStatDto(List<LoanDto> loanStatDto) {
		this.loanStatDto = loanStatDto;
	}

	public String getOverAllLoan() {
		return overAllLoan;
	}

	public void setOverAllLoan(String overAllLoan) {
		this.overAllLoan = overAllLoan;
	}

	public List<InterestChargeDto> getInterestStatDto() {
		return interestStatDto;
	}

	public void setInterestStatDto(List<InterestChargeDto> interestStatDto) {
		this.interestStatDto = interestStatDto;
	}

	public String getOverAllInterest() {
		return overAllInterest;
	}

	public void setOverAllInterest(String overAllInterest) {
		this.overAllInterest = overAllInterest;
	}

	public List<IncomeDto> getIncomeStatDto() {
		return incomeStatDto;
	}

	public void setIncomeStatDto(List<IncomeDto> incomeStatDto) {
		this.incomeStatDto = incomeStatDto;
	}

	public String getOverAllIncome() {
		return overAllIncome;
	}

	public void setOverAllIncome(String overAllIncome) {
		this.overAllIncome = overAllIncome;
	}

	public boolean isPrintPdf() {
		return printPdf;
	}

	public void setPrintPdf(boolean printPdf) {
		this.printPdf = printPdf;
	}	
	
	
}

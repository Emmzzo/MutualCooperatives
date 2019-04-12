/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.ILoanRequest;
import mutual.dao.interfc.IUsers;
import mutual.domain.LoanRequest;
import mutual.domain.Users;

/**
 *
 * @author Ngango
 */

public class LoanRequestImpl extends AbstractDao<Long, LoanRequest> implements ILoanRequest {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public String myNane() {
		return "Nshimiye Emma";
	}
	@Override
	public LoanRequest saveLoanRequest(LoanRequest request) {
		return saveIntable(request);
	}

	@Override
	public List<LoanRequest> getLoanRequest() {
		return (List<LoanRequest>) (Object) getModelList();
	}

	@Override
	public LoanRequest getLoanRequestById(int request, String primaryKeyclomunName) {
		return (LoanRequest) getModelById(request, primaryKeyclomunName);
	}

	@Override
	public LoanRequest UpdateLoanRequest(LoanRequest request) {
		return updateIntable(request);
	}
}

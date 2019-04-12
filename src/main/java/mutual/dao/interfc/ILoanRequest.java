/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;

import java.util.List;

import mutual.domain.LoanRequest;
import mutual.domain.Sector;

/**
 *
 * @author Emmanuel
 */
public interface ILoanRequest {
	public LoanRequest saveLoanRequest(LoanRequest request);

	public List<LoanRequest> getLoanRequest();

	public LoanRequest getLoanRequestById(int request, String primaryKeyclomunName);

	public LoanRequest UpdateLoanRequest(LoanRequest request);
}

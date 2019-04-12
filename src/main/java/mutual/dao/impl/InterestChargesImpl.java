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
import mutual.dao.interfc.IFunds;
import mutual.dao.interfc.IInterestCharges;
import mutual.dao.interfc.IUsers;
import mutual.domain.Funds;
import mutual.domain.InterestCharges;
import mutual.domain.Users;

/**
 *
 * @author Ngango
 */

public class InterestChargesImpl extends AbstractDao<Long, InterestCharges> implements IInterestCharges {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	@Override
	public String myNane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterestCharges saveInterestCharges(InterestCharges charge) {
		return saveIntable(charge);
	}

	@Override
	public List<InterestCharges> getListInterestCharges() {
		return (List<InterestCharges>) (Object) getModelList();
	}

	@Override
	public InterestCharges getInterestChargesById(int chargeId, String primaryKeyclomunName) {
		return (InterestCharges) getModelById(chargeId, primaryKeyclomunName);
	}

	@Override
	public InterestCharges UpdateInterestCharges(InterestCharges charges) {
		return updateIntable(charges);
	}

	@Override
	public InterestCharges getInterestChargesWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (InterestCharges) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
}

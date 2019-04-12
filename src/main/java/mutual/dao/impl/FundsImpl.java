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
import mutual.dao.interfc.IUsers;
import mutual.domain.Funds;
import mutual.domain.Users;

/**
 *
 * @author Ngango
 */

public class FundsImpl extends AbstractDao<Long, Funds> implements IFunds {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	@Override
	public Funds saveFunds(Funds funds) {
		return saveIntable(funds);
	}

	@Override
	public List<Funds> getListFunds() {
		return (List<Funds>) (Object) getModelList();
	}

	@Override
	public Funds getFundsById(int findId, String primaryKeyclomunName) {
		return (Funds) getModelById(findId, primaryKeyclomunName);
	}

	@Override
	public Funds UpdateFunds(Funds funds) {
		return updateIntable(funds);
	}

	@Override
	public Funds getFundsWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Funds) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

	@Override
	public String myNane() {
		// TODO Auto-generated method stub
		return null;
	}
}

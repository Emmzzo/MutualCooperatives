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
import mutual.dao.interfc.IFines;
import mutual.dao.interfc.IFunds;
import mutual.dao.interfc.IUsers;
import mutual.domain.Fines;
import mutual.domain.Funds;
import mutual.domain.Users;

/**
 *
 * @author Ngango
 */

public class FinesImpl extends AbstractDao<Long, Fines> implements IFines {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	@Override
	public String myNane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fines saveFines(Fines fines) {
		return saveIntable(fines);
	}

	@Override
	public List<Fines> getListFines() {
		return (List<Fines>) (Object) getModelList();
	}

	@Override
	public Fines getFinesById(int fineId, String primaryKeyclomunName) {
		return (Fines) getModelById(fineId, primaryKeyclomunName);
	}

	@Override
	public Fines UpdateFines(Fines fines) {
		return updateIntable(fines);
	}

	@Override
	public Fines getFinesWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Fines) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
}

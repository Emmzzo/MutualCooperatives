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
import mutual.dao.interfc.IDistributedInterest;
import mutual.domain.DistributedInterest;

/**
 *
 * @author Ngango
 */

public class DistributedInterestImpl extends AbstractDao<Long, DistributedInterest> implements IDistributedInterest {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	@Override
	public DistributedInterest saveInterest(DistributedInterest income) {
		return saveIntable(income);
	}

	@Override
	public List<DistributedInterest> getListDistributedInterest() {
		return (List<DistributedInterest>) (Object) getModelList();
	}

	@Override
	public DistributedInterest getDistributedInterestById(int incomeId, String primaryKeyclomunName) {
		return (DistributedInterest) getModelById(incomeId, primaryKeyclomunName);
	}

	@Override
	public DistributedInterest UpdateDistributedInterest(DistributedInterest income) {
		return updateIntable(income);
	}

	@Override
	public DistributedInterest getDistributedInterestWithQuery(String[] propertyName, Object[] value,
			String hqlStatement) {
			try {
				return (DistributedInterest) getModelWithMyHQL(propertyName, value, hqlStatement);
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
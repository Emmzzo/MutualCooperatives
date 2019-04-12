/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.IMutualCooperative;
import mutual.dao.interfc.ISector;
import mutual.domain.Funds;
import mutual.domain.MutualCooperative;
import mutual.domain.Sector;
import mutual.domain.Users;

/**
 *
 * @author Emmanuel
 */
public class MutualCooperativeImpl extends AbstractDao<Long, MutualCooperative> implements IMutualCooperative {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	@Override
	public MutualCooperative saveMutualCooperative(MutualCooperative mutualcoop) {
		return saveIntable(mutualcoop);
	}

	@Override
	public List<MutualCooperative> getListMutualCooperative() {
		return (List<MutualCooperative>) (Object) getModelList();
	}

	@Override
	public MutualCooperative getMutualCooperativeById(int mutualcoopId, String primaryKeyclomunName) {
		return (MutualCooperative) getModelById(mutualcoopId, primaryKeyclomunName);
	}

	@Override
	public MutualCooperative UpdateMutualCooperative(MutualCooperative mutualcoopId) {
		return updateIntable(mutualcoopId);
	}

	@Override
	public String myNane() {
		// TODO Auto-generated method stub
		return "Emma";
	}

	@Override
	public MutualCooperative getMutualCooperativeWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (MutualCooperative) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

}

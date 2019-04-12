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
import mutual.dao.interfc.IMutualCoopMembers;
import mutual.dao.interfc.IUsers;
import mutual.domain.MutualCoopMembers;
import mutual.domain.Users;

/**
 *
 * @author Ngango
 */

public class MutualCoopMembersImpl extends AbstractDao<Long, MutualCoopMembers> implements IMutualCoopMembers{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public String myNane() {
		return "Nshimiye Emma";
	}

	@Override
	public MutualCoopMembers saveMutualCoopMembers(MutualCoopMembers mutualMemberId) {
		return saveIntable(mutualMemberId);
	}

	@Override
	public List<MutualCoopMembers> getListMutualCoopMembers() {
		return (List<MutualCoopMembers>) (Object) getModelList();
	}

	@Override
	public MutualCoopMembers getMutualCoopMembersById(int mutualMemberId, String primaryKeyclomunName) {
		return (MutualCoopMembers) getModelById(mutualMemberId, primaryKeyclomunName);
	}

	@Override
	public MutualCoopMembers UpdateMutualCoopMembers(MutualCoopMembers mutualMemberId) {
		return updateIntable(mutualMemberId);
	}

	@Override
	public MutualCoopMembers getMutualCoopMembersWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (MutualCoopMembers) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
}

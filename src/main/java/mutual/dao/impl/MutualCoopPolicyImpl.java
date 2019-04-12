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
import mutual.dao.interfc.IMutualCoopPolicy;
import mutual.dao.interfc.IUsers;
import mutual.domain.MutualCoopPolicy;
import mutual.domain.Users;

/**
 *
 * @author Ngango
 */

public class MutualCoopPolicyImpl extends AbstractDao<Long, MutualCoopPolicy> implements IMutualCoopPolicy {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public String myNane() {
		return "Nshimiye Emma";
	}

	@Override
	public MutualCoopPolicy saveMutualCoopPolicy(MutualCoopPolicy policyId) {
		return saveIntable(policyId);
	}

	@Override
	public List<MutualCoopPolicy> getListMutualCoopPolicy() {
		return (List<MutualCoopPolicy>) (Object) getModelList();
	}

	@Override
	public MutualCoopPolicy getMutualCoopPolicyById(int policyId, String primaryKeyclomunName) {
		return (MutualCoopPolicy) getModelById(policyId, primaryKeyclomunName);
	}

	@Override
	public MutualCoopPolicy UpdateMutualCoopPolicy(MutualCoopPolicy policyId) {
		return updateIntable(policyId);
	}

	@Override
	public MutualCoopPolicy getMutualCoopPolicyWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (MutualCoopPolicy) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
}

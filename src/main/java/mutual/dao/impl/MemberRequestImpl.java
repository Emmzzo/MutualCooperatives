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
import mutual.dao.interfc.IMemberRequest;
import mutual.dao.interfc.IUsers;
import mutual.domain.Funds;
import mutual.domain.MemberRequest;
import mutual.domain.Users;

/**
 *
 * @author Ngango
 */

public class MemberRequestImpl extends AbstractDao<Long, MemberRequest> implements IMemberRequest {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public String myNane() {
		return "Nshimiye Emma";
	}

	@Override
	public MemberRequest saveMemberRequest(MemberRequest memberRequest) {
		return saveIntable(memberRequest);
	}

	@Override
	public List<MemberRequest> getListMemberRequest() {
		return (List<MemberRequest>) (Object) getModelList();
	}

	@Override
	public MemberRequest getMemberRequestById(int memberRequest, String primaryKeyclomunName) {
		return (MemberRequest) getModelById(memberRequest, primaryKeyclomunName);
	}

	@Override
	public MemberRequest UpdateMemberRequest(MemberRequest memberRequest) {
		return updateIntable(memberRequest);
	}

	@Override
	public MemberRequest getMemberRequestWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (MemberRequest) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

	
}

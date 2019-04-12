/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.impl;

import java.util.List;

import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.IUserCategory;
import mutual.domain.UserCategory;

/**
 *
 * @author Ngango
 */
public class UserCategoryImpl extends AbstractDao<Long, UserCategory> implements IUserCategory {

	public UserCategory saveUsercategory(UserCategory usercategory) {

		return saveIntable(usercategory);
	}

	public List<UserCategory> getListUsercategory() {

		return (List<UserCategory>) (Object) getModelList();
	}

	public UserCategory getUserCategoryById(int usercatId, String primaryKeyColumn) {
		return (UserCategory) getModelById(usercatId, primaryKeyColumn);

	}

	public UserCategory UpdateUsercategory(UserCategory usercategory) {

		return updateIntable(usercategory);
	}

}

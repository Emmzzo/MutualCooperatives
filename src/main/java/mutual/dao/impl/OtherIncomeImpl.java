/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.impl;

import java.util.List;
import java.util.logging.Logger;
import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.IOtherIncome;
import mutual.domain.OtherIncome;


/**
 *
 * @author Emma
 */

public class OtherIncomeImpl extends AbstractDao<Long, OtherIncome> implements IOtherIncome {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public String myNane() {
		return "Nshimiye Emma";
	}

	@Override
	public OtherIncome saveOtherIncome(OtherIncome incomeId) {
		return saveIntable(incomeId);
	}
	@Override
	public List<OtherIncome> getListOtherIncome() {
		return (List<OtherIncome>) (Object) getModelList();
	}
	@Override
	public OtherIncome getOtherIncomeById(int incomeId, String primaryKeyclomunName) {
		return (OtherIncome) getModelById(incomeId, primaryKeyclomunName);
	}

	@Override
	public OtherIncome UpdateOtherIncome(OtherIncome incomeId) {
		return updateIntable(incomeId);
	}

	@Override
	public OtherIncome getOtherIncomeWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (OtherIncome) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
	}

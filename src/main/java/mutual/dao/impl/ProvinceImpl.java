/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.impl;

import java.util.List;

import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.IProvince;
import mutual.domain.Province;
/**
 *
 * @author Ngango
 */
public class ProvinceImpl extends AbstractDao<Long, Province> implements IProvince {

	public Province saveProvince(Province province) {

		return saveIntable(province);
	}

	public List<Province> getListProvinces() {

		return (List<Province>) (Object) getModelList();
	}

	public Province getProvinceById(int provinceId, String primaryKeyclomunName) {

		return (Province) getModelById(provinceId, primaryKeyclomunName);
	}

	public Province UpdateProvince(Province province) {

		return updateIntable(province);
	}

}

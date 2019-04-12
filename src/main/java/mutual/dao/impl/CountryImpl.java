package mutual.dao.impl;

import java.util.List;

import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.ICountry;
import mutual.domain.Country;
public class CountryImpl extends AbstractDao<Long, Country> implements ICountry {

	public Country saveCountry(Country country) {
		return saveIntable(country);
	}

	public List<Country> getListCountrys() {
		return (List<Country>) (Object) getModelList();
	}

	public Country getCountryById(int countryId, String primaryKeyclomunName) {
		return (Country) getModelById(countryId, primaryKeyclomunName);
	}

	public Country UpdateCountry(Country country) {
		return updateIntable(country);
	}

}

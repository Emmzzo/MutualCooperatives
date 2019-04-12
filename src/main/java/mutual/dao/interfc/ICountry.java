package mutual.dao.interfc;

import java.util.List;

import mutual.domain.Country;

public interface ICountry {
	public Country saveCountry(Country country);

	public List<Country> getListCountrys();

	public Country getCountryById(int countryId, String primaryKeyclomunName);

	public Country UpdateCountry(Country country);
}

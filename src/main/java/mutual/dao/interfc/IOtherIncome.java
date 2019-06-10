/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;
import mutual.domain.Funds;
import mutual.domain.MutualCoopPolicy;
import mutual.domain.OtherIncome;
/**
 *
 * @author Emma
 */
public interface IOtherIncome {
	public OtherIncome saveOtherIncome(OtherIncome incomeId);
	public List<OtherIncome> getListOtherIncome();
	public OtherIncome getOtherIncomeById(int incomeId, String primaryKeyclomunName);
	public OtherIncome UpdateOtherIncome(OtherIncome incomeId);
	public String myNane();
	public OtherIncome getOtherIncomeWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

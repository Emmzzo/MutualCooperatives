/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;
import mutual.domain.Funds;
/**
 *
 * @author Emma
 */
public interface IFunds {
	public Funds saveFunds(Funds funds);

	public List<Funds> getListFunds();

	public Funds getFundsById(int findId, String primaryKeyclomunName);

	public Funds UpdateFunds(Funds funds);

	public String myNane();

	public Funds getFundsWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;

import mutual.domain.Fines;
import mutual.domain.Funds;
import mutual.domain.InterestCharges;
/**
 *
 * @author Emma
 */
public interface IInterestCharges {
	public InterestCharges saveInterestCharges(InterestCharges charge);

	public List<InterestCharges> getListInterestCharges();

	public InterestCharges getInterestChargesById(int chargeId, String primaryKeyclomunName);

	public InterestCharges UpdateInterestCharges(InterestCharges charges);

	public String myNane();

	public InterestCharges getInterestChargesWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

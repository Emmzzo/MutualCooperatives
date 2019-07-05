/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;

import mutual.domain.DistributedInterest;
/**
 *
 * @author Emma
 */
public interface IDistributedInterest {
	public DistributedInterest saveInterest(DistributedInterest income);

	public List<DistributedInterest> getListDistributedInterest();

	public DistributedInterest getDistributedInterestById(int incomeId, String primaryKeyclomunName);

	public DistributedInterest UpdateDistributedInterest(DistributedInterest income);

	public String myNane();

	public DistributedInterest getDistributedInterestWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

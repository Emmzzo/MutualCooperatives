/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;

import mutual.domain.Fines;
import mutual.domain.Funds;
/**
 *
 * @author Emma
 */
public interface IFines {
	public Fines saveFines(Fines fines);

	public List<Fines> getListFines();

	public Fines getFinesById(int fineId, String primaryKeyclomunName);

	public Fines UpdateFines(Fines fines);

	public String myNane();

	public Fines getFinesWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

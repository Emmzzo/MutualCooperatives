/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;
import mutual.domain.Funds;
import mutual.domain.MutualCooperative;
/**
 *
 * @author Emma
 */
public interface IMutualCooperative {
	public MutualCooperative saveMutualCooperative(MutualCooperative mutualcoop);

	public List<MutualCooperative> getListMutualCooperative();

	public MutualCooperative getMutualCooperativeById(int mutualcoopId, String primaryKeyclomunName);

	public MutualCooperative UpdateMutualCooperative(MutualCooperative mutualcoopId);

	public String myNane();

	public MutualCooperative getMutualCooperativeWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

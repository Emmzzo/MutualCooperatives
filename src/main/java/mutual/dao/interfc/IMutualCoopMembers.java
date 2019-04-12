/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;
import mutual.domain.Funds;
import mutual.domain.MutualCoopMembers;
import mutual.domain.Posts;
/**
 *
 * @author Emma
 */
public interface IMutualCoopMembers {
	public MutualCoopMembers saveMutualCoopMembers(MutualCoopMembers mutualMemberId);

	public List<MutualCoopMembers> getListMutualCoopMembers();

	public MutualCoopMembers getMutualCoopMembersById(int mutualMemberId, String primaryKeyclomunName);

	public MutualCoopMembers UpdateMutualCoopMembers(MutualCoopMembers mutualMemberId);

	public String myNane();

	public MutualCoopMembers getMutualCoopMembersWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

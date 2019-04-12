/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;
import mutual.domain.Funds;
import mutual.domain.MutualCoopPolicy;
/**
 *
 * @author Emma
 */
public interface IMutualCoopPolicy {
	public MutualCoopPolicy saveMutualCoopPolicy(MutualCoopPolicy policyId);

	public List<MutualCoopPolicy> getListMutualCoopPolicy();

	public MutualCoopPolicy getMutualCoopPolicyById(int policyId, String primaryKeyclomunName);

	public MutualCoopPolicy UpdateMutualCoopPolicy(MutualCoopPolicy policyId);

	public String myNane();

	public MutualCoopPolicy getMutualCoopPolicyWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

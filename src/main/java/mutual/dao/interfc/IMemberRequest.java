/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;
import mutual.domain.Funds;
import mutual.domain.MemberRequest;
/**
 *
 * @author Emma
 */
public interface IMemberRequest {
	public MemberRequest saveMemberRequest(MemberRequest memberRequest);

	public List<MemberRequest> getListMemberRequest();

	public MemberRequest getMemberRequestById(int memberRequest, String primaryKeyclomunName);

	public MemberRequest UpdateMemberRequest(MemberRequest memberRequest);

	public String myNane();

	public MemberRequest getMemberRequestWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.interfc;
import java.util.List;
import mutual.domain.Funds;
import mutual.domain.Posts;
/**
 *
 * @author Emma
 */
public interface IPosts {
	public Posts savePosts(Posts postId);

	public List<Posts> getListPost();

	public Posts getPostsById(int postId, String primaryKeyclomunName);

	public Posts UpdatePosts(Posts post);

	public String myNane();

	public Posts getPostsWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

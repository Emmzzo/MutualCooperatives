/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.IPosts;
import mutual.dao.interfc.IUsers;
import mutual.domain.Posts;
import mutual.domain.Users;

/**
 *
 * @author Ngango
 */

public class PostsImpl extends AbstractDao<Long,  Posts> implements IPosts {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public String myNane() {
		return "Nshimiye Emma";
	}

	@Override
	public Posts savePosts(Posts postId) {
		return saveIntable(postId);
	}

	@Override
	public List<Posts> getListPost() {
		return (List<Posts>) (Object) getModelList();
	}

	@Override
	public Posts getPostsById(int postId, String primaryKeyclomunName) {
		return (Posts) getModelById(postId, primaryKeyclomunName);
	}

	@Override
	public Posts UpdatePosts(Posts post) {
		return updateIntable(post);
	}

	@Override
	public Posts getPostsWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Posts) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
}

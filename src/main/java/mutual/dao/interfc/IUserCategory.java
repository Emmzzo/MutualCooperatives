package mutual.dao.interfc;

import java.util.List;

import mutual.domain.UserCategory;

public interface IUserCategory {
	public UserCategory saveUsercategory(UserCategory usercategory);

	public List<UserCategory> getListUsercategory();

	public UserCategory UpdateUsercategory(UserCategory usercategory);

}

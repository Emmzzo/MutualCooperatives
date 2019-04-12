package mutual.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import mutual.dao.impl.UserImpl;
import mutual.services.interfaces.ILoginControllerService;

@Stateless
public class LoginControllerServiceImpl implements ILoginControllerService {

	@Inject
	public transient UserImpl usersImpl;

	public String getMyNgaboName() {

		return usersImpl.myNane();
	}

}

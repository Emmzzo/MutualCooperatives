package mutual.common;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mutual.dao.impl.UserImpl;

public class TestClass {
	
	@Inject
	private static transient UserImpl usersImpl;
//	private MenuGroup menuGroup;
//	
//	private List<MenuAssignment> menuAssignmentDetails = new ArrayList<MenuAssignment>();
//	private List<MenuGroup> menuGroupDetails = new ArrayList<MenuGroup>();
//	MenuAssignmentImpl menuAssignmentImpl=new MenuAssignmentImpl();
//	 MenuGroupImpl menuGroupImpl=new MenuGroupImpl();
//	
   public static void main(String ... dddd){

	aa();
}
public static void aa() {
	   
			
}
public static UserImpl getUsersImpl() {
	return usersImpl;
}

public static void setUsersImpl(UserImpl usersImpl) {
	TestClass.usersImpl = usersImpl;
}


}


package com.mutual.coop;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;

import mutual.common.DbConstant;
import mutual.common.JSFBoundleProvider;
import mutual.common.JSFMessagers;
import mutual.common.SessionUtils;
import mutual.common.UploadUtility;
import mutual.dao.impl.DocumentsImpl;
import mutual.dao.impl.UploadingFilesImpl;
import mutual.dao.impl.UserImpl;
import mutual.domain.Documents;
import mutual.domain.UploadingFiles;
import mutual.domain.Users;

@ManagedBean
@ViewScoped
public class FormSampleController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "FormSampleController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	/* end manage validation messages */
	private Users users;
//	private MenuAssignment menuAssignment;
//	private MenuGroup menuGroup;
//	private List<MenuGroup> menuGroupDetails = new ArrayList<MenuGroup>();

	/* class injection */
//	GenerateNotificationTemplete gen = new GenerateNotificationTemplete();
	JSFBoundleProvider provider = new JSFBoundleProvider();
	UserImpl usersImpl = new UserImpl();
//	MenuAssignmentImpl menuAssignmentImpl = new MenuAssignmentImpl();
//	MenuGroupImpl menuGroupImpl = new MenuGroupImpl();
	DocumentsImpl documentsImpl = new DocumentsImpl();
	UploadingFilesImpl uploadingFilesImpl = new UploadingFilesImpl();
	/* end class injection */
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private Documents documents;
	private UploadingFiles uploadingFiles;
	private Users usersSession;
	
	private List<UploadingFiles>filesUploaded= new ArrayList<UploadingFiles>();
	private DocumentsImpl docsImpl = new DocumentsImpl();
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");
		if (users == null) {
			users = new Users();
		}

		if (documents == null) {
			documents = new Documents();
		}
		if (uploadingFiles == null) {
			uploadingFiles = new UploadingFiles();
		}
//		if (menuGroup == null) {
//			menuGroup = new MenuGroup();
//		}
//		try {
//			 menuGroupDetails=menuGroupImpl.getGenericListWithHQLParameter(new String[]
//			 {"genericStatus"},new Object[] {ACTIVE}, "MenuGroup", "menuGroupId asc");
//		} catch (Exception e) {
//			setValid(false);
//			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
//			LOGGER.info(e.getMessage());
//			e.printStackTrace();
//		}

	}

	public void fileUpload(FileUploadEvent event) {

		UploadUtility ut = new UploadUtility();
		String validationCode = "PROFILEIMAGE";
		try {
			documents = ut.fileUploadUtil(event, validationCode);

			// need to put exact users
			Users u = new Users();
			u.setUserId(1);
			uploadingFiles.setUser(u);
			uploadingFiles.setCrtdDtTime(timestamp);
			uploadingFiles.setGenericStatus(ACTIVE);
			uploadingFiles.setDocuments(documents);
			uploadingFiles.setCreatedBy(usersSession.getViewId());
			uploadingFilesImpl.saveIntable(uploadingFiles);

			LOGGER.info(CLASSNAME + event.getFile().getFileName() + "uploaded successfully ... ");
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("upload.message.success"));
		} catch (Exception e) {
			LOGGER.info(CLASSNAME + "testing save methode ");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			e.printStackTrace();
		}

	}
	
	/*@SuppressWarnings("unchecked")
	public void deleteExistCategoryImage(ProductCategory cat) throws Exception {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String realPath = ctx.getRealPath("/");
		LOGGER.info("Filse Reals Path::::" + realPath);
		if(null != cat) {
			filesUploaded=uploadingFilesImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "productCategory"},
					new Object[] { ACTIVE, cat},
					"UploadingFiles", "uploadId asc");
			
			for(UploadingFiles files:filesUploaded) {
				Documents doc = new Documents();
				doc = docsImpl.getModelWithMyHQL(new String[] { "DocId" },
						new Object[] { files.getDocuments().getDocId()}, " from Documents");
				final Path destination = Paths.get(realPath + FILELOCATION + doc.getSysFilename());
				LOGGER.info("Path::" + destination);
				File file = new File(destination.toString());
				uploadingFilesImpl.deleteIntable(files);
				docsImpl.deleteIntable(documents);
				LOGGER.info("Delete in db operation done!!!:");
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");		
				} else {
					System.out.println("Delete operation is failed.");
					
				}
			}
			
		}
	}*/
	/*@SuppressWarnings("unchecked")
	public void deleteExistProductImage(Product product) throws Exception {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String realPath = ctx.getRealPath("/");
		LOGGER.info("Filse Reals Path::::" + realPath);
		if(null != product) {
			filesUploaded=uploadingFilesImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "product"},
					new Object[] { ACTIVE, product},
					"UploadingFiles", "uploadId asc");
			
			for(UploadingFiles files:filesUploaded) {
				Documents doc = new Documents();
				doc = docsImpl.getModelWithMyHQL(new String[] { "DocId" },
						new Object[] { files.getDocuments().getDocId()}, " from Documents");
				final Path destination = Paths.get(realPath + FILELOCATION + doc.getSysFilename());
				LOGGER.info("Path::" + destination);
				File file = new File(destination.toString());
				uploadingFilesImpl.deleteIntable(files);
				docsImpl.deleteIntable(documents);
				LOGGER.info("Delete in db operation done!!!:");
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");		
				} else {
					System.out.println("Delete operation is failed.");
					
				}
			}
			
		}
	}*/
	@SuppressWarnings("unchecked")
	public void deleteExistImage() throws Exception {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String realPath = ctx.getRealPath("/");
		LOGGER.info("Filse Reals Path::::" + realPath);
		if(null != usersSession) {
			filesUploaded=uploadingFilesImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "user"},
					new Object[] { ACTIVE, usersSession},
					"UploadingFiles", "uploadId asc");
			
			for(UploadingFiles files:filesUploaded) {
				Documents doc = new Documents();
				doc = docsImpl.getModelWithMyHQL(new String[] { "DocId" },
						new Object[] { files.getDocuments().getDocId()}, " from Documents");
				final Path destination = Paths.get(realPath + FILELOCATION + doc.getSysFilename());
				LOGGER.info("Path::" + destination);
				File file = new File(destination.toString());
				uploadingFilesImpl.deleteIntable(files);
				docsImpl.deleteIntable(documents);
				LOGGER.info("Delete in db operation done!!!:");
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");		
				} else {
					System.out.println("Delete operation is failed.");
					
				}
			}
			
		}
	}
	public String uploadProfile(FileUploadEvent event) {

		try {
			if (null != usersSession) {
				UploadUtility ut = new UploadUtility();
				String validationCode = "PROFILEIMAGE";
				deleteExistImage();
				documents = ut.fileUploadUtilUsers(event, validationCode);
				uploadingFiles.setUser(usersSession);
				uploadingFiles.setCrtdDtTime(timestamp);
				uploadingFiles.setGenericStatus(ACTIVE);
				uploadingFiles.setDocuments(documents);
				uploadingFilesImpl.saveIntable(uploadingFiles);
				LOGGER.info(CLASSNAME + event.getFile().getFileName() + "uploaded successfully ... ");
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addInfoMessage(getProvider().getValue("upload.message.success"));
				/* addErrorMessage(getProvider().getValue("upload.message.success")); */
				return "/menu/EditProfile.xhtml?faces-redirect=true";
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
			}
		} catch (Exception e) {
			LOGGER.info(CLASSNAME + "testing profile upload methode ");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.userprofile.error"));
			e.printStackTrace();
		}
		return "/menu/EditProfile.xhtml?faces-redirect=true";
	}

	
	
	
	
	
	public void downloadFile() {
		UploadUtility ut = new UploadUtility();
		try {
			ut.downloadFileUtil();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<UploadingFiles> fileList() {

		try {
			return uploadingFilesImpl.getListWithHQL("from UploadingFiles");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	public void sendMailTest() throws AddressException, MessagingException {
//		/* sending content in a table example */
//		String name = "Mukamana";
//		String fname = "Eric";
//
//		String msg = "<p>Kindly refer to the  below status.</p>" + "<table width=\"50%\" border=\"5px\">\n"
//				+ "  <tbody>\n" + "	<tr>\n" + "      <td class=\"labelbold\">Fname</td>\n" + "      <td>\n" + "		  "
//				+ name + "\n" + "	  </td>\n" + "    </tr>\n" + "	<tr>\n"
//				+ "      <td class=\"labelbold\">Lname</td>\n" + "      <td>\n" + "		  " + fname + "\n"
//				+ "	  </td>\n"
//
//				+ "  </tbody>\n" + "</table>\n";
//		/* End send content in table sample */
//		gen.sendEmailNotification("sibo2540@gmail.com", "Sibo Emma", "Test Email", msg);
//		LOGGER.info("::: notidficatio sent   ");
//	}
//
//	public void sendUserMailTest(String useremail, String userfname, String userlname) throws AddressException, MessagingException {
//		/* sending content in a table example */
//		String name = "Mukamana";
//		String fname = "Eric";
//
//		String msg = "<p>Kindly refer to the  below status.</p>" + "<table width=\"50%\" border=\"5px\">\n"
//				+ "  <tbody>\n" + "	<tr>\n" + "      <td class=\"labelbold\">Fname</td>\n" + "      <td>\n" + "		  "
//				+ name + "\n" + "	  </td>\n" + "    </tr>\n" + "	<tr>\n"
//				+ "      <td class=\"labelbold\">Lname</td>\n" + "      <td>\n" + "		  " + fname + "\n"
//				+ "	  </td>\n"
//
//				+ "  </tbody>\n" + "</table>\n";
//		/* End send content in table sample */
//		gen.sendEmailNotification(useremail, userfname + " " + userlname, "Test Email", msg);
//		LOGGER.info("::: notidficatio sent   ");
//	}

	public void saveData() {
		LOGGER.info(CLASSNAME + "testing save methode ");
		JSFMessagers.resetMessages();
		setValid(false);
		JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));

	}

	public void changeSelectBox(String name) {

		LOGGER.info("Ajax is working with listener::::::" + name);
	}

	public String getCLASSNAME() {
		return CLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		CLASSNAME = cLASSNAME;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}

	public UserImpl getUsersImpl() {
		return usersImpl;
	}

	public void setUsersImpl(UserImpl usersImpl) {
		this.usersImpl = usersImpl;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public DocumentsImpl getDocumentsImpl() {
		return documentsImpl;
	}

	public void setDocumentsImpl(DocumentsImpl documentsImpl) {
		this.documentsImpl = documentsImpl;
	}

	public UploadingFilesImpl getUploadingFilesImpl() {
		return uploadingFilesImpl;
	}

	public void setUploadingFilesImpl(UploadingFilesImpl uploadingFilesImpl) {
		this.uploadingFilesImpl = uploadingFilesImpl;
	}

	public Documents getDocuments() {
		return documents;
	}

	public void setDocuments(Documents documents) {
		this.documents = documents;
	}

	public UploadingFiles getUploadingFiles() {
		return uploadingFiles;
	}

	public void setUploadingFiles(UploadingFiles uploadingFiles) {
		this.uploadingFiles = uploadingFiles;
	}

	public Users getUsersSession() {
		return usersSession;
	}

	public void setUsersSession(Users usersSession) {
		this.usersSession = usersSession;
	}

	public List<UploadingFiles> getFilesUploaded() {
		return filesUploaded;
	}
	
	public void setFilesUploaded(List<UploadingFiles> filesUploaded) {
		this.filesUploaded = filesUploaded;
	}

	public DocumentsImpl getDocsImpl() {
		return docsImpl;
	}

	public void setDocsImpl(DocumentsImpl docsImpl) {
		this.docsImpl = docsImpl;
	}

	

}

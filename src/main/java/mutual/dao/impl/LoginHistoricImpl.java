/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutual.dao.impl;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.ILoginHistoric;
import mutual.domain.LoginHistoric;

public class LoginHistoricImpl extends AbstractDao<Long, LoginHistoric> implements ILoginHistoric, Serializable {
	private static final long serialVersionUID = 1L;

	public LoginHistoric saveLoginHistoric(LoginHistoric loginHistoric) {
		try {
			return saveIntable(loginHistoric); // To change body of generated methods, choose Tools | Templates.
		} catch (Exception ex) {
			Logger.getLogger(LoginHistoricImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public List<LoginHistoric> getListLoginHistoric() {
		return (List<LoginHistoric>) (Object) getModelList();
	}

	public LoginHistoric getLoginHistoricById(int loginHistoricId, String primaryKeyclomunName) {
		return (LoginHistoric) getModelById(loginHistoricId, primaryKeyclomunName);
	}

	public LoginHistoric UpdateLoginHistoric(LoginHistoric loginHistoric) {
		return updateIntable(loginHistoric);
	}

	public String getMachineIp() {
		String ip = null;
		try {
			InetAddress Ip = InetAddress.getLocalHost();
			ip = Ip.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

}

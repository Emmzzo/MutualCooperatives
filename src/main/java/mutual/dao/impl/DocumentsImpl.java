/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutual.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import mutual.dao.generic.AbstractDao;
import mutual.dao.interfc.IDocuments;
import mutual.domain.Documents;

/**
 *
 * @author Ngango
 */

public class DocumentsImpl extends AbstractDao<Long, Documents> implements IDocuments {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public Documents saveDocuments(Documents document) {
		return saveIntable(document);
	}

	@SuppressWarnings("unchecked")
	public List<Documents> getListDocuments() {
		return (List<Documents>) (Object) getModelList();
	}

	public Documents getDocumentById(int DocId, String primaryKeyclomunName) {
		return (Documents) getModelById(DocId, primaryKeyclomunName);
	}

	public Documents UpdateDocuments(Documents document) {
		return updateIntable(document);
	}

	public Documents getDocumentWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Documents) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
}

package com.key.dwsurvey.service.impl;

import com.key.common.service.BaseServiceImpl;
import com.key.dwsurvey.dao.EParametersDao;
import com.key.dwsurvey.entity.EParameters;
import com.key.dwsurvey.service.EParametersManager;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("EParametersManager")
public class EParametersManagerImpl extends BaseServiceImpl<EParameters, String> implements EParametersManager {

	@Autowired
	private EParametersDao eParametersDao;

	@Override
	public void setBaseDao() {
		this.baseDao = eParametersDao;
	}

	/**
	 * <p>
	 * Title: checkIsExist
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param euid
	 * @return
	 * @see com.key.dwsurvey.service.EParametersManager#checkIsExist(java.lang.String,java.lang.String)
	 */
	@Override
	public boolean checkIsExist(String euid,String surveyId) {
		// TODO Auto-generated method stub
		if (euid == null || "".equals(euid)) {
			return false;
		}
		Criterion criterion = Restrictions.eq("eUid", euid);
		Criterion criterion1 = Restrictions.eq("belongId", surveyId);
		EParameters eParameters = eParametersDao.findFirst(criterion,criterion1);
		if (eParameters != null)
			return true;
		else
			return false;

	}
	
}

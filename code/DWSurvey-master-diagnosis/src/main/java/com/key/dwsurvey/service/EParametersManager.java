package com.key.dwsurvey.service;
import com.key.common.service.BaseService;
import com.key.dwsurvey.entity.EParameters;

/**
 * 扩展参数
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface EParametersManager extends BaseService<EParameters, String>{

	public  boolean checkIsExist(String euid,String surveyId);
}

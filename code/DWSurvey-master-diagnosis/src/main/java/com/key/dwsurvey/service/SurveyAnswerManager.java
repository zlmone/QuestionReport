package com.key.dwsurvey.service;

import java.util.List;
import java.util.Map;

import com.key.common.base.entity.User;
import com.key.common.plugs.page.Page;
import com.key.common.service.BaseService;
import com.key.dwsurvey.entity.Question;
import com.key.dwsurvey.entity.SurveyAnswer;
import com.key.dwsurvey.entity.SurveyDetail;
import com.key.dwsurvey.entity.SurveyStats;

import javax.servlet.http.HttpServletRequest;

/**
 * 问卷回答
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface SurveyAnswerManager extends BaseService<SurveyAnswer, String>{

	public void saveAnswer(SurveyAnswer surveyAnswer, Map<String, Map<String, Object>> quMaps);

	public List<Question> findAnswerDetail(SurveyAnswer answer);
	
	public List<SurveyAnswer> answersByIp(String surveyId, String ip);
	
	public SurveyAnswer getTimeInByIp(SurveyDetail surveyDetail, String ip);
	
	public Long getCountByIp(String surveyId, String ip);

	public String exportXLS(String surveyId,String savePath);
	
	public SurveyStats surveyStatsData(SurveyStats surveyStats);
	
	public Page<SurveyAnswer> joinSurvey(Page<SurveyAnswer> page, User user) ;
	
	/**
	 * 取出某份问卷的答卷数据
	 * @param page
	 * @param surveyId
	 * @return
	 */
	public Page<SurveyAnswer> answerPage(Page<SurveyAnswer> page,String surveyId);

}

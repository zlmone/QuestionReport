package com.key.dwsurvey.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;

import com.key.common.base.entity.IdEntity;
/**
 * 扩展参数
 * @author 
 * @date 2017年12月8日09:47:08
 *
 */

@Entity
@Table(name="e_parameters")
public class EParameters extends IdEntity{
	
	//所属问卷ID
	
	private String belongId;
	//对应的答卷信息表ID
	private String belongAnswerId;
	//唯一ID
	@Column(name="e_uid", length=255) 
	private String eUid;
	//结果
	@Column(name="e_desc", length=255) 
	private String eDesc;
	
	private Integer visibility=1;
	
	public EParameters(){

	}
	public EParameters(String surveyId, String surveyAnswerId, String eUid,
			String eDesc) {
		this.belongId=surveyId;
		this.belongAnswerId=surveyAnswerId;
		this.eUid=eUid;
		this.eDesc=eDesc;
	}
	
	public String getBelongId() {
		return belongId;
	}
	public void setBelongId(String belongId) {
		this.belongId = belongId;
	}
	public String getBelongAnswerId() {
		return belongAnswerId;
	}
	public void setBelongAnswerId(String belongAnswerId) {
		this.belongAnswerId = belongAnswerId;
	}
	
	public Integer getVisibility() {
		return visibility;
	}
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	/**  
	 * @Title:  geteUid <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String geteUid() {
		return eUid;
	}
	/**  
	 * @Title:  seteUid <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void seteUid(String eUid) {
		this.eUid = eUid;
	}
	/**  
	 * @Title:  geteDesc <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String geteDesc() {
		return eDesc;
	}
	/**  
	 * @Title:  seteDesc <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void seteDesc(String eDesc) {
		this.eDesc = eDesc;
	}
	
	
}

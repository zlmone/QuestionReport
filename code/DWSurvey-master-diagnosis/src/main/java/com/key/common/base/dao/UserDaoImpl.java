package com.key.common.base.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.key.common.base.entity.User;
import com.key.common.dao.BaseDaoImpl;
import com.key.common.plugs.page.Page;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;


/**
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User, String> implements UserDao{

	@Override
	public void resetUserGroup(String groupId) {
		String sql="UPDATE t_user SET user_group_id = '' WHERE id = id";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
}

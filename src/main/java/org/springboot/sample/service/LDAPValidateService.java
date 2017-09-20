package org.springboot.sample.service;

/**
 * @Description:LDAPValidateService
 * @date 2017年9月14日 下午2:02:16
 */
public interface LDAPValidateService {
	
	public boolean authenricate(String UID, String password);

}

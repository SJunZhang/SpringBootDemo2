package org.springboot.sample.service.impl;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springboot.sample.service.LDAPValidateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @Description:登录调用LDAP协议 校验用户是否存在
 * @date 2017年9月14日 下午2:02:40
 */
@Service
public class LDAPValidateServiceImpl implements LDAPValidateService {
	private static final Logger logger = LoggerFactory.getLogger(LDAPValidateServiceImpl.class);

	@Value("${ldap.datasource.factory}")
	private String FACTORY;

	@Value("${ldap.datasource.url}")
	private String URL; // uri ldap://127.0.0.1/ 或者 ldap://10.110.200.19/

	@Value("${ldap.datasource.basedn}")
	private String BASEDN; // suffix 根据自己情况进行修改 dc=mycompany,dc=com 或者
							// DC=skyinno,DC=com

	@Value("${ldap.datasource.userDN}")
	private String userDN; // rootdn 管理员 根据自己情况修改 cn=Manager,dc=mycompany,dc=com
							// 或者 CN=devbinding,CN=Users,DC=skyinno,DC=com

	@Value("${ldap.datasource.password}")
	private String password; // secret fd35644d5eaa8a2c

	@Value("${ldap.datasource.startSuffix}")
	private String startSuffix;// cn=

	private LdapContext ctx = null;

	private final Control[] connCtls = null;

	/**
	 * LDAP_connect
	 */
	private void LDAP_connect() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		env.put(Context.PROVIDER_URL, URL + BASEDN);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		env.put(Context.SECURITY_PRINCIPAL, userDN); // 管理员
		env.put(Context.SECURITY_CREDENTIALS, password); // 管理员密码

		try {
			ctx = new InitialLdapContext(env, connCtls);
			logger.info("LDAP连接成功");

		} catch (javax.naming.AuthenticationException e) {
			logger.info("LDAP连接失败");
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("LDAP连接出错");
			e.printStackTrace();
		}

	}

	/**
	 * LDAP_connect_close
	 */
	private void closeContext() {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}

		}
	}

	private String getUserDN(String uid) {
		String userDN = "";
		LDAP_connect();
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<SearchResult> en = ctx.search("", startSuffix + uid, constraints);

			if (en == null || !en.hasMoreElements()) {
				logger.info("未找到该用户");
				return userDN;
			}
			// maybe more than one element
			while (en != null && en.hasMoreElements()) {
				Object obj = en.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult si = (SearchResult) obj;
					userDN += si.getName();
					userDN += "," + BASEDN;
				} else {
					logger.info(obj.toString());
				}
			}
		} catch (Exception e) {
			logger.info("LDAP查找用户时产生异常");
			e.printStackTrace();
		}

		return userDN;
	}

	@Override
	public boolean authenricate(String UID, String password) {
		boolean valide = false;
		String userDN = getUserDN(UID);
		if (null != userDN && userDN.trim().length() > 0) {
			try {
				ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
				ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
				ctx.reconnect(connCtls);
				logger.info(userDN + " 验证通过");
				valide = true;
			} catch (AuthenticationException e) {
				logger.info(userDN + " 验证失败");
				e.printStackTrace();
				valide = false;
			} catch (NamingException e) {
				logger.info(userDN + " 验证失败");
				e.printStackTrace();
				valide = false;
			}
			closeContext();
		}
		return valide;
	}

}


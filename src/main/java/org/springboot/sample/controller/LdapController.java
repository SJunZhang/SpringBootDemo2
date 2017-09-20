package org.springboot.sample.controller;

import org.springboot.sample.service.LDAPValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于测试Ldap校验用户是否通过
 * @author DELL
 *
 */
@RestController
@RequestMapping("ldap/")
public class LdapController {
	@Autowired
	private LDAPValidateService ldapValidateService;
	
	@RequestMapping(value="validate/{UID}/{password}")
	public String ldapTest(@PathVariable("UID") String UID,@PathVariable("password") String password){
		
		if(ldapValidateService.authenricate(UID, password)){
			return "用户认证成功";
		}
		return "用户认证失败";
		
	}

}

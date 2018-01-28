package com.maimai.sso.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_user")
@Getter
@Setter
@ToString
public class User implements Serializable {
	
	private static final long serialVersionUID = -5536217163963545543L;
	/** 用户编号，自增长 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 用户名 */
	private String username;
	/** 密码，加密存储 */
	private String password;
	/** 注册手机号 */
	private String phone;
	/** 注册邮箱 */
	private String email;
	/** 创建日期 */
	private Date created;
	/** 修改日期 */
	private Date updated;
	
}
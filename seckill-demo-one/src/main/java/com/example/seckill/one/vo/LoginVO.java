package com.example.seckill.one.vo;

import javax.validation.constraints.NotNull;

import com.example.seckill.one.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class LoginVO {
	
	@NotNull
	@IsMobile
	private String mobile;
	
	@NotNull
	@Length(min = 32)
	private String password;
	
	@Override
	public String toString() {
		return "LoginVO [mobile=" + mobile + ", password=" + password + "]";
	}
}

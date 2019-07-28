package com.example.seckill.one.validator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.seckill.one.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 自定义校验器实现
 * 继承ConstraintValidator<A extends Annotation, T>
 *     Annotation是哪个注解
 *     T 是注解标注的属性的类型
 * **/
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	private boolean required = false;

	@Override
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return ValidatorUtils.isMobile(value);
		}else {
			if(StringUtils.isEmpty(value)) {
				return true;
			}else {
				return ValidatorUtils.isMobile(value);
			}
		}
	}

}

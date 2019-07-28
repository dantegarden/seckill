package com.example.seckill.one.exception;

import com.example.seckill.one.enums.ResultErrorEnum;

public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private ResultErrorEnum cm;
	
	public GlobalException(ResultErrorEnum cm) {
		super(cm.getMessage().toString());
		this.cm = cm;
	}

	public ResultErrorEnum getCm() {
		return cm;
	}

}

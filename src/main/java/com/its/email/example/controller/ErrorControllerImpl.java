package com.its.email.example.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;

@Controller
public class ErrorControllerImpl implements ErrorController{

	@Override
	public String getErrorPath() {
		return "";
	}

}

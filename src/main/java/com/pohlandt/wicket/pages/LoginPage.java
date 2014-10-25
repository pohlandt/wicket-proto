package com.pohlandt.wicket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.shiro.component.LoginPanel;

public class LoginPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public LoginPage(final PageParameters parameters) {
		super(parameters);
		
		add(new LoginPanel("login", true));
    }
}

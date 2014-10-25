package com.pohlandt.wicket.pages;

import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.shiro.ShiroConstraint;
import org.wicketstuff.shiro.annotation.ShiroSecurityConstraint;

import com.google.inject.Inject;
import com.pohlandt.entity.IEntityRepository;
import com.pohlandt.model.User;
import com.pohlandt.security.ISecurityManager;

@ShiroSecurityConstraint(constraint = ShiroConstraint.IsAuthenticated)
public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;

	@Inject IEntityRepository entityRepository;
	@Inject ISecurityManager securityManager;
	
	@SuppressWarnings("serial")
	public HomePage(final PageParameters parameters) {
		super(parameters);
						
		add(new Label("test", new AbstractReadOnlyModel<String>() {

			@Override
			public String getObject() {
				Collection<User> col = entityRepository.findAll(User.class);
				User e1 = col.stream().findFirst().get();
				return "user 1 name=" + (e1 == null ? "null" : e1.getName())  + " user count: " + col.size();
			}
			
		}).setOutputMarkupId(true));
		
		IModel<String> nameModel = Model.of("");
		IModel<String> passModel = Model.of("");
		Form<Void> form = new Form<>("form");
		form.add(new TextField<String>("name", nameModel));
		form.add(new PasswordTextField("pass", passModel));
		Button button = new Button("submit");
		button.add(new AjaxFormSubmitBehavior("click"){
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				securityManager.createUser(nameModel.getObject(), passModel.getObject());
				target.add(HomePage.this.get("test"));
			}
		});
		
		form.add(button);
		
		add(form);
    }
}

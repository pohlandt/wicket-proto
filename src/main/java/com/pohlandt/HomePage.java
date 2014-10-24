package com.pohlandt;

import java.util.Collection;

import model.Text;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.inject.Inject;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	@Inject IEntityRepository entityRepository;
	
	@SuppressWarnings("serial")
	public HomePage(final PageParameters parameters) {
		super(parameters);
						
		add(new Label("test", new AbstractReadOnlyModel<String>() {

			@Override
			public String getObject() {
				Collection<Text> col = entityRepository.findAll(Text.class);
				Text t1 = col.stream().filter(t -> t.getId() == 1).findFirst().get();
				return "text 1 text=" + (t1 == null ? "null" : t1.getText())  + " text count: " + col.size();
			}
			
		}).setOutputMarkupId(true));
		
		IModel<String> textModel = Model.of("");
		Form<Void> form = new Form<>("form");
		form.add(new TextField<String>("text", textModel));
		Button button = new Button("submit");
		button.add(new AjaxFormSubmitBehavior("click"){
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				Text t = new Text();
				t.setText(textModel.getObject());
				entityRepository.persist(t);
				target.add(HomePage.this.get("test"));
			}
		});
		
		form.add(button);
		
		add(form);
		
		add(new HeaderResponseContainer("bottomJS", "bottomJS"));
    }
}

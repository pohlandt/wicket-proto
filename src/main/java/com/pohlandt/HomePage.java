package com.pohlandt;

import java.util.Collection;

import model.Text;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.inject.Inject;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	@Inject IEntityRepository entityRepository;
	
	public HomePage(final PageParameters parameters) {
		super(parameters);

		Collection<Text> col = entityRepository.findAll(Text.class);
		Text t1 = col.stream().filter(t -> t.getId() == 1).findFirst().get();
				
		add(new Label("test", "text 1 text=" + (t1 == null ? "null" : t1.getText())  + " text count: " + col.size()));
    }
}

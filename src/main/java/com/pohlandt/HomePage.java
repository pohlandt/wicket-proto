package com.pohlandt;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Text;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		EntityManager em = Persistence.createEntityManagerFactory("com.pohlandt.wicket-proto").createEntityManager();
		Query query = em.createNamedQuery("Text.findAll");
		Collection<Text> col = (Collection<Text>) query.getResultList();
		Text t1 = em.find(Text.class, 1l);
		
		add(new Label("test", "text 1 text=" + (t1 == null ? "null" : t1.getText())  + " text count: " + col.size()));
    }
}

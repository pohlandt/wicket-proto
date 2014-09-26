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
		Query query = em.createQuery("SELECT t FROM text t");
		Collection<Text> col = (Collection<Text>) query.getResultList();
		
		add(new Label("version", "text count: " + col.size()));

		// TODO Add your page's components here

    }
}

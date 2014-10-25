package com.pohlandt.test;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.pohlandt.wicket.pages.HomePage;
import com.pohlandt.wicket.pages.LoginPage;

public class TestHomePage
{
	private WicketTester tester;

	@Before
	public void setUp()
	{
		tester = new WicketTester(new TestWicketApplication());
	}

	@Test
	public void loginPageRendersSuccessfully()
	{
		//start and render the test page
		tester.startPage(HomePage.class);

		//assert rendered page class
		tester.assertRenderedPage(LoginPage.class);
	}
}
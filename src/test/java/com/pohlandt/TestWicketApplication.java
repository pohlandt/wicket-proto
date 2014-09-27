package com.pohlandt;

import com.google.inject.Module;

public class TestWicketApplication extends WicketApplication {
	
	@Override 
	protected Module[] newGuiceModules() {
		return new Module[]{ new TestModule() };
	}
}

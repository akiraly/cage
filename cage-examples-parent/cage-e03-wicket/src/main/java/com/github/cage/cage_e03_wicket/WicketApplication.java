package com.github.cage.cage_e03_wicket;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.crypt.CharEncoding;

public class WicketApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	@Override
	protected void init() {
		super.init();

		getMarkupSettings().setDefaultMarkupEncoding(CharEncoding.UTF_8);
		getMarkupSettings().setCompressWhitespace(true);
	}
}

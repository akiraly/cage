/*
 * Copyright 2011 Király Attila
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cage.cage_e03_wicket;

import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.cage.Cage;
import com.github.cage.GCage;

public class HomePage extends WebPage {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HomePage.class);

	private static final long serialVersionUID = 3303458191832318970L;
	private static final Cage cage = new GCage();

	static class Token implements Serializable {
		private static final long serialVersionUID = -4488483822751315157L;

		private String token;
		private boolean tokenUsed;

		private boolean showGoodResult;
		private boolean showBadResult;

		private String captcha;

		public String getCaptcha() {
			return captcha;
		}

		public void setCaptcha(String captcha) {
			this.captcha = captcha;
		}

		@Override
		public String toString() {
			return "Token [token=" + token + ", tokenUsed=" + tokenUsed
					+ ", showGoodResult=" + showGoodResult + ", showBadResult="
					+ showBadResult + "]";
		}
	}

	public HomePage() {
		super(new CompoundPropertyModel<Token>(new Token()));
		add(new WebMarkupContainer("goodResult") {
			private static final long serialVersionUID = -5279236538017405828L;

			@Override
			protected void onConfigure() {
				super.onConfigure();

				final Token token = HomePage.this.getToken();

				setVisible(token.showGoodResult);
				token.showGoodResult = false;
			}
		});
		add(new WebMarkupContainer("badResult") {
			private static final long serialVersionUID = -6479933043124566245L;

			@Override
			protected void onConfigure() {
				super.onConfigure();

				final Token token = HomePage.this.getToken();

				setVisible(token.showBadResult);
				token.showBadResult = false;
			}
		});

		add(new Form<Token>("form") {
			private static final long serialVersionUID = -2783383042739263677L;

			@Override
			protected void onInitialize() {
				super.onInitialize();
				add(new RequiredTextField<String>("captcha") {
					private static final long serialVersionUID = 8416111619173955610L;

					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.put("value", "");
					};
				}.add(new StringValidator() {
					private static final long serialVersionUID = 3888825725858419028L;

					@Override
					public void validate(IValidatable<String> validatable) {
						super.validate(validatable);
						if (!validatable.isValid())
							return;

						final Token token = HomePage.this.getToken();

						if (token.token == null
								|| !token.token.equals(validatable.getValue())) {
							LOGGER.error("There was no generated token, or it didn't match the user given one.");
							validatable.error(decorate(
									new ValidationError(this), validatable));
						}
					}
				}));
			}

			@Override
			protected void onSubmit() {
				super.onSubmit();
				onPost(true);
			}

			@Override
			protected void onError() {
				super.onError();
				onPost(false);
			}

			protected void onPost(boolean good) {
				final Token token = HomePage.this.getToken();

				token.showGoodResult = good;
				token.showBadResult = !good;
			}
		});

		add(new Image("captchaImage",
				new DynamicImageResource(cage.getFormat()) {
					private static final long serialVersionUID = -1475355045487272906L;

					@Override
					protected void configureResponse(ResourceResponse response,
							Attributes attributes) {
						super.configureResponse(response, attributes);
						final Token token = HomePage.this.getToken();

						LOGGER.info("Token: {}.", token);

						if (token.token == null || token.tokenUsed) {
							LOGGER.error("Requested captcha without token.");
							response.setError(HttpServletResponse.SC_NOT_FOUND,
									"Captcha not found.");
						}
						token.tokenUsed = true;

						response.disableCaching();
					}

					@Override
					protected byte[] getImageData(Attributes attributes) {
						final Token token = HomePage.this.getToken();

						return cage.draw(token.token);
					}
				}));
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		final Token token = HomePage.this.getToken();

		token.token = cage.getTokenGenerator().next();
		token.tokenUsed = false;

		LOGGER.info("Token: {}.", token);
	}

	private Token getToken() {
		return (Token) getDefaultModelObject();
	}
}

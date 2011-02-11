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

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

import com.github.cage.Cage;
import com.github.cage.GCage;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 3303458191832318970L;
	private static final Cage cage = new GCage();

	private String token = cage.getTokenGenerator().next();
	private String captcha;
	private boolean showGoodResult;
	private boolean showBadResult;

	public HomePage() {
		add(new WebMarkupContainer("goodResult") {
			private static final long serialVersionUID = -5279236538017405828L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(showGoodResult);
				showGoodResult = false;
			}
		});
		add(new WebMarkupContainer("badResult") {
			private static final long serialVersionUID = -6479933043124566245L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(showBadResult);
				showBadResult = false;
			}
		});

		add(new Form<HomePage>("form",
				new CompoundPropertyModel<HomePage>(this)) {
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
					protected void onValidate(IValidatable<String> validatable) {
						if (token == null
								|| !token.equals(validatable.getValue()))
							error(validatable);
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
				showGoodResult = good;
				showBadResult = !good;
				token = cage.getTokenGenerator().next();
			}
		});

		add(new NonCachingImage("captchaImage", new DynamicImageResource(
				cage.getFormat()) {
			private static final long serialVersionUID = -1475355045487272906L;

			@Override
			protected byte[] getImageData(Attributes attributes) {
				return cage.draw(token);
			}
		}));
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}

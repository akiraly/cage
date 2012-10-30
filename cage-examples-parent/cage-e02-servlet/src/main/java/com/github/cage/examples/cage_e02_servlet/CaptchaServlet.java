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
package com.github.cage.examples.cage_e02_servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.github.cage.Cage;
import com.github.cage.GCage;

/**
 * An example servlet that generates captcha images directly to the response
 * stream.
 * 
 * @author akiraly
 * 
 */
public class CaptchaServlet extends HttpServlet {
	private static final long serialVersionUID = 1490947492185481844L;

	private static final Cage cage = new GCage();

	/**
	 * Generates a captcha token and stores it in the session.
	 * 
	 * @param session
	 *            where to store the captcha.
	 */
	public static void generateToken(HttpSession session) {
		final String token = cage.getTokenGenerator().next();

		session.setAttribute("captchaToken", token);
		markTokenUsed(session, false);
	}

	/**
	 * Used to retrieve previously stored captcha token from session.
	 * 
	 * @param session
	 *            where the token is possibly stored.
	 * @return token or null if there was none
	 */
	public static String getToken(HttpSession session) {
		final Object val = session.getAttribute("captchaToken");

		return val != null ? val.toString() : null;
	}

	/**
	 * Marks token as used/unused for image generation.
	 * 
	 * @param session
	 *            where the token usage flag is possibly stored.
	 * @param used
	 *            false if the token is not yet used for image generation
	 */
	protected static void markTokenUsed(HttpSession session, boolean used) {
		session.setAttribute("captchaTokenUsed", used);
	}

	/**
	 * Checks if the token was used/unused for image generation.
	 * 
	 * @param session
	 *            where the token usage flag is possibly stored.
	 * @return true if the token was marked as unused in the session
	 */
	protected static boolean isTokenUsed(HttpSession session) {
		return !Boolean.FALSE.equals(session.getAttribute("captchaTokenUsed"));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final HttpSession session = req.getSession(false);
		final String token = session != null ? getToken(session) : null;
		if (token == null || isTokenUsed(session)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND,
					"Captcha not found.");

			return;
		}

		setResponseHeaders(resp);
		markTokenUsed(session, true);
		cage.draw(token, resp.getOutputStream());
	}

	/**
	 * Helper method, disables HTTP caching.
	 * 
	 * @param resp
	 *            response object to be modified
	 */
	protected void setResponseHeaders(HttpServletResponse resp) {
		resp.setContentType("image/" + cage.getFormat());
		resp.setHeader("Cache-Control", "no-cache, no-store");
		resp.setHeader("Pragma", "no-cache");
		final long time = System.currentTimeMillis();
		resp.setDateHeader("Last-Modified", time);
		resp.setDateHeader("Date", time);
		resp.setDateHeader("Expires", time);
	}
}

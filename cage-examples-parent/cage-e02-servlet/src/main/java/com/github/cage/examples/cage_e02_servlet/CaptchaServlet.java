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

/**
 * An example servlet that generates captcha images directly to the response
 * stream.
 * 
 * @author akiraly
 * 
 */
public class CaptchaServlet extends HttpServlet {
	private static final long serialVersionUID = 1490947492185481844L;

	private static final Cage cage = Cage.likeG();

	public static String getToken(HttpSession session) {
		Object val = session.getAttribute("captchaToken");

		return val != null ? val.toString() : null;
	}

	public static void generateToken(HttpSession session) {
		String token = cage.getTokenGenerator().next();

		session.setAttribute("captchaToken", token);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		String token = session != null ? getToken(session) : null;
		if (token == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND,
					"Captcha not found.");

			return;
		}

		setResponseHeaders(resp);
		cage.draw(token, resp.getOutputStream());
	}

	protected void setResponseHeaders(HttpServletResponse resp) {
		resp.setContentType("image/" + cage.getFormat());
		resp.setHeader("Cache-Control",
				"private, no-cache, no-store, must-revalidate");
		resp.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		resp.setDateHeader("Last-Modified", time);
		resp.setDateHeader("Expires", time);
	}
}

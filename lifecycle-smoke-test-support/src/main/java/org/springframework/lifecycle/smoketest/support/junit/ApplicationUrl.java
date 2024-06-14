/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.lifecycle.smoketest.support.junit;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.URI;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link URI} parameters of JUnit test methods annotated with this annotation will be
 * resolved to the URI for the running Spring Boot application.
 *
 * @author Moritz Halbritter
 */
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER })
public @interface ApplicationUrl {

	/**
	 * Scheme of the produced {@link URI}.
	 * @return the scheme
	 */
	Scheme scheme() default Scheme.HTTP;

	enum Scheme {

		WEBSOCKET("ws"), SECURE_WEBSOCKET("wss"), HTTP("http"), HTTPS("https");

		private final String scheme;

		Scheme(String scheme) {
			this.scheme = scheme;
		}

		String getScheme() {
			return scheme;
		}

	}

}

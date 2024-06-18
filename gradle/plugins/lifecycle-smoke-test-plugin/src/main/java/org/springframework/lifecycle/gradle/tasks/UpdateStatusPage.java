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

package org.springframework.lifecycle.gradle.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

/**
 * Task to update an Asciidoctor status page for the smoke tests.
 *
 * @author Andy Wilkinson
 * @author Sebastien Deleuze
 */
public abstract class UpdateStatusPage extends AbstractSmokeTestsTask {

	@OutputFile
	public abstract RegularFileProperty getOutputFile();

	@TaskAction
	void updateStatusPage() throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("= Smoke Tests Status");
		lines.add(":toc:");
		lines.add(":toc-title: Projects");
		lines.add("");
		lines.add("Check each test for potential configuration guidance.");
		lines.add("");
		smokeTests().forEach((group, tests) -> {
			lines.add("== " + capitalize(group));
			lines.add("");
			lines.add("[%header,cols=\"" + (TestType.values().length + 1) + "\"]");
			lines.add("|===");
			lines.add("h|Smoke Test");
			for (TestType testType : TestType.values()) {
				lines.add("h|" + testType.taskName());
			}
			lines.add("");
			for (SmokeTest test : tests) {
				lines.add("|" + testUrl(group, test.name()) + "[" + test.name() + "]");
				for (TestType testType : TestType.values()) {
					lines.add("|" + testType.badge(test));
				}
				lines.add("");
			}
			lines.add("|===");
			lines.add("");
		});
		Files.write(getOutputFile().get().getAsFile().toPath(), lines);
	}

	private String capitalize(String input) {
		StringBuffer buffer = new StringBuffer(input.length());
		for (char c : input.toCharArray()) {
			buffer.append(buffer.isEmpty() ? Character.toUpperCase(c) : c);
		}
		return buffer.toString();
	}

	private String testUrl(String group, String name) {
		return "https://github.com/spring-projects/spring-lifecycle-smoke-tests/tree/main/" + group + "/" + name;
	}

	private enum TestType {

		APP_TEST(SmokeTest::appTests, "-app-test", "appTest"),
		CR_APP_TEST(SmokeTest::appTests, "-cr-app-test", "checkpointRestoreAppTest"),
		TEST(SmokeTest::tests, "-test", "test");

		private final Predicate<SmokeTest> predicate;

		private final String urlSuffix;

		private final String taskName;

		TestType(Predicate<SmokeTest> predicate, String suffix, String taskName) {
			this.predicate = predicate;
			this.urlSuffix = suffix;
			this.taskName = taskName;
		}

		String badge(SmokeTest smokeTest) {
			if (!this.predicate.test(smokeTest)) {
				return "";
			}
			return "image:" + badgeUrl(smokeTest.name(), this.urlSuffix) + "[link="
					+ jobUrl(smokeTest.name(), this.urlSuffix) + "]";
		}

		String taskName() {
			return this.taskName;
		}

		private String badgeUrl(String name, String suffix) {
			return "https://ci.spring.io/api/v1/teams/spring-lifecycle-smoke-tests/pipelines/spring-lifecycle-smoke-tests-3.3.x/jobs/"
					+ name + suffix + "/badge";
		}

		private String jobUrl(String name, String suffix) {
			return "https://ci.spring.io/teams/spring-lifecycle-smoke-tests/pipelines/spring-lifecycle-smoke-tests-3.3.x/jobs/"
					+ name + suffix;
		}

	}

}

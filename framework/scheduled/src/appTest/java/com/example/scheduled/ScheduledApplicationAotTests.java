package com.example.scheduled;

import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import org.springframework.lifecycle.smoketest.support.assertj.AssertableOutput;
import org.springframework.lifecycle.smoketest.support.junit.ApplicationTest;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationTest
class ScheduledApplicationAotTests {

	@Test
	void fixedRateShouldBeCalled(AssertableOutput output) {
		Awaitility.await()
			.atMost(Duration.ofSeconds(10))
			.untilAsserted(() -> assertThat(output).hasLineContaining("fixedRate()"));
	}

	@Test
	void fixedDelayShouldBeCalled(AssertableOutput output) {
		Awaitility.await()
			.atMost(Duration.ofSeconds(10))
			.untilAsserted(() -> assertThat(output).hasLineContaining("fixedDelay()"));
	}

	@Test
	void cronShouldBeCalled(AssertableOutput output) {
		Awaitility.await()
			.atMost(Duration.ofSeconds(10))
			.untilAsserted(() -> assertThat(output).hasLineContaining("cron()"));
	}

}

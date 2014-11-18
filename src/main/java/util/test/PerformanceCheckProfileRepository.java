package util.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceCheckProfileRepository {

	public static final PerformanceCheckProfileRepository INSTANCE = new PerformanceCheckProfileRepository();

	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceCheckProfileRepository.class);

	private final Map<Class<?>, AbstractProfiledPerformanceAssertion> performanceCheckProfiles = new HashMap<Class<?>, AbstractProfiledPerformanceAssertion>();

	private String currentProfile;

	public PerformanceCheckProfileRepository() {
		this.currentProfile = System.getProperty("TestProfile", "ChwWork");
		LOGGER.info("Using test profile '" + this.currentProfile + "'");
	}

	public void setCurrentProfile(final String currentProfile) {
		this.currentProfile = currentProfile;
	}

	public String getCurrentProfile() {
		return this.currentProfile;
	}

	public void register(final Class<?> testClass, final AbstractProfiledPerformanceAssertion profile) {
		if (profile.getCorrespondingPerformanceProfile().equals(this.currentProfile)) {
			this.performanceCheckProfiles.put(testClass, profile);
		}
	}

	public AbstractProfiledPerformanceAssertion get(final Class<?> clazz) {
		return this.performanceCheckProfiles.get(clazz);
	}
}

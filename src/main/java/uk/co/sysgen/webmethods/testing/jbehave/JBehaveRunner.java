package uk.co.sysgen.webmethods.testing.jbehave;

import java.io.File;
import java.util.List;

import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

public abstract class JBehaveRunner extends JUnitStories {

	@Override
	public InjectableStepsFactory stepsFactory() {
		return new InstanceStepsFactory(configuration(), new WmJBehaveSteps());
	}

	@Override
	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(new File("src/test/resources").getAbsolutePath(), "**/*.story", "");

	}
}

package org.wmaop.bdd.jbehave;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.AbstractStepsFactory;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.junit.runner.RunWith;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;
@RunWith(JUnitReportingRunner.class)
public class JBehaveRunner extends JUnitStories {

	private final WmJBehaveSteps stepsImstance;

	public JBehaveRunner() {
		JUnitReportingRunner.recommendedControls(configuredEmbedder());
		stepsImstance = new WmJBehaveSteps();
		}
	
    @Override 
    public Configuration configuration() { 
        return new MostUsefulConfiguration();
    }

	@Override
	public InjectableStepsFactory stepsFactory() {
		return new AbstractStepsFactory(configuration()) {

			@Override
			public Object createInstanceOfType(Class<?> type) {
				return stepsImstance;
			}

			@Override
			protected List<Class<?>> stepsTypes() {
				return new ArrayList<Class<?>>( Arrays.asList(WmJBehaveSteps.class));
			}
		};
	}
	
    @Override
    protected List<String> storyPaths() {
		return new StoryFinder().findPaths(new File("src/test/resources").getAbsolutePath(), "**/*.story", "");
    }
    

}
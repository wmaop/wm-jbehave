package org.wmaop.bdd.jbehave;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.AbstractStepsFactory;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.junit.runner.RunWith;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;
@RunWith(JUnitReportingRunner.class)
public class JBehaveRunner extends JUnitStories {

	public JBehaveRunner() {
		JUnitReportingRunner.recommendedControls(configuredEmbedder());
		}
	
    @Override 
    public Configuration configuration() { 
        return new MostUsefulConfiguration().usePendingStepStrategy(new FailingUponPendingStep());
    }
    
	@Override
	public InjectableStepsFactory stepsFactory() {
		return new AbstractStepsFactory(configuration()) {

			@Override
			public Object createInstanceOfType(Class<?> type) {
				return new WmJBehaveSteps(); //stepsInstance;
			}

			@Override
			protected List<Class<?>> stepsTypes() {
				return new ArrayList<Class<?>>(Arrays.asList(WmJBehaveSteps.class));
			}
		};
	}
	
    @Override
    protected List<String> storyPaths() {
		return new StoryFinder().findPaths(new File("src/test/resources").getAbsolutePath(), "**/*.story", "");
    }
    

}
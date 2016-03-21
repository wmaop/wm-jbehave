package org.wmaop.bdd.jbehave;

import static org.junit.Assume.assumeThat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.FilePrintStreamFactory.ResolveToPackagedName;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.AbstractStepsFactory;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;

@RunWith(JUnitReportingRunner.class)
public class JBehaveRunner extends JUnitStories {

	@BeforeClass
	public static void setup() {
		assumeThat(System.getProperty("skipStories"), IsNull.nullValue());
	}

	public JBehaveRunner() {
		JUnitReportingRunner.recommendedControls(configuredEmbedder());
		}
	
    @Override 
    public Configuration configuration() { 
        return new MostUsefulConfiguration().usePendingStepStrategy(new FailingUponPendingStep()).useStoryReporterBuilder(
                new StoryReporterBuilder()
                .withDefaultFormats().withPathResolver(new ResolveToPackagedName())
                .withFormats(Format.HTML)
                .withFailureTrace(true).withFailureTraceCompression(true));
    }
    
	@Override
	public InjectableStepsFactory stepsFactory() {
		return new AbstractStepsFactory(configuration()) {

			@Override
			public Object createInstanceOfType(Class<?> type) {
				return new WmJBehaveSteps();
			}

			@Override
			protected List<Class<?>> stepsTypes() {
				return new ArrayList<Class<?>>(Arrays.asList(WmJBehaveSteps.class));
			}
		};
	}
	
    @Override
    public List<String> storyPaths() {
		return new StoryFinder().findPaths(new File("src/test/resources").getAbsolutePath(), "**/*.story", "");
    }
    

}
package org.wmaop.bdd.steps;

public class ThreadContext {

    static final class ContextLocal extends ThreadLocal<BddTestBuilder> {
        @Override
        protected BddTestBuilder initialValue() {
            return new BddTestBuilder(new ExecutionContext("src/test/resources/feature.properties"));
        }
    }

    private static final ThreadLocal<BddTestBuilder> userThreadLocal = new ContextLocal();

    public static BddTestBuilder get() {
        return userThreadLocal.get();
    }
}

package org.wmaop.bdd.steps;

public class ThreadContext {

    private static final ThreadLocal<BddTestBuilder> USER_THREAD_LOCAL = new ContextLocal();

    private ThreadContext() {}
	
    static final class ContextLocal extends ThreadLocal<BddTestBuilder> {
        @Override
        protected BddTestBuilder initialValue() {
            return new BddTestBuilder(new ExecutionContext());
        }
    }

    public static BddTestBuilder get() {
        return USER_THREAD_LOCAL.get();
    }
}

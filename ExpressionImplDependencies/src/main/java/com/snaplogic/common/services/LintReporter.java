package com.snaplogic.common.services;

import com.snaplogic.api.Lint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * Interface used to report lints found during pipeline execution.
 *
 * @author tstack
 */
public interface LintReporter {
    /**
     * Thread-local that maintains a stack of LintReporters.
     */
    final class Local extends ThreadLocal<Stack<LintReporter>> {
        private static final Logger log = LoggerFactory.getLogger(LintReporter.class);
        private static final LintReporter DEFAULT = msg -> log.warn(msg.toString());

        @Override
        protected Stack<LintReporter> initialValue() {
            Stack<LintReporter> retval = new Stack<>();
            retval.push(DEFAULT);
            return retval;
        }

        /**
         * @param reporter The reporter to push onto the stack.
         */
        public void push(LintReporter reporter) {
            get().push(reporter);
        }

        /**
         * Pop the top reporter.
         */
        public void pop() {
            Stack<LintReporter> stack = get();

            if (stack.size() > 1) {
                stack.pop();
            }
        }

        /**
         * @param definition The lint to report.
         */
        public void report(Lint definition) {
            get().peek().report(definition);
        }
    }

    Local LOCAL_HOLDER = new Local();

    /**
     * @param msg The lint being reported.
     */
    void report(Lint msg);
}

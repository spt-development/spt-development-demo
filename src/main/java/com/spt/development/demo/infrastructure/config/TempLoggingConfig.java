package com.spt.development.demo.infrastructure.config;

import com.spt.development.logging.NoLogging;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiConsumer;

// The code in here needs to be moved into the logging project and is just a temporary solution to  get
// the tests passing again
@Configuration
public class TempLoggingConfig {
    private static final String MASKED_ARG = "******";
    private static final String ELLIPSIS = "...";

    private static final int MAX_DEBUG_STR_ARG_LEN = 75;
    private static final int ELLIPSIS_LENGTH = ELLIPSIS.length();

    @Bean
    public Advisor useCaseLoggingAdvisor() {
        final AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();

        pointcutAdvisor.setExpression("execution(* com.spt.development.demo.core.usecase..*(..))");
        pointcutAdvisor.setAdvice(new UseCaseLoggingInterceptor());

        return pointcutAdvisor;
    }

    public static class UseCaseLoggingInterceptor implements MethodInterceptor {

        @Nullable
        @Override
        public Object invoke(@Nonnull MethodInvocation invocation) throws Exception {
            final Method method = invocation.getMethod();
            final Logger log = LoggerFactory.getLogger(method.getDeclaringClass());

            debug(
                log, "{}.{}({})", method.getDeclaringClass().getSimpleName(), method.getName(),
                formatArgs(method.getParameterAnnotations(), method.getParameters())
            );

            return proceed(invocation, log);
        }

        Object proceed(MethodInvocation invocation, Logger log) throws Exception {
            final Method method = invocation.getMethod();
            final Object result;
            try {
                // Exception handling to defeat checkstyle - remove
                // This method should throw Throwable
                result = invocation.proceed();
            } catch (Throwable t) {
                if (t instanceof Exception) {
                    throw (Exception) t;
                }
                throw new RuntimeException(t);
            }

            if (log.isTraceEnabled()) {
                if (!method.getReturnType().equals(void.class)) {
                    trace(log, "{}.{} Returned: {}", method.getDeclaringClass().getSimpleName(), method.getName(), result);

                    return result;
                }
            }
            debug(log, "{}.{} - complete", method.getDeclaringClass().getSimpleName(), method.getName());

            return result;
        }

        void trace(Logger logger, String format, Object... arguments) {
            log(logger::trace, format, arguments);
        }

        void debug(Logger logger, String format, Object... arguments) {
            log(logger::debug, format, arguments);
        }

        private void log(BiConsumer<String, Object[]> log, String format, Object[] arguments) {
            log.accept(format, arguments);
        }

        static String formatArgs(Annotation[][] annotations, Object[] args) {
            final StringBuilder sb = new StringBuilder();

            for (int i = 0; i < annotations.length; i++) {
                sb.append(isNotToBeLogged(annotations[i]) ? MASKED_ARG : strValueOf(args[i]));

                if (i < annotations.length - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        private static boolean isNotToBeLogged(Annotation[] annotations) {
            return Arrays.stream(annotations)
                         .anyMatch(a -> NoLogging.class.isAssignableFrom(a.getClass()));
        }

        private static String strValueOf(Object obj) {
            if (obj instanceof String) {
                String strArg = obj.toString();

                if (strArg.length() > MAX_DEBUG_STR_ARG_LEN) {
                    strArg = String.format("%s%s", strArg.substring(0, MAX_DEBUG_STR_ARG_LEN - ELLIPSIS_LENGTH), ELLIPSIS);
                }
                strArg = strArg.replaceAll("(\r\n|\r|\n)", "\\\\n");
                strArg = strArg.replaceAll("'", "\\'");

                return String.format("'%s'", strArg);
            }
            return String.valueOf(obj);
        }

        @FunctionalInterface
        public interface LoggerConsumer {
            void accept(Logger log, String format, Object... arguments);
        }
    }
}

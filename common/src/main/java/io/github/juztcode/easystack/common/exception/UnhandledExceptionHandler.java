package io.github.juztcode.easystack.common.exception;

public class UnhandledExceptionHandler {

    public interface UnhandledFunction<T> {
        T apply() throws Throwable;
    }

    public interface UnhandledRunnable {
        void run() throws Throwable;
    }

    public static <T> T unhandled(UnhandledFunction<T> function) {
        try {
            return function.apply();
        } catch (Throwable throwable) {
            throw new UnhandledException(throwable);
        }
    }

    public static void unhandled(UnhandledRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw new UnhandledException(throwable);
        }
    }
}


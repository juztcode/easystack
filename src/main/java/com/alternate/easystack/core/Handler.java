package com.alternate.easystack.core;

import java.util.function.BiFunction;

public interface Handler<R extends Request, S extends Response> extends BiFunction<Context, R, S> {
}

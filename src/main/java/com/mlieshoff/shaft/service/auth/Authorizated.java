package com.mlieshoff.shaft.service.auth;

public abstract class Authorizated {

    private static final ThreadLocal<AuthorizationDto> THREAD_LOCAL = new ThreadLocal<>();

    public void executeIntern(AuthorizationDto authorizationDto) throws Exception {
        AuthorizationDto authorized = THREAD_LOCAL.get();
        if (authorized == null) {
            THREAD_LOCAL.set(authorizationDto);
            Exception exception = null;
            try {
                execute(authorizationDto);
            } catch (Exception catched) {
                exception = catched;
            } finally {
                THREAD_LOCAL.remove();
            }
            if (exception != null) {
                throw exception;
            }
        } else {
            throw new IllegalStateException("wrong usage auf auth");
        }
    }

    public abstract void execute(AuthorizationDto authorizationDto) throws Exception;

}

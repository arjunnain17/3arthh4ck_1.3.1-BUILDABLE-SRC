package me.earth.earthhack.api.register.exception;

import me.earth.earthhack.api.util.interfaces.Nameable;

public class AlreadyRegisteredException
extends Exception {
    private final Nameable trying;
    private final Nameable registered;

    public AlreadyRegisteredException(Nameable trying, Nameable registered) {
        this(trying.getName() + " can't be registered, a Nameable with that name is already registered.", trying, registered);
    }

    public AlreadyRegisteredException(String message, Nameable trying, Nameable registered) {
        super(message);
        this.trying = trying;
        this.registered = registered;
    }

    public Nameable getTrying() {
        return this.trying;
    }

    public Nameable getRegistered() {
        return this.registered;
    }
}

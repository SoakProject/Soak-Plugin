package org.soak.utils.single;

public interface SoakSingleInstance<S> {

    void setSponge(S sponge);

    boolean isSame(S sponge);
}

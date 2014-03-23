package com.rubentxu.juegos.core.modelo.base;


public interface State {
    /**
     * Standard {@code Enum} method.
     */
    String name() ;

    /**
     * Standard {@code Enum} method.
     */
    int ordinal() ;

    float getStateTimeMin();

}

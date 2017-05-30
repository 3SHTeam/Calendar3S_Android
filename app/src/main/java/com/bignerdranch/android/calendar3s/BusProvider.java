package com.bignerdranch.android.calendar3s;

import com.squareup.otto.Bus;

/**
 * Created by ieem5 on 2017-05-17.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}

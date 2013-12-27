package org.fbi.fskfq.domain.tps.base;

import java.io.Serializable;

public abstract class TpsToa implements Serializable {
    public TpsToa toBean(String str) {
        return toToa(str);
    }

    protected abstract TpsToa toToa(String str);
}

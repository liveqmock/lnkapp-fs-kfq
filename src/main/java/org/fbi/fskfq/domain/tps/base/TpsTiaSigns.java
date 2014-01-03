package org.fbi.fskfq.domain.tps.base;

import java.io.Serializable;

/**
 * Ç©Ãû
 */
public class TpsTiaSigns implements Serializable {
   private String Sign = "";
   private String Stamp = "";

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getStamp() {
        return Stamp;
    }

    public void setStamp(String stamp) {
        Stamp = stamp;
    }
}

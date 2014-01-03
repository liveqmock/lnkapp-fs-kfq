package org.fbi.fskfq.enums;

import java.util.Hashtable;

/**
 * Linking缴款单状态 0：初始  1：已缴款  2：已撤销
 */
public enum BillStatus implements EnumApp {
    INIT("0", "初始"),
    PAYOFF("1", "已缴款"),
    CANCELED("2", "已撤销");

    private String code = null;
    private String title = null;
    private static Hashtable<String, BillStatus> aliasEnums;

    BillStatus(String code, String title) {
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static BillStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public String toRtnMsg() {
        return this.code + "|" + this.title;
    }
}

package org.fbi.fskfq.domain.cbs.T4010Request;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.math.BigDecimal;

/**
 * User: zhanrui
 * Date: 13-12-25
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia4010 {
    @DataField(seq = 1)
    private String billtypeCode;

    @DataField(seq = 2)
    private String billNo;

    @DataField(seq = 3)
    private String verifyNo;

    @DataField(seq = 4)
    private BigDecimal billMoney;

    @DataField(seq = 5)
    private String setYear;
    @DataField(seq = 6)
    private String areaCode;

    public String getBilltypeCode() {
        return billtypeCode;
    }

    public void setBilltypeCode(String billtypeCode) {
        this.billtypeCode = billtypeCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getVerifyNo() {
        return verifyNo;
    }

    public void setVerifyNo(String verifyNo) {
        this.verifyNo = verifyNo;
    }

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    public String getSetYear() {
        return setYear;
    }

    public void setSetYear(String setYear) {
        this.setYear = setYear;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return "CbsTia4010{" +
                "billtypeCode='" + billtypeCode + '\'' +
                ", billNo='" + billNo + '\'' +
                ", verifyNo='" + verifyNo + '\'' +
                ", billMoney=" + billMoney +
                ", setYear='" + setYear + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }
}

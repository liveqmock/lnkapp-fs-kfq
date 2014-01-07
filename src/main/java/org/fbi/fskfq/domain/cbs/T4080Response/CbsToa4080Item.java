package org.fbi.fskfq.domain.cbs.T4080Response;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.OneToManySeperatedTextMessage;

import java.math.BigDecimal;

/**
 * User: zhanrui
 * Date: 13-12-30
 */
@OneToManySeperatedTextMessage(separator = ",")
public class CbsToa4080Item {
    @DataField(seq = 1)
    private String sn;

    @DataField(seq = 2)
    private String ienCode;

    @DataField(seq = 3)
    private String ienName;

    @DataField(seq = 4)
    private String billNo;

    @DataField(seq = 5)
    private String inBisCode;

    @DataField(seq = 6)
    private String inBisName;

    @DataField(seq = 7)
    private BigDecimal chargemoney;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getIenCode() {
        return ienCode;
    }

    public void setIenCode(String ienCode) {
        this.ienCode = ienCode;
    }

    public String getIenName() {
        return ienName;
    }

    public void setIenName(String ienName) {
        this.ienName = ienName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getInBisCode() {
        return inBisCode;
    }

    public void setInBisCode(String inBisCode) {
        this.inBisCode = inBisCode;
    }

    public String getInBisName() {
        return inBisName;
    }

    public void setInBisName(String inBisName) {
        this.inBisName = inBisName;
    }

    public BigDecimal getChargemoney() {
        return chargemoney;
    }

    public void setChargemoney(BigDecimal chargemoney) {
        this.chargemoney = chargemoney;
    }

    @Override
    public String toString() {
        return "CbsToa4080Item{" +
                "sn='" + sn + '\'' +
                ", ienCode='" + ienCode + '\'' +
                ", ienName='" + ienName + '\'' +
                ", billNo='" + billNo + '\'' +
                ", inBisCode='" + inBisCode + '\'' +
                ", inBisName='" + inBisName + '\'' +
                ", chargemoney=" + chargemoney +
                '}';
    }
}

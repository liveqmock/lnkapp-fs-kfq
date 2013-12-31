package org.fbi.fskfq.domain.cbs.T4010Response;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.OneToManySeperatedTextMessage;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-10
 * Time: ÏÂÎç5:44
 */
@OneToManySeperatedTextMessage(separator = ",")
public class CbsToa4010Item {
    @DataField(seq = 1)
    private String chrId;
    @DataField(seq = 2)
    private String mainId;
    @DataField(seq = 3)
    private String inBisCode;
    @DataField(seq = 4)
    private String inBisName;
    @DataField(seq = 5)
    private String measure;
    @DataField(seq = 6)
    private String chargenum;
    @DataField(seq = 7)
    private String chargestandard;
    @DataField(seq = 8)
    private BigDecimal chargemoney;
    @DataField(seq = 9)
    private String itemChkcode;


    public String getChrId() {
        return chrId;
    }

    public void setChrId(String chrId) {
        this.chrId = chrId;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
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

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getChargenum() {
        return chargenum;
    }

    public void setChargenum(String chargenum) {
        this.chargenum = chargenum;
    }

    public String getChargestandard() {
        return chargestandard;
    }

    public void setChargestandard(String chargestandard) {
        this.chargestandard = chargestandard;
    }

    public BigDecimal getChargemoney() {
        return chargemoney;
    }

    public void setChargemoney(BigDecimal chargemoney) {
        this.chargemoney = chargemoney;
    }

    public String getItemChkcode() {
        return itemChkcode;
    }

    public void setItemChkcode(String itemChkcode) {
        this.itemChkcode = itemChkcode;
    }

    @Override
    public String toString() {
        return "TOA4010Item{" +
                "chrId='" + chrId + '\'' +
                ", mainId='" + mainId + '\'' +
                ", inBisCode='" + inBisCode + '\'' +
                ", inBisName='" + inBisName + '\'' +
                ", measure='" + measure + '\'' +
                ", chargenum='" + chargenum + '\'' +
                ", chargestandard='" + chargestandard + '\'' +
                ", chargemoney=" + chargemoney +
                ", itemChkcode='" + itemChkcode + '\'' +
                '}';
    }
}

package org.fbi.fskfq.domain.cbs.T4013Request;


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
public class CbsTia4013Item {
    @DataField(seq = 1)
    private String inBisCode;
    @DataField(seq = 2)
    private String inBisName;
    @DataField(seq = 3)
    private String chargenum;
    @DataField(seq = 4)
    private BigDecimal chargemoney;

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

    public String getChargenum() {
        return chargenum;
    }

    public void setChargenum(String chargenum) {
        this.chargenum = chargenum;
    }

    public BigDecimal getChargemoney() {
        return chargemoney;
    }

    public void setChargemoney(BigDecimal chargemoney) {
        this.chargemoney = chargemoney;
    }
}

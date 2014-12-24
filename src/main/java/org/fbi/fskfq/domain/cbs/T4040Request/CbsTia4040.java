package org.fbi.fskfq.domain.cbs.T4040Request;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

/**
 * User: zhanrui
 * Date: 13-12-25
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia4040 {
    @DataField(seq = 1)
    private String chrId;

    @DataField(seq = 2)
    private String billtypeCode;

    @DataField(seq = 3)
    private String billNo;

    @DataField(seq = 4)
    private String setYear;

    @DataField(seq = 5)
    private String areaCode;

    public String getChrId() {
        return chrId;
    }

    public void setChrId(String chrId) {
        this.chrId = chrId;
    }

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
        return "CbsTia4040{" +
                "chrId='" + chrId + '\'' +
                ", billtypeCode='" + billtypeCode + '\'' +
                ", billNo='" + billNo + '\'' +
                ", setYear='" + setYear + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }
}

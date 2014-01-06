package org.fbi.fskfq.domain.cbs.T4013Response;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-10
 * Time: ÏÂÎç5:44
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsToa4013 {
    @DataField(seq = 1)
    private String chrId;
    @DataField(seq = 2)
    private String billtypeCode;
    @DataField(seq = 3)
    private String billtypeName;
    @DataField(seq = 4)
    private String billNo;
    @DataField(seq = 5)
    private String verifyNo;
    @DataField(seq = 6)
    private String makedate;
    @DataField(seq = 7)
    private String ienCode;
    @DataField(seq = 8)
    private String ienName;
    @DataField(seq = 9)
    private String setYear;
    @DataField(seq = 10)
    private String succCode;

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

    public String getBilltypeName() {
        return billtypeName;
    }

    public void setBilltypeName(String billtypeName) {
        this.billtypeName = billtypeName;
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

    public String getMakedate() {
        return makedate;
    }

    public void setMakedate(String makedate) {
        this.makedate = makedate;
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

    public String getSetYear() {
        return setYear;
    }

    public void setSetYear(String setYear) {
        this.setYear = setYear;
    }

    public String getSuccCode() {
        return succCode;
    }

    public void setSuccCode(String succCode) {
        this.succCode = succCode;
    }

    @Override
    public String toString() {
        return "CbsToa4013{" +
                "chrId='" + chrId + '\'' +
                ", billtypeCode='" + billtypeCode + '\'' +
                ", billtypeName='" + billtypeName + '\'' +
                ", billNo='" + billNo + '\'' +
                ", verifyNo='" + verifyNo + '\'' +
                ", makedate='" + makedate + '\'' +
                ", ienCode='" + ienCode + '\'' +
                ", ienName='" + ienName + '\'' +
                ", setYear='" + setYear + '\'' +
                ", succCode='" + succCode + '\'' +
                '}';
    }
}

package org.fbi.fskfq.domain.cbs.T4011Request;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia4011 {
    @DataField(seq = 1)
    private String chrId;

    @DataField(seq = 2)
    private String billtypeCode;

    @DataField(seq = 3)
    private String billtypeName;

    @DataField(seq = 4)
    private String billNo;

    @DataField(seq = 5)
    private String bankIndate;

    @DataField(seq = 6)
    private String incomestatus;

    @DataField(seq = 7)
    private String pmCode;

    @DataField(seq = 8)
    private String chequeNo;

    @DataField(seq = 9)
    private String payerbank;

    @DataField(seq = 10)
    private String payeraccount;

    @DataField(seq = 11)
    private String setYear;

    @DataField(seq = 12)
    private String routeUserCode;

    @DataField(seq = 13)
    private String license;

    @DataField(seq = 14)
    private String businessId;

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

    public String getBankIndate() {
        return bankIndate;
    }

    public void setBankIndate(String bankIndate) {
        this.bankIndate = bankIndate;
    }

    public String getIncomestatus() {
        return incomestatus;
    }

    public void setIncomestatus(String incomestatus) {
        this.incomestatus = incomestatus;
    }

    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getPayerbank() {
        return payerbank;
    }

    public void setPayerbank(String payerbank) {
        this.payerbank = payerbank;
    }

    public String getPayeraccount() {
        return payeraccount;
    }

    public void setPayeraccount(String payeraccount) {
        this.payeraccount = payeraccount;
    }

    public String getSetYear() {
        return setYear;
    }

    public void setSetYear(String setYear) {
        this.setYear = setYear;
    }

    public String getRouteUserCode() {
        return routeUserCode;
    }

    public void setRouteUserCode(String routeUserCode) {
        this.routeUserCode = routeUserCode;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String toString() {
        return "CbsTia4011{" +
                "chrId='" + chrId + '\'' +
                ", billtypeCode='" + billtypeCode + '\'' +
                ", billtypeName='" + billtypeName + '\'' +
                ", billNo='" + billNo + '\'' +
                ", bankIndate='" + bankIndate + '\'' +
                ", incomestatus='" + incomestatus + '\'' +
                ", pmCode='" + pmCode + '\'' +
                ", chequeNo='" + chequeNo + '\'' +
                ", payerbank='" + payerbank + '\'' +
                ", payeraccount='" + payeraccount + '\'' +
                ", setYear='" + setYear + '\'' +
                ", routeUserCode='" + routeUserCode + '\'' +
                ", license='" + license + '\'' +
                ", businessId='" + businessId + '\'' +
                '}';
    }
}

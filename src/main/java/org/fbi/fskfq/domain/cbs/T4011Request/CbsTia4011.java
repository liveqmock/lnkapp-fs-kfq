package org.fbi.fskfq.domain.cbs.T4011Request;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.math.BigDecimal;

@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia4011 {
    @DataField(seq = 1)
    private String chrId;

    @DataField(seq = 2)
    private String billtypeCode;

    @DataField(seq = 3)
    private String billNo;

    @DataField(seq = 4)
    private BigDecimal billMoney;

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

    @DataField(seq = 15)
    private String receiver;

    @DataField(seq = 16)
    private String receiverbank;

    @DataField(seq = 17)
    private String receiveraccount;

    @DataField(seq = 18)
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

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverbank() {
        return receiverbank;
    }

    public void setReceiverbank(String receiverbank) {
        this.receiverbank = receiverbank;
    }

    public String getReceiveraccount() {
        return receiveraccount;
    }

    public void setReceiveraccount(String receiveraccount) {
        this.receiveraccount = receiveraccount;
    }

    @Override
    public String toString() {
        return "CbsTia4011{" +
                "chrId='" + chrId + '\'' +
                ", billtypeCode='" + billtypeCode + '\'' +
                ", billNo='" + billNo + '\'' +
                ", billMoney=" + billMoney +
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
                ", receiver='" + receiver + '\'' +
                ", receiverbank='" + receiverbank + '\'' +
                ", receiveraccount='" + receiveraccount + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }
}

package org.fbi.fskfq.domain.cbs.T4013Request;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.OneToMany;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia4013 {
    @DataField(seq = 1)
    private String rgCode;

    @DataField(seq = 2)
    private String billtypeCode;

    @DataField(seq = 3)
    private String billNo;

    @DataField(seq = 4)
    private String verifyNo;

    @DataField(seq = 5)
    private String pmCode;

    @DataField(seq = 6)
    private String ienCode;

    @DataField(seq = 7)
    private String ienName;

    @DataField(seq = 8)
    private String consignIenCode;

    @DataField(seq = 9)
    private String consignIenName;

    @DataField(seq = 10)
    private BigDecimal billMoney;

    @DataField(seq = 11)
    private String setYear;

    @DataField(seq = 12)
    private String bankUser;

    @DataField(seq = 13)
    private String bankNo;

    @DataField(seq = 14)
    private String payer;

    @DataField(seq = 15)
    private String payerbank;

    @DataField(seq = 16)
    private String payeraccount;

    @DataField(seq = 17)
    private String receiver;

    @DataField(seq = 18)
    private String receiverbank;

    @DataField(seq = 19)
    private String receiveraccount;

    @DataField(seq = 20)
    private String isConsign;

    @DataField(seq = 21)
    private String remark;

    @DataField(seq = 22)
    private String areaCode;

    @DataField(seq = 23)
    private String itemNum;

    @DataField(seq = 24)
    @OneToMany(mappedTo = "org.fbi.fskfq.domain.cbs.T4013Request.CbsTia4013Item", totalNumberField = "itemNum")
    private java.util.List<CbsTia4013Item> items;


    public String getRgCode() {
        return rgCode;
    }

    public void setRgCode(String rgCode) {
        this.rgCode = rgCode;
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

    public String getVerifyNo() {
        return verifyNo;
    }

    public void setVerifyNo(String verifyNo) {
        this.verifyNo = verifyNo;
    }

    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
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

    public String getConsignIenCode() {
        return consignIenCode;
    }

    public void setConsignIenCode(String consignIenCode) {
        this.consignIenCode = consignIenCode;
    }

    public String getConsignIenName() {
        return consignIenName;
    }

    public void setConsignIenName(String consignIenName) {
        this.consignIenName = consignIenName;
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

    public String getBankUser() {
        return bankUser;
    }

    public void setBankUser(String bankUser) {
        this.bankUser = bankUser;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
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

    public String getIsConsign() {
        return isConsign;
    }

    public void setIsConsign(String isConsign) {
        this.isConsign = isConsign;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<CbsTia4013Item> getItems() {
        return items;
    }

    public void setItems(List<CbsTia4013Item> items) {
        this.items = items;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return "CbsTia4013{" +
                "rgCode='" + rgCode + '\'' +
                ", billtypeCode='" + billtypeCode + '\'' +
                ", billNo='" + billNo + '\'' +
                ", verifyNo='" + verifyNo + '\'' +
                ", pmCode='" + pmCode + '\'' +
                ", ienCode='" + ienCode + '\'' +
                ", ienName='" + ienName + '\'' +
                ", consignIenCode='" + consignIenCode + '\'' +
                ", consignIenName='" + consignIenName + '\'' +
                ", billMoney=" + billMoney +
                ", setYear='" + setYear + '\'' +
                ", bankUser='" + bankUser + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", payer='" + payer + '\'' +
                ", payerbank='" + payerbank + '\'' +
                ", payeraccount='" + payeraccount + '\'' +
                ", receiver='" + receiver + '\'' +
                ", receiverbank='" + receiverbank + '\'' +
                ", receiveraccount='" + receiveraccount + '\'' +
                ", isConsign='" + isConsign + '\'' +
                ", remark='" + remark + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", itemNum='" + itemNum + '\'' +
                ", items=" + items +
                '}';
    }
}

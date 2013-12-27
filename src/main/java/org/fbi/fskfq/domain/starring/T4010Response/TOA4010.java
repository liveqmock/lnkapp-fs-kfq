package org.fbi.fskfq.domain.starring.T4010Response;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.OneToMany;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.util.List;


@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TOA4010 {
    @DataField(seq = 1)
    private String chrId;

    @DataField(seq = 2)
    private String billtypeCode;

    @DataField(seq = 3)
    private String billtypeName;

    @DataField(seq = 4)
    private String billNo;

    @DataField(seq = 5)
    private String makedate;

    @DataField(seq = 6)
    private String ienCode;

    @DataField(seq = 7)
    private String ienName;

    @DataField(seq = 8)
    private String consignIenCode;

    @DataField(seq = 9)
    private String consignIenName;

    @DataField(seq = 10)
    private String pmCode;

    @DataField(seq = 11)
    private String pmName;

    @DataField(seq = 12)
    private String chequeNo;

    @DataField(seq = 13)
    private String payer;

    @DataField(seq = 14)
    private String payerbank;

    @DataField(seq = 15)
    private String payeraccount;

    @DataField(seq = 16)
    private String receiver;

    @DataField(seq = 17)
    private String receiverbank;

    @DataField(seq = 18)
    private String receiveraccount;

    @DataField(seq = 19)
    private String verifyNo;

    @DataField(seq = 20)
    private String rgCode;

    @DataField(seq = 21)
    private String receivetype;

    @DataField(seq = 22)
    private String inputername;

    @DataField(seq = 23)
    private String isConsign;

    @DataField(seq = 24)
    private String lateflag;

    @DataField(seq = 25)
    private String nosourceIds;

    @DataField(seq = 26)
    private String supplytempletId;

    @DataField(seq = 27)
    private String remark;

    @DataField(seq = 28)
    private String itemNum;

    @DataField(seq = 29)
    @OneToMany(mappedTo = "org.fbi.fskfq.domain.starring.T4010Response.TOA4010Item", totalNumberField = "itemNum")
    private java.util.List<TOA4010Item> items;

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

    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
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

    public String getVerifyNo() {
        return verifyNo;
    }

    public void setVerifyNo(String verifyNo) {
        this.verifyNo = verifyNo;
    }

    public String getRgCode() {
        return rgCode;
    }

    public void setRgCode(String rgCode) {
        this.rgCode = rgCode;
    }

    public String getReceivetype() {
        return receivetype;
    }

    public void setReceivetype(String receivetype) {
        this.receivetype = receivetype;
    }

    public String getInputername() {
        return inputername;
    }

    public void setInputername(String inputername) {
        this.inputername = inputername;
    }

    public String getIsConsign() {
        return isConsign;
    }

    public void setIsConsign(String isConsign) {
        this.isConsign = isConsign;
    }

    public String getLateflag() {
        return lateflag;
    }

    public void setLateflag(String lateflag) {
        this.lateflag = lateflag;
    }

    public String getNosourceIds() {
        return nosourceIds;
    }

    public void setNosourceIds(String nosourceIds) {
        this.nosourceIds = nosourceIds;
    }

    public String getSupplytempletId() {
        return supplytempletId;
    }

    public void setSupplytempletId(String supplytempletId) {
        this.supplytempletId = supplytempletId;
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

    public List<TOA4010Item> getItems() {
        return items;
    }

    public void setItems(List<TOA4010Item> items) {
        this.items = items;
    }
}

package org.fbi.fskfq.domain.cbs.T4099Response;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.math.BigDecimal;

/**
 * User: zhanrui
 * Date: 2014-1-14
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsToa4099 {
    @DataField(seq = 1)
    private String chrId;            //缴款书ID
    @DataField(seq = 2)
    private String billtypeName;     //缴款书样式名称
    @DataField(seq = 3)
    private String billNo;           //票号
    @DataField(seq = 4)
    private String makedate;         //开票日期
    @DataField(seq = 5)
    private String setYear;          //业务年度
    @DataField(seq = 6)
    private BigDecimal billMoney;    //收款金额
    @DataField(seq = 7)
    private String operPayTlrid;     //缴款柜员
    @DataField(seq = 8)
    private String operPayDate;      //缴款日期
    @DataField(seq = 9)
    private String operPayTime;      //缴款时间

    public String getChrId() {
        return chrId;
    }

    public void setChrId(String chrId) {
        this.chrId = chrId;
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

    public String getSetYear() {
        return setYear;
    }

    public void setSetYear(String setYear) {
        this.setYear = setYear;
    }

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    public String getOperPayTlrid() {
        return operPayTlrid;
    }

    public void setOperPayTlrid(String operPayTlrid) {
        this.operPayTlrid = operPayTlrid;
    }

    public String getOperPayDate() {
        return operPayDate;
    }

    public void setOperPayDate(String operPayDate) {
        this.operPayDate = operPayDate;
    }

    public String getOperPayTime() {
        return operPayTime;
    }

    public void setOperPayTime(String operPayTime) {
        this.operPayTime = operPayTime;
    }

    @Override
    public String toString() {
        return "CbsToa4099{" +
                "chrId='" + chrId + '\'' +
                ", billtypeName='" + billtypeName + '\'' +
                ", billNo='" + billNo + '\'' +
                ", makedate='" + makedate + '\'' +
                ", setYear='" + setYear + '\'' +
                ", billMoney=" + billMoney +
                ", operPayTlrid='" + operPayTlrid + '\'' +
                ", operPayDate='" + operPayDate + '\'' +
                ", operPayTime='" + operPayTime + '\'' +
                '}';
    }
}

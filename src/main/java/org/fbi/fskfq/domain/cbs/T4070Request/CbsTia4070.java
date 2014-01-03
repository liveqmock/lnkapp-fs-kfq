package org.fbi.fskfq.domain.cbs.T4070Request;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

/**
 * User: zhanrui
 * Date: 13-12-25
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia4070 {
    @DataField(seq = 1)
    private String rptType;

    @DataField(seq = 2)
    private String areaCode;   //²ÆÖ¤±àÂë

    @DataField(seq = 3)
    private String startDate;

    @DataField(seq = 4)
    private String endDate;

    public String getRptType() {
        return rptType;
    }

    public void setRptType(String rptType) {
        this.rptType = rptType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "CbsTia4070{" +
                "rptType='" + rptType + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}

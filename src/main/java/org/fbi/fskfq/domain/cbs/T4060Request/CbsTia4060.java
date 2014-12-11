package org.fbi.fskfq.domain.cbs.T4060Request;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.OneToMany;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia4060 {
    @DataField(seq = 1)
    private String year;    //年度

    @DataField(seq = 2)
    private String areacode;  //财政编码

    @DataField(seq = 3)
    private String totalamt; //总金额

    @DataField(seq = 4)
    private String startdate; //开始日期

    @DataField(seq = 5)
    private String enddate; //结束日期

    @DataField(seq = 6)
    private String areaCode;

    @DataField(seq = 7)
    private String itemNum;

    @DataField(seq = 8)
    @OneToMany(mappedTo = "org.fbi.fskfq.domain.cbs.T4060Request.CbsTia4060Item", totalNumberField = "itemNum")
    private List<CbsTia4060Item> items;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(String totalamt) {
        this.totalamt = totalamt;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<CbsTia4060Item> getItems() {
        return items;
    }

    public void setItems(List<CbsTia4060Item> items) {
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
        return "CbsTia4060{" +
                "year='" + year + '\'' +
                ", areacode='" + areacode + '\'' +
                ", totalamt='" + totalamt + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", itemNum='" + itemNum + '\'' +
                ", items=" + items +
                '}';
    }
}

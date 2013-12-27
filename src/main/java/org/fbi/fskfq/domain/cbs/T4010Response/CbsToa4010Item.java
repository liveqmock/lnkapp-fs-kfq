package org.fbi.fskfq.domain.cbs.T4010Response;


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
public class CbsToa4010Item {
    @DataField(seq = 1)
    private String chr_id;
    @DataField(seq = 2)
    private String main_id;
    @DataField(seq = 3)
    private String in_bis_code;
    @DataField(seq = 4)
    private String in_bis_name;
    @DataField(seq = 5)
    private String measure;
    @DataField(seq = 6)
    private String chargenum;
    @DataField(seq = 7)
    private String chargestandard;
    @DataField(seq = 8)
    private BigDecimal chargemoney;
    @DataField(seq = 9)
    private String item_chkcode;


    public String getChr_id() {
        return chr_id;
    }

    public void setChr_id(String chr_id) {
        this.chr_id = chr_id;
    }

    public String getMain_id() {
        return main_id;
    }

    public void setMain_id(String main_id) {
        this.main_id = main_id;
    }

    public String getIn_bis_code() {
        return in_bis_code;
    }

    public void setIn_bis_code(String in_bis_code) {
        this.in_bis_code = in_bis_code;
    }

    public String getIn_bis_name() {
        return in_bis_name;
    }

    public void setIn_bis_name(String in_bis_name) {
        this.in_bis_name = in_bis_name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getChargenum() {
        return chargenum;
    }

    public void setChargenum(String chargenum) {
        this.chargenum = chargenum;
    }

    public String getChargestandard() {
        return chargestandard;
    }

    public void setChargestandard(String chargestandard) {
        this.chargestandard = chargestandard;
    }

    public BigDecimal getChargemoney() {
        return chargemoney;
    }

    public void setChargemoney(BigDecimal chargemoney) {
        this.chargemoney = chargemoney;
    }

    public String getItem_chkcode() {
        return item_chkcode;
    }

    public void setItem_chkcode(String item_chkcode) {
        this.item_chkcode = item_chkcode;
    }

    @Override
    public String toString() {
        return "TOA4010Item{" +
                "chr_id='" + chr_id + '\'' +
                ", main_id='" + main_id + '\'' +
                ", in_bis_code='" + in_bis_code + '\'' +
                ", in_bis_name='" + in_bis_name + '\'' +
                ", measure='" + measure + '\'' +
                ", chargenum='" + chargenum + '\'' +
                ", chargestandard='" + chargestandard + '\'' +
                ", chargemoney=" + chargemoney +
                ", item_chkcode='" + item_chkcode + '\'' +
                '}';
    }
}

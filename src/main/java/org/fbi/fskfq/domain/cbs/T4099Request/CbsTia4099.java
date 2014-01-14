package org.fbi.fskfq.domain.cbs.T4099Request;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

/**
 * User: zhanrui
 * Date: 2014-1-14
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia4099 {
    @DataField(seq = 1)
    private String billNo;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @Override
    public String toString() {
        return "CbsTia4099{" +
                "billNo='" + billNo + '\'' +
                '}';
    }
}

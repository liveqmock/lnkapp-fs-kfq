package org.fbi.fskfq.domain.cbs.T9999Response;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|",  mainClass = true)
public class TOA9999 {
    @DataField(seq = 1)
    private String errMsg;   //业务逻辑 错误信息

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "TOA9999{" +
                "errMsg='" + errMsg + '\'' +
                '}';
    }
}

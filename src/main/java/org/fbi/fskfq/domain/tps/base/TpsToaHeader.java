package org.fbi.fskfq.domain.tps.base;

import java.io.Serializable;

public class TpsToaHeader implements Serializable {
    public String src = "";                   // 发送方编码
    public String des = "";                   // 接收方编码
    public String dataType = "";
    public String msgRef = "";                // 报文参考号  发起请求报文时报文参考号同报文标识号
    public String workDate = "";              // 工作日期
    public String msgId = "";                 // 报文编号

    @Override
    public String toString() {
        return "TpsToaHeader{" +
                "src='" + src + '\'' +
                ", des='" + des + '\'' +
                ", dataType='" + dataType + '\'' +
                ", msgRef='" + msgRef + '\'' +
                ", workDate='" + workDate + '\'' +
                ", msgId='" + msgId + '\'' +
                '}';
    }
}

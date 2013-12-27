package org.fbi.fskfq.domain.tps.base.xml;

import java.io.Serializable;

public class TpsTiaHeader implements Serializable {
    public String src = "";                   // 发送方编码
    public String des = "";                   // 接收方编码
    public String dataType = "";
    public String msgId = "";                 // 报文标识号
    public String msgRef = "";                // 报文参考号  发起请求报文时报文参考号同报文标识号
    public String workDate = "";              // 工作日期
}

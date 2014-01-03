package org.fbi.fskfq.domain.tps.base;

import java.io.Serializable;

public class TpsTiaHeader implements Serializable {
    private String src = "";                   // 发送方编码
    private String des = "";                   // 接收方编码
    private String dataType = "";
    private String msgId = "";                 // 报文标识号
    private String msgRef = "";                // 报文参考号  发起请求报文时报文参考号同报文标识号
    private String workDate = "";              // 工作日期

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgRef() {
        return msgRef;
    }

    public void setMsgRef(String msgRef) {
        this.msgRef = msgRef;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    @Override
    public String toString() {
        return "TpsTiaHeader{" +
                "src='" + src + '\'' +
                ", des='" + des + '\'' +
                ", dataType='" + dataType + '\'' +
                ", msgId='" + msgId + '\'' +
                ", msgRef='" + msgRef + '\'' +
                ", workDate='" + workDate + '\'' +
                '}';
    }
}

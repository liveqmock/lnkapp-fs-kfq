package org.fbi.fskfq.domain.tps.txn;

/**
 * 通用收妥回执
 */

public class TpsToa9000 {
    /*
    ori_datatype	原数据类型	NString	4		M
    ori_send_orgcode	原发起方编码	NString	[1,15]		M
    ori_entrust_date	原委托日期	Date		请求发起日期	M
    result	公共处理结果	String	4		M
    add_word	附言	GBString	[1,60]		O
     */
    private String oriDatatype = "";
    private String oriSendOrgcode = "";
    private String oriEntrustDate = "";
    private String result = "";
    private String addWord = "";

    public String getOriDatatype() {
        return oriDatatype;
    }

    public void setOriDatatype(String oriDatatype) {
        this.oriDatatype = oriDatatype;
    }

    public String getOriSendOrgcode() {
        return oriSendOrgcode;
    }

    public void setOriSendOrgcode(String oriSendOrgcode) {
        this.oriSendOrgcode = oriSendOrgcode;
    }

    public String getOriEntrustDate() {
        return oriEntrustDate;
    }

    public void setOriEntrustDate(String oriEntrustDate) {
        this.oriEntrustDate = oriEntrustDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAddWord() {
        return addWord;
    }

    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }
}

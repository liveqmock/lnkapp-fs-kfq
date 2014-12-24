package org.fbi.fskfq.domain.tps.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.TpsTiaBody;
import org.fbi.fskfq.domain.tps.base.TpsTiaHeader;
import org.fbi.fskfq.domain.tps.base.TpsTiaSigns;

import java.io.Serializable;

/**
 * 应收数据收款确认
 */

@XStreamAlias("Root")
public class TpsTia2402 extends TpsTia {
    public TpsTiaHeader Head = new TpsTiaHeader();
    public Body Body = new Body();
    public TpsTiaSigns Signs = new TpsTiaSigns();

    @Override
    public TpsTiaHeader getHeader() {
        return Head;
    }

    @Override
    public TpsTiaBody getBody() {
        return Body;
    }

    @Override
    public TpsTiaSigns getSigns() {
        return Signs;
    }

    public static class Body extends TpsTiaBody{
        private BodyObject Object = new BodyObject();

        public BodyObject getObject() {
            return Object;
        }

        public void setObject(BodyObject object) {
            Object = object;
        }
    }

    public static class BodyObject implements Serializable {
        private BodyRecord Record = new BodyRecord();

        public BodyRecord getRecord() {
            return Record;
        }

        public void setRecord(BodyRecord record) {
            Record = record;
        }
    }


    /*
        chr_id	缴款书ID
        billtype_code	缴款书样式编码
        bill_no	票号
        bill_money	收款金额
        bank_indate	银行收款时间
        incomestatus	收款状态
        pm_code	缴款方式编码
        cheque_no	结算号
        payerbank	缴款人账户开户行
        payeraccount	缴款人账号
        set_year	年度
        route_user_code	路由用户编码
        license	授权序列号
        business_id	交易流水号
     */
    public static class BodyRecord implements Serializable {

        private String chr_id = "";
        private String billtype_code = "";
        private String bill_no = "";
        private String bill_money = "";
        private String bank_indate = "";
        private String incomestatus = "";
        private String pm_code = "";
        private String cheque_no = "";
        private String payerbank = "";
        private String payeraccount = "";
        private String set_year = "";
        private String route_user_code = "";
        private String license = "";
        private String business_id = "";
        private String receiver = "";
        private String receiveraccount = "";
        private String receiverbank = "";

        public String getChr_id() {
            return chr_id;
        }

        public void setChr_id(String chr_id) {
            this.chr_id = chr_id;
        }

        public String getBilltype_code() {
            return billtype_code;
        }

        public void setBilltype_code(String billtype_code) {
            this.billtype_code = billtype_code;
        }

        public String getBill_no() {
            return bill_no;
        }

        public void setBill_no(String bill_no) {
            this.bill_no = bill_no;
        }

        public String getBill_money() {
            return bill_money;
        }

        public void setBill_money(String bill_money) {
            this.bill_money = bill_money;
        }

        public String getBank_indate() {
            return bank_indate;
        }

        public void setBank_indate(String bank_indate) {
            this.bank_indate = bank_indate;
        }

        public String getIncomestatus() {
            return incomestatus;
        }

        public void setIncomestatus(String incomestatus) {
            this.incomestatus = incomestatus;
        }

        public String getPm_code() {
            return pm_code;
        }

        public void setPm_code(String pm_code) {
            this.pm_code = pm_code;
        }

        public String getCheque_no() {
            return cheque_no;
        }

        public void setCheque_no(String cheque_no) {
            this.cheque_no = cheque_no;
        }

        public String getPayerbank() {
            return payerbank;
        }

        public void setPayerbank(String payerbank) {
            this.payerbank = payerbank;
        }

        public String getPayeraccount() {
            return payeraccount;
        }

        public void setPayeraccount(String payeraccount) {
            this.payeraccount = payeraccount;
        }

        public String getSet_year() {
            return set_year;
        }

        public void setSet_year(String set_year) {
            this.set_year = set_year;
        }

        public String getRoute_user_code() {
            return route_user_code;
        }

        public void setRoute_user_code(String route_user_code) {
            this.route_user_code = route_user_code;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(String business_id) {
            this.business_id = business_id;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getReceiveraccount() {
            return receiveraccount;
        }

        public void setReceiveraccount(String receiveraccount) {
            this.receiveraccount = receiveraccount;
        }

        public String getReceiverbank() {
            return receiverbank;
        }

        public void setReceiverbank(String receiverbank) {
            this.receiverbank = receiverbank;
        }
    }

    @Override
    public String toString() {
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(TpsTia2402.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}

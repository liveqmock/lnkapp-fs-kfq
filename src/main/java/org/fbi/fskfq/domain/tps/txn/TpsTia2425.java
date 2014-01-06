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
 * ╤тук
 */

@XStreamAlias("Root")
public class TpsTia2425 extends TpsTia {
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

    public static class BodyRecord implements Serializable {
        private String begin_date = "";
        private String end_date = "";
        private String receiveraccount = "";
        private String consign_ien_code = "";
        private String chr_id = "";
        private String billtype_code = "";
        private String bill_no = "";
        private String bankmoney = "";
        private String is_getcollectinfo = "";
        private String set_year = "";
        private String route_user_code = "";
        private String license = "";
        private String business_id = "";

        public String getBegin_date() {
            return begin_date;
        }

        public void setBegin_date(String begin_date) {
            this.begin_date = begin_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getReceiveraccount() {
            return receiveraccount;
        }

        public void setReceiveraccount(String receiveraccount) {
            this.receiveraccount = receiveraccount;
        }

        public String getConsign_ien_code() {
            return consign_ien_code;
        }

        public void setConsign_ien_code(String consign_ien_code) {
            this.consign_ien_code = consign_ien_code;
        }

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

        public String getBankmoney() {
            return bankmoney;
        }

        public void setBankmoney(String bankmoney) {
            this.bankmoney = bankmoney;
        }

        public String getIs_getcollectinfo() {
            return is_getcollectinfo;
        }

        public void setIs_getcollectinfo(String is_getcollectinfo) {
            this.is_getcollectinfo = is_getcollectinfo;
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
    }
        @Override
    public String toString() {
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(TpsTia2425.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}

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
 * 撤销收款(手工票)
 * 20141210 zhanrui
 */

@XStreamAlias("Root")
public class TpsTia2458 extends TpsTia {
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
        private String chr_id = "";
        private String billtype_code = "";
        private String bill_no = "";
        private String bill_money = "";
        private String rg_code = "";
        private String set_year = "";

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

        public String getSet_year() {
            return set_year;
        }

        public void setSet_year(String set_year) {
            this.set_year = set_year;
        }

        public String getBill_money() {
            return bill_money;
        }

        public void setBill_money(String bill_money) {
            this.bill_money = bill_money;
        }

        public String getRg_code() {
            return rg_code;
        }

        public void setRg_code(String rg_code) {
            this.rg_code = rg_code;
        }
    }
        @Override
    public String toString() {
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(TpsTia2458.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}

package org.fbi.fskfq.domain.tps.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.fskfq.domain.tps.base.TpsToa;
import org.fbi.fskfq.domain.tps.base.TpsToaHeader;

import java.io.Serializable;

/**
 * 通用技术应答
 */

@XStreamAlias("Root")
public class TpsToa9910 extends TpsToa {
    public TpsToaHeader Head = new TpsToaHeader();
    public Body Body = new Body();


    public static class Body implements Serializable {
        public Object Object = new Object();
    }

    public static class Object implements Serializable {
        public Record Record = new Record();
    }


    public static class Record implements Serializable {
        public String ori_datatype = "";
        public String result = "";
        public String add_word = "";

        @Override
        public String toString() {
            return "Record{" +
                    "ori_datatype='" + ori_datatype + '\'' +
                    ", result='" + result + '\'' +
                    ", add_word='" + add_word + '\'' +
                    '}';
        }
    }

    @Override
    public TpsToa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TpsToa9910.class);
        return (TpsToa9910) xs.fromXML(xml);
    }

    @Override
    public String toString() {
        return "TpsToa9910{" +
                "Head=" + Head +
                ", Body=" + Body.Object.Record.toString() +
                '}';
    }
}

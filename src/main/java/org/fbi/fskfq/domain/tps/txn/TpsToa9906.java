package org.fbi.fskfq.domain.tps.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.fskfq.domain.tps.base.TpsToa;
import org.fbi.fskfq.domain.tps.base.TpsToaHeader;

import java.io.Serializable;

/**
 * µÇÂ¼»ØÖ´
 */

@XStreamAlias("Root")
public class TpsToa9906 extends TpsToa {
    public TpsToaHeader Head = new TpsToaHeader();
    public Body Body = new Body();


    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    public static class Record implements Serializable {

        /*
        login_result	µÇÂ¼½á¹û
        accredit_code	ÊÚÈ¨Âë
        add_word	¸½ÑÔ
         */
        public String login_result = "";
        public String accredit_code = "";
        public String add_word = "";
    }

    @Override
    public TpsToa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TpsToa9906.class);
        return (TpsToa9906) xs.fromXML(xml);
    }
}

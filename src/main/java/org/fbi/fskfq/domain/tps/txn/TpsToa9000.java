package org.fbi.fskfq.domain.tps.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.fskfq.domain.tps.base.TpsToa;
import org.fbi.fskfq.domain.tps.base.TpsToaHeader;
import org.fbi.fskfq.domain.tps.base.TpsToaSigns;

import java.io.Serializable;

/**
 * ͨ�����׻�ִ
 */

@XStreamAlias("Root")
public class TpsToa9000 extends TpsToa {
    public TpsToaHeader Head = new TpsToaHeader();
    public Body Body = new Body();
    public TpsToaSigns Signs = new TpsToaSigns();


    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    public static class Record implements Serializable {

        /*
        ori_datatype	ԭ��������	NString	4		M
        ori_send_orgcode	ԭ���𷽱���	NString	[1,15]		M
        ori_entrust_date	ԭί������	Date		����������	M
        result	����������	String	4		M
        add_word	����	GBString	[1,60]		O
         */
        public String ori_datatype = "";
        public String ori_send_orgcode = "";
        public String ori_entrust_date = "";
        public String result = "";
        public String add_word = "";
    }

    @Override
    public TpsToa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TpsToa9000.class);
        return (TpsToa9000) xs.fromXML(xml);
    }
}

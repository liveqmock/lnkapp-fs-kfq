package org.fbi.fskfq.domain.tps.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.fskfq.domain.tps.base.TpsToa;
import org.fbi.fskfq.domain.tps.base.TpsToaHeader;
import org.fbi.fskfq.domain.tps.base.TpsToaSigns;

import java.io.Serializable;

/**
 * 通用收妥回执
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
        ori_datatype	原数据类型	NString	4		M
        ori_send_orgcode	原发起方编码	NString	[1,15]		M
        ori_entrust_date	原委托日期	Date		请求发起日期	M
        result	公共处理结果	String	4		M
        add_word	附言	GBString	[1,60]		O
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

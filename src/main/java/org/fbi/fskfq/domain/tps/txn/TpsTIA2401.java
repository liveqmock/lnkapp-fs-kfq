package org.fbi.fskfq.domain.tps.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.lang.StringUtils;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.xml.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Ӧ�����ݲ�ѯ����
 */

@XStreamAlias("Root")
public class TpsTia2401 extends TpsTia {
    public TpsTiaHeader Head = new TpsTiaHeader();
    public Body Body = new Body();
    public Signs Signs = new Signs();

    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    /*
    billtype_code	�ɿ�����ʽ����
bill_no	Ʊ��
verify_no	ȫƱ��У����
bill_money	�տ���
set_year	���
     */
    public static class Record implements Serializable {

        public String billtype_code = "";
        public String bill_no = "";
        public String verify_no = "";
        public String bill_money = "";
        public String set_year = "";
    }

    @Override
    public String toString() {

        Head.dataType = "2401";
        if (StringUtils.isEmpty(Head.msgId)) {
            Head.msgId = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        }
        Head.msgRef = Head.msgId;
        if (StringUtils.isEmpty(Head.workDate)) {
            Head.workDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        }

        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(TpsTia2401.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}

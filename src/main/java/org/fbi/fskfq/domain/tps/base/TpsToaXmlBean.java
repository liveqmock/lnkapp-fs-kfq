package org.fbi.fskfq.domain.tps.base;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.fskfq.domain.tps.base.xml.TpsToaHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 新报文格式
 */

@XStreamAlias("root")
public class TpsToaXmlBean extends TpsToa {
    public TpsToaHeader head = new TpsToaHeader();
    public Body body = new Body();


    public static class Body implements Serializable {
        public BodyObject object = new BodyObject();
    }

    public static class BodyObject implements Serializable {
        @XStreamAsAttribute
        public String name = "";
        @XStreamAsAttribute
        public String description = "";

        public BodyRecord record = new BodyRecord();
    }

    public static class BodyRecord implements Serializable {
        @XStreamImplicit(itemFieldName = "attribute")
        public List<Attribute> attribute = new ArrayList<Attribute>();

        public DetailObject object = new DetailObject();

        public static class DetailObject implements Serializable {
            @XStreamAsAttribute
            public String name = "";
            @XStreamAsAttribute
            public String description = "";
            public DetailRecord record = new DetailRecord();
        }

        public static class DetailRecord implements Serializable {
            @XStreamImplicit(itemFieldName = "attribute")
            public List<Attribute> attribute = new ArrayList<Attribute>();
        }


    }

    @Override
    public TpsToa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TpsToaXmlBean.class);
        return (TpsToaXmlBean) xs.fromXML(xml);
    }


    public static void main(String[] args) {

        String str = "<?xml version=\"1.0\" encoding=\"GBK\"?>" +
                "<root>" +
                "<head><src>000000CZ-220181</src>" +
                "<des>801000000000003</des>" +
                "<dataType>1402</dataType>" +
                "<msgId>{28A0E920-68B5-11E3-80F7-9E94821B2836}</msgId>" +
                "<msgRef>24022009000000000001</msgRef>" +
                "<workDate>2013-12-19</workDate>" +
                "</head>" +
                "<body>" +
                "<object name=\"\" description=\"\">" +
                "        <record>" +
                "        <attribute name=\"CHR_ID\" description=\"\">{3F072EB3-6593-11E3-862B-EDF31243BFF4}</attribute>" +
                "        <attribute name=\"SET_YEAR\" description=\"\">2013</attribute>" +
                "        <attribute name=\"BILL_NO\" description=\"\">101000000201</attribute>" +
                "        <attribute name=\"SUCC_CODE\" description=\"\">OK</attribute>" +
                "        <attribute name=\"BILLTYPE_CODE\" description=\"\">101</attribute>" +
                "        </record>" +
                "</object>" +
                "</body>" +
                "</root>";

        TpsToaXmlBean toa = new TpsToaXmlBean();
        toa = (TpsToaXmlBean) toa.toToa(str);

        System.out.println(toa.head.dataType);
        System.out.println(toa.head.src);
        System.out.println(toa.head.des);
        System.out.println(toa.head.workDate);
        System.out.println(toa.head.msgRef);


        System.out.println("toaxml.body.object" + toa.body.object.name + toa.body.object.description);

        for (Attribute a : toa.body.object.record.attribute) {
            System.out.print(a.name + a.description + "  " + a.content + "");
        }
        System.out.println("toaxml.body.object.record.object" + toa.body.object.record.object.name
                + toa.body.object.record.object.description);

        for (Attribute a : toa.body.object.record.object.record.attribute) {
            System.out.print(a.name + a.description + "  " + a.content + "");
        }
    }

}

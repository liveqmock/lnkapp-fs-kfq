package org.fbi.fskfq.domain.tps.base;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        public List<Attribute> attributes = new ArrayList<Attribute>();

        public DetailObject object = new DetailObject();

        public static class DetailObject implements Serializable {
            @XStreamAsAttribute
            public String name = "";
            @XStreamAsAttribute
            public String description = "";
            @XStreamImplicit(itemFieldName = "record")
            public List<DetailRecord> records = new ArrayList<DetailRecord>();
        }

        public static class DetailRecord implements Serializable {
            @XStreamImplicit(itemFieldName = "attribute")
            public List<Attribute> attributes = new ArrayList<Attribute>();
        }

    }

    public Map<String, String> getMaininfoMap() {
        Map<String, String> infoMap = new HashMap<String, String>();
        for (Attribute attr : this.body.object.record.attributes) {
            infoMap.put(attr.name, attr.content);
        }
        return infoMap;
    }

    public List<Map<String, String>> getDetailMapList() {
        List<Map<String, String>> detailMapList = new ArrayList<Map<String, String>>();
        for (BodyRecord.DetailRecord record : this.body.object.record.object.records) {
            Map<String, String> detailMap = new HashMap<String, String>();
            for (Attribute attr : record.attributes) {
                detailMap.put(attr.name, attr.content);
            }
            detailMapList.add(detailMap);
        }
        return detailMapList;
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
                "<head><src> 000000CZ-220181</src>" +
                "<des>801000000000003</des>" +
                "<dataType>1401</dataType>" +
                "<msgId>{FE4A499F-68B4-11E3-80F7-9E94821B2836}</msgId>" +
                "<msgRef>24012009000000000001</msgRef>" +
                "<workDate>2013-12-19</workDate>" +
                "</head>" +
                "<body>" +
                "<object name=\"\" description=\"\">" +
                "        <record>" +
                "                <attribute name=\"LATEFLAG\" description=\"\">false</attribute>" +
                "        <attribute name=\"RECEIVER\" description=\"\">非税测试－建行</attribute>" +
                "        <attribute name=\"REMARK\" description=\"\"></attribute>" +
                "        <attribute name=\"PAYERACCOUNT\" description=\"\"></attribute>" +
                "        <attribute name=\"RECEIVETYPE\" description=\"\">1</attribute>" +
                "        <attribute name=\"INPUTERNAME\" description=\"\">杨蓉</attribute>" +
                "        <attribute name=\"IEN_NAME\" description=\"\">青岛经济技术开发区管理委员会办" +
                "公室</attribute>" +
                "        <attribute name=\"IS_SEND\" description=\"\">0</attribute>" +
                "        <attribute name=\"PM_NAME\" description=\"\">现金</attribute>" +
                "        <attribute name=\"SUPPLYTEMPLET_ID\" description=\"\"></attribute>" +
                "        <attribute name=\"RG_CODE\" description=\"\">370211</attribute>" +
                "        <attribute name=\"IEN_CODE\" description=\"\">103001</attribute>" +
                "        <attribute name=\"VERIFY_NO\" description=\"\">5057</attribute>" +
                "        <attribute name=\"BILL_NO\" description=\"\">101000000201</attribute>" +
                "        <attribute name=\"CHR_ID\" description=\"\">{3F072EB3-6593-11E3-862B-EDF3124" +
                "3BFF4}</attribute>" +
                "        <attribute name=\"BILLTYPE_NAME\" description=\"\">山东省非税收入通用票据</a" +
                "ttribute>" +
                "        <attribute name=\"CONSIGN_IEN_NAME\" description=\"\">青岛经济技术开发区管理" +
                "委员会办公室</attribute>" +
                "        <attribute name=\"NOSOURCE_IDS\" description=\"\"></attribute>" +
                "        <attribute name=\"IS_CONSIGN\" description=\"\">false</attribute>" +
                "        <attribute name=\"MAKEDATE\" description=\"\">2013-12-15</attribute>" +
                "        <attribute name=\"RECEIVERBANK\" description=\"\">中国建设银行</attribute>" +
                "        <attribute name=\"BILLTYPE_CODE\" description=\"\">101</attribute>" +
                "        <attribute name=\"CONSIGN_IEN_CODE\" description=\"\">103001</attribute>" +
                "        <attribute name=\"PAYER\" description=\"\">建行</attribute>" +
                "        <attribute name=\"PAYERBANK\" description=\"\"></attribute>" +
                "        <attribute name=\"PM_CODE\" description=\"\">1</attribute>" +
                "        <attribute name=\"CHEQUE_NO\" description=\"\"></attribute>" +
                "        <attribute name=\"RECEIVERACCOUNT\" description=\"\">004001</attribute>" +
                "                <object name=\"\" description=\"\">" +
                "                <record>" +
                "                <attribute name=\"CHR_ID\" description=\"\">{00000000000-6593-11E3-862B" +
                "-EDF31243BFF4}</attribute>" +
                "                <attribute name=\"CHARGENUM\" description=\"\">1.00</attribute>" +
                "                <attribute name=\"MEASURE\" description=\"\"></attribute>" +
                "                <attribute name=\"CHARGESTANDARD\" description=\"\"></attribute>" +
                "                <attribute name=\"ITEM_CHKCODE\" description=\"\"></attribute>" +
                "                <attribute name=\"IN_BIS_NAME\" description=\"\">散装水泥专项资金</a" +
                "ttribute>" +
                "                <attribute name=\"MAIN_ID\" description=\"\">{3F072EB3-6593-11E3-862" +
                "B-EDF31243BFF4}</attribute>" +
                "                <attribute name=\"CHARGEMONEY\" description=\"\">100.00</attribute>" +
                "                <attribute name=\"IN_BIS_CODE\" description=\"\">10101</attribute>" +
                "                </record>" +
                "                <record>" +
                "                <attribute name=\"CHR_ID\" description=\"\">{11111111111116593-11E3-862B" +
                "-EDF31243BFF4}</attribute>" +
                "                <attribute name=\"CHARGENUM\" description=\"\">1.00</attribute>" +
                "                <attribute name=\"MEASURE\" description=\"\"></attribute>" +
                "                <attribute name=\"CHARGESTANDARD\" description=\"\"></attribute>" +
                "                <attribute name=\"ITEM_CHKCODE\" description=\"\"></attribute>" +
                "                <attribute name=\"IN_BIS_NAME\" description=\"\">散装水泥专项资金</a" +
                "ttribute>" +
                "                <attribute name=\"MAIN_ID\" description=\"\">{3F072EB3-6593-11E3-862" +
                "B-EDF31243BFF4}</attribute>" +
                "                <attribute name=\"CHARGEMONEY\" description=\"\">100.00</attribute>" +
                "                <attribute name=\"IN_BIS_CODE\" description=\"\">10101</attribute>" +
                "                </record>" +
                "                <record>" +
                "                <attribute name=\"CHR_ID\" description=\"\">{2222222222222-6593-11E3-862B" +
                "-EDF31243BFF4}</attribute>" +
                "                <attribute name=\"CHARGENUM\" description=\"\">1.00</attribute>" +
                "                <attribute name=\"MEASURE\" description=\"\"></attribute>" +
                "                <attribute name=\"CHARGESTANDARD\" description=\"\"></attribute>" +
                "                <attribute name=\"ITEM_CHKCODE\" description=\"\"></attribute>" +
                "                <attribute name=\"IN_BIS_NAME\" description=\"\">散装水泥专项资金</a" +
                "ttribute>" +
                "                <attribute name=\"MAIN_ID\" description=\"\">{3F072EB3-6593-11E3-862" +
                "B-EDF31243BFF4}</attribute>" +
                "                <attribute name=\"CHARGEMONEY\" description=\"\">100.00</attribute>" +
                "                <attribute name=\"IN_BIS_CODE\" description=\"\">10101</attribute>" +
                "                </record>" +
                "                </object>" +
                "        </record>" +
                "</object>" +
                "</body>" +
                "</root>";

        str= "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<root>\n" +
                "<head><src>CZ-370211</src>\n" +
                "<des>CCB-370211</des>\n" +
                "<dataType>1401</dataType>\n" +
                "<msgId>14012013 000000000404</msgId>\n" +
                "<msgRef>2013123119151433322</msgRef>\n" +
                "<workDate>2013-12-31</workDate>\n" +
                "</head>\n" +
                "<body>\n" +
                "<object name=\"\" description=\"\">\n" +
                "\t<record>\n" +
                "\t\t<attribute name=\"RESULT\" description=\"\">1401002</attribute>\n" +
                "\t<attribute name=\"ADD_WORD\" description=\"\">缴款书已收款</attribute>\n" +
                "\t<attribute name=\"ORI_ENTRUST_DATE\" description=\"\">20131231</attribute>\n" +
                "\t<attribute name=\"ORI_SEND_ORGCODE\" description=\"\">CCB-370211</attribute>\n" +
                "\t<attribute name=\"ORI_DATATYPE_CODE\" description=\"\">2401</attribute>\n" +
                "\t</record>\n" +
                "</object>\n" +
                "</body>\n" +
                "</root>";

        TpsToaXmlBean toa = new TpsToaXmlBean();
        toa = (TpsToaXmlBean) toa.toToa(str);

        System.out.println(toa.head.dataType);
        System.out.println(toa.head.src);
        System.out.println(toa.head.des);
        System.out.println(toa.head.workDate);
        System.out.println(toa.head.msgRef);

        Map<String, String> infomap = toa.getMaininfoMap();
        System.out.println("MainInfo[RECEIVER] : " + infomap.get("RECEIVER"));

        List<Map<String, String>> detailMaplist = toa.getDetailMapList();
        for (Map<String, String> detailMap : detailMaplist) {
            System.out.println("DetailMap[CHR_ID] : " + detailMap.get("CHR_ID"));
        }
    }

}

package org.fbi.fskfq.domain.tps.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.fskfq.domain.tps.base.TpsToa;
import org.fbi.fskfq.domain.tps.base.xml.Signs;
import org.fbi.fskfq.domain.tps.base.xml.TpsToaHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应收数据查询回执
 */

public class TpsTOA1401 extends TpsToa {
    public TpsToaHeader Head = new TpsToaHeader();
    public Body Body = new Body();
    public Signs Signs = new Signs();


    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    /*
    chr_id	缴款书ID	String	38		M
    billtype_code	缴款书样式编码	NString	[1,20]		M
    billtype_name	缴款书样式名称	GBString	[1,60]		M
    bill_no	票号	String	[1,42]		M
    makedate	开票日期	Date			M
    ien_code	执收单位业务码	NString	[1,42]		M
    ien_name	执收单位名称	GBString	[1,80]		M
    consign_ien_code	委托单位业务码	NString	[1,42]		M
    consign_ien_name	委托单位名称	GBString	[1,80]		M
    pm_code	缴款方式编码	NString	[1,42]		M
    pm_name	缴款方式名称	GBString	[1,50]		M
    cheque_no	结算号	String	[1,20]		O
    payer	缴款人全称	GBString	[1,100]		M
    payerbank	缴款人账户开户行	GBString	[1,100]		O
    payeraccount	缴款人账号	String	[1,42]		O
    receiver	收款人全称	GBString	[1,100]		M
    receiverbank	收款人账户开户行	GBString	[1,100]		M
    receiveraccount	收款人账号	String	[1,42]		M
    verify_no	全票面校验码	String	[1,20]		O
    rg_code	区划码	NString	[1,42]		O
    receivetype	征收方式	NString	1	1:直接解缴
    2:集中汇缴
    4:批量代扣
    5:待查收入	M
    inputername	经办人姓名	GBString	[1,60]		O
    is_consign	是否委托	Boolean		false：否
    true：是	M
    lateflag	是否补录	Boolean		false：否
    true：是	M
    nosource_ids	待查收入ID集合	String	[1,1000]	对应于待查收入ID，每个待查收入ID以逗号分隔	O
    supplytemplet_id	批量代扣模板ID	String	[1,1000]	对应于批量代扣模板ID，每个批量代扣模板ID以逗号分隔	O
    remark	备注	String	[1,200]		O
     */
    public static class Record implements Serializable {

        public String chr_id = "";
        public String billtype_code = "";
        public String billtype_name = "";
        public String bill_no = "";
        public String makedate = "";
        public String ien_code = "";
        public String ien_name = "";
        public String consign_ien_code = "";
        public String consign_ien_name = "";
        public String pm_code = "";
        public String pm_name = "";
        public String cheque_no = "";
        public String payer = "";
        public String payerbank = "";
        public String payeraccount = "";
        public String receiver = "";
        public String receiverbank = "";
        public String receiveraccount = "";
        public String verify_no = "";
        public String rg_code = "";
        public String receivetype = "";
        public String inputername = "";
        public String is_consign = "";
        public String lateflag = "";
        public String nosource_ids = "";
        public String supplytemplet_id = "";
        public String remark = "";

        public List<DetailRecord> Object = new ArrayList<DetailRecord>();

        @XStreamAlias("Record")
        public static class DetailRecord {
            /*
            chr_id	缴款书明细ID
            main_id	缴款书ID
            in_bis_code	收入项目业务码
            in_bis_name	收入项目名称
            measure	计收单位
            chargenum	收入数量
            chargestandard	收费标准
            chargemoney	收入金额
            item_chkcode	单位项目校验码
             */
            public String chr_id = "";
            public String main_id = "";
            public String in_bis_code = "";
            public String in_bis_name = "";
            public String measure = "";
            public String chargenum = "";
            public String chargestandard = "";
            public String chargemoney = "";
            public String item_chkcode = "";
        }

    }

    @Override
    public TpsToa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TpsTOA1401.class);
        return (TpsTOA1401) xs.fromXML(xml);
    }

}

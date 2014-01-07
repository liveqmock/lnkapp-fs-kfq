package org.fbi.fskfq.processor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4080Request.CbsTia4080;
import org.fbi.fskfq.domain.cbs.T4080Response.CbsToa4080;
import org.fbi.fskfq.domain.cbs.T4080Response.CbsToa4080Item;
import org.fbi.fskfq.enums.BillStatus;
import org.fbi.fskfq.enums.TxnRtnCode;
import org.fbi.fskfq.helper.MybatisFactory;
import org.fbi.fskfq.repository.dao.FsKfqPaymentInfoMapper;
import org.fbi.fskfq.repository.dao.FsKfqPaymentItemMapper;
import org.fbi.fskfq.repository.model.FsKfqPaymentInfo;
import org.fbi.fskfq.repository.model.FsKfqPaymentInfoExample;
import org.fbi.fskfq.repository.model.FsKfqPaymentItem;
import org.fbi.fskfq.repository.model.FsKfqPaymentItemExample;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanrui on 13-12-31.
 * 日报交易
 */
public class T4080Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        CbsTia4080 tia;
        try {
            tia = getCbsTia(request.getRequestBody());
        } catch (Exception e) {
            logger.error("特色业务平台请求报文解析错误.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }

        //获取本地数据库信息
        CbsToa4080 cbsToa4080 = new CbsToa4080();
        List<CbsToa4080Item> cbsToa4080Items = new ArrayList<>();
        String startDate = tia.getStartDate().substring(0, 4) + "-" + tia.getStartDate().substring(4, 6) + "-" + tia.getStartDate().substring(6, 8);
        String endDate = tia.getEndDate().substring(0, 4) + "-" + tia.getEndDate().substring(4, 6) + "-" + tia.getEndDate().substring(6, 8);
        List<FsKfqPaymentInfo> infos = selectPayoffPaymentInfos(startDate, endDate);
        int count = 0;
        for (FsKfqPaymentInfo info : infos) {
            List<FsKfqPaymentItem> items = selectPayoffPaymentItems(info);
            for (FsKfqPaymentItem item : items) {
                count++;
                CbsToa4080Item cbsToa4080Item = new CbsToa4080Item();
                cbsToa4080Item.setSn("" + count);
                cbsToa4080Item.setIenCode(info.getIenCode());
                cbsToa4080Item.setIenName(info.getIenName());
                cbsToa4080Item.setBillNo(info.getBillNo());
                cbsToa4080Item.setInBisCode(item.getInBisCode());
                cbsToa4080Item.setInBisName(item.getInBisName());
                cbsToa4080Item.setChargemoney(item.getChargemoney());
                cbsToa4080Items.add(cbsToa4080Item);
            }
        }
        cbsToa4080.setItemNum("" + cbsToa4080Items.size());
        cbsToa4080.setItems(cbsToa4080Items);

        //==特色平台响应==
        try {
            String respMsg = getRespMsgForStarring(cbsToa4080);
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            response.setResponseBody(respMsg.getBytes(response.getCharacterEncoding()));
        } catch (Exception e) {
            logger.error("特色平台响应报文处理失败.", e);
            throw new RuntimeException(e);
        }
    }


    //====
    //处理Starring请求报文
    private CbsTia4080 getCbsTia(byte[] body) throws Exception {
        CbsTia4080 tia = new CbsTia4080();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4080) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4080");
        return tia;
    }

    //查找已缴款未撤销的缴款单记录
    private List<FsKfqPaymentInfo> selectPayoffPaymentInfos(String startDate, String endDate) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentInfoMapper mapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            mapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            FsKfqPaymentInfoExample example = new FsKfqPaymentInfoExample();
            example.createCriteria()
                    .andBankIndateBetween(startDate, endDate)
                    .andLnkBillStatusEqualTo(BillStatus.PAYOFF.getCode());
            return mapper.selectByExample(example);
        }
    }

    private List<FsKfqPaymentItem> selectPayoffPaymentItems(FsKfqPaymentInfo paymentInfo) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentItemMapper mapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            mapper = session.getMapper(FsKfqPaymentItemMapper.class);
            FsKfqPaymentItemExample example = new FsKfqPaymentItemExample();
            example.createCriteria()
                    .andMainPkidEqualTo(paymentInfo.getPkid());
            return mapper.selectByExample(example);
        }
    }

    //生成CBS响应报文
    private String getRespMsgForStarring(CbsToa4080 cbsToa) {
        String starringRespMsg = "";
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(cbsToa.getClass().getName(), cbsToa);
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(cbsToa.getClass().getPackage().getName());
        try {
            starringRespMsg = (String) starringDataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("特色平台报文转换失败.", e);
        }
        return starringRespMsg;
    }

}

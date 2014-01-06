package org.fbi.fskfq.processor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4070Request.CbsTia4070;
import org.fbi.fskfq.domain.cbs.T4070Response.CbsToa4070;
import org.fbi.fskfq.domain.cbs.T4070Response.CbsToa4070Item;
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
 * �ձ�����
 */
public class T4070Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        CbsTia4070 tia;
        try {
            tia = getCbsTia(request.getRequestBody());
        } catch (Exception e) {
            logger.error("��ɫҵ��ƽ̨�����Ľ�������.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }

        //��ȡ�������ݿ���Ϣ
        CbsToa4070 cbsToa4070 = new CbsToa4070();
        List<CbsToa4070Item> cbsToa4070Items = new ArrayList<>();
        String startDate = tia.getStartDate().substring(0,4) + "-" +  tia.getStartDate().substring(4,6) +  "-" + tia.getStartDate().substring(6,8);
        String endDate = tia.getEndDate().substring(0,4) +  "-" + tia.getEndDate().substring(4,6) +  "-" + tia.getEndDate().substring(6,8);
        List<FsKfqPaymentInfo> infos = selectPayoffPaymentInfos(startDate, endDate);
        for (FsKfqPaymentInfo info : infos) {
            List<FsKfqPaymentItem> items = selectPayoffPaymentItems(info);
            for (FsKfqPaymentItem item : items) {
                CbsToa4070Item cbsToa4070Item = new CbsToa4070Item();
                cbsToa4070Item.setIenCode(info.getIenCode());
                cbsToa4070Item.setIenName(info.getIenName());
                cbsToa4070Item.setBillNo(info.getBillNo());
                cbsToa4070Item.setInBisCode(item.getInBisCode());
                cbsToa4070Item.setInBisName(item.getInBisName());
                cbsToa4070Item.setChargemoney(item.getChargemoney());
                cbsToa4070Items.add(cbsToa4070Item);
            }
        }
        cbsToa4070.setItemNum(""+ cbsToa4070Items.size());
        cbsToa4070.setItems(cbsToa4070Items);

        //==��ɫƽ̨��Ӧ==
        try {
            String respMsg = getRespMsgForStarring(cbsToa4070);
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            response.setResponseBody(respMsg.getBytes(response.getCharacterEncoding()));
        } catch (Exception e) {
            logger.error("��ɫƽ̨��Ӧ���Ĵ���ʧ��.", e);
            throw new RuntimeException(e);
        }
    }


    //====
    //����Starring������
    private CbsTia4070 getCbsTia(byte[] body) throws Exception {
        CbsTia4070 tia = new CbsTia4070();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4070) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4070");
        return tia;
    }

    //�����ѽɿ�δ�����Ľɿ��¼
    private List<FsKfqPaymentInfo>  selectPayoffPaymentInfos(String startDate, String endDate) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentInfoMapper mapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            mapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            FsKfqPaymentInfoExample example = new FsKfqPaymentInfoExample();
            example.createCriteria()
                    .andBankIndateBetween(startDate, endDate)
                    .andLnkBillStatusEqualTo(BillStatus.PAYOFF.getCode());
             return  mapper.selectByExample(example);
        }
    }
    private List<FsKfqPaymentItem> selectPayoffPaymentItems(FsKfqPaymentInfo paymentInfo) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentItemMapper mapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            mapper = session.getMapper(FsKfqPaymentItemMapper.class);
            FsKfqPaymentItemExample example = new FsKfqPaymentItemExample();
            example.createCriteria()
                    .andMainPkidEqualTo(paymentInfo.getChrId());
            return mapper.selectByExample(example);
        }
    }

    //����CBS��Ӧ����
    private String getRespMsgForStarring(CbsToa4070 cbsToa) {
        String starringRespMsg = "";
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(cbsToa.getClass().getName(), cbsToa);
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(cbsToa.getClass().getPackage().getName());
        try {
            starringRespMsg = (String) starringDataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("��ɫƽ̨����ת��ʧ��.", e);
        }
        return starringRespMsg;
    }

}
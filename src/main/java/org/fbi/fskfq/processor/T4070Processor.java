package org.fbi.fskfq.processor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4070Request.CbsTia4070;
import org.fbi.fskfq.enums.BillStatus;
import org.fbi.fskfq.enums.TxnRtnCode;
import org.fbi.fskfq.helper.MybatisFactory;
import org.fbi.fskfq.repository.dao.FsKfqPaymentInfoMapper;
import org.fbi.fskfq.repository.model.FsKfqPaymentInfo;
import org.fbi.fskfq.repository.model.FsKfqPaymentInfoExample;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        FsKfqPaymentInfo paymentInfo = selectPayoffRecordList(tia.getStartDate(),tia.getEndDate());
        if (paymentInfo == null) {
            assembleAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, "�������ѽɿ�ļ�¼.", response);
            return;
        }

        processTxn(paymentInfo, request);
        response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
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
    private FsKfqPaymentInfo selectPayoffRecordList(String startDate, String endDate) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentInfoMapper mapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            mapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            FsKfqPaymentInfoExample example = new FsKfqPaymentInfoExample();
            example.createCriteria()
                    .andLnkBillStatusEqualTo(BillStatus.PAYOFF.getCode());
            List<FsKfqPaymentInfo> infos = mapper.selectByExample(example);
            if (infos.size() == 0) {
                return null;
            }
            if (infos.size() != 1) { //ͬһ���ɿ�ţ��ѽɿ�δ�������ڱ���ֻ�ܴ���һ����¼
                throw new RuntimeException("��¼״̬����.");
            }
            return infos.get(0);
        }
    }


    //=============
    private void processTxn(FsKfqPaymentInfo paymentInfo, Stdp10ProcessorRequest request) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            paymentInfo.setOperCancelBankid(request.getHeader("branchId"));
            paymentInfo.setOperCancelTlrid(request.getHeader("tellerId"));
            paymentInfo.setOperCancelDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            paymentInfo.setOperCancelTime(new SimpleDateFormat("HHmmss").format(new Date()));

/*
            paymentInfo.setHostBookFlag("1");
            paymentInfo.setHostChkFlag("0");
            paymentInfo.setFbBookFlag("1");
            paymentInfo.setFbChkFlag("0");
*/

//            paymentInfo.setAreaCode("KaiFaQu-FeiShui");
//            paymentInfo.setHostAckFlag("0");
            paymentInfo.setLnkBillStatus(BillStatus.CANCELED.getCode()); //�ѳ���

            //TODO Ӧ��¼�������׵�������ˮ��

            FsKfqPaymentInfoMapper infoMapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            infoMapper.updateByPrimaryKey(paymentInfo);
            session.commit();
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException("ҵ���߼�����ʧ�ܡ�", e);
        } finally {
            session.close();
        }
    }
}

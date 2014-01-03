package org.fbi.fskfq.processor;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4060Request.CbsTia4060;
import org.fbi.fskfq.enums.TxnRtnCode;
import org.fbi.fskfq.helper.MybatisFactory;
import org.fbi.fskfq.repository.dao.FsKfqPaymentInfoMapper;
import org.fbi.fskfq.repository.dao.FsKfqPaymentItemMapper;
import org.fbi.fskfq.repository.model.FsKfqPaymentInfo;
import org.fbi.fskfq.repository.model.FsKfqPaymentItem;
import org.fbi.fskfq.repository.model.FsKfqPaymentItemExample;
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
 * 1534060����
 * zhanrui
 * 20131227
 */
public class T4060processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        CbsTia4060 tia;
        try {
            tia = getCbsTia(request.getRequestBody());
            logger.info("��ɫҵ��ƽ̨������TIA:" + tia.toString());
        } catch (Exception e) {
            logger.error("��ɫҵ��ƽ̨�����Ľ�������.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }


        //���������߼�����
        try {
//            processTxn(paymentInfo, paymentItems, request);
        } catch (Exception e) {
            assembleAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, e.getMessage(), response);
            logger.error("ҵ����ʧ��.", e);
            return;
        }

        //==��ɫƽ̨��Ӧ==
        try {
            //String starringRespMsg = getRespMsgForStarring(paymentInfo, paymentItems);
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            logger.info("===������ɣ��ɹ�");
        } catch (Exception e) {
            logger.error("��ɫƽ̨��Ӧ���Ĵ���ʧ��.", e);
            throw new RuntimeException(e);
        }
    }


    //����Starring������
    private CbsTia4060 getCbsTia(byte[] body) throws Exception {
        CbsTia4060 tia = new CbsTia4060();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4060) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4060");
        return tia;
    }



    //=======ҵ���߼�����=================================================

    private  FsKfqPaymentInfo selectPaymentInfoFromDB(String billNo){
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentInfoMapper infoMapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            infoMapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            return infoMapper.selectByPrimaryKey(billNo);
        }
    }
    private  List<FsKfqPaymentItem> selectPaymentItemsFromDB(FsKfqPaymentInfo paymentInfo){
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FsKfqPaymentItemExample example = new FsKfqPaymentItemExample();
            example.createCriteria().andChrIdEqualTo(paymentInfo.getChrId());
            FsKfqPaymentItemMapper infoMapper = session.getMapper(FsKfqPaymentItemMapper.class);
            return infoMapper.selectByExample(example);
        }
    }



    private void processTxn(FsKfqPaymentInfo paymentInfo, List<FsKfqPaymentItem> paymentItems, Stdp10ProcessorRequest request) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            paymentInfo.setBankIndate(request.getHeader("txnTime").substring(0, 8));
            paymentInfo.setBusinessId(request.getHeader("serialNo"));
            paymentInfo.setOperInitBankid(request.getHeader("branchId"));
            paymentInfo.setOperInitTlrid(request.getHeader("tellerId"));
            paymentInfo.setOperInitDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            paymentInfo.setOperInitTime(new SimpleDateFormat("HHmmss").format(new Date()));

            paymentInfo.setAreaCode("KaiFaQu-FeiShui");
            paymentInfo.setHostAckFlag("0");
            paymentInfo.setArchiveFlag("0");

            paymentInfo.setHostBookFlag("0");
            paymentInfo.setHostChkFlag("0");
            paymentInfo.setFbBookFlag("0");
            paymentInfo.setFbChkFlag("0");

            FsKfqPaymentInfoMapper infoMapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            infoMapper.insert(paymentInfo);

            FsKfqPaymentItemMapper itemMapper = session.getMapper(FsKfqPaymentItemMapper.class);
            int i = 0;
            for (FsKfqPaymentItem item : paymentItems) {
                i++;
                itemMapper.insert(item);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException("ҵ���߼�����ʧ�ܡ�", e);
        } finally {
            session.close();
        }
    }

}

package org.fbi.fskfq.processor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4011Request.CbsTia4011;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.TpsToaXmlBean;
import org.fbi.fskfq.domain.tps.txn.TpsTia2402;
import org.fbi.fskfq.domain.tps.txn.TpsToa9000;
import org.fbi.fskfq.domain.tps.txn.TpsToa9910;
import org.fbi.fskfq.enums.BillStatus;
import org.fbi.fskfq.enums.TxnRtnCode;
import org.fbi.fskfq.helper.FbiBeanUtils;
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
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhanrui on 13-12-31.
 * �ɿ��
 */
public class T4011Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        CbsTia4011 tia;
        try {
            tia = getCbsTia(request.getRequestBody());
        } catch (Exception e) {
            logger.error("��ɫҵ��ƽ̨�����Ľ�������.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }

        //��鱾�����ݿ���Ϣ
        FsKfqPaymentInfo paymentInfo = selectNotCanceledPaymentInfoFromDB(tia.getBillNo());
        if (paymentInfo == null) {
            assembleAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, "��������ѯ����.", response);
            return;
        } else {
            String billStatus = paymentInfo.getLnkBillStatus();
            if (billStatus.equals(BillStatus.PAYOFF.getCode())) { //�ѽɿ�
                response.setHeader("rtnCode", TxnRtnCode.TXN_PAY_REPEATED.getCode());
                logger.info("===�˱ʽɿ�ѽɿ�.");
                return;
            }else if (!billStatus.equals(BillStatus.INIT.getCode())) {  //�ǳ�ʼ״̬
                response.setHeader("rtnCode", TxnRtnCode.TXN_PAY_REPEATED.getCode());
                logger.info("===�˱ʽɿ״̬����.");
                return;
            }
        }

        //����������
        TpsToaXmlBean tpsToa = processTpsTx(tia, request, response);
        //�ж�����
        String result = tpsToa.getMaininfoMap().get("RESULT");
        if (result != null) { //�쳣ҵ����
            TpsToa9000 tpsToa9000 = new TpsToa9000();
            try {
                FbiBeanUtils.copyProperties(tpsToa.getMaininfoMap(), tpsToa9000, true);
                assembleAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, tpsToa9000.getAddWord(), response);
            } catch (Exception e) {
                logger.error("��������������Ӧ���Ľ����쳣.", e);
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            }
        } else { //���������߼�����
            try {
                String rtnStatus = tpsToa.getMaininfoMap().get("SUCC_CODE");
                String chr_id = tpsToa.getMaininfoMap().get("CHR_ID");
                String bill_no = tpsToa.getMaininfoMap().get("BILL_NO");
                if (!paymentInfo.getChrId().equals(chr_id) || !paymentInfo.getBillNo().equals(bill_no)) {
                    assembleAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, "���Ų�����", response);
                } else {
                    if (!"OK".equals(rtnStatus)) {
                        assembleAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, rtnStatus, response);
                    } else {
                        processTxn(paymentInfo, request);
                        response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
                        //response.setResponseBody(starringRespMsg.getBytes(response.getCharacterEncoding()));
                    }
                }
            } catch (Exception e) {
                assembleAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, e.getMessage(), response);
                logger.error("ҵ����ʧ��.", e);
            }
        }

    }

    //������ͨѶ����
    private TpsToaXmlBean processTpsTx(CbsTia4011 tia, Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) {
        TpsTia tpsTia = assembleTpsRequestBean(tia, request);
        TpsToaXmlBean tpsToa = new TpsToaXmlBean();

        byte[] sendTpsBuf;
        try {
            sendTpsBuf = generateTpsTxMsgHeader(tpsTia, request);
        } catch (Exception e) {
            logger.error("���ɵ�����������������ʱ����.", e);
            response.setHeader("rtnCode", TxnRtnCode.TPSMSG_MARSHAL_FAILED.getCode());
            return tpsToa;
        }

        try {
            String dataType = tpsTia.getHeader().getDataType();
            byte[] recvTpsBuf = processThirdPartyServer(sendTpsBuf, dataType);
            String recvTpsMsg = new String(recvTpsBuf, "GBK");

            String rtnDataType = substr(recvTpsMsg, "<dataType>", "</dataType>").trim();
            if ("9910".equals(rtnDataType)) { //�������쳣���� 9910
                TpsToa9910 tpsToa9910 = transXmlToBeanForTps9910(recvTpsBuf);
                //TODO ����ǩ������
                T9905Processor t9905Processor = new T9905Processor();
                t9905Processor.doRequest(request, response);

                logger.info("===���������������ر���(�쳣ҵ����Ϣ��)��\n" + tpsToa9910.toString());
                assembleAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, tpsToa9910.Body.Object.Record.add_word, response);
            } else { //ҵ�����������쳣���� 1402
                tpsToa = transXmlToBeanForTps(recvTpsBuf);
            }
        } catch (SocketTimeoutException e) {
            logger.error("�������������ͨѶ����ʱ.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_RECV_TIMEOUT.getCode());
        } catch (Exception e) {
            logger.error("�������������ͨѶ�����쳣.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_COMM_ERROR.getCode());
        }

        return tpsToa;
    }

    //====
    //����Starring������
    private CbsTia4011 getCbsTia(byte[] body) throws Exception {
        CbsTia4011 tia = new CbsTia4011();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4011) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4011");
        return tia;
    }

    //����δ�����Ľɿ��¼
    private FsKfqPaymentInfo selectNotCanceledPaymentInfoFromDB(String billNo) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentInfoMapper mapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            mapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            FsKfqPaymentInfoExample example = new FsKfqPaymentInfoExample();
            example.createCriteria()
                    .andBillNoEqualTo(billNo)
                    .andLnkBillStatusNotEqualTo(BillStatus.CANCELED.getCode());
            List<FsKfqPaymentInfo> infos = mapper.selectByExample(example);
            if (infos.size() == 0) {
                return null;
            }
            if (infos.size() != 1) { //ͬһ���ɿ�ţ�δ�������ڱ���ֻ�ܴ���һ����¼
                throw new RuntimeException("��¼״̬����.");
            }
            return infos.get(0);
        }
    }

    //���ɵ����������Ķ�ӦBEAN
    private TpsTia assembleTpsRequestBean(CbsTia4011 cbstia, Stdp10ProcessorRequest request) {
        TpsTia2402 tpstia = new TpsTia2402();
        TpsTia2402.BodyRecord record = ((TpsTia2402.Body) tpstia.getBody()).getObject().getRecord();
        FbiBeanUtils.copyProperties(cbstia, record, true);

        generateTpsBizMsgHeader(tpstia, "2402", request);
        return tpstia;
    }


    //=============
    private void processTxn(FsKfqPaymentInfo paymentInfo, Stdp10ProcessorRequest request) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            //setBankIndate ����ɫϵͳ���������ṩ
            //Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(request.getHeader("txnTime"));
            //paymentInfo.setBankIndate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
            //paymentInfo.setBankIndate(new SimpleDateFormat("yyyy-MM-dd").format(date));

            paymentInfo.setBusinessId(request.getHeader("serialNo"));
            paymentInfo.setOperPayBankid(request.getHeader("branchId"));
            paymentInfo.setOperPayTellerid(request.getHeader("tellerId"));
            paymentInfo.setOperPayDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            paymentInfo.setOperPayTime(new SimpleDateFormat("HHmmss").format(new Date()));

            paymentInfo.setHostBookFlag("1");
            paymentInfo.setHostChkFlag("0");
            paymentInfo.setFbBookFlag("1");
            paymentInfo.setFbChkFlag("0");

            //paymentInfo.setAreaCode("KaiFaQu-FeiShui");
            paymentInfo.setHostAckFlag("0");
            paymentInfo.setLnkBillStatus(BillStatus.PAYOFF.getCode()); //�ѽɿ�

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

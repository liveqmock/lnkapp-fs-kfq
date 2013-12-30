package org.fbi.fskfq.processor;


import org.fbi.fskfq.domain.cbs.T4010Request.CbsTia4010;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.TpsToa;
import org.fbi.fskfq.domain.tps.txn.TpsTia2401;
import org.fbi.fskfq.domain.tps.txn.TpsToa1401;
import org.fbi.fskfq.enums.TxnRtnCode;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * 1534010�ɿ��ѯ
 * zhanrui
 * 20131227
 */
public class T4010processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        CbsTia4010 tia;
        try {
            tia = getCbsTia(request.getRequestBody());
            logger.info("��ɫҵ��ƽ̨������TIA:" + tia.toString());
        } catch (Exception e) {
            logger.error("��ɫҵ��ƽ̨�����Ľ�������.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }

        //������ͨѶ���� -
        TpsTia tpstia = assembleTpsRequestBean(tia, request);
        TpsToa tpsToa = null;

        byte[] sendTpsBuf;
        try {
            sendTpsBuf = generateTxMsg(tpstia);
            logger.info("�����������������ģ�\n" +  new String(sendTpsBuf, "GBK"));
        } catch (Exception e) {
            logger.error("���ɵ�����������������ʱ����.", e);
            response.setHeader("rtnCode", TxnRtnCode.TPSMSG_MARSHAL_FAILED.getCode());
            return;
        }

        try {
            tpsToa = sendAndRecvForTps(sendTpsBuf, tpstia.getHeader().dataType);
        } catch (SocketTimeoutException e) {
            logger.error("�������������ͨѶ����ʱ.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_RECV_TIMEOUT.getCode());
            return;
        } catch (Exception e) {
            logger.error("�������������ͨѶ�����쳣.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_COMM_ERROR.getCode());
            return;
        }

        //������������ر���--
        String starringRespMsg = "";
        try {
//            String TpsRntCode = Tpstoa4010.getRntCode();
            String TpsRntCode = "";
            if (!"00".equals(TpsRntCode)) {
                starringRespMsg = getErrorRespMsgForStarring(TpsRntCode);
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            } else {
                //processTxn(Tpstia4010, Tpstoa4010, tia, request);
                TpsToa toa = new TpsToa1401();
                //����ɫƽ̨��Ӧ����--
                starringRespMsg = getRespMsgForStarring(toa);
                response.setHeader("rtnCode", "0000");
            }
        } catch (Exception e) {
            logger.error("��ɫƽ̨��Ӧ���Ĵ���ʧ��.", e);
            throw new RuntimeException(e);
        }
        response.setResponseBody(starringRespMsg.getBytes(response.getCharacterEncoding()));
//        response.setResponseBody("1111|222|".getBytes(response.getCharacterEncoding()));

    }

    //����Starring������
    private CbsTia4010 getCbsTia(byte[] body) throws Exception {
        CbsTia4010 tia = new CbsTia4010();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4010) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4010");
        return tia;
    }

    //���ɵ����������Ķ�ӦBEAN
    private TpsTia assembleTpsRequestBean(CbsTia4010 cbstia, Stdp10ProcessorRequest request) {
        TpsTia2401 tpstia = new TpsTia2401();
        tpstia.Body.Object.Record.billtype_code = cbstia.getBilltypeCode();
        tpstia.Body.Object.Record.bill_no = cbstia.getBillNo();
        tpstia.Body.Object.Record.verify_no = cbstia.getVerifyNo();
        tpstia.Body.Object.Record.bill_money = cbstia.getBillMoney().toString();
        tpstia.Body.Object.Record.set_year = cbstia.getSetYear();

        //������ͷ TODO ȷ��msgId�ĳ���
        tpstia.Head.msgId = request.getHeader("txnTime") + request.getHeader("serialNo");
        tpstia.Head.msgRef =  request.getHeader("serialNo");
        tpstia.Head.workDate = request.getHeader("txnTime").substring(0, 8);

        // TODO
        tpstia.Head.src = "CCB-370211";
        tpstia.Head.des = "CZ-370211";
        tpstia.Head.dataType = "";
        return tpstia;
    }


    //������������ͨѶ
    private TpsToa sendAndRecvForTps(byte[] sendTpsBuf, String txnCode) throws Exception {
        byte[] recvBuf = processThirdPartyServer(sendTpsBuf, txnCode);
        logger.info("���������������ر��ģ�\n" +  new String(recvBuf, "GBK"));

        return transXmlToBeanForTps(recvBuf);
    }


    //����CBS��Ӧ����
    private String getRespMsgForStarring(TpsToa toa) throws Exception {
        String starringRespMsg;
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(toa.getClass().getPackage().getName());
        starringRespMsg = (String) starringDataFormat.toMessage(modelObjectsMap);
        return starringRespMsg;
    }


    //ҵ���߼�����
/*
    private void processTxn(TpsTIA4010 Tpstia, TpsTOA4010 Tpstoa, TIA4010 tia, Stdp10ProcessorRequest request) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            TpsQdeEnt TpsQdeEnt = new TpsQdeEnt();
            TpsQdeEnt.setPregNo(tia.getPregNo());
            TpsQdeEnt.setAreaCode(tia.getAreaCode());
            TpsQdeEnt.setTpsCode(tia.getTpsCode());
            TpsQdeEnt.setTpsName(tia.getTpsName());
            TpsQdeEnt.setTellerId(request.getHeader("TellerId"));
            TpsQdeEnt.setBranchId(request.getHeader("BranchId"));
            TpsQdeEnt.setActNo(tia.getActNo());
            TpsQdeEnt.setActBal(new BigDecimal(tia.getActBal()));

            TpsQdeEntMapper entMapper = session.getMapper(TpsQdeEntMapper.class);
            entMapper.insert(TpsQdeEnt);

            TpsQdeInvesterMapper investerMapper = session.getMapper(TpsQdeInvesterMapper.class);
            int i = 0;
            for (TpsTIA4010Item item : Tpstia.getItems()) {
                String vchSn = Tpstoa.getVchNos().substring(0 + i * 3, 3 + i * 3);
                TpsQdeInvester record = new TpsQdeInvester();
                record.setRegNo(Tpstoa.getPregNo());
                record.setVchSn(vchSn);
                record.setInvesterName(item.getInvesterName());
                record.setActNo(item.getActNo());
                record.setInvAmt(new BigDecimal(item.getInvAmt()));
                record.setActBankName(item.getActBankName());
                record.setCertId(item.getCertId());
                record.setInvDate(request.getHeader("txnTime").substring(0, 8));
                record.setInvestType("1");
                record.setBankHostSn(Tpstoa.getBankHostSn());
                i++;
                investerMapper.insert(record);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException("ҵ���߼��������", e);
        } finally {
            session.close();
        }
    }

*/
}

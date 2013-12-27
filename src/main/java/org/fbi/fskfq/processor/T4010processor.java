package org.fbi.fskfq.processor;


import org.fbi.fskfq.domain.starring.T4010Request.TIA4010;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.TpsToa;
import org.fbi.fskfq.enums.TxnRtnCode;
import org.fbi.linking.codec.dataformat.FixedLengthTextDataFormat;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 1564010入资登记
 * zhanrui
 */
public class T4010processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        TIA4010 tia;
        try {
            tia = getStarringTia(request.getRequestBody());
            logger.info("特色业务平台请求报文TIA:" + tia.toString());
        } catch (Exception e) {
            logger.error("特色业务平台请求报文解析错误.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }

/*
        //工商局通讯处理 -
        TpsTia tpstia = assembleTpsRequestBean(tia, request);
//        aictia4010.setTxnCode("4010");
        TpsToa tpsToa = null;

        String sendMsgForAic = null;
        try {
            sendMsgForAic = getSendMsgForAic(tpstia);
        } catch (Exception e) {
            logger.error("生成工商请求报文时出错.", e);
            response.setHeader("rtnCode", TxnRtnCode.TPSMSG_MARSHAL_FAILED.getCode());
            return;
        }

        try {
            tpsToa = sendAndRecvForAic(sendMsgForAic);
        } catch (SocketTimeoutException e) {
            logger.error("与工商服务器通讯处理超时.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_RECV_TIMEOUT.getCode());
            return;
        } catch (Exception e) {
            logger.error("与工商服务器通讯处理异常.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_COMM_ERROR.getCode());
            return;
        }

        //处理工商局返回报文--
        String starringRespMsg = "";
        try {
//            String aicRntCode = aictoa4010.getRntCode();
            String aicRntCode = "";
            if (!"00".equals(aicRntCode)) {
                starringRespMsg = getErrorRespMsgForStarring(aicRntCode);
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            } else {
                //processTxn(aictia4010, aictoa4010, tia, request);
                TpsToa toa = new TpsTOA1401();
                //组特色平台响应报文--
                starringRespMsg = getRespMsgForStarring(toa);
                response.setHeader("rtnCode", "0000");
            }
        } catch (Exception e) {
            logger.error("特色平台响应报文处理失败.", e);
            throw new RuntimeException(e);
        }
        response.setResponseBody(starringRespMsg.getBytes(response.getCharacterEncoding()));
*/
        response.setResponseBody("1111|222|".getBytes(response.getCharacterEncoding()));

    }

    //处理Starring请求报文
    private TIA4010 getStarringTia(byte[] body) throws Exception {
        TIA4010 tia = new TIA4010();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (TIA4010) starringDataFormat.fromMessage(new String(body, "GBK"), "TIA4010");
        return tia;
    }

    //生成工商请求报文对应BEAN
    private TpsTia assembleTpsRequestBean(TIA4010 tia, Stdp10ProcessorRequest request) {
/*
        AICTIA4010 aictia4010 = new AICTIA4010();

        aictia4010.setTxnCode(request.getHeader("txnCode"));
        aictia4010.setTellerId(request.getHeader("tellerId"));
        aictia4010.setBranchId(request.getHeader("branchId"));
        aictia4010.setBankHostSn(request.getHeader("serialNo"));

        List<AICTIA4010Item> aictia4010Items = new ArrayList<>();
        try {
            for (TIA4010Item item : tia.getItems()) {
                AICTIA4010Item aictia4010Item = new AICTIA4010Item();
                BeanUtils.copyProperties(aictia4010Item, item);
                aictia4010Items.add(aictia4010Item);
            }
        } catch (Exception e) {
            throw new RuntimeException("Bean copy error!");
        }
        aictia4010.setItems(aictia4010Items);
*/
        return null;
    }

    //生成工商请求报文
    private String getSendMsgForAic(TpsTia aictia4010) throws Exception {
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(aictia4010.getClass().getName(), aictia4010);
        FixedLengthTextDataFormat aicReqDataFormat = new FixedLengthTextDataFormat(aictia4010.getClass().getPackage().getName());

        String sendMsg = (String) aicReqDataFormat.toMessage(modelObjectsMap);
        String strLen = "" + (sendMsg.getBytes("GBK").length + 4);
        String lpad = "";
        for (int i = 0; i < 4 - strLen.length(); i++) {
            lpad += "0";
        }
        strLen = lpad + strLen;
        sendMsg = strLen + sendMsg;

        return sendMsg;
    }

    //第三方服务器通讯
    private TpsToa sendAndRecvForAic(String sendMsg) throws Exception {
        String recvMsg = processThirdPartyServer(sendMsg);
        logger.info("工商返回：" + recvMsg);

/*
        AICTOA4010 aictoa4010 = new AICTOA4010();
        FixedLengthTextDataFormat aicRespDataFormat = new FixedLengthTextDataFormat(aictoa4010.getClass().getPackage().getName());
        aictoa4010 = (AICTOA4010) aicRespDataFormat.fromMessage(recvMsg.getBytes("GBK"), "AICTOA4010");
        return aictoa4010;
*/
        return null;
    }


    //处理工商返回报文
    private String getRespMsgForStarring(TpsToa toa) throws Exception {
        String starringRespMsg;
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(toa.getClass().getPackage().getName());
        starringRespMsg = (String) starringDataFormat.toMessage(modelObjectsMap);
        return starringRespMsg;
    }


    //业务逻辑处理
/*
    private void processTxn(AICTIA4010 aictia, AICTOA4010 aictoa, TIA4010 tia, Stdp10ProcessorRequest request) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            AicQdeEnt aicQdeEnt = new AicQdeEnt();
            aicQdeEnt.setPregNo(tia.getPregNo());
            aicQdeEnt.setAreaCode(tia.getAreaCode());
            aicQdeEnt.setAicCode(tia.getAicCode());
            aicQdeEnt.setAicName(tia.getAicName());
            aicQdeEnt.setTellerId(request.getHeader("TellerId"));
            aicQdeEnt.setBranchId(request.getHeader("BranchId"));
            aicQdeEnt.setActNo(tia.getActNo());
            aicQdeEnt.setActBal(new BigDecimal(tia.getActBal()));

            AicQdeEntMapper entMapper = session.getMapper(AicQdeEntMapper.class);
            entMapper.insert(aicQdeEnt);

            AicQdeInvesterMapper investerMapper = session.getMapper(AicQdeInvesterMapper.class);
            int i = 0;
            for (AICTIA4010Item item : aictia.getItems()) {
                String vchSn = aictoa.getVchNos().substring(0 + i * 3, 3 + i * 3);
                AicQdeInvester record = new AicQdeInvester();
                record.setRegNo(aictoa.getPregNo());
                record.setVchSn(vchSn);
                record.setInvesterName(item.getInvesterName());
                record.setActNo(item.getActNo());
                record.setInvAmt(new BigDecimal(item.getInvAmt()));
                record.setActBankName(item.getActBankName());
                record.setCertId(item.getCertId());
                record.setInvDate(request.getHeader("txnTime").substring(0, 8));
                record.setInvestType("1");
                record.setBankHostSn(aictoa.getBankHostSn());
                i++;
                investerMapper.insert(record);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException("业务逻辑处理出错。", e);
        } finally {
            session.close();
        }
    }

*/
}

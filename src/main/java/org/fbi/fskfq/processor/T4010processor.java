package org.fbi.fskfq.processor;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4010Request.CbsTia4010;
import org.fbi.fskfq.domain.cbs.T4010Response.CbsToa4010;
import org.fbi.fskfq.domain.cbs.T4010Response.CbsToa4010Item;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.TpsToaXmlBean;
import org.fbi.fskfq.domain.tps.txn.TpsTia2401;
import org.fbi.fskfq.domain.tps.txn.TpsToa9000;
import org.fbi.fskfq.domain.tps.txn.TpsToa9910;
import org.fbi.fskfq.enums.TxnRtnCode;
import org.fbi.fskfq.helper.FbiBeanUtils;
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
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 1534010缴款查询
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
            logger.info("特色业务平台请求报文TIA:" + tia.toString());
        } catch (Exception e) {
            logger.error("特色业务平台请求报文解析错误.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }


        //检查本地数据库信息
        FsKfqPaymentInfo paymentInfo_db = selectPaymentInfoFromDB(tia.getBillNo());
        if (paymentInfo_db != null) {
            String billStatus = paymentInfo_db.getLnkBillStatus();
            if ("0".equals(billStatus)) { //未缴款，但本地已存在信息
                List<FsKfqPaymentItem> paymentItems = selectPaymentItemsFromDB(paymentInfo_db);
                String starringRespMsg = getRespMsgForStarring(paymentInfo_db, paymentItems);
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
                response.setResponseBody(starringRespMsg.getBytes(response.getCharacterEncoding()));
                logger.info("===特色平台响应报文：\n" + starringRespMsg);
                return;
            } else if ("1".equals(billStatus)){ //已缴款
                response.setHeader("rtnCode", TxnRtnCode.TXN_PAY_REPEATED.getCode());
                logger.info("===此笔缴款单已缴款." );
                return;
            } else if ("2".equals(billStatus)) { //已撤销
                response.setHeader("rtnCode", TxnRtnCode.TXN_PAY_REPEATED.getCode());
                logger.info("===此笔缴款单已撤销.");
                return;
            } else {
                throw new RuntimeException("缴款单状态错误.");
            }
        }


        //第三方通讯处理
        TpsTia tpsTia = assembleTpsRequestBean(tia, request);
        TpsToaXmlBean tpsToa = null;

        byte[] sendTpsBuf;
        try {
            sendTpsBuf = generateTxMsg(tpsTia);
            logger.info("第三方服务器请求报文：\n" + new String(sendTpsBuf, "GBK"));
        } catch (Exception e) {
            logger.error("生成第三方服务器请求报文时出错.", e);
            response.setHeader("rtnCode", TxnRtnCode.TPSMSG_MARSHAL_FAILED.getCode());
            return;
        }

        try {
            String dataType = tpsTia.getHeader().dataType;
            byte[] recvTpsBuf = processThirdPartyServer(sendTpsBuf, dataType);
            String recvTpsMsg = new String(recvTpsBuf, "GBK");
            logger.info("第三方服务器返回报文：\n" + recvTpsMsg);

            String rtnDataType = substr(recvTpsMsg, "<dataType>", "</dataType>").trim();
            if ("9910".equals(rtnDataType)) { //技术性异常报文 9910
                TpsToa9910 tpsToa9910 = transXmlToBeanForTps9910(recvTpsBuf);
                //TODO 发起签到交易
                T9905Processor t9905Processor = new T9905Processor();
                t9905Processor.doRequest(request, response);

                logger.info("===第三方服务器返回报文(异常业务信息类)：\n" + tpsToa9910.toString());
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                String starringRespMsg = getErrorRespMsgForStarring(tpsToa9910.Body.Object.Record.add_word);
                response.setResponseBody(starringRespMsg.getBytes(response.getCharacterEncoding()));
                logger.info("===特色平台响应报文(异常技术信息类9910)：\n" + starringRespMsg);
                return;
            } else { //业务类正常或异常报文 2401
                tpsToa = transXmlToBeanForTps(recvTpsBuf);
            }
        } catch (SocketTimeoutException e) {
            logger.error("与第三方服务器通讯处理超时.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_RECV_TIMEOUT.getCode());
            return;
        } catch (Exception e) {
            logger.error("与第三方服务器通讯处理异常.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_COMM_ERROR.getCode());
            return;
        }

        //处理第三方返回业务报文--
        String starringRespMsg = "";

        String result = tpsToa.getMaininfoMap().get("RESULT");
        if (result != null) { //异常业务报文
            TpsToa9000 tpsToa9000 = new TpsToa9000();
            try {
                FbiBeanUtils.copyProperties(tpsToa.getMaininfoMap(), tpsToa9000);
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                starringRespMsg = getErrorRespMsgForStarring(tpsToa9000.getAddWord());
                response.setResponseBody(starringRespMsg.getBytes(response.getCharacterEncoding()));
                logger.info("===特色平台响应报文(异常业务信息类)：\n" + starringRespMsg);
                return;
            } catch (Exception e) {
                logger.error("第三方服务器响应报文解析异常.", e);
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                return;
            }
        }

        FsKfqPaymentInfo paymentInfo = new FsKfqPaymentInfo();
        List<FsKfqPaymentItem> paymentItems = new ArrayList<>();
        try {
            FbiBeanUtils.copyProperties(tpsToa.getMaininfoMap(), paymentInfo);
            List<Map<String, String>> detailMaplist = tpsToa.getDetailMapList();
            for (Map<String, String> detailMap : detailMaplist) {
                FsKfqPaymentItem item = new FsKfqPaymentItem();
                FbiBeanUtils.copyProperties(detailMap, item);
                paymentItems.add(item);
            }
        } catch (Exception e) {
            logger.error("第三方服务器响应报文解析异常.", e);
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            return;
        }

        //正常交易逻辑处理
        try {
            processTxn(paymentInfo, paymentItems, request);
        } catch (Exception e) {
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            try {
                starringRespMsg = getErrorRespMsgForStarring(e.getMessage());
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
            response.setResponseBody(starringRespMsg.getBytes(response.getCharacterEncoding()));
            logger.error("业务处理失败.", e);
            return;
        }

        //==特色平台响应==
        try {
            starringRespMsg = getRespMsgForStarring(paymentInfo, paymentItems);
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            response.setResponseBody(starringRespMsg.getBytes(response.getCharacterEncoding()));
            logger.info("===特色平台响应报文：\n" + starringRespMsg);
        } catch (Exception e) {
            logger.error("特色平台响应报文处理失败.", e);
            throw new RuntimeException(e);
        }
    }


    //处理Starring请求报文
    private CbsTia4010 getCbsTia(byte[] body) throws Exception {
        CbsTia4010 tia = new CbsTia4010();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4010) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4010");
        return tia;
    }

    //生成第三方请求报文对应BEAN
    private TpsTia assembleTpsRequestBean(CbsTia4010 cbstia, Stdp10ProcessorRequest request) {
        TpsTia2401 tpstia = new TpsTia2401();
        tpstia.Body.Object.Record.billtype_code = cbstia.getBilltypeCode();
        tpstia.Body.Object.Record.bill_no = cbstia.getBillNo();
        tpstia.Body.Object.Record.verify_no = cbstia.getVerifyNo();
        tpstia.Body.Object.Record.bill_money = cbstia.getBillMoney().toString();
        tpstia.Body.Object.Record.set_year = cbstia.getSetYear();

        //处理报文头 TODO 确认msgId的出处
        tpstia.Head.msgId = request.getHeader("txnTime") + request.getHeader("serialNo");
        tpstia.Head.msgRef = request.getHeader("serialNo");
        tpstia.Head.workDate = request.getHeader("txnTime").substring(0, 8);

        // TODO
        tpstia.Head.src = "CCB-370211";
        tpstia.Head.des = "CZ-370211";
        tpstia.Head.dataType = "2401";
        return tpstia;
    }


    //第三方服务器通讯
    private TpsToaXmlBean sendAndRecvForTps(byte[] sendTpsBuf, String txnCode) throws Exception {
        byte[] recvBuf = processThirdPartyServer(sendTpsBuf, txnCode);
        logger.info("第三方服务器返回报文：\n" + new String(recvBuf, "GBK"));

        return transXmlToBeanForTps(recvBuf);
    }


    //生成CBS响应报文
    private String getRespMsgForStarring(FsKfqPaymentInfo paymentInfo, List<FsKfqPaymentItem> paymentItems) {
        CbsToa4010 cbsToa = new CbsToa4010();
        FbiBeanUtils.copyProperties(paymentInfo, cbsToa);

        List<CbsToa4010Item> cbsToaItems = new ArrayList<>();
        for (FsKfqPaymentItem paymentItem : paymentItems) {
            CbsToa4010Item cbsToaItem = new CbsToa4010Item();
            FbiBeanUtils.copyProperties(paymentItem, cbsToaItem);
            cbsToaItems.add(cbsToaItem);
        }
        cbsToa.setItems(cbsToaItems);
        cbsToa.setItemNum("" + cbsToaItems.size());

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

    //=======业务逻辑处理=================================================

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

            paymentInfo.setArchiveFlag("0");

            paymentInfo.setHostBookFlag("0");
            paymentInfo.setHostChkFlag("0");
            paymentInfo.setFbBookFlag("0");
            paymentInfo.setFbChkFlag("0");

            paymentInfo.setAreaCode("KaiFaQu-FeiShui");
            paymentInfo.setHostAckFlag("0");
            paymentInfo.setLnkBillStatus("0"); //初始化

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
            throw new RuntimeException("业务逻辑处理失败。", e);
        } finally {
            session.close();
        }
    }

}

package org.fbi.fskfq.processor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4040Request.CbsTia4040;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.TpsToaXmlBean;
import org.fbi.fskfq.domain.tps.txn.TpsTia2409;
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
 * 撤销交易
 */
public class T4040Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        CbsTia4040 tia;
        try {
            tia = getCbsTia(request.getRequestBody());
        } catch (Exception e) {
            logger.error("特色业务平台请求报文解析错误.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }

        //检查本地数据库信息
        FsKfqPaymentInfo paymentInfo = selectPayoffPaymentInfoFromDB(tia.getBillNo());
        if (paymentInfo == null) {
            marshalAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, "不存在已缴款的记录.", response);
            return;
        }

        //第三方处理
        TpsToaXmlBean tpsToa = processTpsTx(tia, request, response);
        if (tpsToa == null) { //出现异常
            return;
        }
        //判断正误
        String result = tpsToa.getMaininfoMap().get("RESULT");
        if (result != null) { //异常业务报文
            TpsToa9000 tpsToa9000 = new TpsToa9000();
            try {
                FbiBeanUtils.copyProperties(tpsToa.getMaininfoMap(), tpsToa9000, true);
                marshalAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, tpsToa9000.getAddWord(), response);
            } catch (Exception e) {
                logger.error("第三方服务器响应报文解析异常.", e);
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            }
        } else { //正常交易逻辑处理
            try {
                String rtnStatus = tpsToa.getMaininfoMap().get("SUCC_CODE");
                String chr_id = tpsToa.getMaininfoMap().get("CHR_ID");
                String bill_no = tpsToa.getMaininfoMap().get("BILL_NO");
                if (!paymentInfo.getChrId().equals(chr_id) || !paymentInfo.getBillNo().equals(bill_no)) {
                    marshalAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, "单号不符！", response);
                } else {
                    if (!"OK".equals(rtnStatus)) {
                        marshalAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, rtnStatus, response);
                    } else {
                        processTxn(paymentInfo, request);
                        marshalSuccessTxnCbsResponse(response);
                    }
                }
            } catch (Exception e) {
                marshalAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, e.getMessage(), response);
                logger.error("业务处理失败.", e);
            }
        }

    }

    //第三方通讯处理
    private TpsToaXmlBean processTpsTx(CbsTia4040 tia, Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) {
        TpsTia tpsTia = assembleTpsRequestBean(tia, request);
        TpsToaXmlBean tpsToa = new TpsToaXmlBean();

        byte[] sendTpsBuf;
        try {
            sendTpsBuf = generateTpsTxMsgHeader(tpsTia, request);
        } catch (Exception e) {
            logger.error("生成第三方服务器请求报文时出错.", e);
            response.setHeader("rtnCode", TxnRtnCode.TPSMSG_MARSHAL_FAILED.getCode());
            return tpsToa;
        }

        try {
            String dataType = tpsTia.getHeader().getDataType();
            byte[] recvTpsBuf = processThirdPartyServer(sendTpsBuf, dataType);
            String recvTpsMsg = new String(recvTpsBuf, "GBK");

            String rtnDataType = substr(recvTpsMsg, "<dataType>", "</dataType>").trim();
            if ("9910".equals(rtnDataType)) { //技术性异常报文 9910
                TpsToa9910 tpsToa9910 = transXmlToBeanForTps9910(recvTpsBuf);
                //TODO 发起签到交易
                T9905Processor t9905Processor = new T9905Processor();
                t9905Processor.doRequest(request, response);

                logger.info("===第三方服务器返回报文(异常业务信息类)：\n" + tpsToa9910.toString());
                marshalAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, tpsToa9910.Body.Object.Record.add_word, response);
                return null;
            } else { //业务类正常或异常报文 1402
                tpsToa = transXmlToBeanForTps(recvTpsBuf);
            }
        } catch (SocketTimeoutException e) {
            logger.error("与第三方服务器通讯处理超时.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_RECV_TIMEOUT.getCode());
            return null;
        } catch (Exception e) {
            logger.error("与第三方服务器通讯处理异常.", e);
            response.setHeader("rtnCode", TxnRtnCode.MSG_COMM_ERROR.getCode());
            return null;
        }

        return tpsToa;
    }

    //====
    //处理Starring请求报文
    private CbsTia4040 getCbsTia(byte[] body) throws Exception {
        CbsTia4040 tia = new CbsTia4040();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4040) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4040");
        return tia;
    }

    //查找已缴款未撤销的缴款单记录
    private FsKfqPaymentInfo selectPayoffPaymentInfoFromDB(String billNo) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentInfoMapper mapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            mapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            FsKfqPaymentInfoExample example = new FsKfqPaymentInfoExample();
            example.createCriteria()
                    .andBillNoEqualTo(billNo)
                    .andLnkBillStatusEqualTo(BillStatus.PAYOFF.getCode());
            List<FsKfqPaymentInfo> infos = mapper.selectByExample(example);
            if (infos.size() == 0) {
                return null;
            }
            if (infos.size() != 1) { //同一个缴款单号，已缴款未撤销的在表中只能存在一条记录
                throw new RuntimeException("记录状态错误.");
            }
            return infos.get(0);
        }
    }

    //生成第三方请求报文对应BEAN
    private TpsTia assembleTpsRequestBean(CbsTia4040 cbstia, Stdp10ProcessorRequest request) {
        TpsTia2409 tpstia = new TpsTia2409();
        TpsTia2409.BodyRecord record = ((TpsTia2409.Body) tpstia.getBody()).getObject().getRecord();
        FbiBeanUtils.copyProperties(cbstia, record, true);

        generateTpsBizMsgHeader(tpstia, "2409", request);
        return tpstia;
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
            paymentInfo.setOperCancelHostsn(request.getHeader("serialNo"));

/*
            paymentInfo.setHostBookFlag("1");
            paymentInfo.setHostChkFlag("0");
            paymentInfo.setFbBookFlag("1");
            paymentInfo.setFbChkFlag("0");
*/

//            paymentInfo.setAreaCode("KaiFaQu-FeiShui");
//            paymentInfo.setHostAckFlag("0");
            paymentInfo.setLnkBillStatus(BillStatus.CANCELED.getCode()); //已撤销

            //TODO 应记录撤销交易的主机流水号

            FsKfqPaymentInfoMapper infoMapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            infoMapper.updateByPrimaryKey(paymentInfo);
            session.commit();
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException("业务逻辑处理失败。", e);
        } finally {
            session.close();
        }
    }
}

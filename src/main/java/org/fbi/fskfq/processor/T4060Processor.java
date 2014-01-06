package org.fbi.fskfq.processor;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4060Request.CbsTia4060;
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
import java.math.BigDecimal;
import java.util.List;

/**
 * 1534060对账
 * zhanrui
 * 20131227
 */
public class T4060Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        CbsTia4060 tia;
        try {
            tia = getCbsTia(request.getRequestBody());
            logger.info("特色业务平台请求报文TIA:" + tia.toString());
        } catch (Exception e) {
            logger.error("特色业务平台请求报文解析错误.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }


        //获取本地数据库信息
        String startDate = tia.getStartdate().substring(0,4) + "-" +  tia.getStartdate().substring(4,6) +  "-" + tia.getStartdate().substring(6,8);
        String endDate = tia.getEnddate().substring(0,4) +  "-" + tia.getEnddate().substring(4,6) +  "-" + tia.getEnddate().substring(6,8);
        List<FsKfqPaymentInfo> infos = selectPayoffPaymentInfos(startDate, endDate);
        BigDecimal totalAmt = new BigDecimal("0.00");
        for (FsKfqPaymentInfo info : infos) {
            BigDecimal infoAmt = info.getBillMoney();
            List<FsKfqPaymentItem> items = selectPayoffPaymentItems(info);
            BigDecimal totalItemsAmt = new BigDecimal("0.00");
            for (FsKfqPaymentItem item : items) {
                totalItemsAmt = totalItemsAmt.add(item.getChargemoney());
            }
            if (infoAmt.compareTo(totalItemsAmt) != 0) {
                logger.error("缴款单总分不符, billNo=:" + info.getBillNo());
                response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                response.setResponseBody("缴款单总分不符.".getBytes(response.getCharacterEncoding()));
                return;
            }
            totalAmt = totalAmt.add(infoAmt);
        }

        if (totalAmt.compareTo(new BigDecimal(tia.getTotalamt())) != 0) {
            logger.error("对账失败,总金额不符.");
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            response.setResponseBody("对账失败,总金额不符.".getBytes(response.getCharacterEncoding()));
        } else {
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
        }
    }


    //处理Starring请求报文
    private CbsTia4060 getCbsTia(byte[] body) throws Exception {
        CbsTia4060 tia = new CbsTia4060();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4060) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4060");
        return tia;
    }



    //=======业务逻辑处理=================================================
    //查找已缴款未撤销的缴款单记录
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

}

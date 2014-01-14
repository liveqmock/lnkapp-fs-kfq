package org.fbi.fskfq.processor;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.fskfq.domain.cbs.T4099Request.CbsTia4099;
import org.fbi.fskfq.domain.cbs.T4099Response.CbsToa4099;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1534099 根据单号查询缴款单信息(已缴款未撤销) for 撤销交易
 * zhanrui
 * 20140114
 */
public class T4099Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        CbsTia4099 tia;
        try {
            tia = getCbsTia(request.getRequestBody());
            logger.info("特色业务平台请求报文TIA:" + tia.toString());
        } catch (Exception e) {
            logger.error("特色业务平台请求报文解析错误.", e);
            response.setHeader("rtnCode", TxnRtnCode.CBSMSG_UNMARSHAL_FAILED.getCode());
            return;
        }


        //获取本地数据库信息
        List<FsKfqPaymentInfo> infos = selectPayoffPaymentInfos(tia.getBillNo());

        if (infos.size() == 1) {
            String cbsRespMsg = generateCbsRespMsg(infos.get(0));
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            response.setResponseBody(cbsRespMsg.getBytes(response.getCharacterEncoding()));
        }else if (infos.size() == 0) {
            marshalAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, "未查询到相关记录", response);
        } else {
            marshalAbnormalCbsResponse(TxnRtnCode.TXN_EXECUTE_FAILED, "缴款单状态错误", response);
        }
    }

    private String generateCbsRespMsg(FsKfqPaymentInfo paymentInfo) {
        CbsToa4099 cbsToa = new CbsToa4099();
        FbiBeanUtils.copyProperties(paymentInfo, cbsToa);

        String cbsRespMsg = "";
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(cbsToa.getClass().getName(), cbsToa);
        SeperatedTextDataFormat cbsDataFormat = new SeperatedTextDataFormat(cbsToa.getClass().getPackage().getName());
        try {
            cbsRespMsg = (String) cbsDataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("特色平台报文转换失败.", e);
        }
        return cbsRespMsg;

    }


    //处理CBS请求报文
    private CbsTia4099 getCbsTia(byte[] body) throws Exception {
        CbsTia4099 tia = new CbsTia4099();
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
        tia = (CbsTia4099) starringDataFormat.fromMessage(new String(body, "GBK"), "CbsTia4099");
        return tia;
    }



    //=======业务逻辑处理=================================================
    //查找已缴款未撤销的缴款单记录
    private List<FsKfqPaymentInfo>  selectPayoffPaymentInfos(String billNo) {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
        FsKfqPaymentInfoMapper mapper;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            mapper = session.getMapper(FsKfqPaymentInfoMapper.class);
            FsKfqPaymentInfoExample example = new FsKfqPaymentInfoExample();
            example.createCriteria()
                    .andBillNoEqualTo(billNo)
                    .andLnkBillStatusEqualTo(BillStatus.PAYOFF.getCode());
            return  mapper.selectByExample(example);
        }
    }
}

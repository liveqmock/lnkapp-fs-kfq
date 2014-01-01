package org.fbi.fskfq.processor;

import org.fbi.fskfq.domain.tps.txn.TpsTia9905;
import org.fbi.fskfq.domain.tps.txn.TpsToa9906;
import org.fbi.fskfq.helper.ProjectConfigManager;
import org.fbi.linking.processor.ProcessorContext;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhanrui on 13-12-31.
 * 签到交易，自动发起
 */
public class T9905Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        TpsTia9905 tia = new TpsTia9905();
        tia.Body.Object.Record.user_code = ProjectConfigManager.getInstance().getProperty("tps.txn.login.user_code");
        tia.Body.Object.Record.password = ProjectConfigManager.getInstance().getProperty("tps.txn.login.password");
        tia.Body.Object.Record.new_password = "";
        tia.Head.src = ProjectConfigManager.getInstance().getProperty("tps.txn.login.src");
        tia.Head.des = ProjectConfigManager.getInstance().getProperty("tps.txn.login.des");
        tia.Head.dataType = "9905";

        try {
            String sendMsg = tia.toString();

            String dataType = tia.getHeader().dataType;
            byte[] recvTpsBuf = processThirdPartyServer(sendMsg.getBytes("GBK"), dataType);
            String recvTpsMsg = new String(recvTpsBuf, "GBK");
            logger.info("第三方服务器返回报文：\n" + recvTpsMsg);

            String rtnDataType = substr(recvTpsMsg, "<dataType>", "</dataType>").trim();

            if ("9906".equals(rtnDataType)){ //业务类正常报文 9906
                int authLen = Integer.parseInt(new String(recvTpsBuf, 51, 3)) +1;
                String msgdata = new String(recvTpsBuf, 69 + authLen, recvTpsBuf.length - 69 - authLen);
                logger.info("===第三方服务器返回报文(业务信息类)：\n" + msgdata);
                TpsToa9906 tpsToa9906  = new TpsToa9906();
                tpsToa9906 = (TpsToa9906)tpsToa9906.toToa(msgdata);

                //保存授权码信息
                ProcessorContext context = request.getProcessorContext();
                context.setAttribute(CONTEXT_TPS_AUTHCODE, tpsToa9906.Body.Object.Record.accredit_code);
            }
        } catch (Exception e) {
            logger.error("与第三方服务器通讯处理异常.", e);
            throw new RuntimeException("与第三方服务器通讯处理异常.",e);
        }
    }
}

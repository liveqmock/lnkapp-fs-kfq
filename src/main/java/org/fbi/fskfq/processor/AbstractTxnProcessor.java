package org.fbi.fskfq.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.fskfq.domain.cbs.T9999Response.TOA9999;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.TpsToaXmlBean;
import org.fbi.fskfq.domain.tps.txn.TpsToa9910;
import org.fbi.fskfq.enums.TxnRtnCode;
import org.fbi.fskfq.helper.ProjectConfigManager;
import org.fbi.fskfq.helper.TpsSocketClient;
import org.fbi.fskfq.internal.AppActivator;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10Processor;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: zhanrui
 * Date: 13-12-18
 * Time: 下午6:16
 */
public abstract class AbstractTxnProcessor extends Stdp10Processor {
    protected static String CONTEXT_TPS_AUTHCODE = "CONTEXT_TPS_AUTHCODE";
    protected static String TPS_ENCODING = "GBK";  //第三方服务器编码方式
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static String tps_authcode = "";

    @Override
    public void service(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        String txnCode = request.getHeader("txnCode");
        String tellerId = request.getHeader("tellerId");
        if (StringUtils.isEmpty(tellerId)) {
            tellerId = "TELLERID";
        }

        try {
            MDC.put("txnCode", txnCode);
            MDC.put("tellerId", tellerId);
            logger.info("特色平台请求报文:" + request.toString());
            doRequest(request, response);
            logger.info("特色平台响应报文:" + response.toString());
        }catch (Exception e){
            response.setHeader("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            throw new RuntimeException(e);
        } finally {
            MDC.remove("txnCode");
            MDC.remove("tellerId");
        }
    }

    abstract protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException;

    //cbs异常报文处理
    protected void assembleAbnormalCbsResponse(TxnRtnCode txnRtnCode, String errMsg, Stdp10ProcessorResponse response) {
        String msg = getErrorRespMsgForStarring(errMsg);
        response.setHeader("rtnCode", txnRtnCode.getCode());
        try {
            response.setResponseBody(msg.getBytes(response.getCharacterEncoding()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("编码错误", e);
        }
    }

    //组统一的错误响应报文 txtMsg
    private String getErrorRespMsgForStarring(String errMsg) {
        TOA9999 toa = new TOA9999();
        toa.setErrMsg("交易失败-" + errMsg);
        String starringRespMsg;
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(toa.getClass().getPackage().getName());
        try {
            starringRespMsg = (String) starringDataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return starringRespMsg;
    }

    //根据第三方服务器的返回码从配置文件中获取对应的信息
    private String getRtnMsg(String rtnCode) {
        BundleContext bundleContext = AppActivator.getBundleContext();
        URL url = bundleContext.getBundle().getEntry("rtncode.properties");

        Properties props = new Properties();
        try {
            props.load(url.openConnection().getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("错误码配置文件解析错误", e);
        }
        String property = props.getProperty(rtnCode);
        if (property == null) {
            property = "未定义对应的错误信息(错误码:" + rtnCode + ")";
        }
        return property;
    }

    //生成第三方服务器通讯报文头
    protected byte[] generateTpsTxMsgHeader(TpsTia tpstia, Stdp10ProcessorRequest request) throws UnsupportedEncodingException {
        String isSign = "0";
/*
        String authCode = (String)request.getProcessorContext().getAttribute(CONTEXT_TPS_AUTHCODE);
        if (StringUtils.isEmpty(authCode)) {
            authCode = ProjectConfigManager.getInstance().getProperty("authCode");
        }
*/
        String authCode = this.tps_authcode;
        if (StringUtils.isEmpty(authCode)) {
            authCode = ProjectConfigManager.getInstance().getProperty("authCode");
        }
        String authLen = "" + authCode.length();
        String reserve = "               ";

        String sendTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 报文文档中的数据类型为交易码
        String msg = StringUtils.leftPad(tpstia.getHeader().getDataType(), 6, " ")
                + StringUtils.leftPad(tpstia.getHeader().getSrc(), 15, " ")
                + StringUtils.leftPad(tpstia.getHeader().getDes(), 15, " ")
                + sendTime
                + isSign
                + StringUtils.leftPad(authLen, 3, "0")
                + reserve
                + authCode
                + tpstia.toString();

        int msglength = msg.getBytes("GBK").length + 8;
        String len = StringUtils.leftPad("" + msglength, 8, "0");

        byte[] buffer = new byte[msglength];
        System.arraycopy(len.getBytes(), 0, buffer, 0, 8);
        System.arraycopy(msg.getBytes("GBK"), 0, buffer, 8, msglength - 8);

        return buffer;
    }

    //生成第三方服务器业务报文头
    protected void generateTpsBizMsgHeader(TpsTia tpstia, String dataType, Stdp10ProcessorRequest request) {
        //处理报文头 TODO 确认msgId的出处
        tpstia.getHeader().setMsgId(request.getHeader("txnTime") + request.getHeader("serialNo"));
        tpstia.getHeader().setMsgRef(request.getHeader("serialNo"));
        tpstia.getHeader().setWorkDate(request.getHeader("txnTime").substring(0, 8));

        tpstia.getHeader().setSrc(ProjectConfigManager.getInstance().getProperty("tps.txn.login.src"));
        tpstia.getHeader().setDes(ProjectConfigManager.getInstance().getProperty("tps.txn.login.des"));
        tpstia.getHeader().setDataType(dataType);
    }


    //第三方服务处理：可根据交易号设置不同的超时时间
    protected byte[] processThirdPartyServer(byte[] sendTpsBuf, String txnCode) throws Exception {
        String servIp = ProjectConfigManager.getInstance().getProperty("tps.server.ip");
        int servPort = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("tps.server.port"));
        TpsSocketClient client = new TpsSocketClient(servIp, servPort);

        String timeoutCfg = ProjectConfigManager.getInstance().getProperty("tps.server.timeout.txn." + txnCode);
        if (timeoutCfg != null) {
            int timeout = Integer.parseInt(timeoutCfg);
            client.setTimeout(timeout);
        } else {
            timeoutCfg = ProjectConfigManager.getInstance().getProperty("tps.server.timeout");
            if (timeoutCfg != null) {
                int timeout = Integer.parseInt(timeoutCfg);
                client.setTimeout(timeout);
            }
        }

        logger.info("第三方服务器请求报文:" + new String(sendTpsBuf, TPS_ENCODING));
        byte[] rcvTpsBuf = client.call(sendTpsBuf);
        logger.info("第三方服务器响应报文:" + new String(rcvTpsBuf, TPS_ENCODING));
        return rcvTpsBuf;
    }

    //一般技术性异常报文处理 9910
    protected TpsToa9910 transXmlToBeanForTps9910(byte[] buf) {
        int authLen = Integer.parseInt(new String(buf, 51, 3));
        String msgdata = new String(buf, 69 + authLen, buf.length - 69 - authLen);
        System.out.println("===XML报文体：\n" + msgdata);


        TpsToa9910 toa = new TpsToa9910();
        return (TpsToa9910) toa.toToa(msgdata);
    }

    protected TpsToaXmlBean transXmlToBeanForTps(byte[] buf) {
        String txnCode = new String(buf, 0, 6).trim();
        int authLen = Integer.parseInt(new String(buf, 51, 3));
        String msgdata = new String(buf, 69 + authLen, buf.length - 69 - authLen);

        System.out.println("===XML报文体：\n" + msgdata);

        TpsToaXmlBean toa = new TpsToaXmlBean();
        toa = (TpsToaXmlBean) toa.toToa(msgdata);
        return toa;
    }

    protected String substr(String content, String startStr, String endStr) {
        int length = startStr.length();
        int start = content.indexOf(startStr) + length;
        int end = content.indexOf(endStr);
        return content.substring(start, end);
    }

    //======================
}

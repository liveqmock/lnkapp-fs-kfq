package org.fbi.fskfq.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.fskfq.domain.cbs.T9999Response.TOA9999;
import org.fbi.fskfq.domain.tps.base.TpsTia;
import org.fbi.fskfq.domain.tps.base.TpsToaXmlBean;
import org.fbi.fskfq.domain.tps.txn.TpsToa9910;
import org.fbi.fskfq.helper.ProjectConfigManager;
import org.fbi.fskfq.helper.TpsSocketClient;
import org.fbi.fskfq.internal.AppActivator;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10Processor;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.osgi.framework.BundleContext;
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
            doRequest(request, response);
        } finally {
            MDC.remove("txnCode");
            MDC.remove("tellerId");
        }
    }

    abstract protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException;

    protected String getErrorRespMsgForStarring(String errMsg) throws Exception {
        TOA9999 toa = new TOA9999();
        toa.setErrMsg("交易失败-" + errMsg);
        String starringRespMsg;
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        SeperatedTextDataFormat starringDataFormat = new SeperatedTextDataFormat(toa.getClass().getPackage().getName());
        starringRespMsg = (String) starringDataFormat.toMessage(modelObjectsMap);
        return starringRespMsg;
    }

    //根据返回码获取返回信息
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

    //生成通讯报文头
    protected byte[] generateTxMsg(TpsTia tpstia) throws UnsupportedEncodingException {
        String isSign = "0";
        String authCode = ProjectConfigManager.getInstance().getProperty("authCode");
        String authLen = "" + authCode.length();
        String reserve = "               ";

        String sendTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 报文文档中的数据类型为交易码
        String msg = StringUtils.leftPad(tpstia.getHeader().dataType, 6, " ")
                + StringUtils.leftPad(tpstia.getHeader().src, 15, " ")
                + StringUtils.leftPad(tpstia.getHeader().des, 15, " ")
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

        return client.call(sendTpsBuf);
    }

    //一般技术性异常报文处理 9910
    protected TpsToa9910 transXmlToBeanForTps9910(byte[] buf){
        int authLen = Integer.parseInt(new String(buf, 51, 3)) +1;
        String msgdata = new String(buf, 69 + authLen, buf.length - 69 - authLen);

        TpsToa9910 toa = new TpsToa9910();
        return  (TpsToa9910)toa.toToa(msgdata);
    }
    protected TpsToaXmlBean transXmlToBeanForTps(byte[] buf){
        String txnCode = new String(buf, 0, 6).trim();
        int authLen = Integer.parseInt(new String(buf, 51, 3)) +1;
        String msgdata = new String(buf, 69 + authLen, buf.length - 69 - authLen);
        //System.out.println("===报文体：\n" + msgdata);

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

package org.fbi.fskfq.domain.tps.base.xml;

import java.io.Serializable;

public class TpsToaHeader implements Serializable {
    public String src = "";                   // ���ͷ�����
    public String des = "";                   // ���շ�����
    public String dataType = "";
    public String msgRef = "";                // ���Ĳο���  ����������ʱ���Ĳο���ͬ���ı�ʶ��
    public String workDate = "";              // ��������
    public String msgId = "";                 // ���ı��

}

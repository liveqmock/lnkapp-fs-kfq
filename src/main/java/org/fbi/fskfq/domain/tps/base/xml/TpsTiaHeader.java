package org.fbi.fskfq.domain.tps.base.xml;

import java.io.Serializable;

public class TpsTiaHeader implements Serializable {
    public String src = "";                   // ���ͷ�����
    public String des = "";                   // ���շ�����
    public String dataType = "";
    public String msgId = "";                 // ���ı�ʶ��
    public String msgRef = "";                // ���Ĳο���  ����������ʱ���Ĳο���ͬ���ı�ʶ��
    public String workDate = "";              // ��������
}

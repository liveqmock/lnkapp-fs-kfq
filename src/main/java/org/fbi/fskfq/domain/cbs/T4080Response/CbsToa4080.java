package org.fbi.fskfq.domain.cbs.T4080Response;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.OneToMany;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.util.List;


@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsToa4080 {
    @DataField(seq = 1)
    private String itemNum;

    @DataField(seq = 2)
    @OneToMany(mappedTo = "org.fbi.fskfq.domain.cbs.T4080Response.CbsToa4080Item", totalNumberField = "itemNum")
    private List<CbsToa4080Item> items;

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<CbsToa4080Item> getItems() {
        return items;
    }

    public void setItems(List<CbsToa4080Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CbsToa4080{" +
                "itemNum='" + itemNum + '\'' +
                ", items=" + items +
                '}';
    }
}

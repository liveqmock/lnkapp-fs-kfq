package org.fbi.fskfq.domain.cbs.T4070Response;


import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.OneToMany;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.util.List;


@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsToa4070 {
    @DataField(seq = 1)
    private String itemNum;

    @DataField(seq = 2)
    @OneToMany(mappedTo = "org.fbi.fskfq.domain.cbs.T4070Response.CbsToa4070Item", totalNumberField = "itemNum")
    private List<CbsToa4070Item> items;

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<CbsToa4070Item> getItems() {
        return items;
    }

    public void setItems(List<CbsToa4070Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CbsToa4070{" +
                "itemNum='" + itemNum + '\'' +
                ", items=" + items +
                '}';
    }
}

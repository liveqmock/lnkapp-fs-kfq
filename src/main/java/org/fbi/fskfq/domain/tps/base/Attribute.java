package org.fbi.fskfq.domain.tps.base;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 *ÐÂ±¨ÎÄ
 */
@XStreamAlias("attribute")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"content"})
public class Attribute {
    @XStreamAsAttribute
    public String name = new String();
    @XStreamAsAttribute
    public String description = new String();
    public String content = "";

    public Attribute(String name, String description) {
        this.name = name;
        this.description = description;
    }

}

package org.fbi.fskfq.service;


import org.fbi.linking.processor.Processor;
import org.fbi.linking.processor.ProcessorManagerService;

/**
 * User: zhanrui
 * Date: 13-8-22
 * Time: 上午7:49
 */
public class ProcessorManagerServiceImpl implements ProcessorManagerService {
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public Processor getProcessor(String txnCode) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] names = this.getClass().getPackage().getName().split("\\.");
        Class clazz = Class.forName(names[0] + "." + names[1] + "." + names[2] + ".processor.T" + txnCode + "processor");
        Processor processor = (Processor)clazz.newInstance();

        //TODO
        return processor;
    }
}

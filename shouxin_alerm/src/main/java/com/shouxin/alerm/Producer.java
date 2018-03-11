package com.shouxin.alerm;


import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by lixingkai on 2018/1/15.
 */
//怎么去调用这个Producer里面的方法？

@Component
public class Producer {

        private static Logger LOG = Logger.getLogger(Producer.class);
        private kafka.javaapi.producer.Producer<String, byte[]> producer;
        private String topic;
        private Properties props = new Properties();
        private static Producer staticproducer = null;
//        @Value("${encode.type}")
//        private String encodeType;
        //为什么这么写？配置
        public Producer() {
            this.props.put("key.serializer", "org.apache.kafka.common.serialization,StringSerializer");
            this.props.put("value.serializer", "org.apache.kafka.common.serialization,StringSerializer");
        }

        public Producer create_norepeat(String brokerlist, String group, String tpc) {
            System.out.println("brokerlist==="+brokerlist+"/"+"topic===="+tpc);
            if (staticproducer == null) {
                staticproducer = create(brokerlist, group, tpc);
            }
            return staticproducer;
        }

        public static Producer create(String brokerlist, String group, String tpc) {
            if (group.length() < 1) {
                return new Producer(tpc, brokerlist);
            }
            return new Producer(tpc, brokerlist, group);
        }

        public Producer(String topic, String brokerlist) {
            System.out.println("brokerlist1==="+brokerlist+"/"+"topic1===="+topic);
            this.props.put("key.serializer", "org.apache.kafka.common.serialization,StringSerializer");
            this.props.put("value.serializer", "org.apache.kafka.common.serialization,StringSerializer");
            this.props.put("metadata.broker.list", brokerlist);
            this.producer = new kafka.javaapi.producer.Producer(new ProducerConfig(this.props));
            this.topic = topic;
        }

        public Producer(String topic, String brokerlist, String group) {
            this.props.put("key.serializer", "org.apache.kafka.common.serialization,StringSerializer");
            this.props.put("value.serializer", "org.apache.kafka.common.serialization,StringSerializer");
            this.props.put("metadata.broker.list", brokerlist);
            this.props.put("group.id", group);
            this.producer = new kafka.javaapi.producer.Producer(new ProducerConfig(this.props));
            this.topic = topic;
        }

        public synchronized void send(String s, String tpc) throws Exception {
            this.producer.send(new KeyedMessage(tpc, s.getBytes("gbk")));
        }

        public synchronized void send(String s) throws Exception,java.nio.channels.ClosedChannelException{
            System.out.println("s====="+s);
            this.producer.send(new KeyedMessage(this.topic, s.getBytes("utf-8")));
        }


}

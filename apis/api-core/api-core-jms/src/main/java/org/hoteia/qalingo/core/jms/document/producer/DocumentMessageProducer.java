package org.hoteia.qalingo.core.jms.document.producer;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hoteia.qalingo.core.mapper.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component(value = "documentMessageProducer")
public class DocumentMessageProducer {

    protected final Log logger = LogFactory.getLog(getClass());

    @Resource(name="documentJmsTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    protected XmlMapper xmlMapper;
    
    /**
     * Generates JMS messages
     * 
     */
    public void generateAndSendMessages(final DoucmentMessageJms doucmentMessageJms) {
        try {
            final String valueJMSMessage = xmlMapper.getXmlMapper().writeValueAsString(doucmentMessageJms);

            if(StringUtils.isNotEmpty(valueJMSMessage)){
                jmsTemplate.send( new MessageCreator() {
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage message = session.createTextMessage(valueJMSMessage);
                        if (logger.isDebugEnabled()) {
                            logger.info("Sending JMS message: " + valueJMSMessage);
                        }
                        return message;
                    }
                });                
            } else {
                logger.warn("Doucment Message Jms Message is empty");
            }

        } catch (JmsException e) {
            logger.error("Exception during create/send message process");
        } catch (JsonProcessingException e) {
            logger.error("Exception during build message process");
        } 
    }
    
}
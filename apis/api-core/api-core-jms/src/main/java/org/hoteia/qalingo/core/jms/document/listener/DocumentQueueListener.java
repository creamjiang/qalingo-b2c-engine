package org.hoteia.qalingo.core.jms.document.listener;

import java.beans.ExceptionListener;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hoteia.qalingo.core.domain.OrderCustomer;
import org.hoteia.qalingo.core.jms.document.producer.DoucmentMessageJms;
import org.hoteia.qalingo.core.mapper.XmlMapper;
import org.hoteia.qalingo.core.service.DocumentService;
import org.hoteia.qalingo.core.service.OrderCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "documentQueueListener")
public class DocumentQueueListener implements MessageListener, ExceptionListener {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    protected XmlMapper xmlMapper;
    
    @Autowired
    protected DocumentService documentService;
    
    @Autowired
    protected OrderCustomerService orderCustomerService;
    
    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage) message;
                String valueJMSMessage = tm.getText();
                
                if(StringUtils.isNotEmpty(valueJMSMessage)){
                    final DoucmentMessageJms doucmentMessageJms = xmlMapper.getXmlMapper().readValue(valueJMSMessage, DoucmentMessageJms.class);
                    
                    final OrderCustomer order = orderCustomerService.getOrderById(doucmentMessageJms.getOrderId());
                    documentService.generateOrderConfirmation(order);
                    
                    if (logger.isDebugEnabled()) {
                        logger.debug("Processed message, value: " + valueJMSMessage);
                    }
                } else {
                    logger.warn("Document generation: Jms Message is empty");
                }
            }
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    @Override
    public void exceptionThrown(Exception e) {
        logger.debug("Exception on queue listener: " + e.getCause() + ":" + e.getLocalizedMessage());
    }

}
package com.convista.groovydemo

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    //Body 
       def body = message.getBody();
       message.setBody(body + "Body is modified");
       //Headers 
       def map = message.getHeaders();
       def value = map.get("oldHeader");
       message.setHeader("oldHeader", value + "modified");
       message.setHeader("newHeader", "newHeader");
       //Properties 
       map = message.getProperties();
       value = map.get("oldProperty");
       message.setProperty("oldProperty", value + "modified");
       message.setProperty("newProperty", "newProperty");
       return message;
}

package agus.prasetyo.backend.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeviceDetectService {
    Logger logger= LoggerFactory.getLogger(DeviceDetectService.class);
    public String detect(String device){
        device=String.valueOf(device);
        if(device.contains("Mobile")){
            logger.info("Mobile");
            return "Mobile";
        } else if (device.contains("Windows")) {
            logger.info("Windows");
            return "windows";

        }else if (device.contains("Mac")) {
            logger.info("Mac");
            return "Mac";
        }
        return "Application";
    }
}

package com.example.admin2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import de.codecentric.boot.admin.server.notify.LoggingNotifier;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 李文
 * @create 2020-09-24 13:39
 **/
@Component
public class WeChatNotifier extends AbstractStatusChangeNotifier
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingNotifier.class);

    public WeChatNotifier(InstanceRepository repository, WeChatNews news) {
        super(repository);
        this.news = news;
    }


    WeChatNews news;

    private static final ObjectMapper o = new ObjectMapper();

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                Registration registration = instance.getRegistration();
                StatusInfo statusInfo = ((InstanceStatusChangedEvent) event).getStatusInfo();
                String status = statusInfo.getStatus();
                switch (status) {
                    // 健康检查没通过
                    case "DOWN":
                        String s = down(statusInfo);
                        news.postForEntity(registration.getName(), "异常", registration.getServiceUrl().substring(7), s);
                        break;
                    // 服务离线
                    //case "OFFLINE":
                    //    news.postForEntity(registration.getName(), "服务离线", registration.getServiceUrl().substring(7), null);
                    //    System.out.println("发送 服务离线 的通知！");
                    //    break;
                    ////服务上线
                    //case "UP":
                    //    news.postForEntity(registration.getName(), "服务上线", registration.getServiceUrl().substring(7), null);
                    //    System.out.println("发送 服务上线 的通知！");
                    //    break;
                    //// 服务未知异常
                    //case "UNKNOWN":
                    //    news.postForEntity(registration.getName(), "无法访问", registration.getServiceUrl().substring(7), null);
                    //    System.out.println("发送 服务未知异常 的通知！");
                    //    break;
                    default:
                        break;
                }
            }
        });
    }

    private String down(StatusInfo statusInfo) {
        Map<String, Object> a = new HashMap<>(2);
        Map<String, Object> s = statusInfo.getDetails();
        for (Map.Entry<String, Object> e : s.entrySet()) {
            if ("status".equals(e.getKey())) {
                continue;
            }
            try {
                LinkedHashMap m = (LinkedHashMap) e.getValue();
                if (!"UP".equals(m.get("status"))) {
                    a.put(e.getKey(), e.getValue());
                }
            } catch (Exception ignored) {

            }
            System.out.println(e.getKey() + "  " + e.getValue());
        }
        o.enable(SerializationFeature.INDENT_OUTPUT);
        String st = null;
        try {
            st = StringEscapeUtils.escapeJava(o.writeValueAsString(a));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //context.setVariable("json", st);
        //return text.getValue(context, String.class);

        return st;
    }


}

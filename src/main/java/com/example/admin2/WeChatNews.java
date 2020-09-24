package com.example.admin2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author 李文
 * @create 2020-03-06 10:37
 **/
@Component
public class WeChatNews
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatNews.class);

    @Value("${WebhookUrl}")
    public String WebhookUrl;

    static String down = "{\n" +
            "    \"msgtype\": \"markdown\",\n" +
            "    \"markdown\": {\n" +
            "        \"content\": \"## #{#status} #{#name} \\n\n" +
            "### #{#url}  \\n\n" +
            "~~~\n" +
            "#{#data}\n" +
            "~~~\"\n" +
            "    }\n" +
            "}";
    static SpelExpressionParser parser = new SpelExpressionParser();
    private static final Expression downExpression = parser.parseExpression(down, ParserContext.TEMPLATE_EXPRESSION);


    public String getNews(String name, String status, String url, String data) {
        EvaluationContext ctx = new StandardEvaluationContext();
        //设置变量
        ctx.setVariable("name", name);
        ctx.setVariable("url", url);
        ctx.setVariable("data", data);
        ctx.setVariable("status", status);
        return downExpression.getValue(ctx, String.class);
    }

    private static final RestTemplate restTemplate = new RestTemplate();

    public void postForEntity(String name,String status, String url, String data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity entity = new HttpEntity<>(getNews(name,status, url, data), headers);
        ResponseEntity e = restTemplate.postForEntity(WebhookUrl, entity, String.class);
        LOGGER.debug("响应内容：" + e.toString());
    }
}

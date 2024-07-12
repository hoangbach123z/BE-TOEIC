package com.backend.spring.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.base.config.ApplicationProperties;
import com.kss.base.httpClient.RestClient;
import com.kss.base.utils.JsonUtils;
import com.kss.transferservice.repository.CommonAdapter;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public abstract class BaseService {

  /** Common logger for use in subclasses. */
  protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

//  protected ModelMapper modelMapper = Mappers.getMapper(ModelMapper.class);

  @Autowired protected ApplicationContext applicationContext;
  @Autowired protected ApplicationProperties applicationProperties;

  @Autowired protected ApplicationEventPublisher applicationEventPublisher;

  @Autowired protected ThreadPoolTaskExecutor taskExecutor;

  @Autowired protected RestClient restClient;;
  @Autowired protected EntityManager entityManager;
  @Autowired
  protected CommonAdapter commonAdapter;

  @Value("${partner.document-service.url}")
  protected String baseDocumentServiceUrl;
  @Value("${partner.document-service.api-key}")
  protected String baseDocumentServiceApiKey;
  @Value("${partner.flex.host}")
  protected String baseFlexUrl;
  @Value("${partner.flex.x-api-key}")
  protected String baseFlexApiKey;
  @Value("${partner.order-sender.host}")
  protected String baseOrderSenderUrl;
  @Value("${partner.order-sender.x-api-key}")
  protected String baseOrderSenderApiKey;
  @Value("${partner.notification-service.host}")
  protected String baseNotificationUrl;
  @Value("${partner.notification-service.x-api-key}")
  protected String baseNotificationApiKey;
//  @Autowired protected CacheRepository cacheRepository;

  protected ObjectMapper objectMapper = JsonUtils.getMapper();
  protected HttpHeaders getHttpHeaders(){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    return headers;
  }

//  public  <T> T getObjectFromCache(String cacheKey,String objKey,Class<T> responseType) throws Exception {
//    T result =null;
//
//    var map = cacheRepository.hmget(cacheKey);
//    if (map.containsKey(objKey))
//    {
//      String jsonObj = (String)map.get(objKey);
//      if (StringUtils.isNotEmpty(jsonObj)) {
//        result = (T) JsonUtils.fromJson(jsonObj, responseType);
//      }
//    }
//    return result;
//  }

//  protected Authentication getCurrentAuthentication() {
//    return SecurityContextHolder.getContext().getAuthentication();
//  }
//
//  protected String getCurrentUsername() {
//    var authentication = getCurrentAuthentication();
//    if (authentication != null) {
//      return authentication.getName();
//    }
//    throw new BaseException(CommonErrorCode.UNAUTHORIZED);
//  }
}

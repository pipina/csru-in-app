package config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.SetDlznici;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import repository.SetDlzniciRepository;
import service.SendMessageService;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SendNewCompaniesTimer {

    @Autowired
    private SetDlzniciRepository setDlzniciRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SendMessageService sendMessageService;

    private static final String ACTIVEMQ_TOPIC_QUEUE = "nove_podniky";
    private static final String ERROR_PARSING_TO_JSON = "Chyba pri spracovaní objektov do JSON formátu.";

    @Scheduled(cron = "${system.cron.send-data}")
    public void poslatNovePodnikyDoQueue(){
        try{
            List<SetDlznici> zoznamNovychPodnikov = setDlzniciRepository.findAllBySync(false);
            zoznamNovychPodnikov.forEach(x-> x.setSetDlzniciObdobieList(new ArrayList<>()));
            String jsonMessage = convertListToJson(zoznamNovychPodnikov);
            sendMessageService.sendMessage(ACTIVEMQ_TOPIC_QUEUE, jsonMessage);
        }catch (Exception e){
            System.err.println(ERROR_PARSING_TO_JSON);
            e.printStackTrace();
        }

    }

    private String convertListToJson(List<SetDlznici> entities) throws JsonProcessingException {
        return objectMapper.writeValueAsString(entities);
    }

}

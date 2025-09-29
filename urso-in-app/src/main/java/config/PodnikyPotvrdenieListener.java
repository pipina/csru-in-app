package config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.SetDlznici;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import repository.SetDlzniciRepository;

import java.util.List;

@Component
public class PodnikyPotvrdenieListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SetDlzniciRepository setDlzniciRepository;

    private static final String ERROR_PARSING_JSON = "Chyba pri prijatí dát a ich spracovaní do objektov.";

    @JmsListener(destination = "nove_podniky_potvrdenie")
    public void receiveMessage(String message) {
        try {
        System.out.println("Received <" + message + ">");
        List<SetDlznici> dlznici = convertStringToObjects(message);
        setDlzniciRepository.saveAll(dlznici);

        } catch (JsonProcessingException ex){
            System.err.println(ERROR_PARSING_JSON);
            ex.printStackTrace();
        }
    }

    private List<SetDlznici> convertStringToObjects(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<List<SetDlznici>>(){});
    }
}

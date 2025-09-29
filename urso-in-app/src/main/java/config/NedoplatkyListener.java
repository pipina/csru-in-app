package config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.SetDlznici;
import model.SetDlzniciDto;
import model.SetDlzniciObdobie;
import model.SetDlzniciRefresh;
import model.SetObdobieDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import repository.SetDlzniciObdobieRepository;
import repository.SetDlzniciRefreshRepository;
import repository.SetDlzniciRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class NedoplatkyListener {

    private static final Logger logger = LoggerFactory.getLogger(NedoplatkyListener.class);

    private static final String ERROR_PARSING_JSON = "Chyba pri prijatí dát a ich spracovaní do objektov.";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SetDlzniciObdobieRepository setDlzniciObdobieRepository;

    @Autowired
    private SetDlzniciRefreshRepository setDlzniciRefreshRepository;

    @Autowired
    private SetDlzniciRepository setDlzniciRepository;

    @JmsListener(destination = "aktualne_nedoplatky")
    public void receiveMessage(String message) {
        try {
            logger.info("[aktualne_nedoplatky][Method - receiveMessage] Zacinam aktualizaciu csru_set.");
            List<String> messages = objectMapper.readValue(message, new TypeReference<List<String>>(){});
            List<SetObdobieDto> obdobie = objectMapper.readValue(messages.get(0), new TypeReference<List<SetObdobieDto>>() {});
            List<SetDlzniciRefresh> refresh = objectMapper.readValue(messages.get(1), new TypeReference<List<SetDlzniciRefresh>>() {});
            List<SetDlzniciDto> dlzniciDtos = objectMapper.readValue(messages.get(2), new TypeReference<List<SetDlzniciDto>>() {});

            List<SetDlzniciObdobie> dlzniciObdobies = mapDtoToObdobie(obdobie);

            setDlzniciObdobieRepository.saveAll(dlzniciObdobies);
            setDlzniciRefreshRepository.saveAll(refresh);

            List<SetDlznici> dlznici = mapDtoToDlznici(dlzniciDtos);
            setDlzniciRepository.saveAll(dlznici);
            logger.info("[aktualne_nedoplatky][Method - receiveMessage] Aktualizacia csru_set uspesne ukoncena.");
        }catch (Exception ex){
            logger.error("[aktualne_nedoplatky][Method - receiveMessage] Aktualizacia csru_set neuspesna. (Error - " + ex.getMessage() + ")");
            System.err.println(ERROR_PARSING_JSON);
            ex.printStackTrace();
        }
    }

    private List<SetDlzniciObdobie> mapDtoToObdobie(List<SetObdobieDto> setObdobieDtos){
        List<SetDlzniciObdobie> dlzniciObdobies = new ArrayList<>();
        for (SetObdobieDto setObdobieDto : setObdobieDtos){
            logger.info("[aktualne_nedoplatky][Method - mapDtoToObdobie] Mapujem obdobie s id " + setObdobieDto.getId());
            SetDlzniciObdobie setDlzniciObdobie = new SetDlzniciObdobie();
            setDlzniciObdobie.setId(setObdobieDto.getId());
            setDlzniciObdobie.setObdobieOd(setObdobieDto.getObdobieOd());
            setDlzniciObdobie.setObdobieDo(setObdobieDto.getObdobieDo());
            setDlzniciObdobie.setTypDlznika(setObdobieDto.getTypDlznika());
            setDlzniciObdobie.setZdroj(setObdobieDto.getZdroj());
            if (setObdobieDto.getSetDlznici() != null) {
                setDlzniciObdobie.setSetDlznici(setDlzniciRepository.findById(setObdobieDto.getSetDlznici()).get());
            }
            dlzniciObdobies.add(setDlzniciObdobie);
        }
        return dlzniciObdobies;
    }

    private List<SetDlznici> mapDtoToDlznici(List<SetDlzniciDto> dlzniciDtos) {
        List<SetDlznici> dlznici = new ArrayList<>();
        for (SetDlzniciDto dlznikDto : dlzniciDtos) {
            logger.info("[aktualne_nedoplatky][Method - mapDtoToDlznici] Mapujem dlznika s id " + dlznikDto.getId());
            SetDlznici dlznik = new SetDlznici();
            dlznik.setId(dlznikDto.getId());
            dlznik.setNazov(dlznikDto.getNazov());
            dlznik.setIco(dlznikDto.getIco());
            dlznik.setIdPo(dlznikDto.getIdPo());
            if (dlznikDto.getSetDlzniciRefresh() != null) {
                dlznik.setSetDlzniciRefresh(setDlzniciRefreshRepository.findById(dlznikDto.getSetDlzniciRefresh()).get());
            }
            dlznik.setSync(dlznikDto.getSync());
            dlznici.add(dlznik);
        }
        return dlznici;
    }
}

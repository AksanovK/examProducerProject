package exam.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exam.models.dto.StatementsDto;

public class JsonConverter {
    public static String pdfDtoToJson(StatementsDto statementsDto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(statementsDto);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

package exam.services;

import com.itextpdf.text.*;
import exam.models.Statements;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatementsServiceImpl implements StatementsService {

    private final RabbitTemplate rabbitTemplate;
    private final static String STATEMENT_RECOVERY_KEY = "statement.recovery";
    private final static String STATEMENT_ACADEMIC_LEAVE_KEY = "statement.academic_leave";

    @Autowired
    private DirectExchange directExchange;

    @Override
    public byte[] recoveryStatementGeneration(Statements statements) throws IOException, DocumentException {
        Map<String, Serializable> map = new HashMap<>();
        map.put("firstName", statements.getFirstName());
        map.put("lastName", statements.getLastName());
        map.put("type", statements.getType().getType());
        return getBytes(map, STATEMENT_RECOVERY_KEY);
    }

    @Override
    public byte[] academicLeaveStatementGeneration(Statements statements) throws IOException, DocumentException {
        Map<String, Serializable> map = new HashMap<>();
        map.put("firstName", statements.getFirstName());
        map.put("lastName", statements.getLastName());
        map.put("type", statements.getType().getType());
        return getBytes(map, STATEMENT_ACADEMIC_LEAVE_KEY);
    }

    private byte[] getBytes(Map<String, Serializable> map, String key) {
        byte[] bytes = SerializationUtils.serialize(map);
        Optional<Message> messageOptional = Optional.ofNullable(rabbitTemplate.sendAndReceive(directExchange.getName(), key, new Message(bytes)));
        return messageOptional.orElseThrow(IllegalArgumentException::new).getBody();
    }

}

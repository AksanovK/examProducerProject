package exam.controllers;

import exam.models.Statements;
import exam.models.TypeOfStatement;
import exam.models.dto.StatementsDto;
import exam.repositories.StatementsRepository;
import exam.repositories.TypeOfStatementsRepository;
import exam.services.StatementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statements")
public class StatementsController {

    private final StatementsRepository statementsRepository;
    private final StatementsService statementsService;
    private final TypeOfStatementsRepository typeOfStatementsRepository;
    private final RabbitTemplate rabbitTemplate;

    @GetMapping
    public List<Statements> getAllUsers() {
        return statementsRepository.findAll();
    }

    @PostMapping("/recovery")
    public ResponseEntity<InputStreamResource> recoveryStatementSending(@RequestBody StatementsDto statementsDto) {
         try {
             Optional<TypeOfStatement> typeOfStatement = typeOfStatementsRepository.findById(statementsDto.getType());
             Statements statements = new Statements(getAllUsers().size() - 1L, statementsDto.getFirstName(), statementsDto.getLastName(), typeOfStatement.get());
             statementsRepository.save(statements);
             return getPdfInputStreamResourceResponseEntity(statementsService.recoveryStatementGeneration(statements));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/academicLeave")
    public ResponseEntity<InputStreamResource> academicLeaveStatementSending(@RequestBody StatementsDto statementsDto) {
        try {
            Optional<TypeOfStatement> typeOfStatement = typeOfStatementsRepository.findById(statementsDto.getType());
            Statements statements = new Statements(getAllUsers().size() - 1L, statementsDto.getFirstName(), statementsDto.getLastName(), typeOfStatement.get());
            statementsRepository.save(statements);
            return getPdfInputStreamResourceResponseEntity(statementsService.academicLeaveStatementGeneration(statements));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private ResponseEntity<InputStreamResource> getPdfInputStreamResourceResponseEntity(byte[] content) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(inputStream.available());
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
//    @PostMapping("/recovery")
//    @ResponseBody
//    public ResponseEntity<Document> addUser(@RequestBody StatementsDto statementsDto) throws DocumentException, IOException {
//        System.out.println(statementsDto.getType());
//        Optional<TypeOfStatement> typeOfStatement = typeOfStatementsRepository.findById(statementsDto.getType());
//        Statements statements = new Statements(getAllUsers().size() - 1L, statementsDto.getFirstName(), statementsDto.getLastName(), typeOfStatement.get());
//        statementsRepository.save(statements);
//        Document document = statementsService.pdfGenerator(statements);
//        return ResponseEntity
//                .ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;")
//                .body(document);
//    }

}

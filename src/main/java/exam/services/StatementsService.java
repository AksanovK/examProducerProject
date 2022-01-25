package exam.services;

import com.itextpdf.text.DocumentException;
import exam.models.Statements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface StatementsService {
    byte[] recoveryStatementGeneration(Statements statements) throws IOException, DocumentException;
    byte[] academicLeaveStatementGeneration(Statements statements) throws IOException, DocumentException;
}

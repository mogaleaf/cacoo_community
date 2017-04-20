package com.mogaleaf.community.api.service;

import com.mogaleaf.auth.ExecuteRequest;
import com.mogaleaf.auth.RequestService;
import com.mogaleaf.auth.UserToken;
import com.mogaleaf.community.db.DatabaseService;
import com.mogaleaf.community.model.Diagram;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DiagramsServiceTest {

    @Mock
    private DatabaseService database;

    @Mock
    private RequestService requestService;

    @Mock
    private ExecuteRequest requestExecutor;

    @InjectMocks
    private DiagramsService instance = new DiagramsService();

    @Mock
    private InputStream inputStream;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void retrieveTemplateDiagramsTest() throws  Exception {
        UserToken falseToken = new UserToken();
        Mockito.when(database.retrieveCredential("email")).thenReturn(falseToken);
        Mockito.when(requestService.getService(falseToken)).thenReturn(requestExecutor);
        Mockito.when(requestExecutor.executeRequest(Mockito.anyString())).thenReturn(this.getClass().getClassLoader().getResourceAsStream("diagrams.json"));
        instance.retrieveTemplateDiagrams("email");
        ArgumentCaptor<List<Diagram>> diagsCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(database).addDiagrams(diagsCaptor.capture());

        List<Diagram> capture = diagsCaptor.getValue();
        assertThat(capture).hasSize(1);
        Diagram aDiag = capture.get(0);

        assertThat(aDiag.id).isEqualTo("00e77f4dc9973517");
        assertThat(aDiag.imageUrl).isEqualTo("https://cacoo.com/diagrams/00e77f4dc9973517.png");
        assertThat(aDiag.name).isEqualTo("Wireframe");
        assertThat(aDiag.numberOfRate).isEqualTo(0);
        assertThat(aDiag.rate).isEqualTo(0);
    }

    @Test
    public void rateExistTest(){
        Mockito.when(database.exist("id")).thenReturn(true);
        instance.rate("id", 5);
        Mockito.verify(database).rate("id",5);
        Mockito.verify(database).exist("id");
    }

    @Test
    public void rateNotExistTest(){
        Mockito.when(database.exist("id")).thenReturn(false);
        instance.rate("id", 5);
        Mockito.verify(database,Mockito.never()).rate("id",5);
        Mockito.verify(database).exist("id");
    }
}

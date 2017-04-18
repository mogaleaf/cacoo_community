package com.mogaleaf.cacoo.api;

import com.mogaleaf.auth.ExecuteRequest;
import com.mogaleaf.cacoo.model.Diagrams;
import com.mogaleaf.cacoo.model.Result;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class DiagramsBuilderTest {

    @Mock
    ExecuteRequest request;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuilder() throws IOException {
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(request.executeRequest(urlCaptor.capture())).thenReturn(this.getClass().getClassLoader().getResourceAsStream("diagrams.json"));
        DiagramsBuilder diagramsBuilder = new DiagramsBuilder(request);
        Diagrams build = diagramsBuilder.setFolderId(23).setKeyWord("keyWord").setLimit(10).setOffset(1).setSortOn(DiagramsBuilder.SortOn.folder).setSortType(DiagramsBuilder.SortType.asc).setType(DiagramsBuilder.Type.all).build();
        assertThat(build.count).isEqualTo(85);
        assertThat(build.result).hasSize(1);
        Result diagram = build.result.get(0);
        assertThat(diagram.url).isEqualTo("https://cacoo.com/diagrams/00e77f4dc9973517");
        assertThat(diagram.imageUrl).isEqualTo("https://cacoo.com/diagrams/00e77f4dc9973517.png");
        assertThat(diagram.imageUrlForApi).isEqualTo("https://cacoo.com/api/v1/diagrams/00e77f4dc9973517.png");
        assertThat(diagram.diagramId).isEqualTo("00e77f4dc9973517");
        assertThat(urlCaptor.getValue()).isEqualTo("https://cacoo.com/api/v1/diagrams.json?offset=1&limit=10&type=all&sortOn=folder&sortType=asc&folderId=23&keyWord=keyWord");
    }
}

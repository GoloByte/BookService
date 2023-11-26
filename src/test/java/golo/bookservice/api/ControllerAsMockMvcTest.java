package golo.bookservice.api;

import golo.bookservice.api.dto.Charge;
import golo.bookservice.core.Core;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ControllerAsMockMvcTest {

    private MockMvc mockMvc;

    @Mock
    private Core core;

    @InjectMocks
    private Controller controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testImportCodes() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/books.csv");
        MockMultipartFile file = new MockMultipartFile("file", "books.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, inputStream);


        when(core.importBooks(file)).thenReturn(new Charge());

        mockMvc.perform(multipart("/books/import")
                        .file("file", "content".getBytes()))
                .andExpect(status().isOk());
    }

}
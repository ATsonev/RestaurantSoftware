package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.repository.TableRepository;
import com.google.zxing.WriterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class QRCodeServiceImplTest {

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    @Spy
    private QRCodeServiceImpl qrCodeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setProperty("ip_address", "127.0.0.1");
    }

    @Test
    public void testGenerateAndSaveQRCodes() throws WriterException, IOException {
        Table table1 = new Table();
        table1.setId(1L);
        Table table2 = new Table();
        table2.setId(2L);

        List<Table> tables = Arrays.asList(table1, table2);
        when(tableRepository.findAll()).thenReturn(tables);

        Path tempDirectory = Files.createTempDirectory("qr_codes");
        for (Table table : tables) {
            Path filePath = tempDirectory.resolve("table_" + table.getId() + "_qr_code.png");
            Files.createFile(filePath);
            doNothing().when(qrCodeService).generateQRCodeImage(anyString(), anyInt(), anyInt(), anyString());
        }

        qrCodeService.generateAndSaveQRCodes();

        verify(tableRepository, times(1)).findAll();
        verify(qrCodeService, times(tables.size())).generateQRCodeImage(anyString(), anyInt(), anyInt(), anyString());
        verify(tableRepository, times(tables.size())).save(any(Table.class));
    }
}

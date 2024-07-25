package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.repository.TableRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

@Service
public class QRCodeServiceImpl {

    @Autowired
    private TableRepository tableRepository;

    @Transactional
    public void generateAndSaveQRCodes() throws WriterException, IOException {
        String localIpAddress = System.getProperty("ip_address");
        String baseUrl = "http://"+localIpAddress+ ":8080/customer-order/";
        int width = 350;
        int height = 350;

        List<Table> tables = tableRepository.findAll();
        for (Table table : tables) {
            if(table.getQrCodePath() == null) {
                String url = baseUrl + table.getId();
                String filePath = "src/main/resources/static/qr_codes/table_" + table.getId() + "_qr_code.png";
                generateQRCodeImage(url, width, height, filePath);
                table.setQrCodePath("/qr_codes/table_" + table.getId() + "_qr_code.png");
                tableRepository.save(table);
                System.out.println("QR Code for table " + table.getId() + " generated and saved as " + filePath);
            }
        }
    }

    protected void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}

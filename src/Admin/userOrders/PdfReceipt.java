/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.userOrders;

/**
 *
 * @author user
 */
import com.google.zxing.WriterException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

public class PdfReceipt {

    public static void generateReceiptPdf(String receiptText, String filePath, String fontPath, String referenceNumber, double latitude, double longitude) throws WriterException, IOException {
        String qrData = "https://www.google.com/maps/dir/?api=1&destination=" 
                + latitude + "," + longitude 
                + "&label=REF-" + referenceNumber;
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, new File(fontPath));

            try (PDPageContentStream stream = new PDPageContentStream(document, page)) {

                // Draw receipt text
                stream.beginText();
                stream.setFont(font, 12);
                stream.setLeading(14f);
                stream.newLineAtOffset(50, 750);
                for (String line : receiptText.split("\n")) {
                    stream.showText(line);
                    stream.newLine();
                }
                stream.endText();

                float startX = 130;          // left margin
                float startY = 640;         // top of page
                float lineHeight = 14f;     // must match stream.setLeading
                int fontSize = 12;

                String[] lines = receiptText.split("\n");
                float textHeight = lines.length * lineHeight;

                float barcodeY = startY - textHeight - 20; 
                
                // Draw Qrcode
                BufferedImage qrImage = barcodeGenerator.generateQRCode(qrData, 150, 150);
                PDImageXObject pdImage = LosslessFactory.createFromImage(document, qrImage);

                // Center it better (optional tweak)
                float qrX = startX + 75;
                float qrY = barcodeY;

                stream.drawImage(pdImage, qrX, qrY, 150, 150);
                stream.beginText();
                stream.setFont(font, 10);
                stream.newLineAtOffset(qrX, qrY - 15);
                stream.endText();
            }

            document.save(filePath);
            System.out.println("PDF saved to: " + filePath);
        }
    }
}

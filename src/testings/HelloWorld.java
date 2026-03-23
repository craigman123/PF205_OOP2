/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testings;

/**
 *
 * @author user
 */

import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;

public class HelloWorld {
  public static void main(String[] args) throws Exception {
        File f = new File("receipts/OrderReceipt_11.pdf");
        PDDocument doc = PDDocument.load(f);
        System.out.println("PDF loaded successfully!");
        doc.close();
    }
}

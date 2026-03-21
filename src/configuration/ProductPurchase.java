/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

/**
 *
 * @author user
 */
public class ProductPurchase {
    private String productName;
    private java.sql.Timestamp purchaseTime;

    public ProductPurchase(String productName, java.sql.Timestamp purchaseTime) {
        this.productName = productName;
        this.purchaseTime = purchaseTime;
    }

    public String getProductName() {
        return productName;
    }

    public java.sql.Timestamp getPurchaseTime() {
        return purchaseTime;
    }
}

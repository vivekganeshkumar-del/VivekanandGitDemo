package com.banking.framework.support;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel access is intentionally kept inside CommonLib.java.
 * There is no separate ExcelReader.java in this framework.
 */
public class CommonLib {
    private static final String EXCEL_PATH = "src/test/resources/TestData.xlsx";

    /**
     * Fillo queries the requested worksheet and returns the matching Recordset.
     * Callers must advance the cursor with next() before calling getField().
     */
    public Recordset getData(String testCaseNo, String testDataNo, String sourceSheet,
                             Connection[] connectionHolder) throws Exception {
        Fillo fillo = new Fillo();
        Connection connection = fillo.getConnection(Path.of(EXCEL_PATH).toString());
        connectionHolder[0] = connection;
        String query = "SELECT * FROM " + sourceSheet
                + " WHERE TC_NO = '" + escapeSql(testCaseNo)
                + "' AND TD_NO = '" + escapeSql(testDataNo) + "'";
        return connection.executeQuery(query);
       
    }

    /**
     * Convenience method used by the Cucumber steps: query one row and map its
     * Excel columns to Java strings. The Fillo connection and Recordset are closed here.
     */
    
    //Hello
   
    public Map<String, String> getPaymentData(String testCaseNo, String testDataNo, String sourceSheet) throws Exception {
        Connection[] holder = new Connection[1];
        Recordset rs = null;
        try {
            rs = getData(testCaseNo, testDataNo, sourceSheet, holder);
            if (!rs.next()) {
                throw new IllegalArgumentException("No Excel row found for TC_NO=" + testCaseNo
                        + ", TD_NO=" + testDataNo + ", sheet=" + sourceSheet);
            }
            Map<String, String> data = new LinkedHashMap<>();
            String[] columns = {"TC_NO", "TD_NO", "Description", "messageId", "debtor", "creditor",
                    "amount", "currency", "Authorization", "expectedStatus", "expectedCode"};
            for (String column : columns) {
                data.put(column, rs.getField(column));
            }
            return data;
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (Exception ignored) {}
            }
            if (holder[0] != null) {
                try { holder[0].close(); } catch (Exception ignored) {}
            }
        }
    }

    private String escapeSql(String value) {
        return value.replace("'", "''");
    }
}

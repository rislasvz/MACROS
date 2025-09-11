package org.scotiabank.productosGTB.util;

import org.apache.poi.ss.usermodel.*;
import org.scotiabank.productosGTB.model.LecturaTEFXLSTOXLSX;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class readxlstoxlsx {


    public List<LecturaTEFXLSTOXLSX> leerArchivo(String rutaArchivo) throws IOException {

        List<LecturaTEFXLSTOXLSX> inputsTEF = new ArrayList<>();

        File file = new File(rutaArchivo);
        if (!file.exists()) {
            throw new IOException("El archivo no existe: " + rutaArchivo);
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet hoja = workbook.getSheetAt(0);

            if (hoja == null) {
                throw new IOException("La hoja está vacía en el archivo: " + rutaArchivo);
            }

            // DataFormatter preserva el formato tal cual se ve en Excel (ej. evita 12345.0)
            DataFormatter formatter = new DataFormatter();

            // Iteramos desde la fila 1 (suponiendo que la 0 es cabecera)
            for (int i = 1; i <= hoja.getLastRowNum(); i++) {
                Row fila = hoja.getRow(i);
                if (fila == null) continue;

                LecturaTEFXLSTOXLSX lectura = new LecturaTEFXLSTOXLSX();
                lectura.setFormaPago(getCellValue(fila.getCell(0), formatter));
                lectura.setTipoCuenta(getCellValue(fila.getCell(1), formatter));
                lectura.setBancoReceptor(getCellValue(fila.getCell(2), formatter));
                lectura.setCuenta(getCellValue(fila.getCell(3), formatter));
                lectura.setImportePago(getCellValue(fila.getCell(4), formatter));
                lectura.setClaveBeneficiario(getCellValue(fila.getCell(5), formatter));
                lectura.setRfcBeneficiario(getCellValue(fila.getCell(6), formatter));
                lectura.setNombreBeneficiario(getCellValue(fila.getCell(7), formatter));
                lectura.setReferenciaPago(getCellValue(fila.getCell(8), formatter));
                lectura.setConceptoPago(getCellValue(fila.getCell(9), formatter));
                lectura.setDiasVigencia(getCellValue(fila.getCell(10), formatter));
                lectura.setCampoDeInformacionParaAgruparPagos(getCellValue(fila.getCell(11), formatter));
                lectura.setDetalleMail(getCellValue(fila.getCell(12), formatter));
                lectura.setGetReferenciaAbonoBanxico(getCellValue(fila.getCell(13), formatter));
                lectura.setTipoDeOperacion(getCellValue(fila.getCell(14), formatter));
                inputsTEF.add(lectura);
            }
        }

        return inputsTEF;
    }

    /**
     * Método auxiliar para obtener el valor de una celda como String,
     * respetando el formato del archivo Excel.
     */

    private String getCellValue(Cell cell, DataFormatter formatter) {
        if (cell == null) return null;
        return formatter.formatCellValue(cell);
    }


    public class ReadExcelGeneric {

        public List<Map<String, String>> leerArchivoGenerico(String rutaArchivo) throws IOException {
            List<Map<String, String>> registros = new ArrayList<>();

            File file = new File(rutaArchivo);
            if (!file.exists()) {
                throw new IOException("El archivo no existe: " + rutaArchivo);
            }

            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fis)) {

                Sheet hoja = workbook.getSheetAt(0);
                if (hoja == null) {
                    throw new IOException("La hoja está vacía en el archivo: " + rutaArchivo);
                }

                DataFormatter formatter = new DataFormatter();

                // Obtenemos la fila de encabezados (asumimos que es la primera, índice 0)
                Row encabezado = hoja.getRow(0);
                if (encabezado == null) {
                    throw new IOException("El archivo no contiene encabezados en la primera fila.");
                }

                // Guardamos los nombres de las columnas
                List<String> columnas = new ArrayList<>();
                for (Cell cell : encabezado) {
                    columnas.add(formatter.formatCellValue(cell));
                }

                // Iteramos desde la fila 1 (datos)
                for (int i = 1; i <= hoja.getLastRowNum(); i++) {
                    Row fila = hoja.getRow(i);
                    if (fila == null) continue;

                    Map<String, String> registro = new LinkedHashMap<>();

                    for (int j = 0; j < columnas.size(); j++) {
                        Cell cell = fila.getCell(j);
                        String valor = (cell == null) ? null : formatter.formatCellValue(cell);
                        registro.put(columnas.get(j), valor);
                    }

                    registros.add(registro);
                }
            }

            return registros;
        }





}

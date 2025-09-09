package org.scotiabank.productosGTB.controllers;

import org.scotiabank.productosGTB.model.LecturaReporteTEFDetMailCuenta;
import org.scotiabank.productosGTB.model.LecturaReporteTEFDetalle;
import org.scotiabank.productosGTB.model.LecturaReporteTEFHeader;
import org.scotiabank.productosGTB.model.LecturaReporteTEFHeaderBloque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class SistemaDispersionFondosLecturaController {

    private static final Logger log = LoggerFactory.getLogger(SistemaDispersionFondosLecturaController.class);

    public void processPositionalFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        LecturaReporteTEFHeader lecturaReporteTEFHeader = new LecturaReporteTEFHeader();
        LecturaReporteTEFHeaderBloque lecturaReporteTEFHeaderBloque = new LecturaReporteTEFHeaderBloque();
        List<LecturaReporteTEFDetalle> lecturaReporteTEFDetalleList = new ArrayList<>();
        List<LecturaReporteTEFDetMailCuenta> lecturaReporteTEFDetMailCuenta = new ArrayList<>();
        if (selectedFile != null) {
            Path filePath = selectedFile.toPath();
            System.out.println("Procesando archivo: " + selectedFile.getName());

            try (Stream<String> lines = Files.lines(filePath)) {
                // Obtener un iterador para procesar las líneas secuencialmente
                Iterator<String> iterator = lines.iterator();
                // Procesar la primera línea (Encabezado)
                if (iterator.hasNext()) {
                    String firstLine = iterator.next();
                    System.out.println("Línea de Encabezado: " + firstLine);
                    lecturaReporteTEFHeader.setTipoDeArchivo(firstLine.substring(0, 2));
                    lecturaReporteTEFHeader.setTipoDeRegistro(firstLine.substring(2, 4));
                    lecturaReporteTEFHeader.setNumeroDeConvenio(firstLine.substring(4, 9));
                    lecturaReporteTEFHeader.setSecuencia(firstLine.substring(9, 11));
                    lecturaReporteTEFHeader.setFechaGeneracionArchivo(firstLine.substring(11, 19));
                    lecturaReporteTEFHeader.setFechaInicial(firstLine.substring(19, 27));
                    lecturaReporteTEFHeader.setFechaFinal(firstLine.substring(27, 35));
                    lecturaReporteTEFHeader.setCodigoStatusRegistro(firstLine.substring(35, 38));
                    lecturaReporteTEFHeader.setFiller(firstLine.substring(38, 370));
                }

                // Procesar la segunda línea (Encabezado de Bloque)
                if (iterator.hasNext()) {
                    String secondLine = iterator.next();
                    System.out.println("Línea de Datos (segunda): " + secondLine);
                    lecturaReporteTEFHeaderBloque.setTipoDeArchivo(secondLine.substring(0, 2));
                    lecturaReporteTEFHeaderBloque.setTipoDeRegistro(secondLine.substring(2, 4));
                    lecturaReporteTEFHeaderBloque.setMonedaCTARegistro(secondLine.substring(4, 9));
                    lecturaReporteTEFHeaderBloque.setUsoFuturo(secondLine.substring(9, 11));
                    lecturaReporteTEFHeaderBloque.setCuentaDeCargo(secondLine.substring(11, 19));
                    lecturaReporteTEFHeaderBloque.setReferecniaEmpresa(secondLine.substring(19, 27));
                    lecturaReporteTEFHeaderBloque.setCodigoStatusRegistro(secondLine.substring(27, 35));
                    lecturaReporteTEFHeaderBloque.setFiller(secondLine.substring(35, 38));
                }

                // Procesar las líneas restantes, deteniéndose al encontrar "SPTA"
                while (iterator.hasNext()) {
                    String line = iterator.next();
                    if (line.startsWith("SPTA")) {
                        System.out.println("Fin del ciclo, se encontró la línea 'SPTA'.");
                        break; // Rompe el bucle
                    }
                    LecturaReporteTEFDetalle lecturaReporteTEFDetalle = new LecturaReporteTEFDetalle();
                    lecturaReporteTEFDetalle.setTipoArchivo(line);
                    lecturaReporteTEFDetalle.setTipoRegistro(line);
                    lecturaReporteTEFDetalle.setTipoMovimiento(line);
                    lecturaReporteTEFDetalle.setCVEMonedaPago(line);
                    lecturaReporteTEFDetalle.setImporte(line);
                    lecturaReporteTEFDetalle.setFechaAplicacion(line);
                    lecturaReporteTEFDetalle.setServicioConcepto(line);
                    lecturaReporteTEFDetalle.setCVEBeneficiario(line);
                    lecturaReporteTEFDetalle.setRFCBeneficiario(line);
                    lecturaReporteTEFDetalle.setNombreBeneficiario(line);
                    lecturaReporteTEFDetalle.setReferenciaDePago(line);
                    lecturaReporteTEFDetalle.setPlazaDePagoSBI(line);
                    if (!iterator.hasNext()) break;
                    String line2 = iterator.next();
                    if (line2.startsWith("SPTA")) {
                        break;
                    }
                    lecturaReporteTEFDetalle.setSucursalDePago(line);
                    lecturaReporteTEFDetalle.setNumeroCuentaBeneficiario(line);
                    lecturaReporteTEFDetalle.setPais(line);
                    lecturaReporteTEFDetalle.setCiudadEstado(line);
                    lecturaReporteTEFDetalle.setTipoCuenta(line);
                    lecturaReporteTEFDetalle.setDigitoIntercambio(line);
                    lecturaReporteTEFDetalle.setPlazaCuentaBanco(line);
                    lecturaReporteTEFDetalle.setNumBancoEmisor(line);
                    lecturaReporteTEFDetalle.setNumBancoReceptor(line);
                    if (!iterator.hasNext()) break;
                    String line3 = iterator.next();
                    if (line3.startsWith("SPTA")) {
                        break;
                    }
                    lecturaReporteTEFDetalle.setDiasVigencia(line);
                    lecturaReporteTEFDetalle.setConceptoPago(line);
                    lecturaReporteTEFDetalle.setCampoUsoEmpresa1(line);
                    lecturaReporteTEFDetalle.setCampoUsoEmpresa2(line);
                    lecturaReporteTEFDetalle.setCampoUsoEmpresa3(line);
                    lecturaReporteTEFDetalle.setCodigoStatusRegistro(line);
                    lecturaReporteTEFDetalle.setCodigoCambioInst(line);
                    lecturaReporteTEFDetalle.setFechaDePago(line);
                    lecturaReporteTEFDetalle.setPlazaDePago(line);
                    lecturaReporteTEFDetalle.setSucursalDelPago(line);
                    lecturaReporteTEFDetalle.setFolioUnico(line);
                    lecturaReporteTEFDetalle.setFiller(line);
                    lecturaReporteTEFDetalleList.add(lecturaReporteTEFDetalle);

                    //lecturaReporteTEFDetMailCuenta.


                }
                String lineFinal = iterator.next();
                if (lineFinal.startsWith("SPTA")) {

                    System.out.println("Fin del ciclo, se encontró la línea 'SPTA'.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al leer el archivo: " + selectedFile.getName());
            }
        }
    }
    
}

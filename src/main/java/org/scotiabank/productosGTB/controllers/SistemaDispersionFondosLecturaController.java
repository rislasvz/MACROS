package org.scotiabank.productosGTB.controllers;

import org.scotiabank.productosGTB.model.*;
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
        List<LecturaReporteTEFDetalle> lecturaReporteTEFDetalleList = new ArrayList<>();
        List<LecturaReporteTEFDetMailCuenta> lecturaReporteTEFDetMailCuentaList = new ArrayList<>();
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
                    LecturaReporteTEFHeader lecturaReporteTEFHeader = new LecturaReporteTEFHeader();
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
                    LecturaReporteTEFHeaderBloque lecturaReporteTEFHeaderBloque = new LecturaReporteTEFHeaderBloque();
                    lecturaReporteTEFHeaderBloque.setTipoDeArchivo(secondLine.substring(0, 2));
                    lecturaReporteTEFHeaderBloque.setTipoDeRegistro(secondLine.substring(2, 4));
                    lecturaReporteTEFHeaderBloque.setMonedaCTARegistro(secondLine.substring(4, 6));
                    lecturaReporteTEFHeaderBloque.setUsoFuturo(secondLine.substring(6, 10));
                    lecturaReporteTEFHeaderBloque.setCuentaDeCargo(secondLine.substring(10, 21));
                    lecturaReporteTEFHeaderBloque.setReferecniaEmpresa(secondLine.substring(21, 31));
                    lecturaReporteTEFHeaderBloque.setCodigoStatusRegistro(secondLine.substring(31, 34));
                    lecturaReporteTEFHeaderBloque.setFiller(secondLine.substring(34, 370));
                }

                // Procesar las líneas restantes, deteniéndose al encontrar "SPTA"
                while (iterator.hasNext()) {
                    String line = iterator.next();
                    if (line.startsWith("SPTA")) {
                        System.out.println("Fin del ciclo, se encontró la línea 'SPTA'.");
                        break;
                    }
                    LecturaReporteTEFDetalle lecturaReporteTEFDetalle = new LecturaReporteTEFDetalle();
                    lecturaReporteTEFDetalle.setTipoArchivo(line.substring(0, 2));
                    lecturaReporteTEFDetalle.setTipoRegistro(line.substring(2, 4));
                    lecturaReporteTEFDetalle.setTipoMovimiento(line.substring(4, 6));
                    lecturaReporteTEFDetalle.setCVEMonedaPago(line.substring(6, 8));
                    lecturaReporteTEFDetalle.setImporte(line.substring(8, 23));
                    lecturaReporteTEFDetalle.setFechaAplicacion(line.substring(23, 31));
                    lecturaReporteTEFDetalle.setServicioConcepto(line.substring(31, 33));
                    lecturaReporteTEFDetalle.setCVEBeneficiario(line.substring(33, 53));
                    lecturaReporteTEFDetalle.setRFCBeneficiario(line.substring(53, 66));
                    lecturaReporteTEFDetalle.setNombreBeneficiario(line.substring(66, 106));
                    lecturaReporteTEFDetalle.setReferenciaDePago(line.substring(106, 122));
                    lecturaReporteTEFDetalle.setPlazaDePagoSBI(line.substring(122, 127));
                    lecturaReporteTEFDetalle.setSucursalDePago(line.substring(127, 132));
                    if (!iterator.hasNext()) break;
                    String line2 = iterator.next();
                    if (line2.startsWith("SPTA")) {
                        break;
                    }
                    lecturaReporteTEFDetalle.setNumeroCuentaBeneficiario(line.substring(132, 152));
                    lecturaReporteTEFDetalle.setPais(line.substring(152, 157));
                    lecturaReporteTEFDetalle.setCiudadEstado(line.substring(157, 197));
                    lecturaReporteTEFDetalle.setTipoCuenta(line.substring(197, 198));
                    lecturaReporteTEFDetalle.setDigitoIntercambio(line.substring(198, 199));
                    lecturaReporteTEFDetalle.setPlazaCuentaBanco(line.substring(199, 204));
                    lecturaReporteTEFDetalle.setNumBancoEmisor(line.substring(204, 207));
                    lecturaReporteTEFDetalle.setNumBancoReceptor(line.substring(207, 210));
                    if (!iterator.hasNext()) break;
                    String line3 = iterator.next();
                    if (line3.startsWith("SPTA")) {
                        break;
                    }
                    lecturaReporteTEFDetalle.setDiasVigencia(line.substring(210, 213));
                    lecturaReporteTEFDetalle.setConceptoPago(line.substring(213, 263));
                    lecturaReporteTEFDetalle.setCampoUsoEmpresa1(line.substring(263, 283));
                    lecturaReporteTEFDetalle.setCampoUsoEmpresa2(line.substring(283, 303));
                    lecturaReporteTEFDetalle.setCampoUsoEmpresa3(line.substring(303, 323));
                    lecturaReporteTEFDetalle.setCodigoStatusRegistro(line.substring(323, 326));
                    lecturaReporteTEFDetalle.setCVECambioInst(line.substring(326, 327));
                    lecturaReporteTEFDetalle.setCodigoCambioInst(line.substring(327, 330));
                    lecturaReporteTEFDetalle.setFechaDePago(line.substring(330, 338));
                    lecturaReporteTEFDetalle.setPlazaDePago(line.substring(338, 343));
                    lecturaReporteTEFDetalle.setSucursalDelPago(line.substring(343, 348));
                    lecturaReporteTEFDetalle.setFolioUnico(line.substring(348, 364));
                    lecturaReporteTEFDetalle.setFiller(line.substring(364, 370));
                    lecturaReporteTEFDetalleList.add(lecturaReporteTEFDetalle);

                    LecturaReporteTEFDetMailCuenta lecturaReporteTEFDetMailCuenta = new LecturaReporteTEFDetMailCuenta();
                    lecturaReporteTEFDetMailCuenta.setTipoDeArchivo(line.substring(0, 2));
                    lecturaReporteTEFDetMailCuenta.setTipoDeRegistro(line.substring(2, 4));
                    lecturaReporteTEFDetMailCuenta.setMailBeneficiario(line.substring(4, 104));
                    lecturaReporteTEFDetMailCuenta.setUsoFuturo(line.substring(104, 138));
                    lecturaReporteTEFDetMailCuenta.setClaveRastreo(line.substring(138, 168));
                    lecturaReporteTEFDetMailCuenta.setReferenciaDeAbonoBanxico(line.substring(168, 188));
                    lecturaReporteTEFDetMailCuenta.setTipoDeOperacion(line.substring(188, 190));
                    lecturaReporteTEFDetMailCuenta.setFiller(line.substring(190, 370));
                    lecturaReporteTEFDetMailCuentaList.add(lecturaReporteTEFDetMailCuenta);
                }
                String lineFinal = iterator.next();
                if (lineFinal.startsWith("SPTA")) {
                    LecturaReporteTEFTrailerBloque lecturaReporteTEFTrailerBloque = new LecturaReporteTEFTrailerBloque();
                    lecturaReporteTEFTrailerBloque.setTipoArchivo(lineFinal.substring(0, 2));
                    lecturaReporteTEFTrailerBloque.setTipoRegistro(lineFinal.substring(2, 4));
                    lecturaReporteTEFTrailerBloque.setCantidadMovimientoSolicitadosAltas(lineFinal.substring(4, 11));
                    lecturaReporteTEFTrailerBloque.setImporteMovimientoSolicitadosAltas(lineFinal.substring(11, 28));
                    lecturaReporteTEFTrailerBloque.setCantidadMovimientoSolicitadosBajas(lineFinal.substring(28, 35));
                    lecturaReporteTEFTrailerBloque.setImporteMovimientoSolicitadosBajas(lineFinal.substring(35, 52));
                    lecturaReporteTEFTrailerBloque.setCantidadMovimientoAceptadosAltas(lineFinal.substring(52, 59));
                    lecturaReporteTEFTrailerBloque.setImporteMovimientoAceptadosAltas(lineFinal.substring(59, 76));
                    lecturaReporteTEFTrailerBloque.setCantidadMovimientoAceptadosBajas(lineFinal.substring(76, 83));
                    lecturaReporteTEFTrailerBloque.setImporteMovimientoAceptadosBajas(lineFinal.substring(83, 100));
                    lecturaReporteTEFTrailerBloque.setCantidadMovimientoRechazadosAltas(lineFinal.substring(100, 107));
                    lecturaReporteTEFTrailerBloque.setImporteMovimientoRechazadosAltas(lineFinal.substring(107, 124));
                    lecturaReporteTEFTrailerBloque.setCantidadMovimientoRechazadosBajas(lineFinal.substring(124, 131));
                    lecturaReporteTEFTrailerBloque.setImporteMovimientoRechazadosBajas(lineFinal.substring(131, 148));
                    lecturaReporteTEFTrailerBloque.setCantidadDeMovimeientosPagados(lineFinal.substring(148, 155));
                    lecturaReporteTEFTrailerBloque.setImporteDeMovimeientosPagados(lineFinal.substring(155, 172));
                    lecturaReporteTEFTrailerBloque.setCantidadDeMovimientosDevueltos(lineFinal.substring(172, 179));
                    lecturaReporteTEFTrailerBloque.setImporteDeMovimientosDevueltos(lineFinal.substring(179, 196));
                    lecturaReporteTEFTrailerBloque.setCantidadDeMovimientosRech(lineFinal.substring(196, 203));
                    lecturaReporteTEFTrailerBloque.setImporteDeMovimientosRech(lineFinal.substring(203, 220));
                    lecturaReporteTEFTrailerBloque.setCantidadDeMovimientosBaja(lineFinal.substring(220, 227));
                    lecturaReporteTEFTrailerBloque.setImporteMovimientosBaja(lineFinal.substring(227, 244));
                    lecturaReporteTEFTrailerBloque.setCodigoStausRegistro(lineFinal.substring(244, 247));
                    lecturaReporteTEFTrailerBloque.setFiller(lineFinal.substring(247, 370));
                    System.out.println("Fin del ciclo, se encontró la línea 'SPTA'.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al leer el archivo: " + selectedFile.getName());
            }
        }
    }
    
}

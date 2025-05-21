package com.gest.art.reporting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gest.art.reporting.dto.EReportType;
import com.gest.art.reporting.dto.ReportFormat;
import com.gest.art.reporting.dto.ReportSource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author : <a href="siguizana08@gmail.com">BRAHIMA TRAORE </a>.
 * @version : 1.0
 * @since : 08/03/2022 à 13:38:22 Configuration du service des états.
 */
@Service
@Slf4j
@SuppressWarnings("ALL")
public class ReportConfigService {
    private final ReportingTemplateConfig.ReportingTemplate reportingTemplate;

    public ReportConfigService(ReportingTemplateConfig.ReportingTemplate reportingTemplate) {
        this.reportingTemplate = reportingTemplate;
    }

    /**
     * Building du rapport.
     *
     * @param reportingInput
     * @param dto
     * @param parameterMap
     * @return ReportingResponseDto
     * @throws IOException
     * @throws JRException
     */
    public byte[] buildReport(final EReportType eReportType, final HashMap<String, ? super Object> parameterMap,
                              final ReportFormat format, final Object dto) throws IOException, JRException {
        // recuperation du fichier jasper
        final String reportTemplate = reportingTemplate.getTemplateMap().get(eReportType.name());

        if (null == reportTemplate) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "reportType " + eReportType.name() + " jasper template is missing in the config object");
        }
        InputStream fileInputStream = getClass().getResourceAsStream(reportTemplate);
        // convert DTO into the JsonDatasource
        InputStream jsonFile = this.convertDtoToInputStream(dto);
        JRDataSource jsonDataSource = new JsonDataSource(jsonFile);
        byte[] bytes = this.genererRapport(fileInputStream, parameterMap, jsonDataSource, format, ReportSource.BINARY);
        return bytes;
    }

    /**
     * Convertir en inputStream.
     *
     * @param dto
     * @return InputStream
     * @throws IOException
     */
    private InputStream convertDtoToInputStream(final Object dto) throws IOException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).findAndRegisterModules();
        ;
        // Java object to JSON string
        String jsonString = mapper.writeValueAsString(dto);
       // System.out.println("\n Json String:  \n" + jsonString);
        // if (profiles == "dev") {
        createJsonFile(jsonString);
        // }
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }

    /**
     * generation du rapport en byte[].
     *
     * @param inputStream
     * @param parametres
     * @param jsonDataSource
     * @param format
     * @param source
     * @return byte[]
     */
    private byte[] genererRapport(@NotNull final InputStream inputStream,
                                  final HashMap<String, ? super Object> parametres, final JRDataSource jsonDataSource,
                                  @NotNull final ReportFormat format, @NotNull final ReportSource source) {
        byte[] fluxFichier = null;
        JasperPrint jasperPrint = null;
        try {
            if (source.equals(ReportSource.SOURCE)) {
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametres, jsonDataSource);
            } else {
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametres, jsonDataSource);
            }
            switch (format) {
                case PDF:
                    fluxFichier = exportToPDF(jasperPrint);
                    break;
                case EXCEL:
                    fluxFichier = exportToExcel(jasperPrint);
                    break;
                case CSV:
                    fluxFichier = exportToCSV(jasperPrint);
                    break;
                case WORD:
                    fluxFichier = exportToWord(jasperPrint);
                    break;
                case XPRINT:
                    fluxFichier = exportToExprinter(jasperPrint);
                    break;
                default:
                    return null;
            }
        } catch (Exception ex) {
            System.out.println("Erreur de generation du report" + ex.getMessage());
            // log.error("Erreur de generation du report", ex);
        }
        return fluxFichier;
    }

    /**
     * Cette méthode permet de generer un état sous format PDF.
     *
     * @param documentImprimable
     * @return un flux de bytes de données
     * @throws JRException
     */
    private byte[] exportToPDF(final JasperPrint documentImprimable) throws JRException {
        return JasperExportManager.exportReportToPdf(documentImprimable);
    }

    /**
     * Cette méthode permet de générer un état sous format Excel.
     *
     * @param doc
     * @return un tableau de bytes
     * @throws JRException
     */
    private byte[] exportToExcel(final JasperPrint doc) throws JRException {
        ByteArrayOutputStream excelReportStream = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(doc));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(excelReportStream));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setDetectCellType(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        return excelReportStream.toByteArray();
    }

    /**
     * Cette méthode permet de générer un état sous format CSV.
     *
     * @param doc
     * @return un tableau de bytes
     * @throws JRException
     */
    private byte[] exportToCSV(final JasperPrint doc) throws JRException {
        ByteArrayOutputStream excelReportStream = new ByteArrayOutputStream();
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(doc));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(excelReportStream));
        exporter.exportReport();
        return excelReportStream.toByteArray();

    }

    private byte[] exportToWord(final JasperPrint doc) throws JRException {
        ByteArrayOutputStream wordReportStream = new ByteArrayOutputStream();
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(doc));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(wordReportStream));
        SimpleDocxExporterConfiguration configuration = new SimpleDocxExporterConfiguration();
        configuration.setEmbedFonts(Boolean.TRUE);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        return wordReportStream.toByteArray();
    }

    /**
     * exportation direct dans une impimante.
     *
     * @param jasperPrint
     * @return byte
     */
    private byte[] exportToExprinter(final JasperPrint jasperPrint) {
        ByteArrayOutputStream xPrinterReportStream = new ByteArrayOutputStream();
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(MediaSizeName.ISO_A8);
        if (jasperPrint.getOrientationValue() == net.sf.jasperreports.engine.type.OrientationEnum.LANDSCAPE) {
            printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
        } else {
            printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
        }
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        PrintRequestAttributeSet reqAttribs = new HashPrintRequestAttributeSet();
        reqAttribs.add(new Copies(1));
        reqAttribs.add(MediaSizeName.ISO_A8);
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
        configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
        configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
        configuration.setPrintRequestAttributeSet(reqAttribs);
        configuration.setDisplayPageDialog(false);
        configuration.setDisplayPrintDialog(false);

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setConfiguration(configuration);
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xPrinterReportStream));
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        try {
            exporter.exportReport();
            return xPrinterReportStream.toByteArray();
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Création json file.
     *
     * @param str
     * @throws IOException
     */
    private void createJsonFile(final String str) throws IOException {
       BufferedWriter writer = new BufferedWriter(new FileWriter("json_file.json"));
        writer.write(str);
        writer.close();
    }
}

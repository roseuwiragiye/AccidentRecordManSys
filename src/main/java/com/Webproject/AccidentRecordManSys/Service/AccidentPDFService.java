package com.Webproject.AccidentRecordManSys.Service;
import com.Webproject.AccidentRecordManSys.Model.Accident;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class AccidentPDFService {
    public static ByteArrayInputStream accidentPDFReport(List<Accident> accidents) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            // Add Content to PDF file ->
            Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
            Paragraph para = new Paragraph("Animal List", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(7);
            // Add PDF Table Header ->
            Stream.of("No", "Driver's Name", "Plate Number","Location", "Number of Death", "Number of Patients","The accident cause").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
                header.setBackgroundColor(Color.BLUE);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for (Accident accident : accidents) {
                PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(accident.getId())));
                idCell.setPaddingLeft(4);
                idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell NameCell = new PdfPCell(new Phrase(accident.getName()));
                NameCell.setPaddingLeft(4);
                NameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                NameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(NameCell);

                PdfPCell plateNoCell = new PdfPCell(new Phrase(String.valueOf(accident.getPlateNo())));
                plateNoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                plateNoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                plateNoCell.setPaddingRight(4);
                table.addCell(plateNoCell);

                PdfPCell locationCell = new PdfPCell(new Phrase(String.valueOf(accident.getLocation())));
                locationCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                locationCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                locationCell.setPaddingRight(4);
                table.addCell(locationCell);

//                PdfPCell DateCell = new PdfPCell(new Phrase(String.valueOf(accident.getDateTime())));
//                DateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                DateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                DateCell.setPaddingRight(4);
//                table.addCell(DateCell);

                PdfPCell deathCell = new PdfPCell(new Phrase(String.valueOf(accident.getDeath())));
                deathCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                deathCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                deathCell.setPaddingRight(4);
                table.addCell(deathCell);
                PdfPCell patientsCell = new PdfPCell(new Phrase(String.valueOf(accident.getPatients())));
                patientsCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                patientsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                patientsCell.setPaddingRight(4);
                table.addCell(patientsCell);
                PdfPCell commentCell = new PdfPCell(new Phrase(String.valueOf(accident.getComment())));
                commentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                commentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                commentCell.setPaddingRight(4);
                table.addCell(commentCell);
            }
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}

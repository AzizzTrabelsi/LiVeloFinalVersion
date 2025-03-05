package services;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.geom.PageSize;
import models.Facture;

import java.io.FileNotFoundException;

public class PDFservice {

    public PDFservice(Facture facture) {
        if (facture == null) {
            System.err.println("Error: Facture object is null.");
            return;
        }

        String path = "LiVeloBill_" + facture.getIdFacture() + ".pdf"; // Dynamic file name based on facture ID
        try {
            // Create PdfWriter instance
            PdfWriter pdfWriter = new PdfWriter(path);

            // Create PdfDocument instance
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);

            // Create Document instance (for adding content to the PDF)
            Document document = new Document(pdfDocument);

            // Add title
            Paragraph title = new Paragraph("LiveloBill #" + facture.getIdFacture())
                    .setFontSize(20)
                    .setBold();
            title.setMarginBottom(20);
            document.add(title);

            // Add facture details (ID, Amount, Date, Payment Type, Order ID)
            Table table = new Table(2); // 2 columns: one for the field and one for the value
            table.setWidth(500); // You can set the total width of the table (in points)

            // Add table rows for each facture detail
            table.addCell(new Cell().add(new Paragraph("Bill ID:")).setBold());
            table.addCell(new Cell().add(new Paragraph(String.valueOf(facture.getIdFacture()))));

            table.addCell(new Cell().add(new Paragraph("Amount:")).setBold());
            table.addCell(new Cell().add(new Paragraph(facture.getMontant() + " TND")));

            table.addCell(new Cell().add(new Paragraph("Date:")).setBold());
            table.addCell(new Cell().add(new Paragraph(String.valueOf(facture.getDatef()))));

            table.addCell(new Cell().add(new Paragraph("Payment Type:")).setBold());
            table.addCell(new Cell().add(new Paragraph(String.valueOf(facture.getTypePaiement()))));

            table.addCell(new Cell().add(new Paragraph("Order ID:")).setBold());
            table.addCell(new Cell().add(new Paragraph(String.valueOf(facture.getCommandeId()))));

            // Add table to document
            document.add(table);

            // Add footer
            Paragraph footer = new Paragraph("Thank you for your business!")
                    .setFontSize(12)
                    .setItalic();
            footer.setMarginTop(20);
            document.add(footer);

            // Close the document
            document.close();

            System.out.println("PDF created successfully at " + path);

        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

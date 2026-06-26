package com.smarterp.services;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.smarterp.entities.Voucher;
import com.smarterp.entities.VoucherItem;
import com.smarterp.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoicePdfService {

    private final VoucherRepository voucherRepository;

    @Transactional
    public byte[] generateInvoicePdf(Long voucherId) {
        Voucher voucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Header
            document.add(new Paragraph("SmartERP — GST Invoice")
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(" "));

            // Voucher Info
            document.add(new Paragraph("Invoice No: " + voucher.getVoucherNumber()).setBold());
            document.add(new Paragraph("Date: " + voucher.getVoucherDate()));
            document.add(new Paragraph("Customer: " + voucher.getLedger().getName()));
            document.add(new Paragraph(" "));

            // Items Table
            Table table = new Table(UnitValue.createPercentArray(
                    new float[]{30, 10, 15, 15, 15, 15}))
                    .setWidth(UnitValue.createPercentValue(100));

            // Table Headers
            String[] headers = {"Item Name", "Qty", "Rate", "Amount", "GST", "Total"};
            for (String header : headers) {
                table.addHeaderCell(new Cell()
                        .add(new Paragraph(header).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            // Table Rows
            List<VoucherItem> items = voucher.getItems();
            for (VoucherItem item : items) {
                table.addCell(item.getStockItem().getName());
                table.addCell(item.getQuantity().toString());
                table.addCell("₹" + item.getRate());
                table.addCell("₹" + item.getAmount());
                table.addCell(item.getGstRate() + "%");
                table.addCell("₹" + item.getTotalAmount());
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Totals
            document.add(new Paragraph("Subtotal: ₹" + voucher.getTotalAmount())
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("GST: ₹" + voucher.getTotalGst())
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Grand Total: ₹" + voucher.getGrandTotal())
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.RIGHT));

            if (voucher.getNotes() != null && !voucher.getNotes().isEmpty()) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Notes: " + voucher.getNotes()));
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
    }
}
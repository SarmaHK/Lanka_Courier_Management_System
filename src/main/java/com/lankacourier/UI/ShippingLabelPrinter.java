package com.lankacourier.UI;

import com.lankacourier.Model.Branch;
import com.lankacourier.Model.Customer;
import com.lankacourier.Model.Parcel;
import com.lankacourier.Util.FormatUtil;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;

/**
 * ShippingLabelPrinter compatible with PDFBox 3.x
 */
public class ShippingLabelPrinter {

    public static void generateLabel(
            Parcel p,
            Customer sender,
            Customer receiver,
            Branch origin,
            Branch dest,
            String filePath
    ) throws Exception {

        if (p == null) throw new IllegalArgumentException("Parcel cannot be null");

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A6);
            doc.addPage(page);

            // PDFBox 3.x fonts
            PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font monoBold = new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float marginLeft = 18;
                float y = page.getMediaBox().getHeight() - 28;

                // Title
                cs.beginText();
                cs.setFont(titleFont, 12);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText("Lanka Courier - Shipping Label");
                cs.endText();
                y -= 18;

                // Tracking ID
                cs.beginText();
                cs.setFont(regularFont, 10);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText("Tracking: " + FormatUtil.safe(p.getTrackingId()));
                cs.endText();
                y -= 14;

                // Sender
                String senderName = sender == null ? "" : FormatUtil.safe(sender.getContactPersonName());
                String senderAddr = sender == null ? "" : FormatUtil.safe(sender.getAddress());

                cs.beginText();
                cs.setFont(regularFont, 9);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText("From: " + senderName + " / " + senderAddr);
                cs.endText();
                y -= 12;

                // Receiver
                String recvName = receiver == null ? "" : FormatUtil.safe(receiver.getContactPersonName());
                String recvAddr = receiver == null ? "" : FormatUtil.safe(receiver.getAddress());

                cs.beginText();
                cs.setFont(regularFont, 9);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText("To: " + recvName + " / " + recvAddr);
                cs.endText();
                y -= 12;

                // Branch info
                cs.beginText();
                cs.setFont(regularFont, 9);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText(
                        "Origin: " + (origin == null ? "" : origin.getName()) +
                                "  Dest: " + (dest == null ? "" : dest.getName())
                );
                cs.endText();
                y -= 14;

                // Weight
                cs.beginText();
                cs.setFont(regularFont, 9);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText("Weight(kg): " +
                        (p.getWeightKg() == null ? "" : FormatUtil.formatWeight(p.getWeightKg())));
                cs.endText();
                y -= 12;

                // Price
                cs.beginText();
                cs.setFont(regularFont, 9);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText("Price(LKR): " +
                        (p.getPriceLkr() == null ? "" : FormatUtil.formatPrice(p.getPriceLkr())));
                cs.endText();
                y -= 12;

                // COD
                cs.beginText();
                cs.setFont(regularFont, 9);
                cs.newLineAtOffset(marginLeft, y);

                String codText = p.isCod()
                        ? "YES (Amt: " + FormatUtil.formatPrice(p.getCodAmountLkr()) + ")"
                        : "NO";

                cs.showText("COD: " + codText);
                cs.endText();
                y -= 16;

                // Barcode-like text
                cs.beginText();
                cs.setFont(monoBold, 12);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText("|| " + FormatUtil.safe(p.getTrackingId()) + " ||");
                cs.endText();
            }

            File out = new File(filePath);
            if (out.getParentFile() != null && !out.getParentFile().exists()) {
                out.getParentFile().mkdirs();
            }

            doc.save(out);
        }
    }
}

package com.lankacourier.Util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumericDocumentFilter extends DocumentFilter {

    private final boolean allowDecimal;
    private final int maxLength;

    public NumericDocumentFilter(boolean allowDecimal) {
        this(allowDecimal, -1);
    }

    public NumericDocumentFilter(boolean allowDecimal, int maxLength) {
        this.allowDecimal = allowDecimal;
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (string == null)
            return;
        if (isValid(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null)
            return;
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

        if (isValid(newText)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isValid(String text) {
        if (maxLength > 0 && text.length() > maxLength) {
            return false;
        }
        if (text.isEmpty())
            return true;
        if (allowDecimal) {
            // Allow digits and at most one dot
            return text.matches("\\d*\\.?\\d*");
        } else {
            // Allow only digits
            return text.matches("\\d*");
        }
    }
}

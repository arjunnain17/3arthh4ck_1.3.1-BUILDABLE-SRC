package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.ui.FlatTextFieldUI;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import javax.swing.JFormattedTextField;
import javax.swing.plaf.UIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

public class FlatCaret
extends DefaultCaret
implements UIResource {
    private final String selectAllOnFocusPolicy;
    private final boolean selectAllOnMouseClick;
    private boolean wasFocused;
    private boolean wasTemporaryLost;
    private boolean isMousePressed;

    public FlatCaret(String selectAllOnFocusPolicy, boolean selectAllOnMouseClick) {
        this.selectAllOnFocusPolicy = selectAllOnFocusPolicy;
        this.selectAllOnMouseClick = selectAllOnMouseClick;
    }

    @Override
    public void install(JTextComponent c) {
        int length;
        super.install(c);
        Document doc = c.getDocument();
        if (doc != null && this.getDot() == 0 && this.getMark() == 0 && (length = doc.getLength()) > 0) {
            this.setDot(length);
        }
    }

    @Override
    protected void adjustVisibility(Rectangle nloc) {
        Insets padding;
        JTextComponent c = this.getComponent();
        if (c != null && c.getUI() instanceof FlatTextFieldUI && (padding = ((FlatTextFieldUI)c.getUI()).getPadding()) != null) {
            nloc.x -= padding.left;
            nloc.y -= padding.top;
        }
        super.adjustVisibility(nloc);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (!(this.wasTemporaryLost || this.isMousePressed && !this.selectAllOnMouseClick)) {
            this.selectAllOnFocusGained();
        }
        this.wasTemporaryLost = false;
        this.wasFocused = true;
        super.focusGained(e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        this.wasTemporaryLost = e.isTemporary();
        super.focusLost(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.isMousePressed = true;
        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.isMousePressed = false;
        super.mouseReleased(e);
    }

    protected void selectAllOnFocusGained() {
        JTextComponent c = this.getComponent();
        Document doc = c.getDocument();
        if (doc == null || !c.isEnabled() || !c.isEditable()) {
            return;
        }
        Object selectAllOnFocusPolicy = c.getClientProperty("JTextField.selectAllOnFocusPolicy");
        if (selectAllOnFocusPolicy == null) {
            selectAllOnFocusPolicy = this.selectAllOnFocusPolicy;
        }
        if ("never".equals(selectAllOnFocusPolicy)) {
            return;
        }
        if (!"always".equals(selectAllOnFocusPolicy)) {
            int mark;
            if (this.wasFocused) {
                return;
            }
            int dot = this.getDot();
            if (dot != (mark = this.getMark()) || dot != doc.getLength()) {
                return;
            }
        }
        if (c instanceof JFormattedTextField) {
            EventQueue.invokeLater(() -> {
                this.setDot(0);
                this.moveDot(doc.getLength());
            });
        } else {
            this.setDot(0);
            this.moveDot(doc.getLength());
        }
    }

    public void scrollCaretToVisible() {
        JTextComponent c = this.getComponent();
        if (c == null || c.getUI() == null) {
            return;
        }
        try {
            Rectangle loc = c.getUI().modelToView(c, this.getDot(), this.getDotBias());
            if (loc != null) {
                this.adjustVisibility(loc);
                this.damage(loc);
            }
        }
        catch (BadLocationException badLocationException) {
            // empty catch block
        }
    }
}

package com.calculator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InputPanel extends JPanel {
    InputField inputField;

    public InputPanel(Dimension windowSize) {
        int height = (int) (windowSize.height * 0.20);

        Dimension panelSize = new Dimension(windowSize.width, height);
        this.setPreferredSize(panelSize);
        this.setBackground(Palette.BACKGROUND.getColor());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setLayout(null);

        this.inputField = new InputField(panelSize);
        this.add(this.inputField);
        this.add(new ResetButton(panelSize, this.inputField));
    }

    public InputField getInputField() {
        return this.inputField;
    }
}

class InputField extends JLabel {
    String content;
    int padding;

    public InputField(Dimension panelSize) {
        this.content = "";
        this.padding = 10;

        this.setFontByDecimal(2.5f);
        this.setBounds(10, 10, (int) (panelSize.width * 0.70), panelSize.height - 20);
        this.setBorder(BorderFactory.createLineBorder(Palette.FOREGROUND.getColor()));
        this.setHorizontalAlignment(SwingConstants.RIGHT);
        this.updateText();
        this.setForeground(Palette.FOREGROUND.getColor());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void updateText() {
        this.setText(String.format(
            "<html><div style='padding:%d'>%s</div></html>",
            this.padding,
            this.content
        ));
    }

    public void setFontByDecimal(float fontSize) {
        Font currentFont = this.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * fontSize);
        this.setFont(newFont);
    }
}

class ResetButton extends JButton {
    public ResetButton(Dimension panelSize, InputField inputField) {
        super("C");
        this.setBorder(BorderFactory.createLineBorder(Palette.FOREGROUND.getColor()));
        this.setBackground(Palette.BACKGROUND.getColor());
        this.setForeground(Palette.FOREGROUND.getColor());
        this.addActionListener(new ResetButtonListener(inputField));
        this.setFontByDecimal(2.5f);
        this.setBounds(
            (int) (panelSize.width * 0.70) + 20,
            10,
            (int) (panelSize.width * 0.20),
            panelSize.height - 20
        );
    }

    public void setFontByDecimal(float fontSize) {
        Font currentFont = this.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * fontSize);
        this.setFont(newFont);
    }
}

class ResetButtonListener implements ActionListener {
    InputField inputField;

    public ResetButtonListener(InputField inputField) {
        this.inputField = inputField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        inputField.setContent("");
        inputField.updateText();
    }
}

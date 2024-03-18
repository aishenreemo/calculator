package com.calculator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsPanel extends JPanel {
    public ButtonsPanel(Dimension windowSize, InputField inputField) {
        int height = (int) (windowSize.height * 0.80);

        this.setPreferredSize(new Dimension(windowSize.width, height));
        this.setBackground(Palette.BACKGROUND.getColor());
        this.setLayout(new GridLayout(4, 4, 10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String buttonLabels[] = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
        };

        for (int i = 0; i < 16; i++) {
            CalculatorButton button = new CalculatorButton(buttonLabels[i], inputField);
            this.add(button);
        }
    }
}

class CalculatorButton extends JButton {
    public CalculatorButton(String buttonLabel, InputField inputField) {
        super(buttonLabel);
        this.setBorder(BorderFactory.createLineBorder(Palette.FOREGROUND.getColor()));
        this.addActionListener(new CalculatorButtonListener(inputField, buttonLabel));
        this.setBackground(Palette.BACKGROUND.getColor());
        this.setForeground(Palette.FOREGROUND.getColor());
        this.setFontByDecimal(2.5f);
    }

    public void setFontByDecimal(float fontSize) {
        Font currentFont = this.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * fontSize);
        this.setFont(newFont);
    }
}

class CalculatorButtonListener implements ActionListener {
    InputField inputField;
    String buttonLabel;

    public CalculatorButtonListener(InputField inputField, String buttonLabel) {
        this.inputField = inputField;
        this.buttonLabel = buttonLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (buttonLabel != "=") {
            inputField.setContent(String.format("%s%s", inputField.getContent(), buttonLabel));
            inputField.updateText();
            return;
        }

        try {
            double result = CalculatorButtonListener.parse(inputField.getContent());
            inputField.setContent(String.format("%.2f", result));
        } catch (Exception err) {
            inputField.setContent("INVALID EXPR!");
        }

        inputField.updateText();
        inputField.setContent("");
    }

    public static double parse(String expression) {
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                operandStack.push(Double.parseDouble(sb.toString()));
                i--;
            } else if (isOperator(c)) {
                while (!operatorStack.isEmpty() && hasPrecedence(operatorStack.peek(), c)) {
                    Double operand2 = operandStack.pop();
                    Double operand1 = operandStack.pop();
                    char operator = operatorStack.pop();
                    Double result = performOperation(operand1, operand2, operator);
                    operandStack.push(result);
                }
                operatorStack.push(c);
            }
        }

        while (!operatorStack.isEmpty()) {
            Double operand2 = operandStack.pop();
            Double operand1 = operandStack.pop();
            char operator = operatorStack.pop();
            Double result = performOperation(operand1, operand2, operator);
            operandStack.push(result);
        }

        return operandStack.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static boolean hasPrecedence(char op1, char op2) {
        return (op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-');
    }

    private static Double performOperation(Double operand1, Double operand2, char operator) {
        if (operator == '+') {
            return operand1 + operand2;
        } else if (operand2 == '-') {
            return operand1 - operand2;
        } else if (operand2 == '*') {
            return operand1 * operand2;
        }

        if (operand2 == 0) {
            throw new ArithmeticException("Division by zero");
        }

        return operand1 / operand2;
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class Calculator implements ActionListener, KeyListener {

    private JFrame frame;
    private JTextField displayField;
    private JButton[] numberButtons = new JButton[10];
    private JButton[] functionButtons = new JButton[10];
    private JButton addButton, subButton, mulButton, divButton;
    private JButton decButton, equButton, delButton, clrButton, negButton, sqrtButton;
    private JPanel panel;
    private JPanel historyPanel;
    private JTextArea historyArea;

    private Font mainFont = new Font("Ink Free", Font.BOLD, 24);
    private Font displayFont = new Font("Ink Free", Font.BOLD, 30);
    private Color backgroundColor = UIManager.getColor("Panel.background");
    private Color operatorColor = UIManager.getColor("Button.background");
    private Color numberColor = UIManager.getColor("Button.background");
    private Color equalsColor = UIManager.getColor("Button.background");
    private Color clearColor = UIManager.getColor("Button.background");

    private double num1 = 0, num2 = 0, result = 0;
    private char operator;
    private boolean isOperatorClicked = false;
    private DecimalFormat formatter = new DecimalFormat("#.##########");
    private StringBuilder history = new StringBuilder();

    public Calculator() {
        initializeFrame();
        initializeDisplayField();
        initializeFunctionButtons();
        initializeNumberButtons();
        createButtonPanel();
        createHistoryPanel();

        frame.add(historyPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("Modern Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(backgroundColor);
        frame.setResizable(true);
    }

    private void initializeDisplayField() {
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        displayPanel.setBackground(backgroundColor);

        displayField = new JTextField("0");
        displayField.setFont(displayFont);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setBackground(Color.WHITE);
        displayField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 10)));
        displayField.addKeyListener(this);

        displayPanel.add(displayField, BorderLayout.CENTER);
        frame.add(displayPanel, BorderLayout.NORTH);
    }

    private void initializeFunctionButtons() {
        addButton = createButton("+", operatorColor);
        subButton = createButton("-", operatorColor);
        mulButton = createButton("×", operatorColor);
        divButton = createButton("÷", operatorColor);
        decButton = createButton(".", numberColor);
        equButton = createButton("=", equalsColor);
        delButton = createButton("Del", clearColor);
        clrButton = createButton("C", clearColor);
        negButton = createButton("(-)", operatorColor);
        sqrtButton = createButton("Sqrt", operatorColor);

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;
        functionButtons[8] = negButton;
        functionButtons[9] = sqrtButton;

        for (JButton button : functionButtons) {
            button.addActionListener(this);
            button.setFocusable(false);
        }
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(mainFont);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        return button;
    }

    private void initializeNumberButtons() {
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createButton(String.valueOf(i), numberColor);
            numberButtons[i].addActionListener(this);
        }
    }

    private void createButtonPanel() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 8, 8));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Row 1
        panel.add(clrButton);
        panel.add(delButton);
        panel.add(negButton);
        panel.add(sqrtButton);

        // Row 2
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(divButton);

        // Row 3
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(mulButton);

        // Row 4
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(subButton);

        // Row 5
        panel.add(numberButtons[0]);
        panel.add(decButton);
        panel.add(equButton);
        panel.add(addButton);

        frame.add(panel, BorderLayout.CENTER);
    }

    private void createHistoryPanel() {
        historyPanel = new JPanel(new BorderLayout());
        historyPanel.setPreferredSize(new Dimension(180, 0));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 15));
        historyPanel.setBackground(backgroundColor);

        JLabel historyLabel = new JLabel("History");
        historyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        historyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton clearHistoryButton = new JButton("Clear History");
        clearHistoryButton.addActionListener(e -> {
            historyArea.setText("");
            history = new StringBuilder();
        });
        clearHistoryButton.setFocusable(false);

        historyPanel.add(historyLabel, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.add(clearHistoryButton, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle number buttons
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                handleNumberInput(i);
                return;
            }
        }


        if (e.getSource() == decButton) {
            handleDecimalInput();
            return;
        }


        if (e.getSource() == addButton) {
            handleOperator('+');
        } else if (e.getSource() == subButton) {
            handleOperator('-');
        } else if (e.getSource() == mulButton) {
            handleOperator('*');
        } else if (e.getSource() == divButton) {
            handleOperator('/');
        } else if (e.getSource() == equButton) {
            calculateResult();
        } else if (e.getSource() == clrButton) {
            clearCalculator();
        } else if (e.getSource() == delButton) {
            handleDelete();
        } else if (e.getSource() == negButton) {
            handleNegation();
        } else if (e.getSource() == sqrtButton) {
            handleSquareRoot();
        }
    }

    private void handleNumberInput(int num) {
        if (isOperatorClicked) {
            displayField.setText(String.valueOf(num));
            isOperatorClicked = false;
        } else {
            String currentText = displayField.getText();
            if (currentText.equals("0")) {
                displayField.setText(String.valueOf(num));
            } else {
                displayField.setText(currentText + num);
            }
        }
    }

    private void handleDecimalInput() {
        if (isOperatorClicked) {
            displayField.setText("0.");
            isOperatorClicked = false;
        } else {
            String currentText = displayField.getText();
            if (!currentText.contains(".")) {
                displayField.setText(currentText + ".");
            }
        }
    }

    private void handleOperator(char op) {
        try {
            if (!isOperatorClicked) {
                num1 = Double.parseDouble(displayField.getText());
            }
            operator = op;
            isOperatorClicked = true;

            String displayOp;
            switch(op) {
                case '+'-> displayOp = "+";
                case '-'-> displayOp = "-";
                case '*'->displayOp = "×";
                case '/'->displayOp = "÷";
                default-> displayOp = String.valueOf(op);
            }

            history.append(formatter.format(num1)).append(" ").append(displayOp).append(" ");
        } catch (NumberFormatException ex) {
            displayField.setText("Error");
        }
    }

    private void calculateResult() {
        try {
            if (!isOperatorClicked) {
                num2 = Double.parseDouble(displayField.getText());

                // Add the second number to history
                history.append(formatter.format(num2)).append(" = ");

                switch (operator) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 == 0) {
                            displayField.setText("Cannot divide by zero");
                            history.append("Error\n");
                            historyArea.setText(history.toString());
                            return;
                        }
                        result = num1 / num2;
                        break;
                }

                // Format and display the result
                String formattedResult = formatResult(result);
                displayField.setText(formattedResult);

                // Complete the history entry
                history.append(formattedResult).append("\n");
                historyArea.setText(history.toString());

                // Reset for next calculation
                num1 = result;
                isOperatorClicked = true;
            }
        } catch (NumberFormatException ex) {
            displayField.setText("Error");
        }
    }

    private String formatResult(double result) {
        // Remove trailing zeros for whole numbers
        if (result == (long) result) {
            return String.valueOf((long) result);
        } else {
            return formatter.format(result);
        }
    }

    private void clearCalculator() {
        displayField.setText("0");
        num1 = num2 = result = 0;
        isOperatorClicked = false;
    }

    private void handleDelete() {
        String currentText = displayField.getText();
        if (currentText.length() > 0 && !currentText.equals("0")) {
            if (currentText.length() == 1) {
                displayField.setText("0");
            } else {
                displayField.setText(currentText.substring(0, currentText.length() - 1));
            }
        }
    }

    private void handleNegation() {
        try {
            double value = Double.parseDouble(displayField.getText());
            value *= -1;
            displayField.setText(formatResult(value));
        } catch (NumberFormatException ex) {
            displayField.setText("Error");
        }
    }

    private void handleSquareRoot() {
        try {
            double value = Double.parseDouble(displayField.getText());
            if (value < 0) {
                displayField.setText("Invalid input");
                return;
            }

            double sqrtValue = Math.sqrt(value);
            history.append("√(").append(formatter.format(value)).append(") = ");

            String formattedResult = formatResult(sqrtValue);
            displayField.setText(formattedResult);

            history.append(formattedResult).append("\n");
            historyArea.setText(history.toString());

            num1 = sqrtValue;
            isOperatorClicked = true;
        } catch (NumberFormatException ex) {
            displayField.setText("Error");
        }
    }

    // KeyListener methods for keyboard input
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        // Handle number keys
        if (Character.isDigit(c)) {
            handleNumberInput(Character.getNumericValue(c));
        }

        // Handle operator keys
        switch (c) {
            case '+':
                handleOperator('+');
                break;
            case '-':
                handleOperator('-');
                break;
            case '*':
                handleOperator('*');
                break;
            case '/':
                handleOperator('/');
                break;
            case '.':
                handleDecimalInput();
                break;
            case '=':
            case '\n':
                calculateResult();
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Handle special keys
        if (keyCode == KeyEvent.VK_BACK_SPACE) {
            handleDelete();
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            clearCalculator();
        } else if (keyCode == KeyEvent.VK_ENTER) {
            calculateResult();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed but required by KeyListener interface
    }

    public static void main(String[] args) {
        try {
            // Set look and feel to the system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Calculator();
        });
    }
}
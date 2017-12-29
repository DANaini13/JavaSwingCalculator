package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




class Calculator extends JFrame {


    private static class ScreenView extends JPanel {

        boolean pendingDot = false;
        boolean containsDot = false;

        private JLabel screenText       = new JLabel();
        private JLabel operatingBoard   = new JLabel();

        ScreenView() {
            this.setLayout(new GridLayout(2, 1));
            screenText.setText("Description");
            screenText.setForeground(new Color(68, 68, 68));
            screenText.setFont(new Font("Black", Font.PLAIN, 18));
            this.add(screenText);
            operatingBoard.setText("0");
            operatingBoard.setFont(new Font("Black", Font.PLAIN, 32));
            this.add(operatingBoard);
        }

        void appendNumber(int number) {
            if(pendingDot) {
                operatingBoard.setText(operatingBoard.getText() + "." + Integer.toString(number));
                pendingDot = false;
                containsDot = true;
            } else {
                operatingBoard.setText(operatingBoard.getText() + Integer.toString(number));
            }
        }

        void replaceNumber(double number) {
            if(number == (int) number) {
                operatingBoard.setText((Integer.toString((int) number)));
            }
            else {
                operatingBoard.setText(Double.toString(number));
                containsDot = true;
            }
        }

        double getNumber() {
            return Double.parseDouble(operatingBoard.getText());
        }

        void setDot() {
            if(!containsDot)
                pendingDot = true;
        }

        void reset() {
            screenText.setText("Description");
            operatingBoard.setText("0");
            pendingDot = false;
            containsDot = false;
        }
    }

    private static class KeyboardView extends JPanel {

        boolean userInTheMiddleOfInput = false;

        KeyboardView() {
            this.setLayout(new GridLayout(5, 4));
            initKeys();
        }

        private void initKeys() {
            JButton numberKeys[] = new JButton[10];
            for(int i=0; i<10; ++i) {
                numberKeys[i]= new JButton(Integer.toString(i));
                numberKeys[i].setForeground(new Color(106, 106, 106));
                numberKeys[i].setFont(new Font("Black", Font.PLAIN, 20));

                int finalI = i;
                numberKeys[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(userInTheMiddleOfInput)
                            screenView.appendNumber(finalI);
                        else
                            screenView.replaceNumber(finalI);
                        userInTheMiddleOfInput = true;
                    }
                });
            }

            final int operationsNumber = 8;
            JButton operations[] = new JButton[operationsNumber];
            for(int i = 0; i<operationsNumber; ++i) {
                operations[i] = new JButton("");
                operations[i].setForeground(new Color(73, 118, 255));
                operations[i].setFont(new Font("Black", Font.PLAIN, 20));
                int finalI = i;
                operations[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        brain.setOperand(screenView.getNumber());
                        brain.performOperation(operations[finalI].getText());
                        screenView.replaceNumber(brain.accumulator);
                        userInTheMiddleOfInput = false;
                    }
                });
            }

            JButton clearButton = new JButton("C");
            clearButton.setForeground(new Color(255, 69, 0));
            clearButton.setFont(new Font("Black", Font.PLAIN, 20));
            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    brain.reset();
                    screenView.reset();
                    userInTheMiddleOfInput = false;
                }
            });

            JButton dotButton = new JButton(".");
            dotButton.setFont(new Font("Black", Font.PLAIN, 20));
            dotButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    screenView.setDot();
                }
            });

            operations[0].setText("π");
            operations[1].setText("%");
            operations[2].setText("÷");
            operations[3].setText("×");
            operations[4].setText("-");
            operations[5].setText("+");
            operations[6].setText("+/-");
            operations[7].setText("=");

            this.add(clearButton);
            this.add(operations[0]);  // pi
            this.add(operations[1]);  // %
            this.add(operations[2]);  // /

            this.add(numberKeys[7]);
            this.add(numberKeys[8]);
            this.add(numberKeys[9]);
            this.add(operations[3]);  // x

            this.add(numberKeys[4]);
            this.add(numberKeys[5]);
            this.add(numberKeys[6]);
            this.add(operations[4]);  // -

            this.add(numberKeys[1]);
            this.add(numberKeys[2]);
            this.add(numberKeys[3]);
            this.add(operations[5]);  // +

            this.add(operations[6]);  // +/-
            this.add(numberKeys[0]);
            this.add(dotButton);
            this.add(operations[7]);

        }
    }

    static private CalculatorBrain brain        = new CalculatorBrain();
    static private ScreenView screenView        = new ScreenView();
    static private KeyboardView keyboardView    = new KeyboardView();


    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame mainFrame = new JFrame("Calculator");
        initializeMainFrame(mainFrame);

        JPanel mainPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();

        mainPanel.add(screenView);

        mainPanel.add(keyboardView);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 0.4;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        layout.setConstraints(screenView, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 0.6;
        layout.setConstraints(keyboardView, constraints);
        mainPanel.setLayout(layout);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private static void initializeMainFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final int height = CalculatorArguments.calculatorHeight;
        final int width = CalculatorArguments.calculatorWidth;
        frame.setSize(height, width);
    }


}


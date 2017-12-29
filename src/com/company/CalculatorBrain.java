package com.company;

import java.util.HashMap;

public class CalculatorBrain {

    double accumulator = 0;
    PendingBinaryOperation pbo = null;

    class PendingBinaryOperation {
        final double number;
        final BinaryFunction operation;

        PendingBinaryOperation(double number, BinaryFunction operation) {
            this.number = number;
            this.operation = operation;
        }

        double performPending(double number) {
            return operation.function(this.number, number);
        }
    }

    private interface UnaryFunction {
         double function(double number);
    }

    private interface BinaryFunction {
        double function(double number1, double number2);
    }

    private class Operation {
    }

    private class Constant extends Operation {
        final double value;

        Constant(Double value) {
            this.value = value;
        }
    }

    private class UnaryOperation extends Operation {
        final UnaryFunction operation;

        UnaryOperation(UnaryFunction operation) {
            this.operation = operation;
        }
    }

    private class BinaryOperation extends Operation {
        final BinaryFunction operation;

        BinaryOperation(BinaryFunction operation) {
            this.operation = operation;
        }
    }

    private HashMap<String, Operation> operationHashMap = new HashMap<String, Operation>();

    private void initOperationMap() {
        operationHashMap.put("π", new Constant( 3.141592657));

        operationHashMap.put("+/-", new UnaryOperation((double number) -> number * -1));

        operationHashMap.put("+", new BinaryOperation((double number1, double number2) -> number1 + number2));
        operationHashMap.put("-", new BinaryOperation((double number1, double number2) -> number1 - number2));
        operationHashMap.put("×", new BinaryOperation((double number1, double number2) -> number1 * number2));
        operationHashMap.put("÷", new BinaryOperation((double number1, double number2) -> number1 / number2));
        operationHashMap.put("%", new BinaryOperation((double number1, double number2) -> number1 % number2));
    }

    CalculatorBrain() {
        initOperationMap();
    }

    void setOperand(double number) {
        accumulator = number;
    }

    void performOperation(String symbol) {

        if(symbol.equals("=")) {
            accumulator = pbo.performPending(accumulator);
            pbo = null;
            return;
        }

        Operation operation = operationHashMap.get(symbol);
        if(operation == null) {
            System.err.println("unknown operation!");
            System.exit(-1);
        }

        if(operation instanceof Constant) {
            accumulator = ((Constant) operation).value;
        } else if(operation instanceof UnaryOperation) {
            accumulator = ((UnaryOperation) operation).operation.function(accumulator);
        } else if(operation instanceof BinaryOperation) {
            if (pbo == null) {
                pbo = new PendingBinaryOperation(accumulator, ((BinaryOperation) operation).operation);
            }
        }
    }

    void reset() {
        pbo = null;
        accumulator = 0;
    }
}

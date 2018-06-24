package com.bitmain.intelligent.tax.policy;

public interface CalculateStage {





    boolean inScope(float salary);



    Result stageResult(Chain calculateChain,float salary);




    class Result{

        float taxResult;
        int start;
        int end;
        float taxRate;

        public float getTaxResult() {
            return taxResult;
        }

        public void setTaxResult(float taxResult) {
            this.taxResult = taxResult;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public float getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(float taxRate) {
            this.taxRate = taxRate;
        }
    }

}

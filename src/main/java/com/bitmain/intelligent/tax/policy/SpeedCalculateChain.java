package com.bitmain.intelligent.tax.policy;

import java.util.ArrayList;
import java.util.List;

public class SpeedCalculateChain implements Chain {

    private List<CalculateStage> calculateStages = new ArrayList<>();

    private int index = 0;

    private SpeedCalculateChain addCalculateStage(CalculateStage calculateStage) {
        calculateStages.add(calculateStage);
        return this;
    }

    @Override
    public int stageProcess(int salary) {

        if (index >= calculateStages.size()) {
            return -1;
        }
        CalculateStage calculateStage = calculateStages.get(index++);

        if (!calculateStage.inScope(salary)) {
            return stageProcess(salary);
        }
        CalculateStage.Result result = calculateStage.stageResult(this, salary);
        return (int) result.taxResult;
    }


    public static SpeedCalculateChain standardChain() {
        Builder builder = new Builder();
        builder.addCalculateStage(new SpeedCalculateStage(0, 1500, 0.03f, 0));
        builder.addCalculateStage(new SpeedCalculateStage(1500, 4500, .1f, 105));
        builder.addCalculateStage(new SpeedCalculateStage(4500, 9000, .2f, 555));
        builder.addCalculateStage(new SpeedCalculateStage(9000, 35000, .25f, 1005));
        builder.addCalculateStage(new SpeedCalculateStage(35000, 55000, .3f, 2755));
        builder.addCalculateStage(new SpeedCalculateStage(55000, 80000, .35f, 5505));
        builder.addCalculateStage(new SpeedCalculateStage(80000, Integer.MAX_VALUE, .45f, 13505));
        return builder.build();
    }

    public static SpeedCalculateChain latestChain(){
        Builder builder = new Builder();
        builder.addCalculateStage(new SpeedCalculateStage(0, 3000, 0.03f, 0));
        builder.addCalculateStage(new SpeedCalculateStage(3000, 12000, .1f, 210));
        builder.addCalculateStage(new SpeedCalculateStage(12000, 25000, .2f, 1410));
        builder.addCalculateStage(new SpeedCalculateStage(25000, 35000, .25f, 2660));
        builder.addCalculateStage(new SpeedCalculateStage(35000, 55000, .3f, 4410));
        builder.addCalculateStage(new SpeedCalculateStage(55000, 80000, .35f, 7160));
        builder.addCalculateStage(new SpeedCalculateStage(80000, Integer.MAX_VALUE, .45f, 15160));
        return builder.build();
    }


    public static class Builder {
        SpeedCalculateChain speedCalculateChain;

        public Builder() {
            speedCalculateChain = new SpeedCalculateChain();
        }

        public Builder addCalculateStage(SpeedCalculateStage speedCalculateStage) {
            speedCalculateChain.addCalculateStage(speedCalculateStage);
            return this;
        }

        public SpeedCalculateChain build() {
            return speedCalculateChain;
        }
    }


    public static class SpeedCalculateStage implements CalculateStage {
        private int start;
        private int end;
        private float rate;
        private int speedMinNumber;

        public SpeedCalculateStage(int start, int end, float rate, int speedMinNumber) {
            this.start = start;
            this.end = end;
            this.rate = rate;
            this.speedMinNumber = speedMinNumber;
        }


        @Override
        public boolean inScope(float salary) {
            return salary >= start && salary <= end;
        }

        @Override
        public Result stageResult(Chain calculateChain, float salary) {
            SpeedCalculateResult speedCalculateResult = new SpeedCalculateResult();
            speedCalculateResult.speedNumber = speedMinNumber;
            speedCalculateResult.start = start;
            speedCalculateResult.end = end;
            speedCalculateResult.taxRate = rate;
            speedCalculateResult.taxResult = salary * rate - speedMinNumber;
            return speedCalculateResult;
        }
    }

    public static class SpeedCalculateResult extends CalculateStage.Result {
        private int speedNumber;
    }
}

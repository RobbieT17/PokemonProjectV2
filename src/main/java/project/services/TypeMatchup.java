package project.services;

public class TypeMatchup {
    
    private final double[] matchupsDefense;
    private final double[] matchupsOffense;
    private final double averageDefense;
    private final double averageOffense;

    private double average(double[] matchups) {
        if (matchups == null) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (double value : matchups) {sum += value;}
        return sum / (double) matchups.length;
    }

    public TypeMatchup(double[] defArr, double[] offArr) {
        this.matchupsDefense = defArr;
        this.matchupsOffense = offArr;
        this.averageDefense = average(defArr);
        this.averageOffense = average(offArr);
    }

    public double[] getMatchupsDefense() {return this.matchupsDefense;}
    public double[] getMatchupsOffense() {return this.matchupsOffense;}
    public double getAverageDefense() {return this.averageDefense;}
    public double getAverageOffense() {return this.averageOffense;}
    
}

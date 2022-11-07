/*
 * SimpleT1FLS.java
 *
 * Created on May 20th 2012
 *
 * Copyright 2012 Christian Wagner All Rights Reserved.
 */
package examples;

import generic.Input;
import generic.Output;
import generic.Tuple;
import tools.JMathPlotter;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Trapezoidal;
import type1.sets.T1MF_Triangular;
import type1.system.T1_Antecedent;
import type1.system.T1_Consequent;
import type1.system.T1_Rule;
import type1.system.T1_Rulebase;

/**
 * A simple example of a type-1 FLS based on the "How much to tip the waiter"
 *  scenario.
 * We have two inputs: food quality and service level and as an output we would
 * like to generate the applicable tip.
 * @author Christian Wagner
 */
public class Dianogsis
{
    Input calcification, mass;    //the inputs to the FLS
    Output impression;             //the output of the FLS
    T1_Rulebase rulebase;   //the rulebase captures the entire FLS

    public Dianogsis()
    {
        //Define the inputs
        calcification = new Input("Calcification", new Tuple(0,10));      //a rating given by a person between 0 and 10
        mass = new Input("Mass", new Tuple(0,30));  //a rating given by a person between 0 and 10
        impression = new Output("Impression", new Tuple(-20,20));               //a percentage for the tip

        // Calc Trapeziod array
        double[] lessCalcX = {0,0,2.0,4.0};
        double[] lessCalcY = {1,1,0,0};
        double[] moreCalcX = {6.0,8.0,10.0,10.0};
        double[] moreCalcY = {1,1,0,0};
        //Set up the membership functions (MFs) for each input and output
        T1MF_Trapezoidal lessCalcificationTrapezoidal = new T1MF_Trapezoidal("MF for several calcification", lessCalcX,lessCalcY);
        T1MF_Triangular severalCalcificatioTriangular = new T1MF_Triangular("MF for several calcification",2.0, 5.0, 8.0);
        T1MF_Trapezoidal moreCalcificationTrapezoidal = new T1MF_Trapezoidal("MF for several calcification", moreCalcX,moreCalcY);

        // mass Trapeziod array
        double[] smallMassX = {0,0,4.0,12.0};
        double[] smallMassY = {1,1,0,0};
        double[] largeMassX = {17.0,25.0,30.0,30.0};
        double[] largeMassY = {1,1,0,0};
        //Set up the membership functions (MFs) for each input and output
        T1MF_Trapezoidal smallMassTrapezoidal = new T1MF_Trapezoidal("MF for small mass", smallMassX,smallMassY);
        T1MF_Triangular mediumMassTriangular = new T1MF_Triangular("MF for medium mass",5.0, 15.0,25.0);
        T1MF_Trapezoidal largeMassTrapezoidal = new T1MF_Trapezoidal("MF for large mass", largeMassX,largeMassY);

        // setup impression functions
        T1MF_Triangular benignTriangular = new T1MF_Triangular("Benign", -20, -10, 0);
        T1MF_Triangular suspiciousTriangular = new T1MF_Triangular("Suspicious Maglinant", -10, 0, 10);
        T1MF_Triangular malignantTriangular = new T1MF_Triangular("Malignant", 0, 10, 20);


        //Set up the antecedents and consequents - note how the inputs are associated...
        T1_Antecedent lessCalcificatioAntecedent = new T1_Antecedent("LessCalc",lessCalcificationTrapezoidal, calcification);
        T1_Antecedent severalCalcificatioAntecedent = new T1_Antecedent("SeveralCalc",severalCalcificatioTriangular, calcification);
        T1_Antecedent moreCalcificatioAntecedent = new T1_Antecedent("MoreCalc",moreCalcificationTrapezoidal, calcification);

        T1_Antecedent smallMassAntecedent = new T1_Antecedent("smallMass",smallMassTrapezoidal, mass);
        T1_Antecedent mediumMassAntecedent = new T1_Antecedent("mediumMass",mediumMassTriangular, mass);
        T1_Antecedent largeMassAntecedent = new T1_Antecedent("largeMass",largeMassTrapezoidal, mass);

        // Consequents (impression)
        T1_Consequent benignConsequent = new T1_Consequent("Benign",benignTriangular, impression);
        T1_Consequent suspiciousConsequent = new T1_Consequent("Suspicious Malignant", suspiciousTriangular, impression);
        T1_Consequent malignantConsequent = new T1_Consequent("Malignant",malignantTriangular, impression);

        //Set up the rulebase and add rules
        rulebase = new T1_Rulebase(9);
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lessCalcificatioAntecedent, smallMassAntecedent}, benignConsequent));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lessCalcificatioAntecedent,mediumMassAntecedent},benignConsequent));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lessCalcificatioAntecedent,largeMassAntecedent},benignConsequent));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severalCalcificatioAntecedent,smallMassAntecedent},suspiciousConsequent));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severalCalcificatioAntecedent,mediumMassAntecedent},suspiciousConsequent));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severalCalcificatioAntecedent,largeMassAntecedent},suspiciousConsequent));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moreCalcificatioAntecedent,smallMassAntecedent},malignantConsequent));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moreCalcificatioAntecedent,mediumMassAntecedent},malignantConsequent));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moreCalcificatioAntecedent,largeMassAntecedent},malignantConsequent));

        //just an example of setting the discretisation level of an output - the usual level is 100
        impression.setDiscretisationLevel(50);

        //get some outputs
        getImpression(7, 15);

        //plot some sets, discretizing each input into 100 steps.
        plotMFs("Calcification Membership Functions", new T1MF_Interface[]{lessCalcificationTrapezoidal, severalCalcificatioTriangular,moreCalcificationTrapezoidal}, calcification.getDomain(), 100);
        plotMFs("Mass Membership Functions", new T1MF_Interface[]{smallMassTrapezoidal,mediumMassTriangular,largeMassTrapezoidal}, mass.getDomain(), 100);
        plotMFs("Impression Membership Functions", new T1MF_Interface[]{benignTriangular,suspiciousTriangular,malignantTriangular}, impression.getDomain(), 100);

        //plot control surface
        //do either height defuzzification (false) or centroid d. (true)
        plotControlSurface(true, 100, 100);

        //print out the rules
        System.out.println("\n"+rulebase);
    }

    /**
     * Basic method that prints the output for a given set of inputs.
     * @param foodQuality
     * @param serviceLevel
     */
    private void getImpression(double calcLevel, double massLevel)
    {
        //first, set the inputs
        calcification.setInput(calcLevel);
        mass.setInput(massLevel);
        //now execute the FLS and print output
        System.out.println("The calcification was: "+calcification.getInput());
        System.out.println("The mass was: "+mass.getInput());
        System.out.println("Using height defuzzification, the FLS recommends an impression of "
                + rulebase.evaluate(0).get(impression));
        System.out.println("Using centroid defuzzification, the FLS recommends an impression of "
                + rulebase.evaluate(1).get(impression));
    }

    private void plotMFs(String name, T1MF_Interface[] sets, Tuple xAxisRange, int discretizationLevel)
    {
        JMathPlotter plotter = new JMathPlotter(17,17,15);
        for (int i=0;i<sets.length;i++)
        {
            plotter.plotMF(sets[i].getName(), sets[i], discretizationLevel, xAxisRange, new Tuple(0.0,1.0), false);
        }
        plotter.show(name);
    }

    private void plotControlSurface(boolean useCentroidDefuzzification, int input1Discs, int input2Discs)
    {
        double output;
        double[] x = new double[input1Discs];
        double[] y = new double[input2Discs];
        double[][] z = new double[y.length][x.length];
        double incrX, incrY;
        incrX = calcification.getDomain().getSize()/(input1Discs-1.0);
        incrY = mass.getDomain().getSize()/(input2Discs-1.0);

        //first, get the values
        for(int currentX=0; currentX<input1Discs; currentX++)
        {
            x[currentX] = currentX * incrX;
        }
        for(int currentY=0; currentY<input2Discs; currentY++)
        {
            y[currentY] = currentY * incrY;
        }

        for(int currentX=0; currentX<input1Discs; currentX++)
        {
            calcification.setInput(x[currentX]);
            for(int currentY=0; currentY<input2Discs; currentY++)
            {
                mass.setInput(y[currentY]);
                if(useCentroidDefuzzification)
                    output = rulebase.evaluate(1).get(impression);
                else
                    output = rulebase.evaluate(0).get(impression);
                z[currentY][currentX] = output;
            }
        }

        //now do the plotting
        JMathPlotter plotter = new JMathPlotter(17, 17, 14);
        plotter.plotControlSurface("Control Surface",
                new String[]{calcification.getName(), mass.getName(), "Impression"}, x, y, z, new Tuple(0.0,30.0), true);
       plotter.show("Type-1 Fuzzy Logic System Control Surface for Breast Cancer Mammogram Diagnosis System Example");
    }

    public static void main (String args[])
    {
        new Dianogsis();
    }
}

package una.pdi.AWFGA.ga.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.component.GenerateBoxplotsWithR;
import org.uma.jmetal.util.experiment.component.GenerateFriedmanTestTables;
import org.uma.jmetal.util.experiment.component.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.util.experiment.component.GenerateWilcoxonTestTablesWithR;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

import una.pdi.AWFGA.ga.model.FiltroProblema;

public class ExperimentRunner {

	
	public static void main(String[] args) {
		
		
		
		
		
		 
	}
	
	public static void run(Properties p) throws IOException{
		int objectives=Integer.parseInt(p.getProperty("ALGORITM_GA_OBJETIVES"));
	    int filterColumnSize=Integer.parseInt(p.getProperty("FILTER_COLUMN_SIZE"));
	    List<ExperimentProblem<IntegerSolution>> problemList = new ArrayList<>();
	    problemList.add(new ExperimentProblem<>(new FiltroProblema(filterColumnSize, objectives),"FiltroMedianaAdaptativo"));
	    List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> algorithmList =
	            configureAlgorithmList(problemList.get(0),p);
	
	    List<String> referenceFrontFileNames = Arrays.asList("GA1.pf");
	    Experiment<IntegerSolution,List<IntegerSolution>> experiment =
	            new ExperimentBuilder<IntegerSolution, List<IntegerSolution>>("GA_Study")
	                    .setAlgorithmList(algorithmList)
	                    .setProblemList(problemList)
	                    .setExperimentBaseDirectory(p.getProperty("EXPERIMENT_BASE_DIRECTORY"))
	                    .setOutputParetoFrontFileName("FUN")
	                    .setOutputParetoSetFileName("VAR")
	                    .setReferenceFrontDirectory("/pareto_fronts")
	                    .setReferenceFrontFileNames(referenceFrontFileNames)
	                    .setIndicatorList(Arrays.asList(
	                            new Epsilon<IntegerSolution>(),
	                            new Spread<IntegerSolution>(),
	                            new GenerationalDistance<IntegerSolution>(),
	                            new PISAHypervolume<IntegerSolution>(),
	                            new InvertedGenerationalDistance<IntegerSolution>(),
	                            new InvertedGenerationalDistancePlus<IntegerSolution>()))
	                    .setIndependentRuns(Integer.parseInt(p.getProperty("ALGORITM_INDEPENDENT_RUNS")))
	                    .setNumberOfCores(8)
	                    .build();
	   try {new ExecuteAlgorithms<>(experiment).run();} catch (Exception e) {System.out.println("ExperimentRunner.run() ExecuteAlgorithms: "+e);}
	    
	   // new ComputeQualityIndicators<>(experiment).run();
//	    new GenerateLatexTablesWithStatistics(experiment).run();
//	    new GenerateWilcoxonTestTablesWithR<>(experiment).run();
//	    new GenerateFriedmanTestTables<>(experiment).run();
	    new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run();
	
	}

	
	
	static List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> configureAlgorithmList(
	          ExperimentProblem<IntegerSolution> experimentProblem, Properties p) {
	    List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> algorithms = new ArrayList<>();

	    int popuplationSize=Integer.parseInt(p.getProperty("ALGORITM_GA_POPULATION"));
	    int initialIteration=Integer.parseInt(p.getProperty("ALGORITM_GA_ITERATIONS_START"));
	    int finalInteration=Integer.parseInt(p.getProperty("ALGORITM_GA_ITERATIONS_END"));
	    int increase=Integer.parseInt(p.getProperty("ALGORITM_GA_ITERATIONS_INCREASE"));
	    
	    int currentIteration=initialIteration;
	     while(currentIteration<=finalInteration){
	    	Algorithm<List<IntegerSolution>> algorithm;
	  		

	  		CrossoverOperator<IntegerSolution> crossoverOperator = new IntegerSBXCrossover(0.9,1) ;
	  		MutationOperator<IntegerSolution> polynomialMutation = new IntegerPolynomialMutation() ;
	  		SelectionOperator<List<IntegerSolution>, IntegerSolution> selectionOperator = new BinaryTournamentSelection<IntegerSolution>();

//	  		algorithm = new GeneticAlgorithmBuilder<IntegerSolution>(experimentProblem.getProblem()
//	  					, crossoverOperator
//	  					, mutationOperator)
//	  				.setPopulationSize(popuplationSize)
//	  				.setMaxEvaluations(currentIteration*popuplationSize)
//	  				.setSelectionOperator(selectionOperator)
//	  				.build() ;
	       // algorithms.add(new ExperimentAlgorithm<>(algorithm, "GA_"+currentIteration, experimentProblem.getTag()));
	  		algorithm=new NSGAIIBuilder<>(
	  				experimentProblem.getProblem(),
	  				crossoverOperator,
	  				polynomialMutation)
	                .setMaxEvaluations(currentIteration*popuplationSize)
	                .setPopulationSize(popuplationSize)
	                .setSelectionOperator(selectionOperator)
	                .build();
	  		 algorithms.add(new ExperimentAlgorithm<>(algorithm, "GA_"+currentIteration, experimentProblem.getTag()));
	  		
	  		currentIteration+=increase;
	     }
	      
	     
	     
	    return algorithms;
	  }
}

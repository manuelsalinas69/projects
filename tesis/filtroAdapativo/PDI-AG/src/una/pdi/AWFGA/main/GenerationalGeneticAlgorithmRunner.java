//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package una.pdi.AWFGA.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import pdi.image.util.ImageDataLoader;
import una.pdi.AWFGA.model.FiltroProblema;

/**
 * Class to configure and run a generational genetic algorithm. The target problem is OneMax.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class GenerationalGeneticAlgorithmRunner {
	/**
	 * Usage: java org.uma.jmetal.runner.singleobjective.GenerationalGeneticAlgorithmRunner
	 */
	public static void main(String[] args) throws Exception {

		ImageDataLoader idl= new ImageDataLoader();
		idl.loadBaseImage("/Users/Manuel/Documents/Tesis/input/test.jpg",true);

		int populationSize=200;
		int iteraciones=10;

		Algorithm<IntegerSolution> algorithm;
		IntegerProblem problem = new FiltroProblema(3,1) ;

		CrossoverOperator<IntegerSolution> crossoverOperator = new IntegerSBXCrossover(0.9,1) ;
		MutationOperator<IntegerSolution> mutationOperator = new IntegerPolynomialMutation() ;
		SelectionOperator<List<IntegerSolution>, IntegerSolution> selectionOperator = new BinaryTournamentSelection<IntegerSolution>();

		algorithm = new GeneticAlgorithmBuilder<IntegerSolution>(problem, crossoverOperator, mutationOperator)
				.setPopulationSize(populationSize)
				.setMaxEvaluations(iteraciones*populationSize)
				.setSelectionOperator(selectionOperator)
				.build() ;

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
				.execute() ;

		IntegerSolution solution = algorithm.getResult() ;
		List<IntegerSolution> population = new ArrayList<>(1) ;
		population.add(solution) ;

		long computingTime = algorithmRunner.getComputingTime() ;

		new SolutionListOutput(population)
		.setSeparator("\t")
		.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
		.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
		.print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}

	public static void run(Properties p){
		int populationSize=Integer.parseInt(p.getProperty("ALGORITM_GA_POPULATION"));
		int iteraciones=Integer.parseInt(p.getProperty("ALGORITM_GA_ITERATIONS"));

		
		Algorithm<IntegerSolution> algorithm;
		IntegerProblem problem = new FiltroProblema(Integer.parseInt(p.getProperty("FILTER_COLUMN_SIZE", "3"))
				,Integer.parseInt(p.getProperty("ALGORITM_GA_OBJETIVES"))) ;
		CrossoverOperator<IntegerSolution> crossoverOperator = new IntegerSBXCrossover(0.9,1) ;
		MutationOperator<IntegerSolution> mutationOperator = new IntegerPolynomialMutation() ;
		SelectionOperator<List<IntegerSolution>, IntegerSolution> selectionOperator = new BinaryTournamentSelection<IntegerSolution>();

		algorithm = new GeneticAlgorithmBuilder<IntegerSolution>(problem, crossoverOperator, mutationOperator)
				.setPopulationSize(populationSize)
				.setMaxEvaluations(iteraciones*populationSize)
				.setSelectionOperator(selectionOperator)
				.build() ;

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
				.execute() ;

		IntegerSolution solution = algorithm.getResult() ;
		List<IntegerSolution> population = new ArrayList<>(1) ;
		population.add(solution) ;

		long computingTime = algorithmRunner.getComputingTime() ;

		new SolutionListOutput(population)
		.setSeparator("\t")
		.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
		.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
		.print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
	}
}
